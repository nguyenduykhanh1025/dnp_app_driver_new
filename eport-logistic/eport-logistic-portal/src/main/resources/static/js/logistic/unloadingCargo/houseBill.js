const PREFIX = ctx + "logistic/unloading-cargo";
var dogrid = document.getElementById("container-grid"),
  hot;
var minRowAmount = 1,
  sourceData;
var allChecked, checkList, cfsHouseBillList, cfsHouseBillIds;

$(document).ready(function () {
  $("#houseBillNumber").on("input", function () {
    if ($("#houseBillNumber").val()) {
      try {
        let oldSource = hot.getSourceData();
        let rowAmount = parseInt($("#houseBillNumber").val(), 10);
        if (rowAmount < minRowAmount) {
          $("#houseBillNumber").val(minRowAmount);
          hot.destroy();
          configHandson();
          hot = new Handsontable(dogrid, config);
          hot.loadData(oldSource);
          hot.render();
          $.modal.msgError(
            "Số lượng house bill tối thiểu là " + minRowAmount + "."
          );
        } else if (rowAmount <= 100) {
          hot.destroy();
          configHandson();
          hot = new Handsontable(dogrid, config);
          hot.loadData(oldSource);
          hot.render();
        } else {
          $("#houseBillNumber").val(100);
          hot.destroy();
          configHandson();
          hot = new Handsontable(dogrid, config);
          hot.loadData(oldSource);
          hot.render();
          $.modal.msgError(
            "Số lượng house bill có thể nhập không được vượt qua 100."
          );
        }
      } catch (err) {
        $.modal.alertWarning("Ký tự bạn nhập vào không phải là số.");
      }
    }
  });

  // RENDER HANSONTABLE FIRST TIME
  checkList = Array(minRowAmount).fill(0);
  allChecked = false;
  configHandson();
  hot = new Handsontable(dogrid, config);
  loadHouseBill();
});

// LOAD SHIPMENT DETAIL LIST
function loadHouseBill() {
  let rowAmount = $("#houseBillNumber").val() + 1;
  checkList = Array(rowAmount).fill(0);
  allChecked = false;
  $.modal.loading("Đang xử lý ...");
  $.ajax({
    url: PREFIX + "/shipment-detail/" + shipmentDetailId + "/house-bills",
    method: "GET",
    success: function (res) {
      $.modal.closeLoading();
      if (res.code == 0) {
        sourceData = res.cfsHouseBills;
        if (sourceData != null && sourceData.length != 0) {
          if (sourceData.length == 0) {
            minRowAmount = 1;
            $("#houseBillNumber").val(1);
          } else {
            minRowAmount = sourceData.length;
            $("#houseBillNumber").val(minRowAmount);
          }
        }
        hot.destroy();
        configHandson();
        hot = new Handsontable(dogrid, config);
        hot.loadData(sourceData);
        hot.render();
      }
    },
    error: function (data) {
      $.modal.closeLoading();
    },
  });
}

function saveHouseBill() {
  if (getDataFromTable(true)) {
    const data = hot.getSourceData();
    if (data) {
      // console.log(cfsHouseBillList);
      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: PREFIX + "/shipment-detail/" + shipmentDetailId + "/house-bills",
        method: "post",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (res) {
          if (res.code == 0) {
            $.modal.alertSuccess(res.msg);
            loadHouseBill();
          } else {
            $.modal.alertError(res.msg);
          }
          $.modal.closeLoading();
        },
        error: function (res) {
          $.modal.alertError(
            "Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau."
          );
          $.modal.closeLoading();
        },
      });
   
    } else {
      $.modal.alertError("Quý khách chưa nhập thông tin chi tiết lô.");
    }
  }
}

function deleteHouseBill() {
  if (getDataSelectedFromTable(true) && cfsHouseBillList.length > 0) {
    $.modal.confirmShipment("Xác nhận xóa khai báo house bill ?", function () {
      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: PREFIX + "/house-bills",
        method: "delete",
        data: {
          houseBillIds: cfsHouseBillIds,
        },
        success: function (result) {
          if (result.code == 0) {
            $.modal.alertSuccess(result.msg);
            loadHouseBill();
          } else {
            $.modal.alertError(result.msg);
          }
          $.modal.closeLoading();
        },
        error: function (result) {
          $.modal.alertError(
            "Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau."
          );
          $.modal.closeLoading();
        },
      });
    });
  }
}

