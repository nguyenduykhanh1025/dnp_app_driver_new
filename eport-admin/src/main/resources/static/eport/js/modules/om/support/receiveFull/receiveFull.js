const PREFIX = ctx + "om/support/receive-full";
var bill;
var processOrder = new Object();
processOrder.serviceType = 1;
var shipmentDetails = new Object();
var currentLeftWidth = $(".table-left").width();
var currentRightWidth = $(".table-right").width();
var dogrid = document.getElementById("container-grid"), hot;
var rowAmount = 0;
var processOrderSelected;
var sourceData;

$(document).ready(function () {
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
  loadTable(processOrder);
  $('#checkCustomStatusByProcessOrderId').attr("disabled", true);
  $('#checkProcessStatusByProcessOrderId').attr("disabled", true);

  $("#searchInfoProcessOrder").keyup(function (event) {
    if (event.keyCode == 13) {
      blNo = $("#searchInfoProcessOrder").val().toUpperCase();
      if (blNo == "") {
        loadTable(processOrder);
      }
      processOrder.blNo = blNo;
      loadTable(processOrder);
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

function loadTable(processOrder) {
  $("#dg").datagrid({
    url: PREFIX + "/orders",
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
          data: processOrder,
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
function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'payType' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'payer' + row).addClass("htMiddle");
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
                    return "Chủ hàng";
                case 3:
                    return "Tàu - Chuyến";
                case 4:
                    return "Trọng lượng";
                case 5:
                    return "Loại hàng";
                case 6:
                	return "Cảng Dở Hàng";
                case 7:
                    return "PTTT";
                case 8:
                    return "Payer";
                case 9:
                    return "Ghi Chú";
            }
        },
        // colWidths: [ 100, 100, 100, 120, 100, 100, 100, 150, 100, 120, 100, 80, 80, 80, 150],
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
                data: "payType",
                renderer: payTypeRenderer
            },
            {
                data: "payer",
                renderer: payerRenderer
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
function loadTableByContainer(processOrderId) {
    $.ajax({
        url:  PREFIX + "/processOrderId/" + processOrderId + "/shipmentDetails",
        method: "GET",
        success: function (data) {
            if (data.code == 0) {
                sourceData = data.shipmentDetails;
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
	processOrderSelected = row;
	rowAmount = processOrderSelected.contAmount;
    shipmentDetails.processOrderId = row.id;
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
function formatType(value, row, index) {
	if (value == 1){
		return "eDO";
	}
	return "DO";
}
function formatBatch(value, row, index) {
	  return '<a onclick="shipmentDetailsInfo(' + row.shipmentId + ")\"> " + value + "</a>";
	}
function shipmentDetailsInfo(shipmentId) {
  $.modal.openShipmentDetailsInfo("Thông Tin Lô: " + shipmentId, ctx + "om/support/shipment/" + shipmentId + "/shipmentDetails/info", 1100, 470, function () {
    $.modal.close();
  });
}
function formatLogistic(value, row, index) {
  return '<a onclick="logisticInfo(' + row.logisticGroupId + "," + "'" + value + "')\"> " + value + "</a>";
}

function logisticInfo(id, logistics) {
  $.modal.openLogisticInfo("Thông tin: " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function () {
  });
}

function executedSuccess() {
  $.modal.open("Xác nhận", PREFIX + "/verify-executed-command-success/process-order/" + processOrderSelected.id , 400, 270);
}

function msgSuccess(msg) {
	$.modal.alertSuccess(msg);
	loadTable(processOrder);
}
function msgError(msg) {
	$.modal.alertError(msg);
}

function resetProcessStatus() {
	$.modal.open("Xác nhận", PREFIX + "/reset-process-status/process-order/" + processOrderSelected.id , 400, 270);
}

//function formatVessel(value, row) {
//  return row.vslNm + " - " + row.vslName + " - " + row.voyNo;
//}

$('#logistic').change(function () {
  if (0 != $('#logistic option:selected').val()) {
    processOrder.logisticGroupId = $('#logistic option:selected').val();
  } else {
    processOrder.logisticGroupId = '';
  }
  loadTable(processOrder);
});

function formatUpdateTime(value) {
  let updateTime = new Date(value);
  let now = new Date();
  let offset = now.getTime() - updateTime.getTime();
  let totalMinutes = Math.round(offset / 1000 / 60);
  return totalMinutes;
}

