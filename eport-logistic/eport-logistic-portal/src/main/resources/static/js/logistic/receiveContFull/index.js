var prefix = ctx + "logistic/receiveContFull";
var shipmentSelected, shipmentDetails, shipmentDetailIds, sourceData;
var contList = [];
var conts = '';
var allChecked = false;
var checkList = [];
var rowAmount = 0;
var emptyDepotList = ["Cảng Tiên Sa", "Cảng khác"];

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
  loadTable();
  $(".left-side").css("height", $(document).height());
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
  
  // CONNECT WEB SOCKET
  connectToWebsocketServer();
});
function handleCollapse(status) {
  if (status) {
    $(".left-side").css("width", "0.5%");
    $(".left-side").children().hide();
    $("#btn-collapse").hide();
    $("#btn-uncollapse").show();
    $(".right-side").css("width", "99%");
    setTimeout(function () {
      hot.render();
    }, 500);
    return;
  }
  $(".left-side").css("width", "33%");
  $(".left-side").children().show();
  $("#btn-collapse").show();
  $("#btn-uncollapse").hide();
  $(".right-side").css("width", "67%");
  setTimeout(function () {
    hot.render();
  }, 500);
}

// LOAD SHIPMENT LIST
function loadTable() {
  $("#dg").datagrid({
    url: prefix + "/listShipment",
    height: window.innerHeight - 70,
    singleSelect: true,
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
      let opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        data: {
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
        },
        dataType: "json",
        success: function (data) {
          success(data);
          $("#dg").datagrid("hideColumn", "id");
          $("#dg").datagrid("hideColumn", "edoFlg");
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
  return day + "/" + monthText + "/" + date.getFullYear();
}

// FORMAT REMARK FOR SHIPMENT LIST
function formatRemark(value) {
  return '<div class="easyui-tooltip" title="'+ ((value!=null&&value!="")?value:"không có ghi chú") +'" style="width: 80; text-align: center;"><span>'+ (value!=null?(value.substring(0, 5) + "..."):"...") +'</span></div>';
}

// Handle add
$(function () {
  let options = {
    createUrl: prefix + "/addShipmentForm",
    updateUrl: "0",
    modalName: " Lô"
  };
  $.table.init(options);
});

function handleRefresh() {
  loadTable();
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected() {
  let row = $("#dg").datagrid("getSelected");
  if (row) {
    shipmentSelected = row;
    $(function () {
      let options = {
        createUrl: prefix + "/addShipmentForm",
        updateUrl: prefix + "/editShipmentForm/" + shipmentSelected.id,
        modalName: " Lô"
      };
      $.table.init(options);
    });
    $("#loCode").text(row.id);
    $("#taxCode").text(row.taxCode);
    $("#quantity").text(row.containerAmount);
    if (row.edoFlg == "0") {
      $("#dotype").text("DO");
    } else {
      $("#dotype").text("eDO");
    }
    $("#blNo").text(row.blNo);
    rowAmount = row.containerAmount;
    checkList = Array(rowAmount).fill(0);
    allChecked = false;
    loadShipmentDetail(row.id);
  }
}

// FORMAT HANDSONTABLE COLUMN
function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
  let content = '';
  if (checkList[row] == 1 || value) {
    content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')" checked></div>';
  } else {
    content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')"></div>';
  }
  $(td).attr('id', 'checkbox' + row).addClass("htCenter").addClass("htMiddle").html(content);
  return td;
}
function statusIconsRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'statusIcon' + row).addClass("htCenter").addClass("htMiddle");
  let content = '';
  switch (value) {
    case 1:
      content += '<div><i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i>';
      content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px;"></i>';
      content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px;"></i>';
      content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Nhận Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
      break;
    case 2:
      content += '<div><i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: rgb(5, 148, 148);"></i>';
      content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px;"></i>';
      content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Nhận Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
      break;
    case 3:
      content += '<div><i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Đã Xác Nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
      content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i>';
      content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Nhận Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
      break;
    case 4:
      content += '<div><i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Đã Xác Nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
      content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Nhận DO Gốc" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i></div>';
      break;
    case 5:
      content += '<div><i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
      content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Đã Nhận DO gốc" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i></div>';
      break;
    case 5:
      content += '<div><i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
      content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Đã Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i></div>';
      break;
    default:
      break;
  }
  $(td).html(content);
  return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'containerNo' + row).addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != '') {
    if (hot.getDataAtCell(row, 1) != null) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (value != null && value != '') {
    if (value.substring(2, 3) != "/") {
      value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
    }
    $(td).attr('id', 'expiredDem' + row).html(value).addClass("htMiddle");
  } else {
    $(td).html('');
  }
  return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'consignee' + row).addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != '') {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  return td;
}
function emptyDepotRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'emptyDepot' + row).addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != '') {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  return td;
}
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'opeCode' + row).addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != '') {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'vslNm' + row).addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != '') {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  return td;
}
function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'voyNo' + row).addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != '') {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'sztp' + row).addClass("htMiddle");
  if (value != null && value != '') {
    value = value.split(':')[0];
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  $(td).html(value);
  return td;
}
function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'sztp' + row).addClass("htMiddle");
  if (value != null && value != '') {
    value = value.split(':')[0];
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  $(td).html(value);
  return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'wgt' + row).addClass("htMiddle");
  $(td).html(value);
  if (value != null && value != '') {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  return td;
}
function loadingPortRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'loadingPort' + row).addClass("htMiddle");
  if (value != null && value != '') {
    value = value.split(':')[0];
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  $(td).html(value);
  return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'dischargePort' + row).addClass("htMiddle");
  if (value != null && value != '') {
    value = value.split(':')[0];
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  $(td).html(value);
  return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'remark' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
  config = {
    stretchH: "all",
    height: document.documentElement.clientHeight - 105,
    minRows: rowAmount,
    maxRows: rowAmount,
    width: "100%",
    minSpareRows: 0,
    rowHeights: 30,
    fixedColumnsLeft: 3,
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
          return "Trạng Thái";
        case 2:
          return '<span>Container No</span><span style="color: red;">(*)</span>';
        case 3:
          return '<span>Hạn Lệnh</span><span style="color: red;">(*)</span>';
        case 4:
          return '<span>Chủ hàng</span><span style="color: red;">(*)</span>';
        case 5:
          return '<span>Nơi Hạ Vỏ</span><span style="color: red;">(*)</span>';
        case 6:
          return '<span>Hãng Tàu</span><span style="color: red;">(*)</span>';
        case 7:
          return '<span>Tàu</span><span style="color: red;">(*)</span>';
        case 8:
          return '<span>Chuyến</span><span style="color: red;">(*)</span>';
        case 9:
          return "Kích Thước";
        case 10:
          return "Seal No";
        case 11:
          return "Trọng tải";
        case 12:
          return '<span>Tàu</span><span style="color: red;">(*)</span>';
        case 13:
          return '<span>Chuyến</span><span style="color: red;">(*)</span>';
        case 14:
          return "Cảng Xếp Hàng";
        case 15:
          return "Cảng Dỡ Hàng";
        case 16:
          return "Ghi Chú";
      }
    },
    colWidths: [50, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 200],
    filter: "true",
    columns: [
      {
        data: "active",
        type: "checkbox",
        className: "htCenter",
      },
      {
        data: "status",
        readOnly: true,
        renderer: statusIconsRenderer
      },
      {
        data: "containerNo",
        renderer: containerNoRenderer
      },
      {
        data: "expiredDem",
        type: "date",
        dateFormat: "DD/MM/YYYY",
        correctFormat: true,
        defaultDate: new Date(),
        renderer: expiredDemRenderer
      },
      {
        data: "consignee",
        strict: true,
        renderer: consigneeRenderer
      },
      {
        data: "opeCode",
        readOnly: true,
        renderer: opeCodeRenderer
      },
      {
        data: "emptyDepot",
        type: "autocomplete",
        source: emptyDepotList,
        strict: true,
        renderer: emptyDepotRenderer
      },
      {
        data: "sztp",
        readOnly: true,
        renderer: sizeRenderer
      },
      {
        data: "sealNo",
        readOnly: true,
        renderer: sealNoRenderer
      },
      {
        data: "wgt",
        readOnly: true,
        renderer: wgtRenderer
      },
      {
        data: "vslNm",
        readOnly: true,
        renderer: vslNmRenderer
      },
      {
        data: "voyNo",
        readOnly: true,
        renderer: voyNoRenderer
      },
      {
        data: "loadingPort",
        readOnly: true,
        renderer: loadingPortRenderer
      },
      {
        data: "dischargePort",
        readOnly: true,
        renderer: dischargePortRenderer
      },
      {
        data: "remark",
        renderer: remarkRenderer
      },
    ],
    afterRenderer: function (TD, row, column, prop, value, cellProperties) {
      switch (column) {
        case 0:
          break;
        case 1:
          if ($(TD).attr("id") != null) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 2:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 11 || $(TD).attr("id").substring(0, 11) != "containerNo")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 3:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 10 || $(TD).attr("id").substring(0, 10) != "expiredDem")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 4:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 9 || $(TD).attr("id").substring(0, 9) != "consignee")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 5:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 7 || $(TD).attr("id").substring(0, 7) != "opeCode")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 6:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 10 || $(TD).attr("id").substring(0, 10) != "emptyDepot")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 7:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 4 || $(TD).attr("id").substring(0, 4) != "size")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 8:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 6 || $(TD).attr("id").substring(0, 6) != "sealNo")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 9:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 3 || $(TD).attr("id").substring(0, 3) != "wgt")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 10:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 5 || $(TD).attr("id").substring(0, 5) != "vslNm")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 11:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 5 || $(TD).attr("id").substring(0, 5) != "voyNo")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 12:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 11 || $(TD).attr("id").substring(0, 11) != "loadingPort")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 13:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 13 || $(TD).attr("id").substring(0, 13) != "dischargePort")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        case 14:
          if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 6 || $(TD).attr("id").substring(0, 6) != "remark")) {
            hot.setDataAtCell(row, column, '');
          }
          break;
        default:
          break;
      }
    },
    afterChange: function (changes, src) {
      //Get data change in cell to render another column
      if (src !== "loadData") {
        let verifyStatus = false;
        let paymentStatus = false;
        let makeOrder = false;
        let notVerify = false;
        changes.forEach(function interate(row) {
          let containerNo;
          if (row[1] == "active" && !isIterate) {
            getDataSelectedFromTable(false, false);
            if (allChecked) {
              $(".checker").prop("checked", true);
              checked = true;
            } else {
              $(".checker").prop("checked", false);
              checked = false;
            }
            if (shipmentDetails.length > 0) {
              let status = 1;
              for (let i=0; i<shipmentDetails.length; i++) {
                console.log(shipmentDetails[i].status);
                switch (shipmentDetails[i].status) {
                  case 1:
                    status = 1;
                    break;
                  case 2:
                    if (shipmentDetails[i].userVerifyStatus == "Y") {
                      if (notVerify || makeOrder || paymentStatus) {
                        status = 1;
                      } else {
                        verifyStatus = true;
                        status = 3;
                      }
                    } else {
                      if (verifyStatus || makeOrder || paymentStatus) {
                        status = 1;
                      } else {
                        status = 2;
                        notVerify = true;
                      }
                    }
                    break;
                  case 3:
                    if (verifyStatus || notVerify || paymentStatus) {
                      status = 1;
                    } else {
                      status = 4;
                      makeOrder = true;
                    }
                    break;
                  case 4:
                    if (verifyStatus || notVerify || makeOrder) {
                      status = 1;
                    } else {
                      status = 5;
                      paymentStatus = true;
                    }
                    break;
                }
              }
              switch (status) {
                case 1:
                  setLayoutCustomStatus(simpleCustom);
                  if (!paymentStatus && !notVerify && !verifyStatus) {
                    $("#deleteBtn").prop("disabled", false);
                  }
                  break;
                case 2:
                  setLayoutVerifyUser();
                  break;
                case 3:
                  setLayoutVerifyUser();
                  $("#verifyBtn").prop("disabled", true);
                  break;
                case 4:
                  setLayoutPaymentStatus();
                  break;
                case 5:
                  setLayoutFinish();
                  break;
              }
            } else {
              switch (originStatus) {
                case 1:
                  setLayoutRegisterStatus();
                  break;
                case 2:
                  setLayoutCustomStatus(simpleCustom);
                  break;
              }
            }
          }
          if (row[1] == "containerNo") {
            containerNo = hot.getDataAtRow(row[0])[2];
            isChange = true;
          } else {
            isChange = false;
          }
          if (containerNo != null && isChange && shipmentSelected.edoFlg == "0" && /[A-Z]{4}[0-9]{7}/g.test(containerNo)) {
            // Call data to auto-fill
            $.ajax({
              url: prefix + "/getContInfo",
              type: "post",
              data: {
                containerNo: containerNo,
                blNo: shipmentSelected.blNo
              }
            }).done(function (shipmentDetail) {
              if (shipmentDetail != null) {
                hot.setDataAtCell(row[0], 3, shipmentDetail.expiredDem); //expiredem
                hot.setDataAtCell(row[0], 4, shipmentDetail.consignee); //consignee
                hot.setDataAtCell(row[0], 5, shipmentDetail.opeCode); //opeCode
                hot.setDataAtCell(row[0], 6, shipmentDetail.emptyDepot); //emptyDepot
                hot.setDataAtCell(row[0], 7, shipmentDetail.sztp); //sztp
                hot.setDataAtCell(row[0], 8, shipmentDetail.sealNo); //sealNo
                hot.setDataAtCell(row[0], 9, shipmentDetail.wgt); //wgt
                hot.setDataAtCell(row[0], 10, shipmentDetail.vslNm); //vslNm
                hot.setDataAtCell(row[0], 11, shipmentDetail.voyNo); //voyNo
                hot.setDataAtCell(row[0], 12, shipmentDetail.loadingPort); //loadingPort
                hot.setDataAtCell(row[0], 13, shipmentDetail.dischargePort); //dischargePort
                hot.setDataAtCell(row[0], 14, shipmentDetail.remark); //remark
              }
            });
          }
        });
      }
    },
    afterSelectionEnd: function (r, c, r2, c2) {
        selectedRow = null;
        if (c == 0 && c2 == 12) {
            selectedRow = r;
        }
    },
    beforeKeyDown: function(e) {
        if (e.keyCode == 8) {
            e.stopImmediatePropagation();
        }
    },
  };
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

