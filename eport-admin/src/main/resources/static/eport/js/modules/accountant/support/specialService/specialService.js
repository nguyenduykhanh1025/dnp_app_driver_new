const PREFIX = ctx + "accountant/support/special-service";
const HIST_PREFIX = ctx + "om/controlling";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
const containerCol = 7;
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, checkList, allChecked, sourceData, rowAmount = 0, shipmentDetailIds, processOrderIds;
var shipmentDetails, specialService, specialServiceDictData;
var fromDate, toDate;
var shipment = new Object();
shipment.params = new Object();
var dischargePortList = [], currentVesselVoyage = '', consigneeList, consigneeTaxCodeList, vslNmList, sizeList = [], berthplanList, currentConsigneeList, opeCodeList, emptyDepotList;
var cargoTypeList = ["AK:Over Dimension", "BB:Break Bulk", "BN:Bundle", "DG:Dangerous", "DR:Reefer & DG", "DE:Dangerous Empty", "FR:Fragile", "GP:General", "MT:Empty", "RF:Reefer"];
$(document).ready(function () {

  if (sId != null) {
    shipment.id = sId;
  }

  $.ajax({
    type: "GET",
    url: PREFIX + "/data-source",
    success(data) {
      if (data.code == 0) {

        // List sztp
        data.sizeList.forEach(element => {
          sizeList.push(element['dictLabel']);
        });

        // List consignee
        consigneeList = data.consigneeList;
        consigneeTaxCodeList = data.listConsigneeWithTaxCode;

        // List vslnm
        berthplanList = data.berthplanList;
        vslNmList = data.vesselAndVoyages;

        // opr list
        opeCodeList = data.oprList;

        // empty depot list
        emptyDepotList = data.emptyDepotList;

        specialService = data.specialService;
        specialServiceDictData = data.specialServiceDictData;
      }
    }
  });

  $(".main-body").layout();

  $(".collapse").click(function () {
    $(".main-body__search-wrapper").height(15);
    $(".main-body__search-wrapper--container").hide();
    $(this).hide();
    $(".uncollapse").show();
  });

  $(".uncollapse").click(function () {
    $(".main-body__search-wrapper").height(SEARCH_HEIGHT + 20);
    $(".main-body__search-wrapper--container").show();
    $(this).hide();
    $(".collapse").show();
  });

  $(".left-side__collapse").click(function () {
    $('#main-layout').layout('collapse', 'west');
  });

  $(".right-side__collapse").click(function () {
    $('#right-layout').layout('collapse', 'south');
    setTimeout(() => {
      hot.updateSettings({ height: $('#right-side__main-table').height() - 35 });
      hot.render();
    }, 200);
  });

  $('#right-layout').layout({
    onExpand: function (region) {
      if (region == "south") {
        hot.updateSettings({ height: $('#right-side__main-table').height() - 35 });
        hot.render();
      }
    }
  });

  $('#right-layout').layout('collapse', 'south');
  setTimeout(() => {
    hot.updateSettings({ height: $('#right-side__main-table').height() - 35 });
    hot.render();
  }, 200);

  $("#paymentStatus").combobox({
    valueField: 'paymentStatusValue',
    textField: 'paymentStatusKey',
    data: [{
      "paymentStatusValue": 'N',
      "paymentStatusKey": "Chưa thanh toán"
    }, {
      "paymentStatusValue": 'W',
      "paymentStatusKey": "Chờ ráp đơn giá",
      "selected": true
    }, {
      "paymentStatusValue": 'Y',
      "paymentStatusKey": "Đã thanh toán"
    }, {
      "paymentStatusValue": 'null',
      "paymentStatusKey": "Tất cả"
    }],
    onSelect: function (paymentStatus) {
      if (paymentStatus.paymentStatusValue != 'null') {
        shipment.params.paymentStatus = paymentStatus.paymentStatusValue;
      } else {
        shipment.params.paymentStatus = null;
      }
      loadTable();
    }
  });

  $('#fromDate').datebox({
    onSelect: function (date) {
      date.setHours(0, 0, 0);
      fromDate = date;
      if (toDate != null && date.getTime() > toDate.getTime()) {
        $.modal.alertWarning("Từ ngày không được lớn hơn đến ngày.");
      } else {
        shipment.params.fromDate = dateToString(date);
        loadTable();
      }
      return date;
    }
  });

  $('#toDate').datebox({
    onSelect: function (date) {
      date.setHours(23, 59, 59);
      toDate = date;
      if (fromDate != null && date.getTime() < fromDate.getTime()) {
        $.modal.alertWarning("Đến ngày không được thấp hơn từ ngày.");
      } else {
        shipment.params.toDate = dateToString(date);
        loadTable();
      }
    }
  });

  $("#logisticGroups").combobox({
    valueField: 'id',
    textField: 'groupName',
    data: logisticGroups,
    onSelect: function (logisticGroup) {
      if (logisticGroup.id != 0) {
        shipment.logisticGroupId = logisticGroup.id;
      } else {
        shipment.logisticGroupId = null;
      }
      $('#vesselAndVoyages').combobox('select', 'Chọn tàu chuyến');
      $("#containerNo").textbox('setText', '');
      $('#fromDate').datebox('setValue', '');
      $('#toDate').datebox('setValue', '');
      loadTable();
    }
  });

  $("#blBookingNo").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      shipment.params.blOrBooking = $("#blBookingNo").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  $("#containerNo").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      shipment.params.containerNo = $("#containerNo").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  vesselAndVoyages[0].selected = true;
  $("#vesselAndVoyages").combobox({
    valueField: 'vslAndVoy',
    textField: 'vslAndVoy',
    data: vesselAndVoyages,
    onSelect: function (vesselAndVoyage) {
      if (vesselAndVoyage.vslAndVoy != 'Chọn tàu chuyến') {
        let vslAndVoyArr = vesselAndVoyage.vslAndVoy.split(" - ");
        shipment.params.vslNm = vslAndVoyArr[0];
        shipment.params.voyNo = vslAndVoyArr[2];
      } else {
        shipment.params.vslNm = null;
        shipment.params.voyNo = null;
      }
      loadTable();
    }
  });

});