// FORMAT HANDSONTABLE COLUMN
function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
  let content = "";
  if (checkList[row] == 1) {
    content +=
      '<div><input type="checkbox" id="check' +
      row +
      '" onclick="check(' +
      row +
      ')" checked></div>';
  } else {
    content +=
      '<div><input type="checkbox" id="check' +
      row +
      '" onclick="check(' +
      row +
      ')"></div>';
  }
  $(td)
    .attr("id", "checkbox" + row)
    .addClass("htCenter")
    .addClass("htMiddle")
    .html(content);
  return td;
}

function dateReceiptRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'expiredDem' + row).addClass("htMiddle").addClass("htCenter");
  if (value != null && value != '') {
    if (value.substring(2, 3) != "/") {
      value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
    }
  } else {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
  config = {
    stretchH: "all",
    height: $(document).height() - 140,
    minRows: $("#houseBillNumber").val(),
    maxRows: $("#houseBillNumber").val(),
    width: "100%",
    minSpareRows: 0,
    rowHeights: 30,
    trimDropdown: false,
    manualColumnResize: true,
    manualRowResize: true,
    renderAllRows: true,
    rowHeaders: true,
    className: "htMiddle",
    colHeaders: function (col) {
      switch (col) {
        case 0:
          let txt = "<input type='checkbox' class='checker' ";
          txt += "onclick='checkAll()' ";
          txt += ">";
          return txt;
        case 1:
          return "House Bill";
        case 2:
          return "Forwarder";
        case 3:
          return "Số Lượng";
        case 4:
          return "Loại Bao Bì";
        case 5:
          return "Trọng Lượng";
        case 6:
          return "Số Khối";
        case 7:
          return "Nhãn/Ký hiệu";
        case 8:
          return "Ghi chú";
      }
    },
    colWidths: [40, 100, 150, 80, 90, 90, 90, 100, 200],
    columns: [
      {
        data: "active",
        type: "checkbox",
        className: "htCenter",
        renderer: checkBoxRenderer,
      },
      {
        data: "houseBill",
        className: "htCenter",
      },
      {
        data: "forwarder",
        className: "htCenter",
      },
      {
        data: "quantity",
        className: "htCenter",
      },
      {
        data: "packagingType",
        className: "htCenter",
      },
      {
        data: "weight",
        className: "htCenter",
      },
      {
        data: "cubicMeter",
        className: "htCenter",
      },
      {
        data: "marks",
        className: "htCenter",
      },
      {
        data: "forwarderRemark",
        className: "htCenter",
      },
    ],
    beforeKeyDown: function (e) {
      let selected;
      switch (e.keyCode) {
        // Arrow Left
        case 37:
          selected = hot.getSelected()[0];
          if (selected[3] == 0) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Up
        case 38:
          selected = hot.getSelected()[0];
          if (selected[2] == 0) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Right
        case 39:
          selected = hot.getSelected()[0];
          if (selected[3] == 16) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Down
        case 40:
          selected = hot.getSelected()[0];
          if (selected[2] == $("#houseBillNumber") - 1) {
            e.stopImmediatePropagation();
          }
          break;
        default:
          break;
      }
    },
  };
}

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
  let rowAmount = $("#houseBillNumber").val();
  if (!allChecked) {
    allChecked = true;
    checkList = Array(rowAmount).fill(0);
    for (let i = 0; i < checkList.length; i++) {
      if (hot.getDataAtCell(i, 1) == null) {
        break;
      }
      checkList[i] = 1;
      $("#check" + i).prop("checked", true);
    }
  } else {
    allChecked = false;
    checkList = Array(rowAmount).fill(0);
    for (let i = 0; i < checkList.length; i++) {
      $("#check" + i).prop("checked", false);
    }
  }
  let tempCheck = allChecked;
  updateLayout();
  hot.render();
  allChecked = tempCheck;
  $(".checker").prop("checked", tempCheck);
}
function check(id) {
  if (sourceData[id].id != null) {
    if (checkList[id] == 0) {
      $("#check" + id).prop("checked", true);
      checkList[id] = 1;
    } else {
      $("#check" + id).prop("checked", false);
      checkList[id] = 0;
    }
    hot.render();
    updateLayout();
  }
}
function updateLayout() {
  allChecked = true;
  for (let i = 0; i < checkList.length; i++) {
    let cellStatus = hot.getDataAtCell(i, 1);
    if (cellStatus != null) {
      if (checkList[i] != 1) {
        allChecked = false;
      }
    }
  }
  $(".checker").prop("checked", allChecked);
}

