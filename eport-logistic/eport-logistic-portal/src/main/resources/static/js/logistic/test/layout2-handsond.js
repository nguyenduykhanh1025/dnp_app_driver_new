const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var dogrid = document.getElementById("dg-right"),
  hot;
var prefix = ctx + "logistic/receive-cont-empty";
var interval, currentPercent, timeout;
var shipmentSelected,
  shipmentDetails,
  shipmentDetailIds,
  sourceData,
  orderNumber = 0,
  currentVslNm;
var contList = [],
  orders = [],
  processOrderIds;
var conts = "";
var allChecked = false;
var checkList = [];
var opeCodeList, vslNmList, consigneeList;
var rowAmount = 5;
var shipmentSearch = new Object();
shipmentSearch.serviceType = 3;
var sizeList = [];
var berthplanList;
$.ajax({
  type: "GET",
  url: ctx + "logistic/size/container/list",
  success(data) {
    if (data.code == 0) {
      data.data.forEach((element) => {
        sizeList.push(element["dictLabel"]);
      });
    }
  },
});
$.ajax({
  url: ctx + "logistic/source/option",
  method: "GET",
  success: function (data) {
    if (data.code == 0) {
      consigneeList = data.consigneeList;
    }
  },
});
$.ajax({
  url: prefix + "/berthplan/ope-code/list",
  method: "GET",
  success: function (data) {
    if (data.code == 0) {
      opeCodeList = data.opeCodeList;
    }
  },
});
var toolbar = [
  {
    text: '<a href="#" class="btn btn-sm btn-default"><i class="fa fa-plus text-success"></i> Thêm</a>',
    handler: function () {
      alert("them");
    },
  },
  {
    text: '<a href="#" class="btn btn-sm btn-default"><i class="fa fa-trash text-danger"></i> Xóa</a>',
    handler: function () {
      alert("sua");
    },
  },
  {
    text: '<a href="#" class="btn btn-sm btn-default"><i class="fa fa-refresh text-success"></i> Làm mới</a>',
    handler: function () {
      alert("xoa");
    },
  },
];

$(".main-body").layout();

loadTable("#dg-left");
$(".collapse").click(function () {
  $(".main-body__search-wrapper").height(15);
  $(".main-body__search-wrapper--container").hide();
  $(this).hide();
  $(".uncollapse").show();
});

$(".uncollapse").click(function () {
  $(".main-body__search-wrapper").height(SEARCH_HEIGHT);
  $(".main-body__search-wrapper--container").show();
  $(this).hide();
  $(".collapse").show();
});

$(".left-side__collapse").click(function () {
  $("#main-layout").layout("collapse", "west");
  setTimeout(() => {
    hot.render();
  }, 200);
});