function dateformatter(date) {
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  var d = date.getDate();
  return (d < 10 ? ('0' + d) : d) + '/' + (m < 10 ? ('0' + m) : m) + '/' + y;
}
function dateparser(s) {
  var ss = (s.split('\.'));
  var d = parseInt(ss[0], 10);
  var m = parseInt(ss[1], 10);
  var y = parseInt(ss[2], 10);
  if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
    return new Date(y, m - 1, d);
  }
}


function loadTable() {
  $("#dg").datagrid({
    url: PREFIX + '/shipments',
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    method: 'POST',
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    onBeforeSelect: function (index, row) {
      getSelected(index, row);
    },
    pageSize: 50,
    nowrap: true,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
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
          data: shipment
        }),
        success: function (res) {
          if (res.code == 0) {
            success(res.shipments);
            $("#dg").datagrid("selectRow", 0);
          } else {
            success([]);
          }
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

// FORMATTER
// Format logistic name for clickable show link 
function formatLogistic(value, row, index) {
  return '<a onclick="logisticInfo(' + row.logisticGroupId + "," + "'" + value + "')\"> " + value + "</a>";
}
// FORMAT STATUS SHIPMENT
function formatStatus(value, row) {
  switch (value) {
    case '1':
      return 'Tạo Lô';
    case '2':
      return 'Khai Báo';
    case '3':
      return 'Làm Lệnh';
    case '4':
      return 'Kết Thúc';
    default:
      '';
  }
}
function formatType(value, row, index) {
  if (row.blNo) {
    return "Hàng nhập";
  }
  if (row.bookingNo) {
    return "Hàng xuất";
  }
  return "Không xác định";
}
function formatBlBooking(value, row, index) {
  if (row.blNo) {
    return row.blNo;
  }
  if (row.bookingNo) {
    return row.bookingNo;
  }
  return "Không xác định";
}
// FORMAT DATE FOR date time format dd/mm/yyyy
function formatDate(value) {
  let date = new Date(value);
  let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  let month = date.getMonth() + 1;
  let monthText = month < 10 ? "0" + month : month;
  return day + "/" + monthText + "/" + date.getFullYear();
}
// Trigger when click a row in easy ui data grid on the left screen
function getSelected(index, row) {
  if (row) {
    shipmentSelected = row;
    rowAmount = shipmentSelected.containerAmount;
    checkList = Array(rowAmount).fill(0);
    allChecked = false;
  }
  dischargePortList = [];
  loadShipmentDetails(shipmentSelected.id);
  loadListComment();
  let html = '';
  $.ajax({
    type: "GET",
    url: PREFIX + "/shipments/" + shipmentSelected.id + "/shipment-images",
    contentType: "application/json",
    success: function (data) {
      html += `<span>Mã lô: ` + shipmentSelected.id;
      if (shipmentSelected.blNo) {
        html += ` - BL No: ` + shipmentSelected.blNo + `</span>`;
      } else {
        html += ` - Booking No: ` + shipmentSelected.bookingNo + `</span>`;
      }
      if (data.code == 0) {
        if (data.shipmentFiles != null && data.shipmentFiles.length > 0) {
          data.shipmentFiles.forEach(function (element, index) {
            html += ' <a href="' + element.path + '" target="_blank"><i class="fa fa-paperclip" style="font-size: 18px;"></i> ' + (index + 1) + '</a>';
          });
        }
      }
      $('#shipment-info').html(html);
    },
    error: function (err) {
      html += `<span>Mã lô: ` + shipmentSelected.id;
      if (shipmentSelected.blNo) {
        html += ` - BL No: ` + shipmentSelected.blNo + `</span>`;
      } else {
        html += ` - Booking No: ` + shipmentSelected.bookingNo + `</span>`;
      }
      $('#shipment-info').html(html);
    }
  });
  if (sId != null) {
    $('#right-layout').layout('expand', 'south');
    shipment.id = null;
    sId = null;
  }
}

// FORMAT HANDSONTABLE COLUMN
function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
  let content = '';
  if (checkList[row] == 1) {
    content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')" checked></div>';
  } else {
    content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')"></div>';
  }
  $(td).attr('id', 'checkbox' + row).addClass("htCenter").addClass("htMiddle").html(content);
  return td;
}

function historyRenderer(instance, td, row, col, prop, value, cellProperties) {
  let historyIcon = '<a id="custom" onclick="openHistoryFormCatos(' + row + ')" class="fa fa-window-restore easyui-tooltip" title="Lịch Sử Catos" aria-hidden="true" style="color: #3498db;"></a>';
  $(td).addClass("htCenter").addClass("htMiddle").html(historyIcon);
}

function historyEportRenderer(instance, td, row, col, prop, value, cellProperties) {
  let historyIcon = '<a id="custom" onclick="openHistoryFormEport(' + row + ')" class="fa fa-history easyui-tooltip" title="Lịch Sử Eport" aria-hidden="true" style="color: #3498db;"></a>';
  $(td).addClass("htCenter").addClass("htMiddle").html(historyIcon);
}

function statusIconsRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'statusIcon' + row).addClass("htCenter").addClass("htMiddle");
  if (sourceData[row] && sourceData[row].processStatus && sourceData[row].paymentStatus && sourceData[row].finishStatus) {
    // Command process status
    let process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
    switch (sourceData[row].processStatus) {
      case 'W':
        process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
        break;
      case 'Y':
        process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
        break;
      case 'N':
        if (value > 1 && sourceData[row].contSupplyStatus == 'Y') {
          process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
        }
        break;
      case 'D':
        process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ hủy lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f93838;"></i>';
        break;
    }

    // Payment status
    let payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
    switch (sourceData[row].paymentStatus) {
      case 'E':
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Lỗi Thanh Toán" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
        break;
      case 'W':
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Ráp Đơn Giá" aria-hidden="true" style="margin-left: 8px; color : #f8ac59;"></i>';
        break;
      case 'Y':
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case 'N':
        if (value > 1) {
          payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        }
        break;
    }
    // released status
    let released = '<i id="finish" class="fa fa-ship easyui-tooltip" title="Chưa Thể Giao Container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
    switch (sourceData[row].finishStatus) {
      case "Y":
        released =
          '<i id="finish" class="fa fa-ship easyui-tooltip" title="Đã Giao Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case "N":
        if (sourceData[row].paymentStatus == "Y") {
          released =
            '<i id="finish" class="fa fa-ship easyui-tooltip" title="Có Thể Giao Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        }
        break;
    }

    // Return the content
    let content = '<div>' + process + payment + released + '</div>';
    $(td).html(content);
  }
  return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 200px; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value && sourceData) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function orderNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function payerNameRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function etaRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value || value == null) {
    value = '';
  }
  if (value != null && value != '') {
    if (value.substring(2, 3) != "/") {
      value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
    }
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function temperatureRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function commodityRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function loadingPortRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function specialServiceRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '';
  } else {
    if (specialServiceDictData != null) {
      specialServiceDictData.forEach(element => {
        if (value == element.dictLabel) {
          value = element.dictValue;
        }
      });
    }
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}

