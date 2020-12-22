const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
const regexRemoveHtml = /(<([^>]+)>)/gi;
const greenBlackColor = "rgb(104 241 131)";
var prefix = ctx + "logistic/unloading-cargo-yard";
var interval, currentPercent, timeout;
var dogrid = document.getElementById("container-grid"),
  hot;
var shipmentSelected,
  shipmentDetails,
  shipmentDetailIds,
  sourceData,
  orderNumber = 0,
  isChange;
var contList = [],
  orders = [],
  processOrderIds,
  taxCodeArr;
var conts = "";
var allChecked = false;
var checkList = [];
var rowAmount = 0;
var shipmentSearch = new Object();
shipmentSearch.params = new Object();
shipmentSearch.serviceType = 18;
var sizeList = [];
var voyCarrier;
var onChangeFlg = false, currentIndexRow, rejectChange = false;
var fromDate, toDate;
var myDropzone;
var containerRemarkArr = []; // array container remark get from catos mapping with row in handsontable by index of element in array
var locations = [];
var consigneeList, opeCodeList, dischargePortList, vslNmList;

const DATE_RECEIPT_STATUS = {
  NO: "N",
  PROGRESS: "P",
  SUCCESS: "S",
  ERROR: "E"
}

$.ajax({
  url: ctx + "logistic/source/taxCode/consignee",
  method: "GET",
  success: function (data) {
    if (data.code == 0) {
      consigneeList = data.consigneeList;
    }
  },
});

//dictionary sizeList
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
      dischargePortList = data.dischargePortList;
      opeCodeList = data.opeCodeList;
      vslNmList = data.vslNmList;
    }
  },
});



var toolbar = [{
  text: '<button class="btn btn-sm btn-default"><i class="fa fa-plus text-success"></i> Thêm</button>',
  handler: function () {
    $.operate.addShipment();
  },
},
{
  text: '<button class="btn btn-sm btn-default" ><i class="fa fa-edit text-warning"></i> Sửa</button>',
  handler: function () {
    $.operate.editShipment();
  },
},
{
  text: '<button class="btn btn-sm btn-default"><i class="fa fa-remove text-danger"></i> Xóa</button>',
  handler: function () {
    removeShipment();
  },
},
{
  text: '<button class="btn btn-sm btn-default"><i class="fa fa-refresh text-success"></i></button>',
  handler: function () {
    handleRefresh();
  },
},
];

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
  // DROP ZONE configuration
  // Get the template HTML and remove it from the doumenthe template HTML and remove it from the doument

  let previewTemplate = "<span data-dz-name></span>";

  if (shipmentSelected == null) {
    shipmentSelected = new Object();
  }

  myDropzone = new Dropzone("#dropzone", {
    url: ctx + "logistic/shipment/" + shipmentSelected.id + "/file/attach",
    method: "post",
    paramName: "file",
    maxFiles: 5,
    maxFilesize: 10, //MB
    // autoProcessQueue: false,
    previewTemplate: previewTemplate,
    previewsContainer: "#previews", // Define the container to display the previews
    clickable: "#attachButton", // Define the element that should be used as click trigger to select files.
    init: function () {
      this.on("maxfilesexceeded", function (file) {
        $.modal.alertWarning(
          "Số lượng tệp đính kèm vượt giới hạn cho phép, quý khách vui lòng đính kèm tệp trong lần comment tiếp theo."
        );
        this.removeFile(file);
      });
    },
    success: function (file, response) {
      if (response.code == 0) {
        $.modal.msgSuccess("Đính kèm tệp thành công.");
        let content =
          '<p><a href="' +
          response.fileLink +
          '" target="_blank">' +
          file.upload.filename +
          "</a></p>";
        $("#content").summernote("editor.pasteHTML", content);
      } else {
        $.modal.msgError(
          "Đính kèm tệp thất bại, quý khách vui lòng thử lại sau."
        );
      }
    },
  });

  $("#right-layout").layout("collapse", "south");
  setTimeout(() => {
    hot.updateSettings({
      height: $("#right-side__main-table").height() - 35
    });
    hot.render();
  }, 200);

  $("#shipmentStatus").combobox({
    onSelect: function (option) {
      shipmentSearch.status = option.value;
      loadTable();
    },
  });

  $("#blNo")
    .textbox("textbox")
    .bind("keydown", function (e) {
      // enter key
      if (e.keyCode == 13) {
        shipmentSearch.blNo = $("#blNo").textbox("getText").toUpperCase();
        loadTable();
      }
    });

  if (sId != null) {
    shipmentSearch.id = sId;
  }

  $("#containerNo")
    .textbox("textbox")
    .bind("keydown", function (e) {
      // enter key
      if (e.keyCode == 13) {
        shipmentSearch.params.containerNo = $("#containerNo")
          .textbox("getText")
          .toUpperCase();
        loadTable();
      }
    });

  $("#consignee")
    .textbox("textbox")
    .bind("keydown", function (e) {
      // enter key
      if (e.keyCode == 13) {
        shipmentSearch.params.consignee = $("#consignee")
          .textbox("getText")
          .toUpperCase();
        loadTable();
      }
    });

  $("#fromDate").datebox({
    onSelect: function (date) {
      date.setHours(0, 0, 0);
      fromDate = date;
      if (toDate != null && date.getTime() > toDate.getTime()) {
        $.modal.alertWarning("Từ ngày không được lớn hơn đến ngày.");
      } else {
        shipmentSearch.params.fromDate = dateToString(date);
        loadTable();
      }
      return date;
    },
  });

  // let now = new Date();
  // now = new Date(now.getFullYear(), now.getMonth(), 1);
  // let nowStr = ("0" + now.getDate()).slice(-2) + "/" + ("0" + (now.getMonth() + 1)).slice(-2) + "/" + now.getFullYear();
  // $('#fromDate').datebox('setValue', nowStr);
  // shipmentSearch.params.fromDate = dateToString(now);

  $("#toDate").datebox({
    onSelect: function (date) {
      date.setHours(23, 59, 59);
      toDate = date;
      if (fromDate != null && date.getTime() < fromDate.getTime()) {
        $.modal.alertWarning("Đến ngày không được thấp hơn từ ngày.");
      } else {
        shipmentSearch.params.toDate = dateToString(date);
        loadTable();
      }
    },
  });
  $(function () {
    let options = {
      createUrl: prefix + "/shipment/add",
      updateUrl: "0",
      modalName: " Lô",
    };
    $.table.init(options);
  });

  loadTable();
});

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

