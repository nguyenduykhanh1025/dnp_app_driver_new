const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var prefix = ctx + "logistic/receive-cont-full";
var interval, currentPercent, timeout;
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, shipmentDetails, shipmentDetailIds, sourceData, orderNumber = 0, isChange;
var contList = [], orders = [], processOrderIds, taxCodeArr;
var conts = '';
var allChecked = false, dnDepot = false;
var checkList = [];
var rowAmount = 0;
var shipmentSearch = new Object();
shipmentSearch.params = new Object();
shipmentSearch.serviceType = 1;
var sizeList = [];
var voyCarrier;
var onChangeFlg = false, currentIndexRow, rejectChange = false;
var fromDate, toDate;
//dictionary sizeList
$.ajax({
	  type: "GET",
	  url: ctx + "logistic/size/container/list",
	  success(data) {
		  if(data.code == 0){
		      data.data.forEach(element => {
		    	  sizeList.push(element['dictLabel'])
		      })
		  }
	  }
})
var consigneeList, opeCodeList, dischargePortList, vslNmList;
$.ajax({
  url: ctx + "logistic/source/option",
  method: "GET",
  success: function (data) {
    if (data.code == 0) {
      dischargePortList = data.dischargePortList;
      opeCodeList = data.opeCodeList;
      vslNmList = data.vslNmList;
    }
  }
});

$.ajax({
  url: ctx + "logistic/source/taxCode/consignee",
  method: "GET",
  success: function (data) {
    if (data.code == 0) {
      consigneeList = data.consigneeList;
    }
  }
});

var toolbar = [
  {
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
      removeShipment()
    },
  },
  {
    text: '<button class="btn btn-sm btn-default"><i class="fa fa-refresh text-success"></i></button>',
    handler: function () {
      handleRefresh();
    },
  },
];

$(".main-body").layout();

$(".collapse").click(function () {
  $(".main-body__search-wrapper").hide();
  $(".main-body__search-wrapper--container").hide();
  $(this).hide();
  $(".uncollapse").show();
});