// CONFIGURATE HANDSONTABLE
function configHandsond() {
  config = {
    stretchH: "all",
    height: $('#right-side__main-table').height() - 35,
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
          var txt = "<input type='checkbox' class='checker' ";
          txt += "onclick='checkAll()' ";
          txt += ">";
          return txt;
        case 1:
          return '<a class="fa fa-window-restore easyui-tooltip" title="Lịch Sử Catos" aria-hidden="true" style="color: #3498db;"></a>';
        case 2:
          return '<a class="fa fa-history easyui-tooltip" title="Lịch Sử Catos" aria-hidden="true" style="color: #3498db;"></a>';
        case 3:
          return "Trạng Thái";
        case 4:
          return "Số Tham Chiếu";
        case 5:
          return 'Container No';
        case 6:
          return 'Dịch vụ';
        case 7:
          return 'Kích Thước';
        case 8:
          return 'Chủ Hàng';
        case 9:
          return 'Tàu và Chuyến';
        case 10:
          return "Ngày tàu đến";
        case 11:
          return 'Cảng Xếp Hàng';
        case 12:
          return 'Cảng Dỡ Hàng';
        case 13:
          return 'Trọng Lượng (kg)';
        case 14:
          return 'Loại Hàng';
        case 15:
          return 'Tên Hàng';
        case 16:
          return 'Số Seal';
        case 17:
          return "Nhiệt Độ (c)";
        case 18:
          return 'PTTT';
        case 19:
          return 'Mã Số Thuế';
        case 20:
          return 'Người Thanh Toán';
        case 21:
          return "Ghi Chú";
      }
    },
    colWidths: [23, 21, 21, 105, 130, 100, 120, 150, 150, 200, 100, 100, 120, 120, 80, 100, 100, 80, 80, 100, 130, 200],
    filter: "true",
    columns: [
      {
        data: "active",
        type: "checkbox",
        renderer: checkBoxRenderer
      },
      {
        data: "historyCatos",
        readOnly: true,
        renderer: historyRenderer
      },
      {
        data: "historyEport",
        readOnly: true,
        renderer: historyEportRenderer
      },
      {
        data: "status",
        readOnly: true,
        renderer: statusIconsRenderer
      },
      {
        data: "orderNo",
        renderer: orderNoRenderer
      },
      {
        data: "containerNo",
        strict: true,
        renderer: containerNoRenderer,
      },
      {
        data: "specialService",
        type: "autocomplete",
        source: specialService,
        strict: true,
        renderer: specialServiceRenderer
      },
      {
        data: "sztp",
        type: "autocomplete",
        source: sizeList,
        strict: true,
        renderer: sizeRenderer
      },
      {
        data: "consignee",
        strict: true,
        type: "autocomplete",
        source: consigneeList,
        renderer: consigneeRenderer
      },
      {
        data: "vslNm",
        type: "autocomplete",
        source: vslNmList,
        strict: true,
        renderer: vslNmRenderer
      },
      {
        data: "eta",
        renderer: etaRenderer
      },
      {
        data: "loadingPort",
        strict: true,
        type: "autocomplete",
        renderer: loadingPortRenderer
      },
      {
        data: "dischargePort",
        strict: true,
        type: "autocomplete",
        renderer: dischargePortRenderer
      },
      {
        data: "wgt",
        type: "numeric",
        strict: true,
        renderer: wgtRenderer
      },
      {
        data: "cargoType",
        strict: true,
        type: "autocomplete",
        source: cargoTypeList,
        renderer: cargoTypeRenderer
      },
      {
        data: "commodity",
        renderer: commodityRenderer
      },
      {
        data: "sealNo",
        renderer: sealNoRenderer
      },
      {
        data: "temperature",
        type: "numeric",
        strict: true,
        readonly: true,
        renderer: temperatureRenderer
      },
      {
        data: "payType",
        renderer: payTypeRenderer
      },
      {
        data: "payer",
        renderer: payerRenderer
      },
      {
        data: "payerName",
        renderer: payerNameRenderer
      },
      {
        data: "remark",
        renderer: remarkRenderer
      }
    ],
    beforeKeyDown: function (e) {
      let selected = hot.getSelected()[0];
      switch (e.keyCode) {
        // Arrow Left
        case 37:
          if (selected[3] == 0) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Up
        case 38:
          if (selected[2] == 0) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Right
        case 39:
          if (selected[3] == 21) {
            e.stopImmediatePropagation();
          }
          break
        // Arrow Down
        case 40:
          if (selected[2] == shipmentSelected.containerAmount - 1) {
            e.stopImmediatePropagation();
          }
          break
        default:
          break;
      }
    },
    afterChange: onChange,
    beforeCopy: beforeCopy
  };
}