// LOAD SHIPMENT LIST
function loadTable() {
  $("#dg").datagrid({
    url: ctx + "logistic/shipments",
    height: $(".main-body").height() - 75,
    method: "post",
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    rownumbers: true,
    toolbar: toolbar,
    pagination: true,
    onBeforeSelect: function (index, row) {
      getSelected(index, row);
    },
    pageSize: 50,
    nowrap: false,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      let opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        contentType: "application/json",
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: shipmentSearch,
        }),
        success: function (data) {
          success(data);
          $("#dg").datagrid("hideColumn", "id");
          $("#dg").datagrid("hideColumn", "edoFlg");
          $("#dg").datagrid("selectRow", 0);
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

// FORMAT DATE FOR SHIPMENT LIST
function formatDate(value) {
  let date = new Date(value);
  let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  let month = date.getMonth() + 1;
  let monthText = month < 10 ? "0" + month : month;
  let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
  let minutes =
    date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
  return (
    day +
    "/" +
    monthText +
    "/" +
    date.getFullYear() +
    " " +
    hours +
    ":" +
    minutes
  );
}

function formatBlNo(value, row) {
  if (row.houseBill) {
    return row.houseBill;
  }
  return value;
}

// FORMAT REMARK FOR SHIPMENT LIST
function formatRemark(value) {
  return (
    '<div class="easyui-tooltip" title="' +
    (value != null ? value : "Trống") +
    '" style="width: 80; text-align: center;"><span>' +
    (value != null ? value : "") +
    "</span></div>"
  );
}

function handleRefresh() {
  loadTable();
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected(index, row) {
  if (rejectChange) {
    rejectChange = false;
    return true;
  } else {
    if (onChangeFlg && currentIndexRow != index) {
      layer.confirm(
        "Thông tin khái báo chưa được lưu, quý khách có muốn di chuyển qua trang khác?", {
        icon: 3,
        title: "Xác Nhận",
        btn: ["Đồng Ý", "Hủy Bỏ"],
      },
        function () {
          layer.close(layer.index);
          currentIndexRow = index;
          if (row) {
            shipmentSelected = row;
            $(function () {
              let options = {
                createUrl: prefix + "/shipment/add",
                updateUrl: prefix + "/shipment/" + shipmentSelected.id,
                modalName: " Lô",
              };
              $.table.init(options);
            });
            let title = "";
            title += "Mã Lô: " + row.id + " - ";
            title += "SL: " + row.containerAmount + " - ";
            if (row.edoFlg == "0") {
              title += "Loại lệnh: DO - ";
              $("#deleteBtn").show();
            } else {
              title += "Loại lệnh: eDO - ";
              $("#deleteBtn").hide();
            }
            title += "B/L No: " + row.blNo;
            $("#right-layout")
              .layout("panel", "center")
              .panel("setTitle", title);
            rowAmount = row.containerAmount;
            checkList = Array(rowAmount).fill(0);
            allChecked = false;
            loadShipmentDetail(row.id);
            onChangeFlg = false;
            currentIndexRow = index;
            loadListComment();

            // Update dropzone url with the shipment id selected
            myDropzone.options.url =
              ctx + "logistic/shipment/" + shipmentSelected.id + "/file/attach";
          }
          return true;
        },
        function () {
          layer.close(layer.index);
          rejectChange = true;
          $("#dg").datagrid("selectRow", currentIndexRow);
          return false;
        }
      );
    } else {
      currentIndexRow = index;
      if (row) {
        shipmentSelected = row;
        $(function () {
          let options = {
            createUrl: prefix + "/shipment/add",
            updateUrl: prefix + "/shipment/" + shipmentSelected.id,
            modalName: " Lô",
          };
          $.table.init(options);
        });
        let title = "";
        title += "Mã Lô: " + row.id + " - ";
        title += "SL: " + row.containerAmount + " - ";
        if (row.edoFlg == "0") {
          title += "Loại lệnh: DO - ";
          $("#deleteBtn").show();
        } else {
          title += "Loại lệnh: eDO - ";
          $("#deleteBtn").hide();
        }
        title += "B/L No: " + row.blNo;
        $("#right-layout").layout("panel", "center").panel("setTitle", title);
        rowAmount = row.containerAmount;
        checkList = Array(rowAmount).fill(0);
        allChecked = false;
        loadShipmentDetail(row.id);
        onChangeFlg = false;
        currentIndexRow = index;
        loadListComment();

        // Update dropzone url with the shipment id selected
        myDropzone.options.url =
          ctx + "logistic/shipment/" + shipmentSelected.id + "/file/attach";
      }
      return true;
    }
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

function statusIconsRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "statusIcon" + row)
    .addClass("htCenter")
    .addClass("htMiddle");
  if (
    sourceData[row] &&
    sourceData[row].id &&
    sourceData[row].processStatus &&
    sourceData[row].finishStatus
  ) {
    // Customs Status
    let customs =
      '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
    switch (sourceData[row].customStatus) {
      case "R":
        customs =
          '<i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case "Y":
        customs =
          '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #ed5565;"></i>';
        break;
      case "N":
        customs =
          '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        break;
    }
    // Command process status
    let process =
      '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
    switch (sourceData[row].processStatus) {
      case "W":
        process =
          '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
        break;
      case "Y":
        process =
          '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
        break;
      case "N":
        if (value > 1) {
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
        }
        break;
      case "D":
        process =
          '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ hủy lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f93838;"></i>';
        break;
      case "E":
        payment =
          '<i id="verify" class="fa fa-windows easyui-tooltip" title="Lỗi làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #ed5565;"></i>';
        break;
    }
    // Payment status
    let payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
    switch (sourceData[row].paymentStatus) {
      case 'E':
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Lỗi Thanh Toán" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
        break;
      case 'Y':
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case 'W':
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa ráp đơn giá" aria-hidden="true" style="margin-left: 8px; color: #f8ac59;"></i>';
        break;
      case 'N':
        if (sourceData[row].processStatus == 'Y') {
          payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        }
        break;
    }
    // do status
    let doStatus = '<i id="doStatus" class="fa fa-file-text easyui-tooltip" title="Chưa Nộp Đầy Đủ Chứng Từ" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
    switch (sourceData[row].doStatus) {
      case 'N':
        doStatus = '<i id="doStatus" class="fa fa-file-text easyui-tooltip" title="Chưa Nộp Đầy Đủ Chứng Từ" aria-hidden="true" style="margin-left: 8px; color : #3498db;"></i>';
        break;
      case 'Y':
        doStatus = '<i id="doStatus" class="fa fa-file-text easyui-tooltip" title="Đã Nộp Đầy Đủ Chứng Từ" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
    }
    // Date receipt status
    let dateReceipt = '<i id="dateReceiptRegister" class="fa fa-clock-o easyui-tooltip" title="Chưa đăng ký ngày đóng hàng" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
    switch (sourceData[row].dateReceiptStatus) {
      case 'N':
        dateReceipt = '<i id="dateReceiptRegister" class="fa fa-clock-o easyui-tooltip" title="Có thể đăng ký ngày đóng hàng" aria-hidden="true" style="margin-left: 8px; color: #3498db"></i>';
        break;
      case 'W':
        dateReceipt = '<i id="dateReceiptRegister" class="fa fa-clock-o easyui-tooltip" title="Ngày đăng ký đóng hàng đang được xét duyệt" aria-hidden="true" style="margin-left: 8px; color: #f8ac59"></i>';
        break;
      case 'Y':
        dateReceipt = '<i id="dateReceiptRegister" class="fa fa-clock-o easyui-tooltip" title="Ngày đăng ký đóng hàng đã được xác nhận" aria-hidden="true" style="margin-left: 8px; color: #1ab394"></i>';
        break;
    }

    // Return the content
    let content = "<div>";
    // Domestic cont: VN --> not show
    if (sourceData[row].loadingPort.substring(0, 2) != "VN") {
      content += customs;
    }
    content += process + payment + doStatus + dateReceipt;
    content += "</div>";
    $(td).html(content);
  }
  return td;
}

function containerNoRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = "";
  }
  $(td)
    .attr("id", "containerNo" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function houseBillBtnRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "houseBillBtn" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  let shipmentDetailId;
  if (sourceData && sourceData.length > row) {
    shipmentDetailId = sourceData[row].id;
  }
  if (shipmentDetailId) {
    value =
      '<button class="btn btn-success btn-xs" id="detailBtn ' +
      row +
      '" onclick="openHouseBillForm(' +
      shipmentDetailId +
      ')"><i class="fa fa-check-circle"></i>Khai báo</button>';
  }
  $(td).html(value);
  cellProperties.readOnly = "true";
  return td;
}

function expiredDemRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  if (shipmentSelected.edoFlg == "1") {
    cellProperties.readOnly = "true";
    $(td).css("background-color", "rgb(232, 232, 232)");
  }
  if (value != null && value != "") {
    if (value.substring(2, 3) != "/") {
      value =
        value.substring(8, 10) +
        "/" +
        value.substring(5, 7) +
        "/" +
        value.substring(0, 4);
    }
    $(td)
      .attr("id", "expiredDem" + row)
      .addClass("htMiddle")
      .addClass("htCenter");
    $(td).html(
      '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
      value +
      "</div>"
    );
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  } else {
    $(td).html(
      '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;"></div>'
    );
  }
  return td;
}

function consigneeRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "consignee" + row)
    .addClass("htMiddle");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function dateReceiptRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'dateReceipt' + row).addClass("htMiddle").addClass("htCenter");
  if (value != null && value != '') {
    if (value.substring(2, 3) != "/") {
      value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4) + " " + value.substring(10, 16);
    } else {
      if (value.length <= 10) {
        value += " 00:00";
        hot.setDataAtCell(row, 5, value);
      }
    }
  } else {
    value = '';
  }
  cellProperties.readOnly = 'true';
  $(td).html(`<div onclick="chooseDateReceipt(` + row + `)" style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">` + value
    + ` <a class="fa fa-calendar"></a></div>`);
  return td;
}

function emptyDepotRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "emptyDepot" + row)
    .addClass("htMiddle");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = "";
  }
  cellProperties.readOnly = "true";
  let backgroundColor = "";
  if (row % 2 == 1) {
    backgroundColor = greenBlackColor;
  } else {
    backgroundColor = "#C6EFCE";
  }
  $(td).css("background-color", backgroundColor);
  $(td).css("color", "#006100");
  $(td)
    .attr("id", "opeCode" + row)
    .addClass("htMiddle");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = "";
  }
  cellProperties.readOnly = "true";
  let backgroundColor = "";
  if (row % 2 == 1) {
    backgroundColor = greenBlackColor;
  } else {
    backgroundColor = "#C6EFCE";
  }
  $(td).css("background-color", backgroundColor);
  $(td).css("color", "#006100");
  $(td)
    .attr("id", "vslNm" + row)
    .addClass("htMiddle");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = "";
  }
  cellProperties.readOnly = "true";
  let backgroundColor = "";
  if (row % 2 == 1) {
    backgroundColor = greenBlackColor;
  } else {
    backgroundColor = "#C6EFCE";
  }
  $(td).css("background-color", backgroundColor);
  $(td).css("color", "#006100");
  $(td)
    .attr("id", "voyNo" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = "";
  }
  cellProperties.readOnly = "true";
  let backgroundColor = "";
  if (row % 2 == 1) {
    backgroundColor = greenBlackColor;
  } else {
    backgroundColor = "#C6EFCE";
  }
  $(td).css("background-color", backgroundColor);
  $(td).css("color", "#006100");
  $(td)
    .attr("id", "sztp" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = "";
  }
  cellProperties.readOnly = "true";
  let backgroundColor = "";
  if (row % 2 == 1) {
    backgroundColor = greenBlackColor;
  } else {
    backgroundColor = "#C6EFCE";
  }
  $(td).css("background-color", backgroundColor);
  $(td).css("color", "#006100");
  $(td)
    .attr("id", "sztp" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = "";
  }
  cellProperties.readOnly = "true";
  let backgroundColor = "";
  if (row % 2 == 1) {
    backgroundColor = greenBlackColor;
  } else {
    backgroundColor = "#C6EFCE";
  }
  $(td).css("background-color", backgroundColor);
  $(td).css("color", "#006100");
  $(td)
    .attr("id", "wgt" + row)
    .addClass("htMiddle")
    .addClass("htRight");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function loadingPortRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  if (!value) {
    value = "";
  }
  cellProperties.readOnly = "true";
  let backgroundColor = "";
  if (row % 2 == 1) {
    backgroundColor = greenBlackColor;
  } else {
    backgroundColor = "#C6EFCE";
  }
  $(td).css("background-color", backgroundColor);
  $(td).css("color", "#006100");
  $(td)
    .attr("id", "loadingPort" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function dischargePortRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  if (!value) {
    value = "";
  }
  cellProperties.readOnly = "true";
  let backgroundColor = "";
  if (row % 2 == 1) {
    backgroundColor = greenBlackColor;
  } else {
    backgroundColor = "#C6EFCE";
  }
  $(td).css("background-color", backgroundColor);
  $(td).css("color", "#006100");
  $(td)
    .attr("id", "dischargePort" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = "";
  }
  $(td)
    .attr("id", "payType" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = "";
  }
  $(td)
    .attr("id", "payer" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function payerNameRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  if (!value) {
    value = "";
  }
  $(td)
    .attr("id", "payerNamer" + row)
    .addClass("htMiddle");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "remark" + row)
    .addClass("htMiddle");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function actualDateReceiptRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'actualDateReceipt' + row).addClass("htMiddle").addClass("htCenter");
  if (value != null && value != '') {
    if (value.length >= 16) {
      value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4) + " " + value.substring(10, 16);
    }
  } else {
    value = '';
  }
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}

function detFreeTimeRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  if (shipmentSelected.edoFlg == "1") {
    cellProperties.readOnly = "true";
    $(td).css("background-color", "rgb(232, 232, 232)");
  }
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  $(td)
    .attr("id", "detFreeTime" + row)
    .addClass("htMiddle")
    .addClass("htRight");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function specialServiceRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  if (value != null) {
    if (4 == value) {
      value = "Kho"
    } else if (value == 5) {
      value = "Bãi";
    }
  } else {
    value = '';
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
  config = {
    stretchH: "all",
    height: $("#right-side__main-table").height() - 35,
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
    className: "htCenter",
    colHeaders: function (col) {
      switch (col) {
        case 0:
          let txt = "<input type='checkbox' class='checker' ";
          txt += "onclick='checkAll()' ";
          txt += ">";
          return txt;
        case 1:
          return "Trạng Thái";
        case 2:
          return '<span class="required">Container No</span>';
        case 3:
          return "Chi Tiết";
        case 4:
          return '<span class="required">Hạn Lệnh</span>';
        case 5:
          return '<span class="required">Ngày Rút Hàng<br>Đăng Ký</span>';
        case 6:
          return 'Ngày Rút Hàng<br>Xác Nhận';
        case 7:
          return "Ngày Miễn<br>Lưu Bãi";
        case 8:
          return '<span class="required">Chủ Hàng</span>';
        case 9:
          return 'Nơi Hạ Vỏ';
        case 10:
          return "Kích Thước";
        case 11:
          return '<span class="required">Hãng Tàu</span>';
        case 12:
          return '<span class="required">Tàu</span>';
        case 13:
          return '<span class="required">Chuyến</span>';
        case 14:
          return "Seal No";
        case 15:
          return "Trọng Lượng (kg)";
        case 16:
          return '<span class="required">Cảng Xếp Hàng</span>';
        case 17:
          return "Cảng Dỡ Hàng";
        case 18:
          return "PTTT";
        case 19:
          return "Mã Số Thuế";
        case 20:
          return "Người Thanh Toán";
        case 21:
          return "Ghi Chú";
      }
    },
    colWidths: [
      40,   // 0
      120,  // 1
      100,  // 2
      100,  // 3
      100,  // 4
      120,  // 5
      100,  // 6
      80, // 7
      150,  // 8
      100,  // 9
      80, // 10
      100,  // 11
      120,  // 12
      70, // 13
      80, // 14
      120,  // 15
      120,  // 16
      100,  // 17
      100,  // 18
      130,  // 19
      130,  // 20
      200  // 21
    ],
    filter: "true",
    columns: [{
      data: "active",
      type: "checkbox",
      className: "htCenter",
      renderer: checkBoxRenderer,
    },
    {
      data: "status",
      readOnly: true,
      renderer: statusIconsRenderer,
    },
    {
      data: "containerNo",
      strict: true,
      renderer: containerNoRenderer,
    },
    {
      data: "housebilBtn",
      renderer: houseBillBtnRenderer,
    },
    {
      data: "expiredDem",
      type: "date",
      dateFormat: "YYYY-MM-DD",
      defaultDate: new Date(),
      renderer: expiredDemRenderer,
    },
    {
      data: "dateReceipt",
      type: "date",
      dateFormat: "DD/MM/YYYY",
      correctFormat: false,
      defaultDate: new Date(),
      renderer: dateReceiptRenderer
    },
    {
      data: "actualDateReceipt",
      renderer: actualDateReceiptRenderer
    },
    {
      data: "detFreeTime",
      type: "numeric",
      renderer: detFreeTimeRenderer,
    },
    {
      data: "consignee",
      type: "autocomplete",
      source: consigneeList,
      strict: true,
      renderer: consigneeRenderer,
    },
    {
      data: "emptyDepot",
      type: "autocomplete",
      source: emptyDepots,
      strict: true,
      renderer: emptyDepotRenderer,
    },
    {
      data: "sztp",
      type: "autocomplete",
      source: sizeList,
      strict: true,
      renderer: sizeRenderer,
    },
    {
      data: "opeCode",
      type: "autocomplete",
      source: opeCodeList,
      strict: true,
      renderer: opeCodeRenderer,
    },
    {
      data: "vslNm",
      type: "autocomplete",
      source: vslNmList,
      strict: true,
      renderer: vslNmRenderer,
    },
    {
      data: "voyNo",
      type: "autocomplete",
      strict: true,
      renderer: voyNoRenderer,
    },
    {
      data: "sealNo",
      renderer: sealNoRenderer,
    },
    {
      data: "wgt",
      renderer: wgtRenderer,
    },
    {
      data: "loadingPort",
      type: "autocomplete",
      source: dischargePortList,
      renderer: loadingPortRenderer,
    },
    {
      data: "dischargePort",
      type: "autocomplete",
      source: dischargePortList,
      renderer: dischargePortRenderer,
    },
    {
      data: "payType",
      renderer: payTypeRenderer,
    },
    {
      data: "payer",
      renderer: payerRenderer,
    },
    {
      data: "payerName",
      renderer: payerNameRenderer,
    },
    {
      data: "remark",
      renderer: remarkRenderer,
    },

    ],
    afterChange: function (changes, src) {
      //Get data change in cell to render another column
      if (!changes) {
        return;
      }
      onChangeFlg = true;
      if (src !== "loadData") {
        changes.forEach(function interate(change) {
          let containerNo;
          if (change[1] == "containerNo") {
            containerNo = hot.getDataAtRow(change[0])[2];
            isChange = true;
          } else {
            isChange = false;
          }
          if (
            containerNo != null &&
            isChange &&
            shipmentSelected.edoFlg == "0" &&
            /[A-Z]{4}[0-9]{7}/g.test(containerNo)
          ) {
            $.modal.loading("Đang xử lý...");
            // CLEAR DATA
            hot.setDataAtCell(change[0], 8, ""); //consignee
            hot.setDataAtCell(change[0], 10, ""); //sztp
            hot.setDataAtCell(change[0], 11, ""); //opeCode
            hot.setDataAtCell(change[0], 12, ""); //vslNm
            hot.setDataAtCell(change[0], 13, ""); //voyNo
            hot.setDataAtCell(change[0], 14, ""); //sealNo
            hot.setDataAtCell(change[0], 15, ""); //wgt
            hot.setDataAtCell(change[0], 16, ""); //loadingPort
            hot.setDataAtCell(change[0], 17, ""); //dischargePort
            containerRemarkArr[change[0]] = ""; // container remark from catos
            locations[change[0]] = ""; // yard position from catos

            // Call data to auto-fill
            $.ajax({
              url: prefix + "/shipment-detail/bl-no/cont/info",
              type: "POST",
              contentType: "application/json",
              data: JSON.stringify({
                blNo: shipmentSelected.blNo,
                containerNo: containerNo,
              }),
            }).done(function (shipmentDetail) {
              if (shipmentDetail != null) {
                hot.setDataAtCell(change[0], 8, shipmentDetail.consignee); //consignee
                hot.setDataAtCell(change[0], 10, shipmentDetail.sztp); //sztp
                hot.setDataAtCell(change[0], 11, shipmentDetail.opeCode); //opeCode
                hot.setDataAtCell(change[0], 12, shipmentDetail.vslNm); //vslNm
                hot.setDataAtCell(change[0], 13, shipmentDetail.voyNo); //voyNo
                hot.setDataAtCell(change[0], 14, shipmentDetail.sealNo); //sealNo
                hot.setDataAtCell(change[0], 15, shipmentDetail.wgt); //wgt
                hot.setDataAtCell(change[0], 16, shipmentDetail.loadingPort); //loadingPort
                hot.setDataAtCell(change[0], 17, shipmentDetail.dischargePort); //dischargePort
                containerRemarkArr[change[0]] = shipmentDetail.containerRemark; // container remark from catos
                locations[change[0]] = shipmentDetail.location; // yard position from catos
                voyCarrier = shipmentDetail.voyCarrier;
              }
            });
          }
        });
        $.modal.closeLoading();
      }
    },
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
          if (selected[3] == 21) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Down
        case 40:
          selected = hot.getSelected()[0];
          if (selected[2] == rowAmount - 1) {
            e.stopImmediatePropagation();
          }
          break;
        default:
          break;
      }
    },
  };
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);
// if (shipment != null) {
//   loadShipmentDetail(shipment.id);
// }

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
  $(".checker").prop("checked", allChecked);
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
    verify = false;
  done = false;
  allChecked = true;
  for (let i = 0; i < checkList.length; i++) {
    let cellStatus = hot.getDataAtCell(i, 1);
    if (cellStatus != null) {
      if (checkList[i] == 1) {
        if (cellStatus == 2 && "Y" == sourceData[i].userVerifyStatus) {
          verify = true;
        }
        check = true;
        if (cellStatus > 2) {
          disposable = false;
          if (sourceData[i].dateReceiptStatus == DATE_RECEIPT_STATUS.SUCCESS) {
            done = true;
          }
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
  if (diff) {
    status = 1;
  } else if (done) {
    status = 5;
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
      setLayoutCustomStatus();
      break;
    case 3:
      setLayoutVerifyUserStatus();
      break;
    case 4:
      setLayoutPaymentStatus();
      break;
    case 5:
      setLayoutFinishStatus();
      break;
    default:
      break;
  }
}

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
        if (data.shipmentDetails[0] != null) {
          voyCarrier = data.shipmentDetails[0].voyCarrier;
        }
        let saved = true;
        // let shiftingFee = false;
        taxCodeArr = Array(rowAmount).fill(new Object());
        containerRemarkArr = Array(rowAmount).fill("");
        locations = Array(rowAmount).fill("");
        sourceData.forEach(function iterate(shipmentDetail, index) {
          if (shipmentDetail.id == null) {
            saved = false;
          }
          taxCodeArr[index].taxCode = shipmentDetail.taxCode;
          taxCodeArr[index].consigneeByTaxCode =
            shipmentDetail.consigneeByTaxCode;
          locations[index] = shipmentDetail.location;
          containerRemarkArr[index] = shipmentDetail.containerRemark;
        });
        if (saved) {
          $("#pickContOnDemandBtn").prop("disabled", false);
        } else {
          $("#pickContOnDemandBtn").prop("disabled", true);
        }

        hot.destroy();
        configHandson();
        hot = new Handsontable(dogrid, config);
        hot.loadData(sourceData);
        hot.render();
        setLayoutRegisterStatus();
        onChangeFlg = false;
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
  setLayoutRegisterStatus();
  loadShipmentDetail(shipmentSelected.id);
}

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataSelectedFromTable(isValidate, isNeedPickedCont) {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  let cleanedGridData = [];
  for (let i = 0; i < checkList.length; i++) {
    if (Object.keys(myTableData[i]).length > 0) {
      if (
        checkList[i] == 1 ||
        (isNeedPickedCont &&
          myTableData[i].userVerifyStatus == "N" &&
          myTableData[i].preorderPickup == "Y")
      ) {
        cleanedGridData.push(myTableData[i]);
      }
    }
  }
  shipmentDetailIds = "";
  shipmentDetails = [];
  let regiterNos = [];
  $.each(cleanedGridData, function (index, object) {
    let shipmentDetail = new Object();
    if (
      object["containerNo"] != null &&
      object["containerNo"] != "" &&
      !/^[A-Z]{4}[0-9]{7}$/g.test(object["containerNo"]) &&
      isValidate
    ) {
      $.modal.alertError(
        "Hàng " + (index + 1) + ": Số container không hợp lệ!"
      );
      errorFlg = true;
    }
    shipmentDetail.blNo = object["blNo"];
    shipmentDetail.containerNo = object["containerNo"];
    shipmentDetail.customStatus = object["customStatus"];
    shipmentDetail.processStatus = object["processStatus"];
    shipmentDetail.paymentStatus = object["paymentStatus"];
    shipmentDetail.userVerifyStatus = object["userVerifyStatus"];
    shipmentDetail.status = object["status"];
    shipmentDetail.shipmentId = shipmentSelected.id;
    shipmentDetail.id = object["id"];
    shipmentDetail.dateReceipt = formatDateToSendServer(object["dateReceipt"]);
    shipmentDetails.push(shipmentDetail);
    if (
      object["registerNo"] != null &&
      !regiterNos.includes(object["registerNo"])
    ) {
      regiterNos.push(object["registerNo"]);
    }
    shipmentDetailIds += object["id"] + ",";
  });

  let temProcessOrderIds = [];
  processOrderIds = "";
  $.each(cleanedGridData, function (index, object) {
    for (let i = 0; i < regiterNos.length; i++) {
      if (
        object["processOrderId"] != null &&
        !temProcessOrderIds.includes(object["processOrderId"]) &&
        regiterNos[i] == object["registerNo"]
      ) {
        temProcessOrderIds.push(object["processOrderId"]);
        processOrderIds += object["processOrderId"] + ",";
      }
    }
  });

  if (processOrderIds != "") {
    processOrderIds = processOrderIds.substring(0, processOrderIds.length - 1);
  }

  // Get result in "selectedList" letiable
  if (shipmentDetails.length == 0 && isValidate) {
    $.modal.alert("Bạn chưa chọn container.");
    errorFlg = true;
  } else {
    shipmentDetailIds = shipmentDetailIds.substring(
      0,
      shipmentDetailIds.length - 1
    );
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
      if (
        myTableData[i].containerNo ||
        myTableData[i].expiredDem ||
        myTableData[i].consignee ||
        myTableData[i].emptyDepot ||
        myTableData[i].opeCode ||
        myTableData[i].vslNm ||
        myTableData[i].voyNo ||
        myTableData[i].sztp ||
        myTableData[i].sealNo ||
        myTableData[i].wgt ||
        myTableData[i].loadingPort ||
        myTableData[i].dischargePort ||
        myTableData[i].remark
      ) {
        cleanedGridData.push(myTableData[i]);
      }
    }
  }
  shipmentDetails = [];
  if (cleanedGridData.length > 0) {
    billNo = cleanedGridData[0]["blNo"];
  }
  contList = [];
  let consignee;
  if (cleanedGridData.length > 0) {
    consignee = cleanedGridData[0].consignee;
  }
  $.each(cleanedGridData, function (index, object) {
    let shipmentDetail = new Object();
    if (isValidate) {
      if (!object["containerNo"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Quý khách chưa nhập số container!"
        );
        errorFlg = true;
        return false;
      } else if (!/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"])) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Số container không hợp lệ!"
        );
        errorFlg = true;
        return false;
      } else if (!object["expiredDem"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Quý khách chưa nhập hạn lệnh!"
        );
        errorFlg = true;
        return false;
      } else if (!object["consignee"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Quý khách chưa chọn chủ hàng!"
        );
        errorFlg = true;
        return false;
      } else if (!object["opeCode"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Quý khách chưa chọn hãng tàu!"
        );
        errorFlg = true;
        return false;
      } else if (!object["vslNm"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Quý khách chưa chọn tàu!"
        );
        errorFlg = true;
        return false;
      } else if (!object["voyNo"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Quý khách chưa chọn chuyến!"
        );
        errorFlg = true;
        return false;
      } else if (!object["loadingPort"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Quý khách chưa chọn Cảng xếp hàng!"
        );
        errorFlg = true;
        return false;
      } else if (consignee != object["consignee"]) {
        $.modal.alertError("Tên chủ hàng không được khác nhau!");
        errorFlg = true;
        return false;
      }
    }

    consignee = object["consignee"];
    let expiredDem = new Date(
      object["expiredDem"].substring(0, 4) +
      "/" +
      object["expiredDem"].substring(5, 7) +
      "/" +
      object["expiredDem"].substring(8, 10)
    );
    shipmentDetail.detFreeTime = object["detFreeTime"];
    shipmentDetail.blNo = shipmentSelected.blNo;
    shipmentDetail.containerNo = object["containerNo"];
    contList.push(object["containerNo"]);
    let carrier = object["opeCode"].split(": ");
    shipmentDetail.opeCode = carrier[0];
    shipmentDetail.carrierName = carrier[1];
    shipmentDetail.sztp = object["sztp"].split(":")[0];
    shipmentDetail.consignee = object["consignee"];
    shipmentDetail.sealNo = object["sealNo"];
    expiredDem.setHours(23, 59, 59);
    shipmentDetail.expiredDem = expiredDem.getTime();
    shipmentDetail.wgt = object["wgt"];
    let vessel = object["vslNm"].split(": ");
    shipmentDetail.vslNm = vessel[0];
    shipmentDetail.vslName = vessel[1];
    shipmentDetail.voyNo = object["voyNo"];
    shipmentDetail.taxCode = taxCodeArr[index].taxCode;
    shipmentDetail.consigneeByTaxCode = taxCodeArr[index].consigneeByTaxCode;
    if (voyCarrier) {
      shipmentDetail.voyCarrier = voyCarrier;
    }
    shipmentDetail.loadingPort = object["loadingPort"];
    shipmentDetail.dischargePort = object["dischargePort"];
    shipmentDetail.transportType = object["transportType"];
    shipmentDetail.emptyDepot = object["emptyDepot"];
    shipmentDetail.remark = object["remark"];
    shipmentDetail.taxCode = object["taxCode"];
    shipmentDetail.consigneeByTaxCode = object["consigneeByTaxCode"];
    shipmentDetail.shipmentId = shipmentSelected.id;
    shipmentDetail.id = object["id"];
    shipmentDetail.containerRemark = containerRemarkArr[index];
    shipmentDetail.location = locations[index];
    shipmentDetail.processStatus = shipmentSelected.taxCode;
    shipmentDetail.customStatus = shipmentSelected.groupName;
    shipmentDetail.tier = shipmentSelected.containerAmount;
    let receiptDate = formatDateToSendServer(object["dateReceipt"]);
        if (receiptDate) {
            if (!checkRegisterDate(receiptDate)) {
                $.modal.alertError("Hàng " + (index + 1) + ": Thời gian đăng ký rút hàng của quý khách quá sớm so với thời điểm hiện tại, vui lòng chọn thời gian khác.");
                errorFlg = true;
                return false;
            }
            shipmentDetail.dateReceipt = receiptDate.getTime();
        }
    shipmentDetails.push(shipmentDetail);
    let now = new Date();
    now.setHours(0, 0, 0);
    expiredDem.setHours(23, 59, 59);
  });

  if (isValidate && !errorFlg) {
    contList.sort();
    let contTemp = "";
    $.each(contList, function (index, cont) {
      if (cont != "" && cont == contTemp) {
        $.modal.alertError("Có container trong lô khai báo bị trùng.");
        errorFlg = true;
        return false;
      }
      contTemp = cont;
    });
  }

  // Get result in "selectedList" variable
  if (shipmentDetails.length == 0 && !errorFlg) {
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
    $.modal.alertError("Hãy chọn lô để thực hiện");
    return;
  } else {
    hot.deselectCell();
    setTimeout(() => {
      if (getDataFromTable(true)) {
        if (
          shipmentDetails.length > 0 &&
          shipmentDetails.length <= shipmentSelected.containerAmount
        ) {
          save(false);
        } else if (shipmentDetails.length > shipmentSelected.containerAmount) {
          $.modal.alertError(
            "Số lượng container nhập vào vượt quá số container của lô."
          );
        } else {
          $.modal.alertError("Quý khách chưa nhập thông tin chi tiết lô.");
        }
      }
    }, 100);
  }
}

