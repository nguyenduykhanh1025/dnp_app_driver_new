const PREFIX = ctx + "om/support/unloading-cargo";
const HIST_PREFIX = ctx + "om/controlling";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
const greenBlackColor = "rgb(104 241 131)";
var bill;
var processOrder = new Object();
processOrder.serviceType = 3;
var shipmentDetails = new Object();
var dogrid = document.getElementById("container-grid"),
  hot;
var rowAmount = 0;
var processOrderSelected;
var sourceData;
var currentHeight;
var shipmentSelected = new Object();

$(document).ready(function () {
  $(".main-body").layout();

  $(".collapse").click(function () {
    $(".main-body__search-wrapper").hide();
    $(".main-body__search-wrapper--container").hide();

    $(this).hide();
    $(".uncollapse").show();
    currentHeight = $(document).height();
    $("#dg").datagrid("resize", {
      height: currentHeight,
    });
    setTimeout(() => {
      hot.updateSettings({ height: currentHeight });
      hot.render();
    }, 200);
  });

  $(".uncollapse").click(function () {
    $(".main-body__search-wrapper").show();
    $(".main-body__search-wrapper--container").show();
    $(this).hide();
    $(".collapse").show();
    currentHeight = $(document).innerHeight() - 70;
    $("#dg").datagrid("resize", {
      height: currentHeight,
    });
    setTimeout(() => {
      hot.updateSettings({ height: currentHeight });
      hot.render();
    }, 200);
  });

  $(".left-side__collapse").click(function () {
    $("#main-layout").layout("collapse", "west");
  });

  $(".right-side__collapse").click(function () {
    $("#right-layout").layout("collapse", "south");
    setTimeout(() => {
      hot.updateSettings({ height: $("#right-side__main-table").height() - 35 });
      hot.render();
    }, 200);
  });

  $("#right-layout").layout({
    onExpand: function (region) {
      if (region == "south") {
        hot.updateSettings({ height: $("#right-side__main-table").height() - 35 });
        hot.render();
      }
    },
  });

  $("#right-layout").layout("collapse", "south");
  setTimeout(() => {
    hot.updateSettings({ height: $("#right-side__main-table").height() - 35 });
    hot.render();
  }, 200);

  loadTable(processOrder);
  $("#checkCustomStatusByProcessOrderId").attr("disabled", true);
  $("#checkProcessStatusByProcessOrderId").attr("disabled", true);

  $("#bookingNo")
    .textbox("textbox")
    .bind("keydown", function (e) {
      // enter key
      if (e.keyCode == 13) {
        bookingNo = $("#bookingNo").textbox("getText").toUpperCase();
        if (bookingNo == "") {
          loadTable(processOrder);
        }
        processOrder.bookingNo = bookingNo;
        loadTable(processOrder);
      }
    });
});
function loadTable(processOrder) {
  $("#dg").datagrid({
    url: PREFIX + "/orders",
    method: "POST",
    singleSelect: true,
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    clientPaging: true,
    collapsible: true,
    pagination: true,
    pageSize: 20,
    rownumbers: true,
    onBeforeSelect: function (index, row) {
      getSelectedRow(index, row);
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
          console.log(data);
          success(data);
          $("#dg").datagrid("selectRow", 0);
          parent.updateReportNumberOm();
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
function historyRenderer(instance, td, row, col, prop, value, cellProperties) {
  let historyIcon = '<a id="custom" onclick="openHistoryFormCatos(' + row + ')" class="fa fa-window-restore easyui-tooltip" title="Lịch Sử Catos" aria-hidden="true" style="color: #3498db;"></a>';
  $(td).addClass("htCenter").addClass("htMiddle").html(historyIcon);
}

function historyEportRenderer(instance, td, row, col, prop, value, cellProperties) {
  let historyIcon = '<a id="custom" onclick="openHistoryFormEport(' + row + ')" class="fa fa-history easyui-tooltip" title="Lịch Sử Eport" aria-hidden="true" style="color: #3498db;"></a>';
  $(td).addClass("htCenter").addClass("htMiddle").html(historyIcon);
}
function orderNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = "true";
  $(td)
    .attr("id", "orderNo" + row)
    .addClass("htMiddle");
  $(td).html(value);
  return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = "true";
  $(td)
    .attr("id", "containerNo" + row)
    .addClass("htMiddle");
  $(td).html(value);
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
  value =
    '<button class="btn btn-success btn-xs" id="detailBtn ' +
    row +
    '" onclick="openHouseBillForm(' +
    shipmentDetailId +
    ')"><i class="fa fa-check-circle"></i>Khai báo</button>';
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
  $(td).attr('id', 'receiptDem' + row).addClass("htMiddle").addClass("htCenter");
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
//CONFIGURATE HANDSONTABLE
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
    manualColumnResize: true,
    manualRowResize: true,
    renderAllRows: true,
    rowHeaders: true,
    wordWrap: false,
    className: "htMiddle",
    colHeaders: function (col) {
      switch (col) {
        case 0:
          return '<a class="fa fa-window-restore easyui-tooltip" title="Lịch Sử Catos" aria-hidden="true" style="color: #3498db;"></a>';
        case 1:
          return '<a class="fa fa-history easyui-tooltip" title="Lịch Sử Catos" aria-hidden="true" style="color: #3498db;"></a>';
        case 2:
          return "Số Tham Chiếu";
        case 3:
          return "Số Container";
        case 4:
          return "House Bill";
        case 5:
          return '<span class="required">Hạn Lệnh</span>';
        case 6:
          return "Ngày Miễn<br>Lưu Bãi";
        case 7:
          return '<span class="required">Chủ Hàng</span>';
        case 8:
          return 'Ngày Rút Hàng';
        case 9:
          return '<span class="required">Nơi Hạ Vỏ</span>';
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
    colWidths:
      [21,
        21,
        150,
        100,
        100,
        100,
        80,
        150,
        100,
        100,
        80,
        100,
        120,
        70,
        80,
        120,
        120,
        100,
        100,
        130,
        130,
        200,],
    filter: "true",
    columns: [
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
        data: "orderNo",
        renderer: orderNoRenderer,
      },
      {
        data: "containerNo",
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
        data: "detFreeTime",
        type: "numeric",
        renderer: detFreeTimeRenderer,
      },
      {
        data: "consignee",
        type: "autocomplete",
        strict: true,
        renderer: consigneeRenderer,
      },
      {
        data: "dateReceipt",
        type: "date",
        dateFormat: "DD/MM/YYYY",
        correctFormat: true,
        defaultDate: new Date(),
        renderer: dateReceiptRenderer,
      },
      {
        data: "emptyDepot",
        type: "autocomplete",
        strict: true,
        renderer: emptyDepotRenderer,
      },
      {
        data: "sztp",
        type: "autocomplete",
        strict: true,
        renderer: sizeRenderer,
      },
      {
        data: "opeCode",
        type: "autocomplete",
        strict: true,
        renderer: opeCodeRenderer,
      },
      {
        data: "vslNm",
        type: "autocomplete",
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

        renderer: loadingPortRenderer,
      },
      {
        data: "dischargePort",
        type: "autocomplete",
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
          if (selected[3] == 16) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Down
        case 40:
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
function loadTableByContainer(processOrderId) {
  console.log('ssssssssssssssssssssssssssss');
  console.log(processOrderId);
  $.ajax({
    url: PREFIX + "/processOrderId/" + processOrderId + "/shipmentDetails",
    method: "GET",
    success: function (data) {
      if (data.code == 0) {
        sourceData = data.shipmentDetails;
        if (sourceData) {
          for (let i = 0; i < sourceData.length; i++) {
            sourceData[i].vslNm = sourceData[i].vslNm + " - " + sourceData[i].vslName + " - " + sourceData[i].voyCarrier;
            sourceData[i].msg = processOrderSelected.msg;
          }
        }
        hot.destroy();
        configHandson();
        hot = new Handsontable(dogrid, config);
        hot.loadData(sourceData);
        hot.render();
      }
    },
  });
}
function getSelectedRow(index, row) {
  if (row) {
    processOrderSelected = row;
    rowAmount = processOrderSelected.contAmount;
    shipmentDetails.processOrderId = row.id;
    $("#checkCustomStatusByProcessOrderId").attr("disabled", false);
    $("#checkProcessStatusByProcessOrderId").attr("disabled", false);
    loadTableByContainer(row.id);
    loadListComment();
    // shipment info
    let html = '';
    html += 'Mã lô: ' + processOrderSelected.shipmentId;
    if (processOrderSelected.errorImagePath) {
      html += ' - Đường dẫn file ảnh lỗi: ' + processOrderSelected.errorImagePath;
    }
    $('#shipment-info').html(html);
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
  if (value == 1) {
    return "Hãng Tàu Cấp";
  }
  return "Cảng Cấp";
}
function formatBatch(value, row, index) {
  return '<a onclick="shipmentDetailsInfo(' + row.shipmentId + ')"> ' + value + "</a>";
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
  $.modal.openLogisticInfo("Thông tin: " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function () { });
}
function executedSuccess() {
  $.modal.open("Xác nhận", PREFIX + "/verify-executed-command-success/process-order/" + processOrderSelected.id, 430, 270);
}

function msgSuccess(msg) {
  $.modal.alertSuccess(msg);
  loadTable(processOrder);
}
function msgError(msg) {
  $.modal.alertError(msg);
}

function resetProcessStatus() {
  $.modal.open("Xác nhận", PREFIX + "/reset-process-status/process-order/" + processOrderSelected.id, 430, 270);
}

//function formatVessel(value, row) {
//  return row.vslNm + " - " + row.vslName + " - " + row.voyNo;
//}

$("#logistic").combobox({
  onSelect: function (serviceType) {
    if (serviceType.value != 0) {
      processOrder.logisticGroupId = serviceType.value;
    } else {
      processOrder.logisticGroupId = "";
    }
    loadTable(processOrder);
  },
});
function formatUpdateTime(value) {
  let updateTime = new Date(value);
  let now = new Date();
  let offset = now.getTime() - updateTime.getTime();
  let totalMinutes = Math.round(offset / 1000 / 60);
  var toHHMMSS = (secs) => {
    var sec_num = parseInt(secs, 10);
    var hours = Math.floor(sec_num / 3600);
    var minutes = Math.floor(sec_num / 60) % 60;
    var seconds = sec_num % 60;

    return [hours, minutes]
      .map((v) => (v < 10 ? "0" + v : v))
      .filter((v, i) => v !== "00" || i > 0)
      .join(":");
  };
  return toHHMMSS(totalMinutes * 60);
}
function clearInput() {
  $("#bookingNo").textbox("setText", "");
  $("#logistic").combobox("setValue", "0");
  $("#logistic").combobox("setText", "Chọn đơn vị Logistics");
  processOrder = new Object();
  loadTable(processOrder);
}
function search() {
  processOrder.bookingNo = $("#bookingNo").textbox("getText").toUpperCase();
  processOrder.logisticGroupId = $("#logistic").combobox("getValue") == "0" ? "" : $("#logistic").combobox("getValue");
  loadTable(processOrder);
  console.log("TCL: search -> processOrder", processOrder);
}

function loadListComment(shipmentCommentId) {
  let req = {
    serviceType: 3,
    shipmentId: processOrderSelected.shipmentId,
  };
  $.ajax({
    url: ctx + "shipment-comment/shipment/list",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify(req),
    success: function (data) {
      if (data.code == 0) {
        let html = "";
        // set title for panel comment
        let commentTitle = '<span style="color: black">Hỗ Trợ<span>';
        let commentNumber = 0;
        if (data.shipmentComments != null) {
          data.shipmentComments.forEach(function (element, index) {
            let createTime = element.createTime;
            let date = "";
            let time = "";
            if (createTime) {
              date = createTime.substring(8, 10) + "/" + createTime.substring(5, 7) + "/" + createTime.substring(0, 4);
              time = createTime.substring(10, 19);
            }

            let resolvedBackground = "";
            if ((shipmentCommentId && shipmentCommentId == element.id) || !element.resolvedFlg) {
              resolvedBackground = 'style="background-color: #ececec;"';
              commentNumber++;
            }

            html += "<div " + resolvedBackground + ">";
            // User name comment and date time comment
            html += '<div><i style="font-size: 15px; color: #015198;" class="fa fa-user-circle" aria-hidden="true"></i><span> <a>' + element.userName + " (" + element.userAlias + ")</a>: <i>" + date + " at " + time + "</i></span></div>";
            // Topic comment
            html += "<div><span><strong>Yêu cầu:</strong> " + element.topic + "</span></div>";
            // Content comment
            html += "<div><span>" + element.content.replaceAll("#{domain}", domain) + "</span></div>";
            html += "</div>";
            html += "<hr>";
          });
        }
        commentTitle += ' <span class="round-notify-count">' + commentNumber + "</span>";
        $("#right-layout").layout("panel", "expandSouth").panel("setTitle", commentTitle);
        $("#commentList").html(html);
        // $("#comment-div").animate({ scrollTop: $("#comment-div")[0].scrollHeight}, 1000);
      }
    },
  });
}

function addComment() {
  let topic = $("#topic").textbox("getText");
  let content = $(".summernote").summernote("code"); // get editor content
  let errorFlg = false;
  if (!topic) {
    errorFlg = true;
    $.modal.alertWarning("Vui lòng nhập chủ đề.");
  } else if (!content) {
    errorFlg = true;
    $.modal.alertWarning("Vui lòng nhập nội dung.");
  }
  if (!errorFlg) {
    let req = {
      topic: topic,
      content: content,
      shipmentId: processOrderSelected.shipmentId,
      logisticGroupId: processOrderSelected.logisticGroupId,
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

function openHistoryFormCatos(row) {
  let containerInfo = sourceData[row];
  let containerNo = containerInfo.containerNo;
  let vslCd = 'EMTY';
  let voyNo = '0000';
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

function retryOrder() {
  layer.confirm("Xác nhận robot làm lệnh lại.", {
    icon: 3,
    title: "Xác Nhận",
    btn: ['Đồng Ý', 'Hủy Bỏ']
  }, function () {

    // UPDATE PROCESS ORDER TO DOING STATUS
    $.ajax({
      url: PREFIX + "/order/retry",
      method: "POST",
      data: {
        processOrderId: processOrderSelected.id
      }
    }).done(function (res) {
      layer.close(layer.index);
      if (res.code != 0) {
        $.modal.alertError(res.msg);
      } else {
        $.modal.alertSuccess(res.msg);
      }
    });
  }, function () {
    // Do nothing
  });
}

function openHouseBillForm(shipmentDetailId) {
  if (shipmentDetailId == null) {
    $.modal.alertWarning("Quý khách chưa khai báo container cần làm lệnh!");
    return;
  }
  $.modal.openCustomForm(
    "Khai báo house bill",
    PREFIX + "/shipment-detail/" + shipmentDetailId + "/house-bill"
  );
}