configHandsond();
hot = new Handsontable(dogrid, config);

function beforeCopy(data, coords) {
  if (coords[0].startCol == containerCol && coords[0].endCol == containerCol) {
    if (data.length > 1) {
      for (let i = 0; i < data.length - 1; i++) {
        data[i][0] = data[i][0] + ',';
      }
    }
  }
}

function onChange(changes, source) {
  if (!changes) {
    return;
  }
  changes.forEach(function (change) {

    // Trigger when vessel-voyage no change, get list discharge port by vessel, voy no
    if (change[1] == "vslNm" && change[3] != null && change[3] != '') {
      let vesselAndVoy = hot.getDataAtCell(change[0], 9);
      //hot.setDataAtCell(change[0], 10, ''); // dischargePort reset
      if (vesselAndVoy) {
        if (currentVesselVoyage != vesselAndVoy) {
          currentVesselVoyage = vesselAndVoy;
          let shipmentDetail = new Object();
          for (let i = 0; i < berthplanList.length; i++) {
            if (vesselAndVoy == berthplanList[i].vslAndVoy) {
              shipmentDetail.vslNm = berthplanList[i].vslNm;
              shipmentDetail.voyNo = berthplanList[i].voyNo;
              shipmentDetail.year = berthplanList[i].year;
              $.modal.loading("Đang xử lý ...");
              $.ajax({
                url: PREFIX + "/vessel/pods",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(shipmentDetail),
                success: function (data) {
                  $.modal.closeLoading();
                  if (data.code == 0) {
                    hot.updateSettings({
                      cells: function (row, col, prop) {
                        if (col == 11 || col == 12) {
                          let cellProperties = {};
                          dischargePortList = data.dischargePorts;
                          cellProperties.source = dischargePortList;
                          return cellProperties;
                        }
                      }
                    });
                  }
                }
              });
            }
          }
        }
      }
    }
  });
}

