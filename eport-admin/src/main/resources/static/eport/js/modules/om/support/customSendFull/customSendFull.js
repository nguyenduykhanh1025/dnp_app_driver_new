const PREFIX = ctx + "om/support/custom-send-full";
var bill;
var shipment = new Object();
shipment.serviceType = 4;
var shipmentDetails = new Object();
var currentLeftWidth = $(".table-left").width();
var currentRightWidth = $(".table-right").width();
var dogrid = document.getElementById("container-grid"), hot;
var rowAmount = 0;
var shipmentSelected;
var sourceData;

$(document).ready(function () {
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
  loadTable(shipment);
  $('#checkCustomStatusByProcessOrderId').attr("disabled", true);
  $('#checkProcessStatusByProcessOrderId').attr("disabled", true);

  $("#searchInfoProcessOrder").keyup(function (event) {
    if (event.keyCode == 13) {
      blNo = $("#searchInfoProcessOrder").val().toUpperCase();
      if (blNo == "") {
        loadTable(shipment);
      }
      shipment.blNo = blNo;
      loadTable(shipment);
    }
  });

});

function handleCollapse(status) {
  if (status) {
    $(".left").css("width", "0.5%");
    $(".left").children().hide();
    $("#btn-collapse").hide();
    $("#btn-uncollapse").show();
    $(".right").css("width", "99%");
    return;
  }
  $(".left").css("width", "25%");
  $(".left").children().show();
  $("#btn-collapse").show();
  $("#btn-uncollapse").hide();
  $(".right").css("width", "74%");
  return;
}

function loadTable(shipment) {
  $("#dg").datagrid({
    url: PREFIX + "/shipments",
    method: "POST",
    singleSelect: true,
    height: $(document).height() - 60,
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
        accept: "text/plain",
        dataType: "text",
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: shipment,
        }),
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
        height: document.documentElement.clientHeight - 100,
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
                    return "Chủ hàng";
                case 5:
                    return "Tàu - Chuyến";
                case 6:
                    return "Trọng lượng";
                case 7:
                    return "Loại hàng";
                case 8:
                	return "Cảng Dở Hàng";
                case 9:
                    return "Ghi Chú";
            }
        },
         colWidths: [ 100, 50, 100, 100, 200, 250, 100, 100, 100, 150],
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
    $('#checkProcessStatusByProcessOrderId').attr("disabled", false);
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


//function formatVessel(value, row) {
//  return row.vslNm + " - " + row.vslName + " - " + row.voyNo;
//}

$('#logistic').change(function () {
  if (0 != $('#logistic option:selected').val()) {
    shipment.logisticGroupId = $('#logistic option:selected').val();
  } else {
    shipment.logisticGroupId = '';
  }
  loadTable(shipment);
});

function formatUpdateTime(value, row, index) {
  if(!row.customsScanTime){
	  return null
  }
  let customsScanTime = new Date(row.customsScanTime);
  let now = new Date();
  let offset = now.getTime() - customsScanTime.getTime();
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