function save(isSendEmpty) {
  if (shipmentDetails.length > 0) {
    shipmentDetails[0].vgmChk = isSendEmpty;
  }
  $.modal.loading("Đang xử lý...");
  $.ajax({
    url: prefix + "/shipment-detail",
    method: "POST",
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
      $.modal.alertError(
        "Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại."
      );
      $.modal.closeLoading();
    },
  });
}

// DELETE SHIPMENT DETAIL
function deleteShipmentDetail() {
  if (getDataSelectedFromTable(true, false) && shipmentDetails.length > 0) {
    $.modal.confirmShipment("Xác nhận xóa khai báo container ?", function () {
      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: prefix +
          "/shipment/" +
          shipmentSelected.id +
          "/shipment-detail/" +
          shipmentDetailIds,
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
          $.modal.alertError(
            "Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại sau."
          );
          $.modal.closeLoading();
        },
      });
    });
  }
}

// Handling logic
function checkCustomStatus() {
  getDataSelectedFromTable(true, false);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm(
      "Khai báo hải quan",
      prefix + "/custom-status/" + shipmentDetailIds,
      720,
      500
    );
  }
}

function verify() {
  getDataSelectedFromTable(true, true);
  if (shipmentDetails.length > 0) {
    let errorMsg = "";
    for (let i = 0; i < shipmentDetails.length; i++) {
      let shipmentDetail = shipmentDetails[i];
      if (shipmentDetail.customStatus != null && shipmentDetail.customStatus != "R") {
        errorMsg += "Container quý khách vừa chọn chưa được thông quan, không thể xác nhận làm lệnh.";
        break;
      }
      if (shipmentDetail.userVerifyStatus == "Y") {
        errorMsg += "Container quý khách vừa chọn đã được xác thực, không thể xác thực lại nữa.";
        break;
      }
    }
    if (errorMsg) {
      $.modal.alertWarning(errorMsg);
    } else {
      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: prefix + "/shipment/" + shipmentSelected.id + "/delegate/permission",
        method: "GET",
        success: function (res) {
          if (res.code == 0) {
            $.ajax({
              url: prefix + "/shipment-detail/validation",
              method: "POST",
              data: {
                shipmentDetailIds: shipmentDetailIds,
              },
              success: function (res) {
                $.modal.closeLoading();
                if (res.code != 0) {
                  $.modal.alertWarning(res.msg);
                } else {
                  getDataSelectedFromTable(true, true);
                  if (shipmentDetails.length > 0) {
                    $.modal.openCustomForm(
                      "Xác nhận làm lệnh",
                      prefix + "/otp/cont-list/confirmation/" + shipmentDetailIds,
                      700,
                      500
                    );
                  }
                }
              },
              error: function (err) {
                $.modal.closeLoading();
                $.modal.alertWarning(
                  "Có lỗi xảy ra trong quá trình thực hiện, xin vui lòng thử lại sau."
                );
              },
            });
          } else {
            $.modal.closeLoading();
            if (res.msg != "") {
              $.modal.alertError(res.msg);
            } else {
              $.modal.alertError(
                "Bạn chưa có ủy quyền từ chủ hàng để thực hiện lô hàng này. Hãy liên hệ với Cảng để thêm ủy quyền."
              );
            }
          }
        },
        error: function (err) {
          $.modal.closeLoading();
          $.modal.alertError("Có lỗi xảy ra, xin vui lòng thử lại sau.");
        },
      });
    }
  }
}