function loadShipmentDetails(id) {
  if (id) {
    $.modal.loading("Đang xử lý ...");
    $.ajax({
      url: PREFIX + "/shipment/" + id + "/shipmentDetails",
      method: "GET",
      success: function (res) {
        $.modal.closeLoading();
        if (res.code == 0) {
          checkList = Array(rowAmount).fill(0);
          allChecked = false;
          $('.checker').prop('checked', false);
          for (let i = 0; i < checkList.length; i++) {
            $('#check' + i).prop('checked', false);
          }
          sourceData = res.shipmentDetails;
          sourceData.forEach(function (element, index) {
            element.vslNm = element.vslNm + ' - ' + element.voyNo;
          });
          hot.destroy();
          currentConsigneeList = consigneeList;
          configHandsond();
          hot = new Handsontable(dogrid, config);
          hot.loadData(sourceData);
          hot.render();
        }
      },
      error: function (data) {
        $.modal.closeLoading();
      }
    });
  } else {
    hot.destroy();
    currentConsigneeList = consigneeList;
    configHandsond();
    hot = new Handsontable(dogrid, config);
    hot.render();
  }
}

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
  if (!allChecked) {
    allChecked = true
    for (let i = 0; i < checkList.length; i++) {
      if (hot.getDataAtCell(i, 3) == null) {
        break;
      }
      checkList[i] = 1;
      $('#check' + i).prop('checked', true);
    }
  } else {
    allChecked = false;
    checkList = Array(rowAmount).fill(0);
    for (let i = 0; i < checkList.length; i++) {
      $('#check' + i).prop('checked', false);
    }
  }
  let tempCheck = allChecked;
  hot.render();
  allChecked = tempCheck;
  $('.checker').prop('checked', tempCheck);
}
function check(id) {
  if (sourceData[id].id != null) {
    if (checkList[id] == 0) {
      $('#check' + id).prop('checked', true);
      checkList[id] = 1;
    } else {
      $('#check' + id).prop('checked', false);
      checkList[id] = 0;
    }
    hot.render();
    updateLayout();
  }
}
function updateLayout() {
  allChecked = true;
  for (let i = 0; i < checkList.length; i++) {
    let cellStatus = hot.getDataAtCell(i, 3);
    if (cellStatus != null) {
      if (checkList[i] != 1) {
        allChecked = false;
      }
    }
  }
  $('.checker').prop('checked', allChecked);
}

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataSelectedFromTable() {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  if (myTableData && checkList) {
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
    processOrderIds = '';
    let temProcessOrderIds = [];
    $.each(cleanedGridData, function (index, object) {
      let cargoType = '', dischargePort = '', loadingPort = '', sztp = '';
      let expiredDem, emptyExpiredDem;
      if (object["cargoType"]) {
        cargoType = object["cargoType"].substring(0, 2);
      }
      if (object["dischargePort"]) {
        dischargePort = object["dischargePort"].split(": ")[0];
      }
      if (object["loadingPort"]) {
        loadingPort = object["loadingPort"].split(": ")[0];
      }
      if (object["sztp"]) {
        sztp = object["sztp"].split(": ")[0];
      }
      let shipmentDetail = {
        id: object["id"],
        blNo: object["blNo"],
        bookingNo: object["bookingNo"],
        containerNo: object["containerNo"],
        opeCode: object["opeCode"],
        sztp: sztp,
        expiredDem: expiredDem,
        emptyExpiredDem: emptyExpiredDem,
        emptyDepot: object["emptyDepot"],
        detFreeTime: object["detFreeTime"],
        consignee: object["consignee"],
        wgt: object["wgt"],
        cargoType: cargoType,
        loadingPort: loadingPort,
        dischargePort: dischargePort,
        payType: object["payType"],
        payer: object["payer"],
        orderNo: object["orderNo"],
        paymentStatus: object["paymentStatus"]
      };
      if (berthplanList) {
        for (let i = 0; i < berthplanList.length; i++) {
          if (object["vslNm"] == berthplanList[i].vslAndVoy) {
            shipmentDetail.vslNm = berthplanList[i].vslNm;
            shipmentDetail.voyNo = berthplanList[i].voyNo;
            shipmentDetail.year = berthplanList[i].year;
            shipmentDetail.vslName = berthplanList[i].vslAndVoy.split(" - ")[1];
            shipmentDetail.voyCarrier = berthplanList[i].voyCarrier;
            shipmentDetail.etd = berthplanList[i].etd;
            shipmentDetail.eta = berthplanList[i].eta;
          }
        }
      }
      shipmentDetails.push(shipmentDetail);
      shipmentDetailIds += object["id"] + ",";
      if (object["processOrderId"] != null && !temProcessOrderIds.includes(object["processOrderId"])) {
        temProcessOrderIds.push(object["processOrderId"]);
        processOrderIds += object["processOrderId"] + ',';
      }
    });

    if (processOrderIds != '') {
      processOrderIds = processOrderIds.substring(0, processOrderIds.length - 1);
    }

    if (shipmentDetailIds.length == 0) {
      $.modal.alertWarning("Bạn chưa chọn container nào.")
      errorFlg = true;
    } else {
      shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length - 1);
    }
  } else {
    $.modal.alertWarning("Bạn chưa chọn lô.");
    errorFlg = true;
  }

  if (errorFlg) {
    return false;
  } else {
    return true;
  }
}