// LOAD SHIPMENT DETAIL LIST
function loadShipmentDetail(id) {
  $.ajax({
    url: prefix + "/listShipmentDetail",
    method: "GET",
    data: {
      shipmentId: id
    },
    success: function (data) {
      if (data.code == 0) {
        sourceData = data.shipmentDetails;
        if (rowAmount < sourceData.length) {
          sourceData = sourceData.slice(0, rowAmount);
        }
        let saved = true;
        customStatus = true;
        simpleCustom = false;
        sourceData.forEach(function iterate(shipmentDetail) {
          if (shipmentDetail.id == null) {
            saved = false;
            setLayoutRegisterStatus();
            originStatus = 1;
          } else {
            if (shipmentDetail.status < 2) {
              customStatus = false;
            } else {
              simpleCustom = true;
            }
            setLayoutCustomStatus(simpleCustom);
            originStatus = 2;
          }
        });
        hot.destroy();
        configHandson();
        hot = new Handsontable(dogrid, config);
        hot.loadData(sourceData);
        hot.render();
        if (!saved) {
          $.modal.alert("Thông tin container đã được hệ thống tự<br>động điền, quý khách vui lòng kiểm tra lại<br>thông tin và lưu khai báo.");
          setLayoutRegisterStatus();
        } else if (customStatus) {
          $("#customBtn").prop("disabled", true);
        }
      }
    }
  });
}