function verifyOtp(shipmentDtIds, taxCode, creditFlag, isSendContEmpty) {
  getDataSelectedFromTable(true, false);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm(
      "Xác thực OTP",
      prefix +
      "/otp/verification/" +
      shipmentDtIds +
      "/" +
      creditFlag +
      "/" +
      taxCode +
      "/" +
      isSendContEmpty +
      "/" +
      shipmentSelected.id,
      600,
      350
    );
  }
}

function pay() {
  getDataSelectedFromTable(true, true);
  if (shipmentDetails.length > 0) {
    let errorMsg = "";
    for (let i = 0; i < shipmentDetails.length; i++) {
      let shipmentDetail = shipmentDetails[i];
      if ("Y" != shipmentDetail.processStatus) {
        errorMsg = "Quý khách không thể thanh toán cho container chưa được làm lệnh.";
        return false;
      }
      if (shipmentDetail.payType == 'Credit') {
        errorMsg = "Quý khách không thể thanh toán cho container cho trường hợp trả sau.";
        return false;
      }
      if ('W' == shipmentDetail.paymentStatus) {
        errorMsg = "Container chưa được ráp đơn giá, không thể thanh toán.";
        return false;
      }
      if (shipmentDetail.paymentStatus == "Y") {
        errorMsg = "Quý khách không thể thanh toán cho container đã được thanh toán.";
        return false;
      }
    }
    if (errorMsg) {
      $.modal.alertWarning(errorMsg);
    } else {
      $.modal.openCustomForm(
        "Thanh toán",
        prefix + "/payment/" + shipmentDetailIds,
        800,
        400
      );
    }
  }
}