function search() {
  loadTable();
}

function clearInput() {
  $('#vesselAndVoyages').combobox('select', 'Chọn tàu chuyến');
  $('#logisticGroups').combobox('select', '0');
  $("#containerNo").textbox('setText', '');
  $("#blBookingNo").textbox('setText', '');
  $('#fromDate').datebox('setValue', '');
  $('#toDate').datebox('setValue', '');
  $("#paymentStatus").combobox('select', 'N');
  shipment = new Object();
  shipment.params = new Object();
  fromDate = null;
  toDate = null;
  loadTable();
}

function logisticInfo(id, logistics) {
  $.modal.openLogisticInfo("Thông tin liên lạc " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function () {
    $.modal.close();
  });
}

function dateToString(date) {
  return ("0" + date.getDate()).slice(-2) + "/" + ("0" + (date.getMonth() + 1)).slice(-2) + "/" + date.getFullYear()
    + " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2) + ":" + ("0" + date.getSeconds()).slice(-2);
}

function loadListComment(shipmentCommentId) {
  let req = {
    shipmentId: shipmentSelected.id
  };
  $.ajax({
    url: ctx + "shipment-comment/shipment/list",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify(req),
    success: function (data) {
      if (data.code == 0) {
        let html = '';
        // set title for panel comment
        let commentTitle = '<span style="color: black">Hỗ Trợ<span>';
        let commentNumber = 0;
        if (data.shipmentComments != null) {
          data.shipmentComments.forEach(function (element, index) {
            let createTime = element.createTime;
            let date = '';
            let time = '';
            if (createTime) {
              date = createTime.substring(8, 10) + "/" + createTime.substring(5, 7) + "/" + createTime.substring(0, 4);
              time = createTime.substring(10, 19);
            }

            let resolvedBackground = '';
            if ((shipmentCommentId && shipmentCommentId == element.id) || !element.resolvedFlg) {
              resolvedBackground = 'style="background-color: #ececec;"';
              commentNumber++;
            }

            html += '<div ' + resolvedBackground + '>';
            // User name comment and date time comment
            html += '<div><i style="font-size: 15px; color: #015198;" class="fa fa-user-circle" aria-hidden="true"></i><span> <a>' + element.userName + ' (' + element.userAlias + ')</a>: <i>' + date + ' at ' + time + '</i></span></div>';
            // Topic comment
            html += '<div><span><strong>Yêu cầu:</strong> ' + element.topic + '</span></div>';
            // Content comment
            html += '<div><span>' + element.content.replaceAll("#{domain}", domain) + '</span></div>';
            html += '</div>';
            html += '<hr>';
          });
        }
        commentTitle += ' <span class="round-notify-count">' + commentNumber + '</span>';
        $('#right-layout').layout('panel', 'expandSouth').panel('setTitle', commentTitle);
        $('#commentList').html(html);
        // $("#comment-div").animate({ scrollTop: $("#comment-div")[0].scrollHeight}, 1000);
      }
    }
  });
}