$(".uncollapse").click(function () {
  $(".main-body__search-wrapper").show();
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


$('#main-layout').layout({
  onExpand: function (region) {
    if (region == "west") {
      hot.render();
    }
  }
})

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {

  $("#blNo").textbox('textbox').bind('keydown', function(e) {
    // enter key
    if (e.keyCode == 13) {
      shipmentSearch.blNo = $("#blNo").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  $("#containerNo").textbox('textbox').bind('keydown', function(e) {
    // enter key
    if (e.keyCode == 13) {
      shipmentSearch.params.containerNo = $("#containerNo").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  $("#consignee").textbox('textbox').bind('keydown', function(e) {
    // enter key
    if (e.keyCode == 13) {
      shipmentSearch.params.consignee = $("#consignee").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  $('#fromDate').datebox({
    onSelect: function(date){
      date.setHours(0,0,0);
      fromDate = date;
      if (toDate != null && date.getTime() > toDate.getTime()) {
        $.modal.alertWarning("Từ ngày không được lớn hơn đến ngày.");
      } else {
        shipmentSearch.params.fromDate = dateToString(date);
        loadTable();
      }
      return date;
    }
  });

  let now = new Date();
  now.setHours(0, 0, 0, 0);
  $('#fromDate').datebox('setValue', ("0" + now.getDate()).slice(-2) + "/" + ("0" + (now.getMonth() + 1)).slice(-2) + "/" + now.getFullYear());
  shipmentSearch.params.fromDate = dateToString(now);

  $('#toDate').datebox({
    onSelect: function(date){
      date.setHours(23,59,59);
      toDate = date;
      if (fromDate != null && date.getTime() < fromDate.getTime()) {
        $.modal.alertWarning("Đến ngày không được thấp hơn từ ngày.");
      } else {
        shipmentSearch.params.toDate = dateToString(date);
        loadTable();
      }
    }
  });
  $(function () {
    let options = {
      createUrl: prefix + "/shipment/add",
      updateUrl: "0",
      modalName: " Lô"
    };
    $.table.init(options);
  });

  loadTable();
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

// LOAD SHIPMENT LIST
function loadTable() {
  $("#dg").datagrid({
    url: ctx + "logistic/shipments",
    height: $('.main-body').height() - 75,
    method: 'post',
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    rownumbers:true,
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
          data: shipmentSearch
        }),
        success: function (data) {
          success(data);
          $("#dg").datagrid("hideColumn", "id");
          $("#dg").datagrid("hideColumn", "edoFlg");
          if (currentIndexRow != null) {
            $("#dg").datagrid("selectRow", currentIndexRow);
          }
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
  let minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
  return day + "/" + monthText + "/" + date.getFullYear() + " " + hours + ":" + minutes;
}

function formatBlNo(value, row) {
  if (row.houseBill) {
    return row.houseBill;
  }
  return value;
}

// FORMAT REMARK FOR SHIPMENT LIST
function formatRemark(value) {
  return '<div class="easyui-tooltip" title="' + (value != null ? value : "Trống") + '" style="width: 80; text-align: center;"><span>' + (value != null ? value : "") + '</span></div>';
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
    if (onChangeFlg) {
      layer.confirm("Thông tin khái báo chưa được lưu, quý khách có muốn di chuyển qua trang khác?", {
        icon: 3,
        title: "Xác Nhận",
        btn: ['Đồng Ý', 'Hủy Bỏ']
      }, function () {
        layer.close(layer.index);
        currentIndexRow = index;
        if (row) {
          shipmentSelected = row;
          $(function () {
            let options = {
              createUrl: prefix + "/shipment/add",
              updateUrl: prefix + "/shipment/" + shipmentSelected.id,
              modalName: " Lô"
            };
            $.table.init(options);
          });
          let title = '';
          title += 'Mã Lô: ' + row.id + ' - ';
          title += 'SL: ' + row.containerAmount + ' - ';
          if (row.edoFlg == "0") {
            title += 'Loại lệnh: DO - ';
            $("#deleteBtn").show();
          } else {
            title += 'Loại lệnh: eDO - ';
            $("#deleteBtn").hide();
          }
          title += 'B/L No: ' + row.blNo;
          $('#main-layout').layout('panel', 'center').panel('setTitle', title);
          rowAmount = row.containerAmount;
          checkList = Array(rowAmount).fill(0);
          allChecked = false;
          loadShipmentDetail(row.id);
          onChangeFlg = false;
          currentIndexRow = index;
        }
        return true;
      }, function () {
        layer.close(layer.index);
        rejectChange = true;
        $('#dg').datagrid('selectRow', currentIndexRow);
        return false;
      });
    } else {
      currentIndexRow = index;
      if (row) {
        shipmentSelected = row;
        $(function () {
          let options = {
            createUrl: prefix + "/shipment/add",
            updateUrl: prefix + "/shipment/" + shipmentSelected.id,
            modalName: " Lô"
          };
          $.table.init(options);
        });
        let title = '';
        title += 'Mã Lô: ' + row.id + ' - ';
        title += 'SL: ' + row.containerAmount + ' - ';
        if (row.edoFlg == "0") {
          title += 'Loại lệnh: DO - ';
          $("#deleteBtn").show();
        } else {
          title += 'Loại lệnh: eDO - ';
          $("#deleteBtn").hide();
        }
        title += 'B/L No: ' + row.blNo;
        $('#main-layout').layout('panel', 'center').panel('setTitle', title);
        rowAmount = row.containerAmount;
        checkList = Array(rowAmount).fill(0);
        allChecked = false;
        loadShipmentDetail(row.id);
        onChangeFlg = false;
        currentIndexRow = index;
      }
      return true;
    }
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
function statusIconsRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'statusIcon' + row).addClass("htCenter").addClass("htMiddle");
  if (sourceData[row] && sourceData[row].dischargePort && sourceData[row].processStatus && sourceData[row].paymentStatus && sourceData[row].customStatus && sourceData[row].finishStatus) {
    // Customs Status
    let customs = '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
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
      if(value > 1) {
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
    if(sourceData[row].loadingPort.substring(0,2) != 'VN') {
      content += customs;
    }
    content += process + payment + released + '</div>';
    $(td).html(content);
  }
  return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (value != null && value != '') {
    if (hot.getDataAtCell(row, 1) != null) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = '';
  }
  $(td).attr('id', 'containerNo' + row).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (shipmentSelected.edoFlg == "1") {
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
  }
  if (value != null && value != '') {
    if (value.substring(2, 3) != "/") {
      value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
    }
    $(td).attr('id', 'expiredDem' + row).addClass("htMiddle").addClass("htCenter");
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  } else {
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;"></div>');
  }
  return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'consignee' + row).addClass("htMiddle");
  if (value != null && value != '') {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function emptyDepotRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'emptyDepot' + row).addClass("htMiddle");
  if (value != null && value != '') {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
      cellProperties.readOnly = 'true';
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "#C6EFCE");
  $(td).css("color", "#006100");
  $(td).attr('id', 'opeCode' + row).addClass("htMiddle");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "#C6EFCE");
  $(td).css("color", "#006100");
  $(td).attr('id', 'vslNm' + row).addClass("htMiddle");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "#C6EFCE");
  $(td).css("color", "#006100");
  $(td).attr('id', 'voyNo' + row).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "#C6EFCE");
  $(td).css("color", "#006100");
  $(td).attr('id', 'sztp' + row).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "#C6EFCE");
  $(td).css("color", "#006100");
  $(td).attr('id', 'sztp' + row).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "#C6EFCE");
  $(td).css("color", "#006100");
  $(td).attr('id', 'wgt' + row).addClass("htMiddle").addClass("htRight");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function loadingPortRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "#C6EFCE");
  $(td).css("color", "#006100");
  $(td).attr('id', 'loadingPort' + row).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "#C6EFCE");
  $(td).css("color", "#006100");
  $(td).attr('id', 'dischargePort' + row).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}

function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  $(td).attr('id', 'payType' + row).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  $(td).attr('id', 'payer' + row).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function payerNameRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  $(td).attr('id', 'payerNamer' + row).addClass("htMiddle");
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'remark' + row).addClass("htMiddle");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}

function detFreeTimeRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (shipmentSelected.edoFlg == "1") {
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
  }
  $(td).attr('id', 'detFreeTime' + row).addClass("htMiddle").addClass("htRight");
  if (!value) {
    value = '';
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
  config = {
    stretchH: "all",
    height: $('.main-body').height() - 110,
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
          return '<span class="required">Hạn Lệnh</span>';
        case 4:
          return 'Ngày Miễn<br>Lưu Bãi';
        case 5:
          return '<span class="required">Chủ Hàng</span>';
        case 6:
          return '<span class="required">Nơi Hạ Vỏ</span>';
        case 7:
            return "Kích Thước";
        case 8:
          return '<span class="required">Hãng Tàu</span>';
        case 9:
          return '<span class="required">Tàu</span>';
        case 10:
          return '<span class="required">Chuyến</span>';
        case 11:
          return "Seal No";
        case 12:
          return "Trọng Lượng (kg)";
        case 13:
          return '<span class="required">Cảng Xếp Hàng</span>';
        case 14:
          return "Cảng Dỡ Hàng";
        case 15:
          return 'PTTT';
        case 16:
          return 'MST Người Trả Tiền';
        case 17:
          return 'Tên Cty Thanh Toán';
        case 18:
          return "Ghi Chú";
      }
    },
    colWidths: [40, 100, 100, 100, 80, 150, 100, 80, 100, 120, 70, 80, 120, 120, 100, 100, 130, 130, 200],
    filter: "true",
    columns: [
      {
        data: "active",
        type: "checkbox",
        className: "htCenter",
        renderer: checkBoxRenderer
      },
      {
        data: "status",
        readOnly: true,
        renderer: statusIconsRenderer
      },
      {
        data: "containerNo",
        strict: true,
        renderer: containerNoRenderer,
      },
      {
        data: "expiredDem",
        type: "date",
        dateFormat: "YYYY-MM-DD",
        defaultDate: new Date(),
        renderer: expiredDemRenderer
      },
      {
        data: "detFreeTime",
        type: "numeric",
        renderer: detFreeTimeRenderer
      },
      {
        data: "consignee",
        type: "autocomplete",
        source: consigneeList,
        strict: true,
        renderer: consigneeRenderer
      },
      {
        data: "emptyDepot",
        type: "autocomplete",
        source: emptyDepots,
        strict: true,
        renderer: emptyDepotRenderer
      },
      {
          data: "sztp",
          type: "autocomplete",
          source: sizeList,
          strict: true,
          renderer: sizeRenderer
      },
      {
        data: "opeCode",
        type: "autocomplete",
        source: opeCodeList,
        strict: true,
        renderer: opeCodeRenderer
      },
      {
        data: "vslNm",
        type: "autocomplete",
        source: vslNmList,
        strict: true,
        renderer: vslNmRenderer
      },
      {
        data: "voyNo",
        type: "autocomplete",
        strict: true,
        renderer: voyNoRenderer
      },
      {
        data: "sealNo",
        renderer: sealNoRenderer
      },
      {
        data: "wgt",
        renderer: wgtRenderer
      },
      {
        data: "loadingPort",
        type: "autocomplete",
        source: dischargePortList,
        renderer: loadingPortRenderer
      },
      {
        data: "dischargePort",
        type: "autocomplete",
        source: dischargePortList,
        renderer: dischargePortRenderer
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
          if (change[1] == "vslNm" && change[3] != null && change[3] != '') {
            $.ajax({
              url: ctx + "logistic/vessel/" + change[3] + "/voyages",
              method: "GET",
              success: function (data) {
                if (data.code == 0) {
                  hot.updateSettings({
                    cells: function (row, col, prop) {
                      if (row == change[0] && col == 10) {
                        let cellProperties = {};
                        cellProperties.source = data.voyages;
                        return cellProperties;
                      }
                    }
                  });
                }
              }
            });
          } else {
            let containerNo;
            if (change[1] == "containerNo") {
              containerNo = hot.getDataAtRow(change[0])[2];
              isChange = true;
            } else {
              isChange = false;
            }
            if (containerNo != null && isChange && shipmentSelected.edoFlg == "0" && /[A-Z]{4}[0-9]{7}/g.test(containerNo)) {
              $.modal.loading("Đang xử lý...");
              // CLEAR DATA
              hot.setDataAtCell(change[0], 5, ''); //consignee
              hot.setDataAtCell(change[0], 7, ''); //sztp
              hot.setDataAtCell(change[0], 8, ''); //opeCode
              hot.setDataAtCell(change[0], 9, ''); //vslNm
              hot.setDataAtCell(change[0], 10, ''); //voyNo
              hot.setDataAtCell(change[0], 11, ''); //sealNo
              hot.setDataAtCell(change[0], 12, ''); //wgt
              hot.setDataAtCell(change[0], 13, ''); //loadingPort
              hot.setDataAtCell(change[0], 14, ''); //dischargePort

              // Call data to auto-fill
              $.ajax({
                url: prefix + "/shipment-detail/bl-no/" + shipmentSelected.blNo + "/cont/" + containerNo,
                type: "GET"
              }).done(function (shipmentDetail) {
                if (shipmentDetail != null) {
                  hot.setDataAtCell(change[0], 5, shipmentDetail.consignee); //consignee
                  hot.setDataAtCell(change[0], 7, shipmentDetail.sztp); //sztp
                  hot.setDataAtCell(change[0], 8, shipmentDetail.opeCode); //opeCode
                  hot.setDataAtCell(change[0], 9, shipmentDetail.vslNm); //vslNm
                  hot.setDataAtCell(change[0], 10, shipmentDetail.voyNo); //voyNo
                  hot.setDataAtCell(change[0], 11, shipmentDetail.sealNo); //sealNo
                  hot.setDataAtCell(change[0], 12, shipmentDetail.wgt); //wgt
                  hot.setDataAtCell(change[0], 13, shipmentDetail.loadingPort); //loadingPort
                  hot.setDataAtCell(change[0], 14, shipmentDetail.dischargePort); //dischargePort
                  voyCarrier = shipmentDetail.voyCarrier;
                }
              });
            }
          }
        });
        $.modal.closeLoading();
      }
    },
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
          if (selected[3] == 18) {
            e.stopImmediatePropagation();
          }
          break
        // Arrow Down
        case 40:
          if (selected[2] == rowAmount - 1) {
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

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

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
  let disposable = true, status = 1, diff = false, check = false, verify = false;
  allChecked = true;
  for (let i = 0; i < checkList.length; i++) {
    let cellStatus = hot.getDataAtCell(i, 1);
    if (cellStatus != null) {
      if (checkList[i] == 1) {
        if (cellStatus == 2 && 'Y' == sourceData[i].userVerifyStatus) {
          verify = true;
        }
        check = true;
        if (cellStatus > 2) {
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
  $('.checker').prop('checked', allChecked);
  if (disposable) {
    $("#deleteBtn").prop("disabled", false);
  } else {
    $("#deleteBtn").prop("disabled", true);
  }
  if (diff) {
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
      setLayoutCustomStatus();
      break;
    case 3:
      setLayoutVerifyUserStatus();
      if (verify) {
        $("#verifyBtn").prop("disabled", true);
        $("#deleteBtn").prop("disabled", true);
      }
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
    	if(data.shipmentDetails[0] != null){
            voyCarrier = data.shipmentDetails[0].voyCarrier;
    	}
        if (rowAmount < sourceData.length) {
          sourceData = sourceData.slice(0, rowAmount);
        }
        let saved = true;
        // let shiftingFee = false;
        taxCodeArr = Array(rowAmount).fill(new Object);
        sourceData.forEach(function iterate(shipmentDetail, index) {
          if (shipmentDetail.id == null) {
            saved = false;
          }
          taxCodeArr[index].taxCode = shipmentDetail.taxCode;
          taxCodeArr[index].consigneeByTaxCode = shipmentDetail.consigneeByTaxCode;
          // if (shipmentDetail.preorderPickup == 'Y' && shipmentDetail.prePickupPaymentStatus == "N") {
          //   shiftingFee = true;
          // }
        });
        if (saved) {
          $('#pickContOnDemandBtn').prop('disabled', false);
        } else {
          $('#pickContOnDemandBtn').prop('disabled', true);
        }

        // if (shiftingFee) {
        //   $('#payShiftingBtn').prop('disabled', false);
        // }

        hot.destroy();
        configHandson();
        hot = new Handsontable(dogrid, config);
        hot.loadData(sourceData);
        hot.render();
        setLayoutRegisterStatus();
        onChangeFlg = false;
//        if (!saved) {
//          $.modal.alert("Thông tin container đã được hệ thống tự<br>động điền, quý khách vui lòng kiểm tra lại<br>thông tin và lưu khai báo.");
//        }
      }
    },
    error: function (data) {
      $.modal.closeLoading();
    }
  });
}

function reloadShipmentDetail() {
  checkList = Array(rowAmount).fill(0);
  allChecked = false;
  $('.checker').prop('checked', false);
  for (let i = 0; i < checkList.length; i++) {
    $('#check' + i).prop('checked', false);
  }
  $("#deleteBtn").prop("disabled", true);
  $("#customBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
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
      if (checkList[i] == 1 || (isNeedPickedCont && myTableData[i].userVerifyStatus == "N" && myTableData[i].preorderPickup == "Y")) {
        cleanedGridData.push(myTableData[i]);
      }
    }
  }
  shipmentDetailIds = "";
  shipmentDetails = [];
  let regiterNos = [];
  $.each(cleanedGridData, function (index, object) {
    let shipmentDetail = new Object();
    if (object["containerNo"] != null && object["containerNo"] != "" && !/^[A-Z]{4}[0-9]{7}$/g.test(object["containerNo"]) && isValidate) {
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
    if (object["registerNo"] != null && !regiterNos.includes(object["registerNo"])) {
      regiterNos.push(object["registerNo"]);
    }
    shipmentDetailIds += object["id"] + ",";
  });

  let temProcessOrderIds = [];
  processOrderIds = '';
  $.each(cleanedGridData, function (index, object) {
    for (let i=0; i<regiterNos.length; i++) {
      if (object["processOrderId"] != null && !temProcessOrderIds.includes(object["processOrderId"]) && regiterNos[i] == object["registerNo"]) {
        temProcessOrderIds.push(object["processOrderId"]);
        processOrderIds += object["processOrderId"] + ',';
      }
    }
  });

  if (processOrderIds != '') {
    processOrderIds = processOrderIds.substring(0, processOrderIds.length - 1);
  }

  // Get result in "selectedList" letiable
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
      if (myTableData[i].containerNo || myTableData[i].expiredDem || myTableData[i].consignee || myTableData[i].emptyDepot ||
        myTableData[i].opeCode || myTableData[i].vslNm || myTableData[i].voyNo || myTableData[i].sztp ||
        myTableData[i].sealNo || myTableData[i].wgt || myTableData[i].loadingPort || myTableData[i].dischargePort ||
        myTableData[i].remark) {
        cleanedGridData.push(myTableData[i]);
      }
    }
  }
  shipmentDetails = [];
  if (cleanedGridData.length > 0) {
    billNo = cleanedGridData[0]["blNo"];
  }
  contList = [];
  dnDepot = false;
  let isSaved = false;
  let currentEmptyDepot = '';
  let consignee, emptydepot;
  if (cleanedGridData.length > 0) {
    consignee = cleanedGridData[0].consignee;
    emptydepot = cleanedGridData[0].emptyDepot;
  }
  $.each(cleanedGridData, function (index, object) {
    let shipmentDetail = new Object();
    if (isValidate) {
      if(!object["containerNo"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập số container!");
        errorFlg = true;
        return false;
      } else if (!/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"])) {
        $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
        errorFlg = true;
        return false;
      } else if (!object["expiredDem"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập hạn lệnh!");
        errorFlg = true;
        return false;
      // } else if (!object["detFreeTime"]) {
      //   $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập Ngày miễn lưu vỏ!");
      //   errorFlg = true;
      //   return false;
      } else if (!object["consignee"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn chủ hàng!");
        errorFlg = true;
        return false;
      } else if (!object["emptyDepot"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn nơi hạ vỏ!");
        errorFlg = true;
        return false;
      } else if (!object["opeCode"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn hãng tàu!");
        errorFlg = true;
        return false;
      } else if (!object["vslNm"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn tàu!");
        errorFlg = true;
        return false;
      } else if (!object["voyNo"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn chuyến!");
        errorFlg = true;
        return false;
      } else if (!object["loadingPort"]) {
        $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn Cảng xếp hàng!");
        errorFlg = true;
        return false;
      } else if (consignee != object["consignee"]) {
        $.modal.alertError("Tên chủ hàng không được khác nhau!");
        errorFlg = true;
        return false;
      } 
      // Noi ha vo co the khac nhau
/*      else if (emptydepot != object["emptyDepot"]) {
        $.modal.alertError("Nơi hạ vỏ không được khác nhau!");
        errorFlg = true;
        return false;
      }  */
    }

    // $.ajax({
    //   url: prefix + "/shipment-detail/bl-no/" + shipmentSelected.blNo + "/cont/" + containerNo,
    //   type: "GET"
    // }).done(function (shipmentDetail) {
      
    // });

    consignee = object["consignee"];
    emptydepot = object["emptyDepot"];
    let expiredDem = new Date(object["expiredDem"].substring(0, 4) + "/" + object["expiredDem"].substring(5, 7) + "/" + object["expiredDem"].substring(8, 10));
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
    shipmentDetail.expiredDem = expiredDem.getTime();
    shipmentDetail.wgt = object["wgt"];
    let vessel = object["vslNm"].split(": ");
    shipmentDetail.vslNm = vessel[0];
    shipmentDetail.vslName = vessel[1];
    shipmentDetail.voyNo = object["voyNo"];
    shipmentDetail.taxCode = taxCodeArr[index].taxCode;
    shipmentDetail.consigneeByTaxCode = taxCodeArr[index].consigneeByTaxCode;
    if(voyCarrier){
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
    shipmentDetail.processStatus = shipmentSelected.taxCode;
    shipmentDetail.customStatus = shipmentSelected.groupName;
    shipmentDetail.tier = shipmentSelected.containerAmount;
    shipmentDetails.push(shipmentDetail);
    if (object["id"] != null) {
      isSaved = true;
    }
    let now = new Date();
    now.setHours(0, 0, 0);
    expiredDem.setHours(23, 59, 59);
    if (expiredDem.getTime() < now.getTime() && isValidate && !errorFlg) {
      errorFlg = true;
      $.modal.alertError("Hàng " + (index + 1) + ": Hạn lệnh không được là ngày quá khứ!")
      return false;
    }
/*
    if (currentEmptyDepot != '' && currentEmptyDepot != object["emptyDepot"] && !errorFlg) {
      errorFlg = true;
      $.modal.alertError("Nơi hạ vỏ không được khác nhau!");
      return false;
    }
*/
    currentEmptyDepot = object["emptyDepot"];
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

  if (currentEmptyDepot == 'Cảng Tiên Sa' && !isSaved) {
    dnDepot = true;
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
        if (shipmentDetails.length > 0 && shipmentDetails.length <= shipmentSelected.containerAmount) {
          if (dnDepot) {
            layer.confirm("Quý khách đã chọn nơi hạ container ở Cảng Tiên Sa, hệ thống sẽ tự động tạo lô và thông tin giao container rỗng.", {
              icon: 3,
              title: "Xác Nhận",
              btn: ['Đồng Ý', 'Hủy Bỏ']
            }, function () {
              save(true)
              layer.close(layer.index);
            }, function () {
              save(false);
            });
          } else {
            save(false);
          }
        } else if (shipmentDetails.length > shipmentSelected.containerAmount) {
          $.modal.alertError("Số lượng container nhập vào vượt quá số container của lô.");
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
    accept: 'text/plain',
    data: JSON.stringify(shipmentDetails),
    dataType: 'text',
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
      $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại.");
      $.modal.closeLoading();
    },
  });
}

// DELETE SHIPMENT DETAIL
function deleteShipmentDetail() {
  if (getDataSelectedFromTable(true, false)) {
    if (shipmentDetails.length > 0) {
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
          $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại sau.");
          $.modal.closeLoading();
        },
      });
    }
  }
}

// Handling logic
function checkCustomStatus() {
  getDataSelectedFromTable(true, false);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm("Khai báo hải quan", prefix + "/custom-status/" + shipmentDetailIds, 720, 500);
  }
}

function verify() {
  $.ajax({
    url: prefix + "/shipment/" + shipmentSelected.id + "/delegate/permission",
    method: "GET",
    success: function(res) {
      if (res.code == 0) {
        getDataSelectedFromTable(true, true);
        if (shipmentDetails.length > 0) {
          $.modal.openCustomForm("Xác nhận làm lệnh", prefix + "/otp/cont-list/confirmation/" + shipmentDetailIds, 700, 600);
        }
      } else {
        $.modal.alertWarning("Quý khách chưa có ủy quyền từ chủ hàng để thực hiện lô hàng này <br>Hãy liên hệ với Cảng để thêm ủy quyền.");
      }
    },
    error: function(err) {
      $.modal.alertError("Có lỗi xảy ra, vui lòng thử lại sau.");
    }
  });
}

function verifyOtp(shipmentDtIds, taxCode, creditFlag, isSendContEmpty) {
  getDataSelectedFromTable(true, false);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm("Xác thực OTP", prefix + "/otp/verification/" + shipmentDtIds + "/" + creditFlag + "/" + taxCode + "/" + isSendContEmpty, 600, 350);
  }
}

function pay() {
  getDataSelectedFromTable(true, true);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm("Thanh toán", prefix + "/payment/" + processOrderIds, 800, 400);
  }
}

function pickContOnDemand() {
  getDataFromTable(false);
  $.modal.openCustomForm("Bốc container chỉ định", prefix + "/cont-list/yard-position/" + billNo, 710, 565);
}

function payShifting() {
  $.modal.openCustomForm("Thanh toán phí dịch chuyển", prefix + "/shipment/" + shipmentSelected.id + "/payment/shifting", 800, 400);
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
  $("#customBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutCustomStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("label-primary disable").addClass("active");
  $("#verifyStatus").removeClass("label-primary active").addClass("disable");
  $("#paymentStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  $("#customBtn").prop("disabled", false);
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", false);
  $("#exportReceiptBtn").prop("disabled", false);
}

function setLayoutVerifyUserStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("label-primary disable").addClass("active");
  $("#paymentStatus").removeClass("active label-primary").addClass("disable");
  $("#finishStatus").removeClass("active label-primary").addClass("disable");
  $("#customBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", false);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutPaymentStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("label-primary disable").addClass("active");
  $("#finishStatus").removeClass("active label-primary").addClass("disable");
  $("#deleteBtn").prop("disabled", true);
  $("#customBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", false);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutFinishStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#finishStatus").removeClass("label-primary disable").addClass("active");
  $("#deleteBtn").prop("disabled", true);
  $("#customBtn").prop("disabled", true);
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

function napasPaymentFormForShifting() {
  $.modal.openFullWithoutButton("Cổng Thanh Toán", ctx + "logistic/shipment/" + shipmentSelected.id + "/napas");
}

function connectToWebsocketServer() {
  // Connect to WebSocket Server.
  $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
  for (let i=0; i<orders.length; i++) {
    $.websocket.subscribe(orders[i] + '/response', onMessageReceived);
  }
}

function onMessageReceived(payload) {
  let message = JSON.parse(payload.body);
  if (message.code != 0) {

    clearTimeout(timeout);

    setProgressPercent(currentPercent=100);
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

      setProgressPercent(currentPercent=100);
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

function onDisconnected() {
  console.log('Disconnected socket.');
}  

function onError(error) {
  //console.error('Could not connect to WebSocket server. Please refresh this page to try again!');
  setTimeout(() => {
    hideProgress();
    $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
  }, 1000);
}

function showProgress(title) {
  $('.progress-wrapper').show();
  $('.dim-bg').show();
  $('#titleProgress').text(title);
  $('.percent-text').text("0%");
  currentPercent = 0;
  interval = setInterval(function() {
    if (currentPercent <=99) {
      setProgressPercent(++currentPercent);
    }
    if (currentPercent >= 99) {
        clearInterval(interval);
    }
  }, 1000);
}

function setProgressPercent(percent) {
  $('#progressBar').prop('aria-valuenow', percent)
  $('#progressBar').css('width', percent+"%")
  $('.percent-text').text(percent+"%");
}

function hideProgress() {
  $('.progress-wrapper').hide();
  $('.dim-bg').hide();
  currentPercent = 0;
  $('.percent-text').text("0%");
  setProgressPercent(0);
}
function exportReceipt(){
	if(!shipmentSelected){
		$.modal.alertError("Bạn chưa chọn Lô!");
		return
	}
    $.modal.openTab("In Biên Nhận", ctx +"logistic/print/receipt/shipment/"+shipmentSelected.id);
}

function removeShipment(){
	if (!shipmentSelected) {
		$.modal.alertError("Bạn chưa chọn Lô!");
		return
	}else {
		//1- chua khai bao cont, 2- khai bao nhung chua lam cac buoc tiep theo
		if(shipmentSelected.status == '1' || shipmentSelected.status == '2'){
			$.modal.confirmShipment("Xác nhận thực hiện xóa Lô " + shipmentSelected.id + "  ?", function() {
				$.modal.loading("Đang xử lý...");
			    $.ajax({
			        url: ctx + 'logistic/shipment/remove',
			        type: 'POST',
			        data: {
			            id: shipmentSelected.id,
			        }
			    }).done(function(rs) {
			    	$.modal.closeLoading();
			        if(rs.code  == 0){
			        	$.modal.msgSuccess(rs.msg);
			        	loadTable();
			        }
			        else{
			        	$.modal.msgError(rs.msg)
			        }
			    });
			});
		} else {
			$.modal.msgError("Không thể xóa Lô " + shipmentSelected.id);
		}
	}
}

function dateToString(date) {
  return ("0" + date.getDate()).slice(-2) + "/" + ("0"+(date.getMonth()+1)).slice(-2) + "/" + date.getFullYear()
  + " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2) + ":" + ("0" + date.getSeconds()).slice(-2);
}

function search() {
  loadTable();
}

function clearInput() {
  $("#blNo").textbox('setText', '');
  $("#containerNo").textbox('setText', '');
  $("#consignee").textbox('setText', '');
  $('#fromDate').datebox('setValue', '');
  $('#toDate').datebox('setValue', '');
  shipmentSearch = new Object();
  shipmentSearch.params = new Object();
  shipmentSearch.serviceType = 1;
  fromDate = null;
  toDate = null;
  loadTable();
}