function pickContOnDemand() {
  getDataFromTable(false);
  $.modal.openCustomForm(
    "Bốc container chỉ định",
    prefix + "/cont-list/yard-position/" + billNo,
    710,
    565
  );
}

function payShifting() {
  $.modal.openCustomForm(
    "Thanh toán phí dịch chuyển",
    prefix + "/shipment/" + shipmentSelected.id + "/payment/shifting",
    800,
    400
  );
}

function exportBill() { }

// Handling UI STATUS
function setLayoutRegisterStatus() {
  $("#registerStatus").removeClass("label-primary disable").addClass("active");
  $("#customStatus").removeClass("label-primary active").addClass("disable");
  $("#verifyStatus").removeClass("label-primary active").addClass("disable");
  $("#dateReceiptStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
}

function setLayoutCustomStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("label-primary disable").addClass("active");
  $("#verifyStatus").removeClass("label-primary active").addClass("disable");
  $("#dateReceiptStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
}

function setLayoutVerifyUserStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("label-primary disable").addClass("active");
  $("#dateReceiptStatus").removeClass("active label-primary").addClass("disable");
  $("#finishStatus").removeClass("active label-primary").addClass("disable");
}

function setLayoutPaymentStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#dateReceiptStatus").removeClass("label-primary disable").addClass("active");
  $("#finishStatus").removeClass("active label-primary").addClass("disable");
}

function setLayoutFinishStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#dateReceiptStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#finishStatus").removeClass("label-primary disable").addClass("active");
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
    reloadShipmentDetail();
    $.modal.alertWarning(
      "Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi."
    );
  }
}

function napasPaymentForm() {
  // $.modal.openFullWithoutButton("Cổng Thanh Toán", ctx + "logistic/payment/napas/" + processOrderIds);
  window.open(ctx + "logistic/payment/napas/" + processOrderIds, "_blank");
}

function napasPaymentFormForShifting() {
  $.modal.openFullWithoutButton(
    "Cổng Thanh Toán",
    ctx + "logistic/shipment/" + shipmentSelected.id + "/napas"
  );
}

function exportReceipt() {
  if (!shipmentSelected) {
    $.modal.alertError("Bạn chưa chọn Lô!");
    return;
  }
  $.modal.openTab(
    "In Biên Nhận",
    ctx + "logistic/print/receipt/shipment/" + shipmentSelected.id
  );
}

function removeShipment() {
  if (!shipmentSelected) {
    $.modal.alertError("Bạn chưa chọn Lô!");
    return;
  } else {
    //1- chua khai bao cont, 2- khai bao nhung chua lam cac buoc tiep theo
    if (shipmentSelected.status == "1" || shipmentSelected.status == "2") {
      $.modal.confirmShipment(
        "Xác nhận thực hiện xóa Lô " + shipmentSelected.id + "  ?",
        function () {
          $.modal.loading("Đang xử lý...");
          $.ajax({
            url: ctx + "logistic/shipment/remove",
            type: "POST",
            data: {
              id: shipmentSelected.id,
            },
          }).done(function (rs) {
            $.modal.closeLoading();
            if (rs.code == 0) {
              $.modal.msgSuccess(rs.msg);
              loadTable();
            } else {
              $.modal.msgError(rs.msg);
            }
          });
        }
      );
    } else {
      $.modal.msgError("Không thể xóa Lô " + shipmentSelected.id);
    }
  }
}

function dateToString(date) {
  return (
    ("0" + date.getDate()).slice(-2) +
    "/" +
    ("0" + (date.getMonth() + 1)).slice(-2) +
    "/" +
    date.getFullYear() +
    " " +
    ("0" + date.getHours()).slice(-2) +
    ":" +
    ("0" + date.getMinutes()).slice(-2) +
    ":" +
    ("0" + date.getSeconds()).slice(-2)
  );
}

function search() {
  shipmentSearch.blNo = $("#blNo").textbox("getText");
  shipmentSearch.params.containerNo = $("#containerNo").textbox("getValue");
  shipmentSearch.params.consignee = $("#consignee").textbox("getValue");
  shipmentSearch.params.fromDate = $("#fromDate").datebox("getValue");
  shipmentSearch.params.toDate = $("#toDate").datebox("getValue");
  loadTable();
}

function clearInput() {
  $("#blNo").textbox("setText", "");
  $("#containerNo").textbox("setText", "");
  $("#consignee").textbox("setText", "");
  $("#fromDate").datebox("setValue", "");
  $("#toDate").datebox("setValue", "");
  shipmentSearch = new Object();
  shipmentSearch.params = new Object();
  shipmentSearch.serviceType = 1;
  fromDate = null;
  toDate = null;
  loadTable();
}

function loadListComment(shipmentCommentId) {
  let req = {
    serviceType: 16,
    shipmentId: shipmentSelected.id,
  };
  $.ajax({
    url: ctx + "logistic/comment/list",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify(req),
    success: function (data) {
      if (data.code == 0) {
        let html = "";
        // set title for panel comment
        let commentTitle = "<span>Hỗ Trợ<span>";
        let commentNumber = 0;
        if (data.shipmentComments != null) {
          data.shipmentComments.forEach(function (element, index) {
            let createTime = element.createTime;
            let date = "";
            let time = "";
            if (createTime) {
              date =
                createTime.substring(8, 10) +
                "/" +
                createTime.substring(5, 7) +
                "/" +
                createTime.substring(0, 4);
              time = createTime.substring(10, 19);
            }

            let seenBackground = "";
            if (
              (shipmentCommentId && shipmentCommentId == element.id) ||
              !element.seenFlg
            ) {
              seenBackground = 'style="background-color: #ececec;"';
              commentNumber++;
            }

            html += "<div " + seenBackground + ">";
            // User name comment and date time comment
            html +=
              '<div><i style="font-size: 15px; color: #015198;" class="fa fa-user-circle" aria-hidden="true"></i><span> <a>' +
              element.userName +
              " (" +
              element.userAlias +
              ")</a>: <i>" +
              date +
              " at " +
              time +
              "</i></span></div>";
            // Topic comment
            html +=
              "<div><span><strong>Yêu cầu:</strong> " +
              element.topic +
              "</span></div>";
            // Content comment
            if (element.content) {
              html +=
                "<div><span>" +
                element.content.replaceAll("#{domain}", domain) +
                "</span></div>";
            }

            html += "</div>";
            html += "<hr>";
          });
        }
        commentTitle +=
          ' <span class="round-notify-count">' + commentNumber + "</span>";
        $("#right-layout")
          .layout("panel", "expandSouth")
          .panel("setTitle", commentTitle);
        $("#commentList").html(html);
        // $("#comment-div").animate({ scrollTop: $("#comment-div")[0].scrollHeight}, 1000);
        if (sId != null) {
          $("#right-layout").layout("expand", "south");
          shipmentSearch.id = null;
          sId = null;
        }
      }
    },
  });
}