function reloadShipmentDetail() {
  loadShipmentDetail(shipmentSelected.id);
}

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataSelectedFromTable(isValidate, isNeedPickedCont) {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
    hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
  }
  let cleanedGridData = [];
  allChecked = true;
  $.each(myTableData, function (rowKey, object) {
    if (!hot.isEmptyRow(rowKey)) {
      if (object["active"]) {
        cleanedGridData.push(object);
      } else {
        allChecked = false;
        if (object["preorderPickup"] == "Y" && isNeedPickedCont) {
          cleanedGridData.push(object);
        }
      }
    } 
  });
  shipmentDetailIds = "";
  shipmentDetails = [];
  $.each(cleanedGridData, function (index, object) {
    let shipmentDetail = new Object();
    if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"]) && isValidate) {
      $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
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
    shipmentDetails.push(shipmentDetail);
    shipmentDetailIds += object["id"]+",";
  });

  // Get result in "selectedList" letiable
  if (shipmentDetails.length == 0 && isValidate) {
    $.modal.alert("Bạn chưa chọn container.");
    errorFlg = true;
  } else {
    shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length-1);
  }
  if (errorFlg) {
    return false;
  }
}

// GET SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataFromTable(isValidate) {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
    hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
  }
  let cleanedGridData = [];
  $.each(myTableData, function (rowKey, object) {
    if (!hot.isEmptyRow(rowKey)) {
      cleanedGridData.push(object);
    }
  });
  shipmentDetails = [];
  if (cleanedGridData.length > 0) {
    billNo = cleanedGridData[0]["blNo"];
  }
  contList = [];
  $.each(cleanedGridData, function (index, object) {
    let shipmentDetail = new Object();
    if (isValidate && object["delFlag"] == null) {
      if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"])) {
        $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
        errorFlg = true;
        return false;
      } else if (object["expiredDem"] == null || object["expiredDem"] == "") {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập hạn lệnh!");
        errorFlg = true;
        return false;
      } else if (object["consignee"] == null || object["consignee"] == "") {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn chủ hàng!");
        errorFlg = true;
        return false;
      } else if (object["emptyDepot"] == null || object["emptyDepot"] == "") {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn nơi hạ vỏ!");
        errorFlg = true;
        return false;
      }
    }
    let expiredDem = new Date(object["expiredDem"].substring(6, 10) + "/" + object["expiredDem"].substring(3, 5) + "/" + object["expiredDem"].substring(0, 2));
    shipmentDetail.blNo = shipmentSelected.blNo;
    shipmentDetail.containerNo = object["containerNo"];
    contList.push(object["containerNo"]);
    shipmentDetail.opeCode = object["opeCode"];
    shipmentDetail.sztp = object["sztp"];
    shipmentDetail.consignee = object["consignee"];
    shipmentDetail.sealNo = object["sealNo"];
    shipmentDetail.expiredDem = expiredDem.getTime();
    shipmentDetail.wgt = object["wgt"];
    shipmentDetail.vslNm = object["vslNm"];
    shipmentDetail.voyNo = object["voyNo"];
    shipmentDetail.loadingPort = object["loadingPort"];
    shipmentDetail.dischargePort = object["dischargePort"];
    shipmentDetail.transportType = object["transportType"];
    shipmentDetail.emptyDepot = object["emptyDepot"];
    shipmentDetail.remark = object["remark"];
    shipmentDetail.shipmentId = shipmentSelected.id;
    shipmentDetail.id = object["id"];
    shipmentDetails.push(shipmentDetail);
    let now = new Date();
    now.setHours(0, 0, 0);
    expiredDem.setHours(23, 59, 59);
    if (expiredDem.getTime() < now.getTime() && isValidate && object["delFlag"] == null) {
      errorFlg = true;
      $.modal.alertError("Hàng " + (index + 1) + ": Hạn lệnh không được trong quá khứ!")
      return false;
    }
  });

  if (isValidate) {
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

// EVENT WHEN DELETE SELECTED SHIPMENT DETAIL WITH BACKSPACE BUTTON
document.addEventListener("keyup", function (e) {
  if (e.keyCode == 8) {
    if (selectedRow != null && $("#processStatus" + selectedRow).html() != "Đã làm lệnh") {
      var myTableData = hot.getSourceData();
      // myTableData[selectedRow].registerNo = '';
      myTableData[selectedRow].containerNo = '';
      myTableData[selectedRow].expiredDem = '';
      myTableData[selectedRow].opeCode = '';
      myTableData[selectedRow].sztp = '';
      myTableData[selectedRow].fe = '';
      myTableData[selectedRow].consignee = '';
      myTableData[selectedRow].sealNo = '';
      myTableData[selectedRow].wgt = '';
      myTableData[selectedRow].vslNm = '';
      myTableData[selectedRow].voyNo = '';
      myTableData[selectedRow].loadingPort = '';
      myTableData[selectedRow].dischargePort = '';
      myTableData[selectedRow].transportType = '';
      myTableData[selectedRow].emptyDepot = '';
      myTableData[selectedRow].customStatus = '';
      myTableData[selectedRow].paymentStatus = '';
      myTableData[selectedRow].processStatus = '';
      myTableData[selectedRow].doStatus = '';
      myTableData[selectedRow].status = '';
      myTableData[selectedRow].remark = '';
      myTableData[selectedRow].delFlag = true;
      hot.loadData(myTableData);
      // $("#registerNo" + selectedRow).html('');
      $("#containerNo" + selectedRow).html('');
      $("#expiredDem" + selectedRow).html('');
      $("#opeCode" + selectedRow).html('');
      $("#size" + selectedRow).html('');
      $("#fe" + selectedRow).html('');
      $("#consignee" + selectedRow).html('');
      $("#sealNo" + selectedRow).html('');
      $("#wgt" + selectedRow).html('');
      $("#vslNm" + selectedRow).html('');
      $("#voyNo" + selectedRow).html('');
      $("#loadingPort" + selectedRow).html('');
      $("#dischargePort" + selectedRow).html('');
      $("#transportType" + selectedRow).html('');
      $("#emptyDepot" + selectedRow).html('');
      $("#customStatus" + selectedRow).html('');
      $("#paymentStatus" + selectedRow).html('');
      $("#processStatus" + selectedRow).html('');
      $("#doStatus" + selectedRow).html('');
      $("#status" + selectedRow).html('');
      $("#remark" + selectedRow).html('');
    }
  }
});

// SAVE/EDIT/DELETE SHIPMENT DETAIL
function saveShipmentDetail() {
  if (shipmentSelected == null) {
    $.modal.msgError("Bạn cần chọn lô trước");
    return;
  } else {
    if (getDataFromTable(true)) {
      if (shipmentDetails.length > 0 && shipmentDetails.length <= shipmentSelected.containerAmount) {
        $.modal.loading("Đang xử lý...");
        $.ajax({
          url: prefix + "/saveShipmentDetail",
          method: "post",
          contentType: "application/json",
          accept: 'text/plain',
          data: JSON.stringify(shipmentDetails),
          dataType: 'text',
          success: function (data) {
            var result = JSON.parse(data);
            if (result.code == 0) {
              $.modal.msgSuccess(result.msg);
              loadShipmentDetail(shipmentSelected.id);
            } else {
              $.modal.msgError(result.msg);
            }
            $.modal.closeLoading();
          },
          error: function (result) {
            $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
            $.modal.closeLoading();
          },
        });
      } else if (shipmentDetails.length > shipmentSelected.containerAmount) {
        $.modal.alertError("Số container nhập vào vượt quá số container<br>của lô.");
      } else {
        $.modal.alertError("Quý khách chưa nhập thông tin chi tiết lô.");
      }
    }
  }
}

// DELETE SHIPMENT DETAIL
function deleteShipmentDetail() {
  $.modal.loading("Đang xử lý...");
  $.ajax({
      url: prefix + "/deleteShipmentDetail",
      method: "post",
      data: {
          shipmentDetailIds: shipmentDetailIds
      },
      success: function (result) {
          if (result.code == 0) {
              $.modal.msgSuccess(result.msg);
              loadShipmentDetail(shipmentSelected.id);
          } else {
              $.modal.msgError(result.msg);
          }
          $.modal.closeLoading();
      },
      error: function (result) {
          $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
          $.modal.closeLoading();
      },
  });
}

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
  getDataFromTable(false);
  isIterate = true;
  if (checked) {
    for (var i=0; i<shipmentDetails.length; i++) {
      hot.setDataAtCell(i, 0, false);
      if (i == shipmentDetails.length-2) {
        isIterate = false;
      }
    }
    $(".checker").prop("checked", false);
    checked = false;
  } else {
    for (var i=0; i<shipmentDetails.length; i++) {
      hot.setDataAtCell(i, 0, true);
      if (i == shipmentDetails.length-2) {
        isIterate = false;
      }
    }
    $(".checker").prop("checked", true);
    checked = true;
  }
}