function loadTable(div) {
  $(div).datagrid({
    url: ctx + "logistic/transport/list",
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    singleSelect: true,
    toolbar: toolbar,
    collapsible: true,
    clientPaging: false,
    pagination: true,
    onClickRow: function () {
      getSelected();
    },
    pageSize: 50,
    nowrap: false,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        data: {
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          groupName: "",
          fullName: "",
          mobileNumber: "",
          validDate: "",
        },
        dataType: "json",
        success: function (data) {
          success(data);
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function formatDate(value) {
  var date = new Date(value);
  var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  var month = date.getMonth() + 1;
  var monthText = month < 10 ? "0" + month : month;
  return day + "/" + monthText + "/" + date.getFullYear();
}

function formatStatus(value) {
  if (value != 0) {
    return "<span class='label label-success'>Khóa</span>";
  }
  return "<span class='label label-default'>Không</span>";
}
function formatExternalRent(value) {
  if (value != 0) {
    return "<span class='label label-success'>Có</span>";
  }
  return "<span class='label label-default'>Không</span>";
}
function formatAction(value, row, index) {
  var actions = [];
  actions.push('<a class="btn btn-default btn-xs " onclick="assignTruck(\'' + row.id + '\')"><i class="fa fa-eyedropper"></i>Chỉ định xe</a>');
  return actions.join("");
}

//Oh no below is handsome table, slow down, it's time to scroll up

// FORMAT HANDSONTABLE COLUMN
function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
  let content = "";
  if (checkList[row] == 1) {
    content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')" checked></div>';
  } else {
    content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')"></div>';
  }
  $(td)
    .attr("id", "checkbox" + row)
    .addClass("htCenter")
    .addClass("htMiddle")
    .html(content);
  return td;
}
function statusIconsRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "statusIcon" + row)
    .addClass("htCenter")
    .addClass("htMiddle");
  if (sourceData[row] && sourceData[row].processStatus && sourceData[row].paymentStatus && sourceData[row].finishStatus) {
    // Command process status
    let process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
    switch (sourceData[row].processStatus) {
      case "E":
        process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
        break;
      case "Y":
        process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
        break;
      case "N":
        process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
        break;
    }
    // Payment status
    let payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
    switch (sourceData[row].paymentStatus) {
      case "E":
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Lỗi Thanh Toán" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
        break;
      case "Y":
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case "N":
        if (value > 1) {
          payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        }
        break;
    }
    // released status
    let released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Chưa thể nhận container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
    switch (sourceData[row].finishStatus) {
      case "Y":
        released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Đã Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case "N":
        if (sourceData[row].paymentStatus == "Y") {
          released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Có Thể Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        }
        break;
    }
    // Return the content
    let content = "<div>" + process + payment + released + "</div>";
    $(td).html(content);
  }
  return td;
}

function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "containerNo" + row)
    .addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (shipmentSelected.specificContFlg == 0) {
    cellProperties.readOnly = "true";
    $(td).css("background-color", "rgb(232, 232, 232)");
  }
  return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "expiredDem" + row)
    .addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != "") {
    if (value.substring(2, 3) != "/") {
      value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
    }
    $(td).html(value);
  } else {
    $(td).html("");
  }
  return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "consignee" + row)
    .addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  return td;
}
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "opeCode" + row)
    .addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "vslNm" + row)
    .addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "sztp" + row)
    .addClass("htMiddle");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  $(td).html(value);
  return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "dischargePort" + row)
    .addClass("htMiddle");
  if (value != null && value != "") {
    value = value.split(":")[0];
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  $(td).html(value);
  return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "remark" + row)
    .addClass("htMiddle");
  $(td).html(value);
  return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
  config = {
    stretchH: "all",
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    minRows: rowAmount,
    maxRows: rowAmount,
    width: "100%",
    minSpareRows: 0,
    rowHeights: 30,
    fixedColumnsLeft: 3,
    trimDropdown: false,
    manualColumnResize: true,
    manualRowResize: true,
    renderAllRows: true,
    rowHeaders: true,
    className: "htMiddle",
    colHeaders: function (col) {
      switch (col) {
        case 0:
          var txt = "<input type='checkbox' class='checker' ";
          txt += "onclick='checkAll()' ";
          txt += ">";
          return txt;
        case 1:
          return "Trạng Thái";
        case 2:
          return "Container No";
        case 3:
          return '<span>Kích Thước</span><span style="color: red;">*</span>';
        case 4:
          return '<span>Hạn Lệnh</span><span style="color: red;">*</span>';
        case 5:
          return '<span>Chủ Hàng</span><span style="color: red;">*</span>';
        case 6:
          return '<span>Hãng Tàu</span><span style="color: red;">*</span>';
        case 7:
          return '<span>Tàu và Chuyến</span><span style="color: red;">*</span>';
        case 8:
          return '<span>Cảng Dỡ Hàng</span><span style="color: red;">*</span>';
        case 9:
          return "Ghi Chú";
      }
    },
    // colWidths: [50, 100, 100, 200, 100, 200, 150, 220, 150, 200],
    filter: "true",
    columns: [
      {
        data: "active",
        type: "checkbox",
        className: "htCenter",
        renderer: checkBoxRenderer,
      },
      {
        data: "status",
        readOnly: true,
        // renderer: statusIconsRenderer
      },
      {
        data: "containerNo",
        // renderer: containerNoRenderer
      },
      {
        data: "sztp",
        type: "autocomplete",
        source: sizeList,
        strict: true,
        // renderer: sizeRenderer
      },
      {
        data: "expiredDem",
        type: "date",
        dateFormat: "DD/MM/YYYY",
        correctFormat: true,
        defaultDate: new Date(),
        // renderer: expiredDemRenderer
      },
      {
        data: "consignee",
        strict: true,
        type: "autocomplete",
        source: consigneeList,
        // renderer: consigneeRenderer
      },
      {
        data: "opeCode",
        type: "autocomplete",
        source: opeCodeList,
        strict: true,
        // renderer: opeCodeRenderer
      },
      {
        data: "vslNm",
        type: "autocomplete",
        strict: true,
        // renderer: vslNmRenderer
      },
      //            {
      //                data: "voyNo",
      //                type: "autocomplete",
      //                strict: true,
      //                renderer: voyNoRenderer
      //            },
      {
        data: "dischargePort",
        type: "autocomplete",
        strict: true,
        // renderer: dischargePortRenderer
      },
      {
        data: "remark",
        // renderer: remarkRenderer
      },
    ],
    // beforeOnCellMouseDown: function restrictSelectionToWholeRowColumn(event, coords) {
    //     if(coords.col == 0) event.stopImmediatePropagation();
    // },
    afterChange: onChange,
  };
}
configHandson();