function addComment() {
  let topic = $("#topic").textbox("getText").trim();
  let content = $(".summernote").summernote("code"); // get editor content
  let errorFlg = false;
  let contentTemp = content
    .replace(regexRemoveHtml, "")
    .replaceAll("&nbsp;", "")
    .trim();
  if (!topic) {
    errorFlg = true;
    $.modal.alertWarning("Vui lòng nhập chủ đề.");
  } else if (!contentTemp) {
    errorFlg = true;
    $.modal.alertWarning("Vui lòng nhập nội dung.");
  }
  if (!errorFlg) {
    let req = {
      topic: topic,
      content: content.replaceAll(domain, "#{domain}"),
      shipmentId: shipmentSelected.id,
    };
    $.ajax({
      url: prefix + "/shipment/comment",
      type: "post",
      contentType: "application/json",
      data: JSON.stringify(req),
      beforeSend: function () {
        $.modal.loading("Đang xử lý, vui lòng chờ...");
      },
      success: function (result) {
        $.modal.closeLoading();
        if (result.code == 0) {
          loadListComment(result.shipmentCommentId);
          $.modal.msgSuccess("Gửi thành công.");
          $("#topic").textbox("setText", "");
          $(".summernote").summernote("code", "");
        } else {
          $.modal.msgError("Gửi thất bại.");
        }
      },
      error: function (error) {
        $.modal.closeLoading();
        $.modal.msgError("Gửi thất bại.");
      },
    });
  }
}

function requestCancelOrder() {
  getDataSelectedFromTable(true);
  if (shipmentDetails.length > 0) {
    // Check if list cont exists cont has been process
    let containers = "";
    shipmentDetails.forEach(function (element) {
      if (element.processStatus != "Y") {
        containers += element.containerNo + ",";
      }
    });
    if (containers.length > 0) {
      containers = containers.substring(0, containers.length - 1);
      $.modal.alertWarning(
        "Các contaienr quý khách chọn chưa được thực hiện làm lệnh, quý khách không thể yêu cầu hủy lệnh cho những container này."
      );
    } else {
      openFormRemarkBeforeReqCancelOrder();
    }
  }
}

function openFormRemarkBeforeReqCancelOrder() {
  // Form confirm req supply cont
  layer.open({
    type: 2,
    area: [500 + "px", 230 + "px"],
    fix: true,
    maxmin: true,
    shade: 0.3,
    title: "Xác Nhận",
    content: prefix + "/req/cancel/confirmation",
    btn: ["Xác Nhận", "Hủy"],
    shadeClose: false,
    yes: function (index, layero) {
      let childLayer = layero.find("iframe")[0].contentWindow.document;
      $.modal.loading("Đang xử lý ...");
      $.ajax({
        url: prefix + "/order-cancel/shipment-detail",
        method: "POST",
        data: {
          shipmentDetailIds: shipmentDetailIds,
          contReqRemark: $(childLayer).find("#message").val(),
        },
        success: function (result) {
          if (result.code == 0) {
            $.modal.alertSuccess(result.msg);
            reloadShipmentDetail();
          } else {
            $.modal.alertError(result.msg);
          }
          $.modal.closeLoading();
          layer.close(index);
        },
        error: function (result) {
          $.modal.alertError(
            "Có lỗi trong quá trình xử lý dữ liệu, vui lòng thử lại sau."
          );
          $.modal.closeLoading();
        },
      });
    },
    cancel: function (index) {
      return true;
    },
  });
}

function openHouseBillForm(shipmentDetailId) {
  if (shipmentDetailId == null) {
    $.modal.alertWarning("Quý khách vui lòng khai báo và lưu thông tin container cần làm lệnh trước khi khai báo house bill!");
    return;
  }
  $.modal.openCustomForm(
    "Khai báo chi tiết thông tin hàng hóa",
    prefix + "/shipment-detail/" + shipmentDetailId + "/house-bill",1000, 500
  );
}

function formatDateToSendServer(date) {
  if (!date) {
    return '';
  }
  let dateReceipt = new Date(date.substring(6, 10) + "/" + date.substring(3, 5) + "/" + date.substring(0, 2));

  // set hours
  dateReceipt.setHours(date.substring(11, 13));

  // set minutes
  dateReceipt.setMinutes(date.substring(14, 16));

  // set seconds
  dateReceipt.setSeconds(0);
  return dateReceipt;
}

function checkRegisterDate(registerDate) {
  let registerTimeMillis = ((registerDate.getHours() * 60 * 60) + (registerDate.getMinutes() * 60)) * 1000;
  let timeShift1 = 6 * 60 * 60 * 1000;
  let timeShift2 = 12 * 60 * 60 * 1000;
  let timeShift3 = 18 * 60 * 60 * 1000;
  let timeShift4 = 24 * 60 * 60 * 1000;
  let limitTimeMillis = ((8 * 60 * 60) + (30 * 60)) * 1000;
  let now = new Date();
  let extraBeforeShiftMillis = 30 * 60 * 1000 * 3;
  let currentTimeMillis = ((now.getHours() * 60 * 60) + (now.getMinutes() * 60)) * 1000;
  let diffInMillies = Math.abs(registerDate.getTime() - now.getTime());
  if (diffInMillies < 0) {
      return false;
  }
  if (diffInMillies < limitTimeMillis) {
      if (registerTimeMillis < timeShift1) {
          if (currentTimeMillis <= timeShift1 || timeShift4 - currentTimeMillis < extraBeforeShiftMillis) {
              return false;
          }
      } else if (registerTimeMillis < timeShift2) {
          if (timeShift1 - currentTimeMillis < extraBeforeShiftMillis) {
              return false;
          }

      } else if (registerTimeMillis < timeShift3) {
          if (timeShift2 - currentTimeMillis < extraBeforeShiftMillis) {
              return false;
          }
      } else if (registerTimeMillis < timeShift4) {
          if (timeShift3 - currentTimeMillis < extraBeforeShiftMillis) {
              return false;
          }
      }
  }
  return true;
}

function chooseDateReceipt(rowIndex) {
  layer.open({
    type: 2,
    area: [250 + 'px', 380 + 'px'],
    fix: true,
    maxmin: true,
    shade: 0.3,
    title: 'Chọn ngày rút hàng',
    content: prefix + "/row/" + rowIndex + "/calendar",
    btn: ["Đóng"],
    shadeClose: false,
    yes: function (index, layero) {
      layer.close(index);
    },
    cancel: function (index) {
      return true;
    }
  });
}

function changeDate(rowIndex, date) {
  hot.setDataAtCell(rowIndex, 5, date)
}
