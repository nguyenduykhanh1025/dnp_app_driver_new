const PREFIX = ctx + "om/support/custom-receive-full";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var bill;
var shipmentDetails = new Object();
var currentLeftWidth = $(".table-left").width();
var currentRightWidth = $(".table-right").width();
var dogrid = document.getElementById("container-grid"), hot;
var rowAmount = 0;
var shipmentSelected;
var sourceData;
var shipment = new Object();
shipment.serviceType = 1;
shipment.params = new Object();

$(document).ready(function () {
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
      $("#consignee").textbox('setText', '');
      loadTable();
    }
  });

  $("#blNo").textbox('textbox').bind('keydown', function(e) {
    // enter key
    if (e.keyCode == 13) {
      shipment.blNo = $("#blNo").textbox('getText').toUpperCase();
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

  $("#consignee").textbox('textbox').bind('keydown', function(e) {
    // enter key
    if (e.keyCode == 13) {
      shipment.params.consignee = $("#consignee").textbox('getText').toUpperCase();
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

function loadTable() {
  $("#dg").datagrid({
    url: PREFIX + "/shipments",
    method: "POST",
    singleSelect: true,
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    clientPaging: true,
    collapsible: true,
    pagination: true,
    pageSize: 20,
    rownumbers: true,
    onClickRow: function () {
      getSelectedRow();
    },
    nowrap: false,
    striped: true,
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
          data: shipment,
        }),
        success: function (res) {
          if (res.code == 0) {
            success(res.shipments);
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
loadTableByContainer();

//FORMAT HANDSONTABLE COLUMN
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'containerNo' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function sztpRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'sztp' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function customsNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'customsNo' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function customStatusRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'customStatus' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'expiredDem' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function emptyDepotRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'emptyDepot' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function detFreeTimeRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'detFreeTime' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'consignee' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'vslNm' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'wgt' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'cargoType' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'dischargePort' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'remark' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
//CONFIGURATE HANDSONTABLE
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
        manualColumnResize: true,
        manualRowResize: true,
        renderAllRows: true,
        rowHeaders: true,
        className: "htMiddle",
        colHeaders: function (col) {
            switch (col) {
                case 0:
                    return "Số Container";
                case 1:
                    return "Sztp";
                case 2:
                    return "Số Tờ Khai HQ";
                case 3:
                    return "T.T.T.Quan";
                case 4:
                    return "Hạn Lệnh";
                case 5:
                    return "Nơi Trả Vỏ";
                case 6:
                    return "Ngày <br> Miễn <br>Lưu";
                case 7:
                    return "Chủ hàng";
                case 8:
                    return "Tàu - Chuyến";
                case 9:
                    return "Trọng lượng";
                case 10:
                    return "Loại hàng";
                case 11:
                	return "Cảng Dở Hàng";
                case 12:
                    return "Ghi Chú";
            }
        },
         colWidths: [ 100, 50, 100, 100, 150, 200, 70, 200, 250, 100, 100, 100, 150],
        filter: "true",
        columns: [
            {
                data: "containerNo",
                renderer: containerNoRenderer
            },
            {
                data: "sztp",
                renderer: sztpRenderer
            },
            {
                data: "customsNo",
                renderer: customsNoRenderer
            },
            {
                data: "customStatus",
                renderer: customStatusRenderer
            },
            {
                data: "expiredDem",
                renderer: expiredDemRenderer
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
            //vslNm = vslNm + "-" + "voyCarrier"
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
                    if (selected[3] == 12) {
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
function loadTableByContainer(shipmentId) {
    $.ajax({
        url:  PREFIX + "/shipmentId/" + shipmentId + "/shipmentDetails",
        method: "GET",
        success: function (data) {
            if (data.code == 0) {
                sourceData = data.shipmentDetails;
                if(sourceData){
                	for(let i = 0; i < sourceData.length;i++){
                		sourceData[i].vslNm = sourceData[i].vslNm + " - " + sourceData[i].vslName + " - " + sourceData[i].voyCarrier;
                	}
                }
                hot.destroy();
                configHandson();
                hot = new Handsontable(dogrid, config);
                hot.loadData(sourceData);
                hot.render();
            }
        }
    });
}
function getSelectedRow() {
  var row = $("#dg").datagrid("getSelected");
  if (row) {
    shipmentSelected = row;
    rowAmount = shipmentSelected.contAmount;
    shipmentDetails.shipmentId = row.id;
    $('#checkCustomStatusByProcessOrderId').attr("disabled", false);
    loadTableByContainer(row.id);
  }
}

function formatBlBooking(value, row) {
  if (row.blNo) {
    return row.blNo;
  }
  if (row.bookingNo) {
    return row.bookingNo;
  }
  return "";
}
function formatType(value, row, index) {
	if (value == 1){
		return "eDO";
	}
	return "DO";
}
function formatLogistic(value, row, index) {
  return '<a onclick="logisticInfo(' + row.logisticGroupId + "," + "'" + value + "')\"> " + value + "</a>";
}

function logisticInfo(id, logistics) {
  $.modal.openLogisticInfo("Thông tin: " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function () {
  });
}

function notifyResult() {
  $.modal.open("Xác nhận", PREFIX + "/confirm-result-notification/shipmentId/" + shipmentSelected.id , 400, 330);
}

function msgSuccess(msg) {
	$.modal.alertSuccess(msg);
	loadTable(shipment);
}
function msgError(msg) {
	$.modal.alertError(msg);
}

function formatUpdateTime(value, row, index) {
  if(!row.customScanTime){
	  return null
  }

  let customScanTime = new Date(row.customScanTime);
  let now = new Date();
  let offset = now.getTime() - customScanTime.getTime();
  let totalMinutes = Math.round(offset / 1000 / 60);
  var toHHMMSS = (secs) => {
	    var sec_num = parseInt(secs, 10)
	    var hours   = Math.floor(sec_num / 3600)
	    var minutes = Math.floor(sec_num / 60) % 60
	    var seconds = sec_num % 60

	    return [hours,minutes]
	        .map(v => v < 10 ? "0" + v : v)
	        .filter((v,i) => v !== "00" || i > 0)
	        .join(":")
	}
  return toHHMMSS(totalMinutes*60);
}

function search() {
  loadTable();
}

function clearInput() {
  $('#vesselAndVoyages').combobox('select', 'Chọn tàu chuyến');
  $('#logisticGroups').combobox('select', '0');
  $("#containerNo").textbox('setText', '');
  $("#consignee").textbox('setText', '');
  $("#blNo").textbox('setText', '');
  shipment = new Object();
  shipment.params = new Object();
  loadTable();
}