function onChange(changes, source) {
  if (!changes) {
    return;
  }
  changes.forEach(function (change) {
    // Trigger when opeCode no change, get list vessel-voyage by opeCode
    if (change[1] == "opeCode" && change[3] != null && change[3] != "") {
      $.modal.loading("Đang xử lý ...");
      $.ajax({
        url: prefix + "/berthplan/ope-code/" + change[3].split(": ")[0] + "/vessel-voyage/list",
        method: "GET",
        success: function (data) {
          $.modal.closeLoading();
          if (data.code == 0) {
            hot.updateSettings({
              cells: function (row, col, prop) {
                if (row == change[0] && col == 7) {
                  let cellProperties = {};
                  berthplanList = data.berthplanList;
                  cellProperties.source = data.vesselAndVoyages;
                  return cellProperties;
                }
              },
            });
          }
        },
      });
    }
    // Trigger when vessel-voyage no change, get list discharge port by vessel, voy no
    else if (change[1] == "vslNm" && change[3] != null && change[3] != "") {
      let vesselAndVoy = hot.getDataAtCell(change[0], 7);
      //hot.setDataAtCell(change[0], 10, ''); // dischargePort reset
      if (vesselAndVoy) {
        let shipmentDetail = new Object();
        for (let i = 0; i < berthplanList.length; i++) {
          if (vesselAndVoy == berthplanList[i].vslAndVoy) {
            shipmentDetail.vslNm = berthplanList[i].vslNm;
            shipmentDetail.voyNo = berthplanList[i].voyNo;
            shipmentDetail.year = berthplanList[i].year;
            $.modal.loading("Đang xử lý ...");
            $.ajax({
              url: ctx + "/logistic/pods",
              method: "POST",
              contentType: "application/json",
              data: JSON.stringify(shipmentDetail),
              success: function (data) {
                $.modal.closeLoading();
                if (data.code == 0) {
                  hot.updateSettings({
                    cells: function (row, col, prop) {
                      if (row == change[0] && col == 8) {
                        let cellProperties = {};
                        cellProperties.source = data.dischargePorts;
                        return cellProperties;
                      }
                    },
                  });
                }
              },
            });
          }
        }
      }
    }
  });
}

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
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
  let disposable = true,
    status = 1,
    diff = false,
    check = false,
    verify = false,
    contNull = false;
  allChecked = true;
  for (let i = 0; i < checkList.length; i++) {
    let cellStatus = hot.getDataAtCell(i, 1);
    if (cellStatus != null) {
      if (checkList[i] == 1) {
        if (shipmentSelected.contSupplyStatus == 0) {
          contNull = true;
        }
        if (cellStatus == 1 && "Y" == sourceData[i].userVerifyStatus) {
          verify = true;
        }
        check = true;
        if (cellStatus > 1) {
          disposable = false;
        }
        if (status != 1 && status != cellStatus) {
          diff = true;
        } else {
          status = cellStatus;
        }
      } else {
        allChecked = false;
      }
    }
  }
  $(".checker").prop("checked", allChecked);
  if (disposable) {
    $("#deleteBtn").prop("disabled", false);
  } else {
    $("#deleteBtn").prop("disabled", true);
  }
  if (diff || contNull) {
    status = 1;
  } else {
    status++;
  }
  if (!check) {
    $("#deleteBtn").prop("disabled", true);
    status = 1;
  }
  switch (status) {
    case 1:
      setLayoutRegisterStatus();
      break;
    case 2:
      setLayoutVerifyUserStatus();
      if (verify) {
        $("#verifyBtn").prop("disabled", true);
        $("#deleteBtn").prop("disabled", true);
      }
      break;
    case 3:
      setLayoutPaymentStatus();
      break;
    case 4:
      setLayoutFinishStatus();
      break;
    default:
      break;
  }
}
loadShipmentDetail(326);
// LOAD SHIPMENT DETAIL LIST
function loadShipmentDetail(id) {
  $.modal.loading("Đang xử lý ...");
  $.ajax({
    url: prefix + "/shipment/" + id + "/shipment-detail",
    method: "GET",
    success: function (data) {
      $.modal.closeLoading();
      if (data.code == 0) {
        sourceData = data.shipmentDetails;
        if (rowAmount < sourceData.length) {
          sourceData = sourceData.slice(0, rowAmount);
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

function reloadShipmentDetail() {
  checkList = Array(rowAmount).fill(0);
  allChecked = false;
  $(".checker").prop("checked", false);
  for (let i = 0; i < checkList.length; i++) {
    $("#check" + i).prop("checked", false);
  }
  $("#deleteBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
  setLayoutRegisterStatus();
  loadShipmentDetail(shipmentSelected.id);
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
  shipmentDetailIds = "";
  shipmentDetails = [];
  let regiterNos = [];
  $.each(cleanedGridData, function (index, object) {
    var shipmentDetail = new Object();
    if (object["containerNo"] && !/^[A-Z]{4}[0-9]{7}$/g.test(object["containerNo"]) && isValidate && shipmentSelected.specificContFlg == 1) {
      $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
      errorFlg = true;
    }
    shipmentDetail.containerNo = object["containerNo"];
    shipmentDetail.processStatus = object["processStatus"];
    shipmentDetail.paymentStatus = object["paymentStatus"];
    shipmentDetail.userVerifyStatus = object["userVerifyStatus"];
    shipmentDetail.status = object["status"];
    shipmentDetail.shipmentId = shipmentSelected.id;
    shipmentDetail.id = object["id"];
    shipmentDetails.push(shipmentDetail);
    if (object["registerNo"] != null && !regiterNos.includes(object["registerNo"])) {
      regiterNos.push(object["registerNo"]);
    }
    shipmentDetailIds += object["id"] + ",";
  });

  let temProcessOrderIds = [];
  processOrderIds = "";
  $.each(cleanedGridData, function (index, object) {
    for (let i = 0; i < regiterNos.length; i++) {
      if (object["processOrderId"] != null && !temProcessOrderIds.includes(object["processOrderId"]) && regiterNos[i] == object["registerNo"]) {
        temProcessOrderIds.push(object["processOrderId"]);
        processOrderIds += object["processOrderId"] + ",";
      }
    }
  });

  if (processOrderIds != "") {
    processOrderIds.substring(0, processOrderIds.length - 1);
  }

  // Get result in "selectedList" variable
  if (shipmentDetails.length == 0 && isValidate) {
    $.modal.alert("Bạn chưa chọn container.");
    errorFlg = true;
  } else {
    shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length - 1);
  }
  if (errorFlg) {
    return false;
  } else {
    return true;
  }
}

// GET SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataFromTable(isValidate) {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  let cleanedGridData = [];
  for (let i = 0; i < checkList.length; i++) {
    if (Object.keys(myTableData[i]).length > 0) {
      if (myTableData[i].containerNo || myTableData[i].expiredDem || myTableData[i].opeCode || myTableData[i].vslNm || myTableData[i].sztp || myTableData[i].dischargePort || myTableData[i].remark) {
        cleanedGridData.push(myTableData[i]);
      }
    }
  }
  shipmentDetails = [];
  contList = [];
  let opecode, vessel, voyage, pod;
  if (cleanedGridData.length > 0) {
    opecode = cleanedGridData[0].opeCode;
    vessel = cleanedGridData[0].vslNm;
    //        voyage = cleanedGridData[0].voyNo;
    pod = cleanedGridData[0].dischargePort;
  }
  $.each(cleanedGridData, function (index, object) {
    var shipmentDetail = new Object();
    if (isValidate) {
      if (!object["containerNo"] && shipmentSelected.specificContFlg == 1) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập số container!");
        errorFlg = true;
        return false;
      } else if (!/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"]) && shipmentSelected.specificContFlg == 1) {
        $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
        errorFlg = true;
        return false;
      } else if (!object["expiredDem"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập hạn lệnh!");
        errorFlg = true;
        return false;
      } else if (!object["consignee"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn chủ hàng!");
        errorFlg = true;
        return false;
      } else if (!object["opeCode"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn Hãng tàu!");
        errorFlg = true;
        return false;
      } else if (!object["vslNm"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn Tàu và chuyến!");
        errorFlg = true;
        return false;
        //            } else if (!object["voyNo"]) {
        //                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn kích thước!");
        //                errorFlg = true;
        //                return false;
      } else if (!object["sztp"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn kích thước!");
        errorFlg = true;
        return false;
      } else if (!object["dischargePort"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn Cảng dở hàng!");
        errorFlg = true;
        return false;
      } else if (opecode != object["opeCode"]) {
        $.modal.alertError("Hãng tàu không được khác nhau!");
        errorFlg = true;
        return false;
      } else if (vessel != object["vslNm"]) {
        $.modal.alertError("Tàu và Chuyến không được khác nhau!");
        errorFlg = true;
        return false;
        //            } else if (voyage != object["voyNo"]) {
        //                $.modal.alertError("Số chuyến không được khác nhau!");
        //                errorFlg = true;
        //                return false;
      } else if (pod.split(": ")[0] != object["dischargePort"].split(": ")[0]) {
        $.modal.alertError("Cảng dỡ hàng không được khác nhau!");
        errorFlg = true;
        return false;
      }
    }
    opecode = object["opeCode"];
    vessel = object["vslNm"];
    //        voyage = object["voyNo"];
    pod = object["dischargePort"];
    var expiredDem = new Date(object["expiredDem"].substring(6, 10) + "/" + object["expiredDem"].substring(3, 5) + "/" + object["expiredDem"].substring(0, 2));
    shipmentDetail.containerNo = object["containerNo"];
    contList.push(object["containerNo"]);
    let sizeType = object["sztp"].split(": ");
    shipmentDetail.sztp = sizeType[0];
    shipmentDetail.sztpDefine = sizeType[1];
    let carrier = object["opeCode"].split(": ");
    shipmentDetail.opeCode = carrier[0];
    shipmentDetail.carrierName = carrier[1];
    shipmentDetail.expiredDem = expiredDem.getTime();
    shipmentDetail.consignee = object["consignee"];
    shipmentDetail.dischargePort = object["dischargePort"].split(": ")[0];
    shipmentDetail.remark = object["remark"];
    if (berthplanList) {
      for (let i = 0; i < berthplanList.length; i++) {
        if (object["vslNm"] == berthplanList[i].vslAndVoy) {
          shipmentDetail.vslNm = berthplanList[i].vslNm;
          shipmentDetail.voyNo = berthplanList[i].voyNo;
          shipmentDetail.year = berthplanList[i].year;
          shipmentDetail.vslName = berthplanList[i].vslAndVoy.split(" - ")[1];
          shipmentDetail.voyCarrier = berthplanList[i].voyCarrier;
        }
      }
    }
    shipmentDetail.bookingNo = shipmentSelected.bookingNo;
    shipmentDetail.bookingNo = shipmentSelected.blNo;
    shipmentDetail.shipmentId = shipmentSelected.id;
    shipmentDetail.id = object["id"];
    shipmentDetails.push(shipmentDetail);
    var now = new Date();
    now.setHours(0, 0, 0);
    expiredDem.setHours(23, 59, 59);
    if (expiredDem.getTime() < now.getTime() && isValidate && object["delFlag"] == null) {
      errorFlg = true;
      $.modal.alertError("Hàng " + (index + 1) + ": Hạn lệnh không được trong quá khứ!");
    }
  });

  if (isValidate && !errorFlg && shipmentSelected.specificContFlg == 1) {
    contList.sort();
    let contTemp = "";
    $.each(contList, function (index, cont) {
      if (cont != "" && cont == contTemp) {
        $.modal.alertError("Số container không được giống nhau!");
        errorFlg = true;
        return false;
      }
      contTemp = cont;
    });
  }

  // Get result in "selectedList" variable
  if (shipmentDetails.length == 0) {
    $.modal.alert("Bạn chưa nhập thông tin.");
    errorFlg = true;
  }

  if (errorFlg) {
    return false;
  } else {
    return true;
  }
}

// SAVE/EDIT/DELETE SHIPMENT DETAIL
function saveShipmentDetail() {
  if (shipmentSelected == null) {
    $.modal.alertError("Bạn cần chọn lô trước");
    return;
  } else {
    if (getDataFromTable(true)) {
      if (shipmentDetails.length > 0 && shipmentDetails.length == shipmentSelected.containerAmount) {
        $.modal.loading("Đang xử lý...");
        $.ajax({
          url: prefix + "/shipment-detail",
          method: "post",
          contentType: "application/json",
          accept: "text/plain",
          data: JSON.stringify(shipmentDetails),
          dataType: "text",
          success: function (data) {
            var result = JSON.parse(data);
            if (result.code == 0) {
              $.modal.alertSuccess(result.msg);
              reloadShipmentDetail();
            } else {
              $.modal.alertError(result.msg);
            }
            $.modal.closeLoading();
          },
          error: function (result) {
            $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
            $.modal.closeLoading();
          },
        });
      } else if (shipmentDetails.length < shipmentSelected.containerAmount) {
        $.modal.alertError("Quý khách chưa nhập đủ số lượng container.");
      } else {
        $.modal.alertError("Quý khách chưa nhập thông tin chi tiết lô.");
      }
    }
  }
}

// DELETE SHIPMENT DETAIL
function deleteShipmentDetail() {
  if (getDataSelectedFromTable(true)) {
    $.modal.loading("Đang xử lý...");
    $.ajax({
      url: prefix + "/shipment/" + shipmentSelected.id + "/shipment-detail/" + shipmentDetailIds,
      method: "delete",
      success: function (result) {
        if (result.code == 0) {
          $.modal.alertSuccess(result.msg);
          reloadShipmentDetail();
        } else {
          $.modal.alertError(result.msg);
        }
        $.modal.closeLoading();
      },
      error: function (result) {
        $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
        $.modal.closeLoading();
      },
    });
  }
}

// Handling logic
function verify() {
  getDataSelectedFromTable(true);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm("Xác nhận làm lệnh", prefix + "/otp/cont-list/confirmation/" + shipmentDetailIds, 600, 400);
  }
}

function verifyOtp(shipmentDtIds, creditFlag) {
  getDataSelectedFromTable(true);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm("Xác thực OTP", prefix + "/otp/verification/" + shipmentDtIds + "/" + creditFlag, 600, 350);
  }
}

function pay() {
  getDataSelectedFromTable(true);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm("Thanh toán", prefix + "/payment/" + processOrderIds, 800, 400);
  }
}

function exportBill() {}

// Handling UI STATUS
function setLayoutRegisterStatus() {
  $("#registerStatus").removeClass("label-primary disable").addClass("active");
  $("#verifyStatus").removeClass("label-primary active").addClass("disable");
  $("#paymentStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutVerifyUserStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("label-primary disable").addClass("active");
  $("#paymentStatus").removeClass("active label-primary").addClass("disable");
  $("#finishStatus").removeClass("active label-primary").addClass("disable");
  $("#verifyBtn").prop("disabled", false);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutPaymentStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("label-primary disable").addClass("active");
  $("#finishStatus").removeClass("active label-primary").addClass("disable");
  $("#deleteBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", false);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutFinishStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("active disable").addClass("label-primary");
  $("#finishStatus").removeClass("label-primary disable").addClass("active");
  $("#deleteBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", false);
  $("#exportReceiptBtn").prop("disabled", false);
}

function finishForm(result) {
  if (result.code == 0) {
    $.modal.alertSuccess(result.msg);
  } else {
    $.modal.alertError(result.msg);
  }
  reloadShipmentDetail();
}

function finishVerifyForm(result) {
  if (result.code == 0 || result.code == 301) {
    //$.modal.loading(result.msg);
    orders = result.processIds;
    orderNumber = result.orderNumber;
    // CONNECT WEB SOCKET
    connectToWebsocketServer();

    showProgress("Đang xử lý ...");
    timeout = setTimeout(() => {
      setTimeout(() => {
        hideProgress();
        reloadShipmentDetail();
        $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
      }, 1000);
    }, 200000);
  } else {
    reloadShipmentDetail();
    $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
  }
}

function napasPaymentForm() {
  $.modal.openFullWithoutButton("Cổng Thanh Toán", ctx + "logistic/payment/napas/" + processOrderIds);
}

function connectToWebsocketServer() {
  // Connect to WebSocket Server.
  $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
  for (let i = 0; i < orders.length; i++) {
    $.websocket.subscribe(orders[i] + "/response", onMessageReceived);
  }
}

function onMessageReceived(payload) {
  let message = JSON.parse(payload.body);
  if (message.code != 0) {
    clearTimeout(timeout);

    setProgressPercent((currentPercent = 100));
    setTimeout(() => {
      hideProgress();

      reloadShipmentDetail();

      $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");

      // Close loading
      //$.modal.closeLoading();

      // Close websocket connection
      $.websocket.disconnect(onDisconnected);
    }, 1000);
  } else {
    orderNumber--;
    if (orderNumber == 0) {
      clearTimeout(timeout);

      setProgressPercent((currentPercent = 100));
      setTimeout(() => {
        hideProgress();

        reloadShipmentDetail();

        $.modal.alertSuccess(message.msg);

        // Close loading
        //$.modal.closeLoading();

        // Close websocket connection
        $.websocket.disconnect(onDisconnected);
      }, 1000);
    }
  }
}

function onError(error) {
  console.error("Could not connect to WebSocket server. Please refresh this page to try again!");
  setTimeout(() => {
    hideProgress();
    $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
  }, 1000);
}

function showProgress(title) {
  $(".progress-wrapper").show();
  $(".dim-bg").show();
  $("#titleProgress").text(title);
  $(".percent-text").text("0%");
  currentPercent = 0;
  interval = setInterval(function () {
    if (currentPercent <= 99) {
      setProgressPercent(++currentPercent);
    }
    if (currentPercent >= 99) {
      clearInterval(interval);
    }
  }, 1000);
}

function setProgressPercent(percent) {
  $("#progressBar").prop("aria-valuenow", percent);
  $("#progressBar").css("width", percent + "%");
  $(".percent-text").text(percent + "%");
}

function hideProgress() {
  $(".progress-wrapper").hide();
  $(".dim-bg").hide();
  currentPercent = 0;
  $(".percent-text").text("0%");
  setProgressPercent(0);
}
function exportReceipt() {
  if (!shipmentSelected) {
    $.modal.alertError("Bạn chưa chọn Lô!");
    return;
  }
  $.modal.openTab("In Biên Nhận", ctx + "logistic/print/receipt/shipment/" + shipmentSelected.id);
}

$("#dg-right").find("table").addClass("zebraStyle");