function addComment() {
  let topic = $('#topic').textbox('getText');
  let content = $('.summernote').summernote('code');// get editor content
  let errorFlg = false;
  if (!topic) {
    errorFlg = true;
    $.modal.alertWarning('Vui lòng nhập chủ đề.');
  } else if (!content) {
    errorFlg = true;
    $.modal.alertWarning('Vui lòng nhập nội dung.');
  }
  if (!errorFlg) {
    let req = {
      topic: topic,
      content: content,
      shipmentId: shipmentSelected.id,
      serviceType: shipmentSelected.serviceType,
      logisticGroupId: shipmentSelected.logisticGroupId
    };
    $.ajax({
      url: PREFIX + "/shipment/comment",
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
          $('#topic').textbox('setText', '');
          $('.summernote').summernote('code', '');
        } else {
          $.modal.msgError("Gửi thất bại.");
        }
      },
      error: function (error) {
        $.modal.closeLoading();
        $.modal.msgError("Gửi thất bại.");
      }
    });
  }
}

function reloadShipmentDetail() {
  checkList = Array(rowAmount).fill(0);
  allChecked = false;
  $('.checker').prop('checked', false);
  for (let i = 0; i < checkList.length; i++) {
    $('#check' + i).prop('checked', false);
  }
  // loadTable();
  loadShipmentDetails(shipmentSelected.id);
}

