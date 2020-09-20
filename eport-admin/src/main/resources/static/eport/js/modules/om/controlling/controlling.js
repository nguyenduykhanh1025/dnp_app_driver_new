const PREFIX = ctx + "om/controlling";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, checkList, allChecked, sourceData, rowAmount = 0, shipmentDetailIds;
var fromDate, toDate;
var shipment = new Object();
shipment.params = new Object();

$( document ).ready(function() {
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

  $('#fromDate').datebox({
    onSelect: function(date){
      date.setHours(0,0,0);
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
    onSelect: function(date){
      date.setHours(23,59,59);
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
      $('#doStatus').combobox('select', 'null');
      $("#containerNo").textbox('setText', '');
      $('#fromDate').datebox('setValue', '');
      $('#toDate').datebox('setValue', '');
      loadTable();
    }
  });

  $("#blNo").textbox('textbox').bind('keydown', function(e) {
    // enter key
    if (e.keyCode == 13) {
      shipment.params.blOrBooking = $("#blNo").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  $("#containerNo").textbox('textbox').bind('keydown', function(e) {
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

  $("#serviceType").combobox({
    valueField: 'serviceId',
    textField: 'text',
    data: [{
      "serviceId": 'null',
      "text": "Loại Dịch Vụ",
      "selected": true
    }, {
      "serviceId": 1,
      "text": "Bốc Hàng"
    }, {
      "serviceId": 2,
      "text": "Hạ Rỗng"
    }, {
      "serviceId": 3,
      "text": "Bốc Rỗng"
    }, {
      "serviceId": 4,
      "text": "Hạ Hàng"
    }],
    onSelect: function (serviceType) {
      if (serviceType.serviceId != 'null') {
        shipment.serviceType = serviceType.serviceId;
      } else {
        shipment.serviceType = null;
      }
      loadTable();
    }
  });

});

function dateformatter(date){
  var y = date.getFullYear();
  var m = date.getMonth()+1;
  var d = date.getDate();
  return (d<10?('0'+d):d) + '/' + (m<10?('0'+m):m) + '/' + y;
}
function dateparser(s){
  var ss = (s.split('\.'));
  var d = parseInt(ss[0],10);
  var m = parseInt(ss[1],10);
  var y = parseInt(ss[2],10);
  if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
    return new Date(y,m-1,d);
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
    onClickRow: function () {
      getSelected();
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

// FORMAT DATE FOR date time format dd/mm/yyyy
function formatDate(value) {
  let date = new Date(value);
  let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  let month = date.getMonth() + 1;
  let monthText = month < 10 ? "0" + month : month;
  return day + "/" + monthText + "/" + date.getFullYear();
}
// FORMAT SERVICE TYPE
function formatServiceType(value, row) {
  switch (value) {
    case 1:
      return 'Bốc Hàng';
    case 2:
      return 'Hạ Rỗng';
    case 3:
      return 'Bốc Rỗng';
    case 4:
      return 'Hạ Hàng';
    default:
      return '';
  }
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

// FORMAT BOOKING OR BL NO
function formatBlBooking(value, row) {
  if (row.bookingNo) {
    return row.bookingNo;
  } else if (row.blNo) {
    return row.blNo;
  }
  return '';
}

// Trigger when click a row in easy ui data grid on the left screen
function getSelected() {
  let row = $("#dg").datagrid("getSelected");
  if (row) {
    shipmentSelected = row;
    rowAmount = shipmentSelected.containerAmount;
    checkList = Array(rowAmount).fill(0);
    allChecked = false;
  }
  loadShipmentDetails(shipmentSelected.id);
  loadListComment();
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
  let historyIcon = customs = '<a id="custom" class="fa fa-history easyui-tooltip" title="Lịch Sử" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></a>';
  $(td).addClass("htCenter").addClass("htMiddle").html(historyIcon);
}

function statusIconsRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'statusIcon' + row).addClass("htCenter").addClass("htMiddle");
  if (sourceData[row] && sourceData[row].dischargePort && sourceData[row].processStatus && sourceData[row].paymentStatus && sourceData[row].finishStatus) {
    // Customs Status
    let customs = '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
    if (sourceData[row].customStatus) {
      switch (sourceData[row].customStatus) {
        case 'R':
          customs = '<i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
          break;
        case 'Y':
          customs = '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #ed5565;"></i>';
          break;
        case 'N':
          customs = '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
          break;
      }
    }
    // Command process status
    let process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
    switch (sourceData[row].processStatus) {
      case 'E':
        process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
        break;
    case 'Y':
      process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
        break;
    case 'N':
      if((value > 1 && shipmentSelected.serviceType != 1) || (value > 2 && shipmentSelected.serviceType == 1)) {
        process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
      } 
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
    case 'N':
      if(value > 2) {
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      }
        break;
    }
    // Do status
    let doStatus = '<i id="do" class="fa fa-file-text easyui-tooltip" title="Chưa Thu Chứng TỪ" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
    if (sourceData[row].doStatus && sourceData[row].doStatus == 'Y') {
      doStatus = '<i id="do" class="fa fa-file-text easyui-tooltip" title="Đã Thu Chứng TỪ" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
    }
    // released status
    let released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Chưa thể nhận container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
    switch (sourceData[row].finishStatus) {
    case 'Y':
      released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Đã Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
    case 'N':
      if(sourceData[row].paymentStatus == 'Y') {
        released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Có Thể Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      }
        break;
    }
    // Return the content
    let content = '<div>';
    // Domestic cont: VN --> not show
    switch (shipmentSelected.serviceType) {
      case 1:
        content += customs + process + payment + doStatus + released + '</div>';
        break;
      case 2:
        content += process + payment + released + '</div>';
        break;
      case 3:
        content += process + payment + released + '</div>';
        break;
      case 4:
        if(sourceData[row].loadingPort && sourceData[row].loadingPort.substring(0,2) != 'VN') {
          content += customs;
        }
        content += process + payment + released + '</div>';
        break;
      default:
        break;
    }
    $(td).html(content);
  }
  return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function sztpRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
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
function emptyExpiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
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
function emptyDepotRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function detFreeTimeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 200px; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  if (sourceData[row] && sourceData[row].vslNm && sourceData[row].voyNo) {
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + sourceData[row].vslNm + ' - ' + sourceData[row].voyNo + '</div>');
  }
  return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function loadingPortRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function orderNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function invoiceNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = '--';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
  config = {
    stretchH: "all",
    height: $('#right-side__main-table').height() - 35,
    minRows: rowAmount,
    maxRows: rowAmount,
    width: "100%",
    minSpareRows: 0,
    rowHeights: 30,
    fixedColumnsLeft: 5,
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
          return ""
        case 2:
          return "Trạng Thái";
        case 3:
          return "Số Container";
        case 4:
          return "Sztp";
        case 5:
          return "Hạn Lệnh";
        case 6:
          return "Hạn Trả Vỏ";
        case 7:
          return "Nơi Hạ Vỏ";
        case 8:
          return "Miễn Lưu";
        case 9:
          return "Chủ Hàng";
        case 10:
          return "Tàu - Chuyến";
        case 11:
          return "Trọng Lượng";
        case 12:
          return "Loại Hàng";
        case 13:
          return "Cảng Dỡ"
        case 14:
          return "Cảng Xếp Hàng";
        case 15:
          return "P.T.T.T";
        case 16:
          return "Payer";
        case 17:
          return "Số Tham Chiếu";
        case 18:
          return "Ghi Chú";
      }
    },
    colWidths: [ 50, 50, 120, 100, 60, 100, 100, 100, 80, 200, 150, 100, 100, 100, 100, 100, 100, 130, 100],
    filter: "true",
    columns: [
      {
        data: "active",
        type: "checkbox",
        renderer: checkBoxRenderer
      },
      {
        data: "history",
        readOnly: true,
        renderer: historyRenderer
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
        data: "sztp",
        renderer: sztpRenderer
      },
      {
        data: "expiredDem",
        type: "date",
        dateFormat: "DD-MM-YYYY",
        defaultDate: new Date(),
        renderer: expiredDemRenderer
      },
      {
        data: "emptyExpiredDem",
        type: "date",
        dateFormat: "DD-MM-YYYY",
        defaultDate: new Date(),
        renderer: emptyExpiredDemRenderer
      },
      {
        data: "emptyDepot",
        renderer: emptyDepotRenderer
      },
      {
        data: "detFreeTime",
        renderer: detFreeTimeRenderer
      },
      {
        data: "consignee",
        renderer: consigneeRenderer
      },
      {
        data: "vslNm",
        renderer: vslNmRenderer
      },
      {
        data: "wgt",
        renderer: wgtRenderer
      },
      {
        data: "cargoType",
        renderer: cargoTypeRenderer
      },
      {
        data: "dischargePort",
        renderer: dischargePortRenderer
      },
      {
        data: "loadingPort",
        renderer: loadingPortRenderer
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
        data: "orderNo",
        renderer: orderNoRenderer
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
          if (selected[3] == 19) {
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
  };
}
configHandson();
hot = new Handsontable(dogrid, config);

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
          hot.destroy();
          configHandson();
          hot = new Handsontable(dogrid, config);
          hot.loadData(sourceData);
          hot.render();
        }
      },
      error: function (data) {
        $.modal.closeLoading();
      }
    });
  }
}

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
  if (!allChecked) {
    allChecked = true
    checkList = Array(rowAmount).fill(0);
    for (let i = 0; i < checkList.length; i++) {
      if (hot.getDataAtCell(i, 1) == null) {
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
  updateLayout();
  hot.render();
  allChecked = tempCheck;
  $('.checker').prop('checked', allChecked);
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
    if (hot.getDataAtCell(i, 1) != null) {
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
    $.each(cleanedGridData, function (index, object) {
      shipmentDetailIds += object["id"] + ",";
    });
  
    if (shipmentDetailIds.length == 0) {
      $.modal.alertWarning("Bạn chưa chọn container nào.")
      errorFlg = true;
    } else {
      shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length-1);
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
  $('#serviceType').combobox('select', 'null');
  $('#logisticGroups').combobox('select', '0');
  $("#containerNo").textbox('setText', '');
  $("#blNo").textbox('setText', '');
  $('#fromDate').datebox('setValue', '');
  $('#toDate').datebox('setValue', '');
  shipment = new Object();
  shipment.params = new Object();
  fromDate = null;
  toDate = null;
  loadTable();
}

function logisticInfo(id, logistics) {
  $.modal.openLogisticInfo("Thông tin liên lạc " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function() {
    $.modal.close();
  });
}

function dateToString(date) {
  return ("0" + date.getDate()).slice(-2) + "/" + ("0"+(date.getMonth()+1)).slice(-2) + "/" + date.getFullYear()
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
            html += '<div><span>' + element.content + '</span></div>';
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