function closeForm() {
  $.modal.close();
}

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataSelectedFromTable(isValidate) {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  let cleanedGridData = [];
  for (let i = 0; i < checkList.length; i++) {
    if (Object.keys(myTableData[i]).length > 0) {
      if (checkList[i] == 1) {
        cleanedGridData.push(myTableData[i]);
      }
    }
  }
  cfsHouseBillIds = "";
  cfsHouseBillList = [];
  $.each(cleanedGridData, function (index, object) {
    let cfsHouseBill = new Object();
    cfsHouseBill.houseBill = object["houseBill"];
    cfsHouseBill.forwarder = object["forwarder"];
    cfsHouseBill.quantity = object["quantity"];
    cfsHouseBill.packagingType = object["packagingType"];
    cfsHouseBill.weight = object["weight"];
    cfsHouseBill.cubicMeter = object["cubicMeter"];
    cfsHouseBill.marks = object["marks"];
    cfsHouseBill.forwarderRemark = object["forwarderRemark"];
    cfsHouseBillList.push(cfsHouseBill);
    cfsHouseBillIds += object["id"] + ",";
  });

  // Get result in "selectedList" variable
  if (cfsHouseBillList.length == 0 && isValidate) {
    $.modal.alert("Bạn chưa chọn house bill.");
    errorFlg = true;
  } else {
    cfsHouseBillIds = cfsHouseBillIds.substring(0, cfsHouseBillIds.length - 1);
  }

  if (errorFlg) {
    return false;
  } else {
    return true;
  }
}

// GET HOUSE BILL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataFromTable(isValidate) {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  let cleanedGridData = [];
  // for (let i = 0; i < checkList.length; i++) {
  //   if (Object.keys(myTableData[i]).length > 0) {
  //     if (
  //       myTableData[i].houseBill ||
  //       myTableData[i].forwarder ||
  //       myTableData[i].quantity ||
  //       myTableData[i].packagingType ||
  //       myTableData[i].weight ||
  //       myTableData[i].cubicMeter ||
  //       myTableData[i].marks ||
  //       myTableData[i].forwarderRemark
  //     ) {
  //       cleanedGridData.push(myTableData[i]);
  //     }
  //   }
  // }
  // console.log(cleanedGridData);
  // cfsHouseBillList = [];
  // $.each(cleanedGridData, function (index, object) {
  //   let cfsHouseBill = new Object();
  //   cfsHouseBill.houseBill = object["houseBill"];
  //   cfsHouseBill.forwarder = object["forwarder"];
  //   cfsHouseBill.quantity = object["quantity"];
  //   cfsHouseBill.packagingType = object["packagingType"];
  //   cfsHouseBill.weight = object["weight"];
  //   cfsHouseBill.cubicMeter = object["cubicMeter"];
  //   cfsHouseBill.marks = object["marks"];
  //   cfsHouseBill.forwarderRemark = object["forwarderRemark"];

  //   if(object["forwarderRemark"]) {
  //     cfsHouseBill.dateReceipt = formatDateToSendServer(object["dateReceipt"]);
  //   }
  //   cfsHouseBillList = {...cleanedGridData, }
  //   cfsHouseBillList.push(cfsHouseBill);
  // });
    console.log(myTableData);
  if (errorFlg) {
    return false;
  } else {
    return true;
  }
}

function dateformatter(date) {
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  var d = date.getDate();
  return (d < 10 ? "0" + d : d) + "/" + (m < 10 ? "0" + m : m) + "/" + y;
}

function dateparser(s) {
  var ss = s.split(".");
  var d = parseInt(ss[0], 10);
  var m = parseInt(ss[1], 10);
  var y = parseInt(ss[2], 10);
  if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
    return new Date(y, m - 1, d);
  }
}

function formatDateToSendServer(date) {
  let expiredDem = new Date(date.substring(6, 10) + "/" + date.substring(3, 5) + "/" + date.substring(0, 2));
  expiredDem.setHours(23, 59, 59);
  return expiredDem.getTime();
}