// Handling logic
function checkCustomStatus() {
  $.modal.openCustomForm("Khai báo hải quan", prefix + "/checkCustomStatusForm/" + shipmentSelected.id, 720, 500);
}

function verify() {
  getDataSelectedFromTable(true, true);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm("Xác nhận làm lệnh", prefix + "/checkContListBeforeVerify/" + shipmentDetailIds, 600, 500);
  } 
}

function verifyOtp(shipmentDtIds) {
  $.modal.openCustomForm("Xác thực OTP", prefix + "/verifyOtpForm/" + shipmentDtIds, 600, 350);
}

function pay() {
  $.modal.openCustomForm("Thanh toán", prefix + "/paymentForm/" + shipmentDetailIds, 700, 300);
}

function pickTruck() {
  $.modal.openFullPickTruck("Điều xe", prefix + "/pickTruckForm/" + shipmentSelected.id + "/" + false + "/" + "0");
}

function pickContOnDemand() {
  getDataFromTable(false);
  $.modal.openCustomForm("Bốc container chỉ định", prefix + "/pickContOnDemandForm/" + billNo, 710, 565);
}

function exportBill() {

}


// Handling UI STATUS
function setLayoutRegisterStatus() {
  $("#registerStatus").removeClass("label-primary disable").addClass("active");
  $("#customStatus").removeClass("label-primary active").addClass("disable");
  $("#verifyStatus").removeClass("label-primary active").addClass("disable");
  $("#paymentStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  $("#saveShipmentDetailBtn").prop("disabled", false);
  $("#customBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#pickContOnDemandBtn").prop("disabled", true);
  $("#pickTruckBtn").prop("disabled", true);
  $("#deleteBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
}

function setLayoutCustomStatus(simpleCustoms) {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("label-primary disable").addClass("active");
  $("#verifyStatus").removeClass("label-primary active").addClass("disable");
  $("#paymentStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  $("#saveShipmentDetailBtn").prop("disabled", false);
  $("#verifyBtn").prop("disabled", true);
  if (simpleCustoms) {
    $("#pickContOnDemandBtn").prop("disabled", false);
  } else {
    $("#pickContOnDemandBtn").prop("disabled", true);
  }
  if (customStatus) {
    $("#customBtn").prop("disabled", true);
  } else {
    $("#customBtn").prop("disabled", false);
  }
  $("#pickTruckBtn").prop("disabled", false);
  $("#deleteBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
}

function setLayoutVerifyUser() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("label-primary disable").addClass("active");
  $("#paymentStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  // $("#saveShipmentDetailBtn").prop("disabled", false);
  // $("#customBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", false);
  $("#deleteBtn").prop("disabled", true);
  // $("#pickContOnDemandBtn").prop("disabled", false);
  // $("#pickTruckBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
}

function setLayoutPaymentStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("label-primary disable").addClass("active");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  // $("#saveShipmentDetailBtn").prop("disabled", true);
  // $("#customBtn").prop("disabled", true);
  $("#deleteBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  // $("#pickContOnDemandBtn").prop("disabled", true);
  // $("#pickTruckBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", false);
  $("#exportBillBtn").prop("disabled", true);
}

function setLayoutFinish() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("active disable").addClass("label-primary");
  $("#finishStatus").removeClass("label-primary disable").addClass("active");
  // $("#saveShipmentDetailBtn").prop("disabled", true);
  // $("#customBtn").prop("disabled", true);
  $("#deleteBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  // $("#pickContOnDemandBtn").prop("disabled", true);
  // $("#pickTruckBtn").prop("disabled", false);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", false);
}

// function setLayoutPickCont() {
//   $("#registerStatus").removeClass("active disable").addClass("label-primary");
//   $("#customStatus").removeClass("active disable").addClass("label-primary");
//   $("#verifyStatus").removeClass("active disable").addClass("label-primary");
//   $("#paymentStatus").removeClass("label-primary disable").addClass("active");
//   $("#finishStatus").removeClass("label-primary active").addClass("disable");
//   $("#saveShipmentDetailBtn").prop("disabled", true);
//   $("#customBtn").prop("disabled", true);
//   $("#verifyBtn").prop("disabled", true);
//   $("#pickContOnDemandBtn").prop("disabled", false);
//   $("#pickTruckBtn").prop("disabled", true);
//   $("#payBtn").prop("disabled", true);
//   $("#exportBillBtn").prop("disabled", true);
// }

function finishForm(result) {
  if (result.code == 0) {
    $.modal.msgSuccess(result.msg);
  } else {
    $.modal.msgError(result.msg);
  }
  reloadShipmentDetail();
}