function updateShipmentDetail() {
  if (getDataSelectedFromTable()) {
    $.modal.loading("Đang xử lý...");
    $.ajax({
      url: PREFIX + "/shipment-detail",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(shipmentDetails),
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
        $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại.");
        $.modal.closeLoading();
      },
    });
  }
}

function openHistoryFormCatos(row) {
  let containerInfo = sourceData[row];
  let vslCd = '';
  if (containerInfo.vslNm) {
    vslCd = containerInfo.vslNm.split(" ")[0];
  }
  let voyNo = containerInfo.voyNo;
  let containerNo = containerInfo.containerNo;
  // service type current shipment is pickup empty
  if (shipmentSelected.serviceType == 3) {
    vslCd = 'EMTY';
    voyNo = '0000';
  }
  if (containerInfo == null || !containerNo || !vslCd || !voyNo) {
    $.modal.alertWarning("Container chưa được khai báo.");
  } else {
    layer.open({
      type: 2,
      area: [1002 + 'px', 500 + 'px'],
      fix: true,
      maxmin: true,
      shade: 0.3,
      title: 'Lịch Sử Container ' + containerNo + ' Catos',
      content: HIST_PREFIX + "/container/history/" + voyNo + "/" + vslCd + "/" + containerNo,
      btn: ["Đóng"],
      shadeClose: false,
      yes: function (index, layero) {
        layer.close(index);
      }
    });
  }
}

function openHistoryFormEport(row) {
  let containerInfo = sourceData[row];
  if (containerInfo == null || !containerInfo.id) {
    $.modal.alertWarning("Container chưa được khai báo.");
  } else {
    layer.open({
      type: 2,
      area: [967 + 'px', 500 + 'px'],
      fix: true,
      maxmin: true,
      shade: 0.3,
      title: 'Lịch Sử Container ' + (containerInfo.containerNo != null ? containerInfo.containerNo : '') + ' Eport',
      content: HIST_PREFIX + "/container/history/" + containerInfo.id,
      btn: ["Đóng"],
      shadeClose: false,
      yes: function (index, layero) {
        layer.close(index);
      }
    });
  }
}

function applyPrice() {
  if (getDataSelectedFromTable()) {
    layer.open({
      type: 2,
      area: [600 + 'px', 300 + 'px'],
      fix: true,
      maxmin: true,
      shade: 0.3,
      title: 'Ráp đơn giá',
      content: PREFIX + "/price",
      btn: ["Xác Nhận", "Hủy"],
      shadeClose: false,
      yes: function (index, layero) {
        applyPriceReq(index, layero);
      },
      cancel: function (index) {
        return true;
      }
    });
  }
}

function applyPriceReq(index, layero) {
  let childLayer = layero.find("iframe")[0].contentWindow.document;
  $.modal.loading("Đang xử lý ...");
  $.ajax({
    url: PREFIX + "/price",
    method: "POST",
    data: {
      invoiceNo: $(childLayer).find("#invoiceNo").val(),
      price: $(childLayer).find("#price").val(),
      shipmentDetailIds: shipmentDetailIds,
      shipmentId: shipmentSelected.id,
      logisticGroupId: shipmentSelected.logisticGroupId
    },
    success: function (res) {
      layer.close(index);
      reloadShipmentDetail();
      $.modal.closeLoading();
      if (res.code == 0) {
        $.modal.alertSuccess(res.msg);
      } else {
        $.modal.alertError(res.msg);
      }
    },
    error: function (data) {
      layer.close(index);
      reloadShipmentDetail();
      $.modal.closeLoading();
    }
  });
}

