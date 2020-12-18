const PREFIX = ctx + "reefer-group/extend-draw-date";
const HIST_PREFIX = ctx + "om/controlling";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var dogrid = document.getElementById("container-grid"),
  hot;
var shipmentSelected,
  checkList,
  allChecked,
  sourceData,
  rowAmount = 0,
  shipmentDetailIds;
var shipment = new Object();
shipment.params = new Object();
shipment.params.serviceArray = [1];
shipment.params.powerDrawDateStatus = "P";
const CONT_SPECIAL_STATUS = {
  INIT: "I", // cont đã được lưu
  REQ: "R", // cont đã được yêu cầu xác nhận
  YES: "Y", // cont đã được phê duyệt yêu cầu xác nhận
  CANCEL: "C", // cont đã bị từ chối yêu cầu xác nhận
};

var detailInformationForContainerSpecial = {
  data: [],
  indexSelected: -1,
};

const SERVICE_TYPE = {
  pickupFull: 1,
  dropFull: 4,
};

$(document).ready(function () {
  $(".main-body").layout();

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
  });

  $(".right-side__collapse").click(function () {
    $("#right-layout").layout("collapse", "south");
    setTimeout(() => {
      hot.updateSettings({
        height: $("#right-side__main-table").height() - 35,
      });
      hot.render();
    }, 200);
  });

  $("#right-layout").layout({
    onExpand: function (region) {
      if (region == "south") {
        hot.updateSettings({
          height: $("#right-side__main-table").height() - 35,
        });
        hot.render();
      }
    },
  });

  $("#right-layout").layout("collapse", "south");
  setTimeout(() => {
    hot.updateSettings({ height: $("#right-side__main-table").height() - 35 });
    hot.render();
  }, 200);

  $("#fromDate").datebox({
    onSelect: function (date) {
      date.setHours(0, 0, 0);
      shipment.params.fromDateSetupTemperature = dateToString(date);
      loadTable();
      return date;
    },
  });

  $("#toDate").datebox({
    onSelect: function (date) {
      date.setHours(0, 0, 0);
      shipment.params.toDateSetupTemperature = dateToString(date);
      loadTable();
      return date;
    },
  });

  $("#logisticGroups").combobox({
    valueField: "id",
    textField: "groupName",
    data: logisticGroups,
    onSelect: function (logisticGroup) {
      if (logisticGroup.id != 0) {
        shipment.logisticGroupId = logisticGroup.id;
      } else {
        shipment.logisticGroupId = null;
      }
      $("#opr").combobox("select", "Chọn OPR");
      $("#containerNo").textbox("setText", "");
      loadTable();
    },
  });

  $("#blNo")
    .textbox("textbox")
    .bind("keydown", function (e) {
      // enter key
      if (e.keyCode == 13) {
        shipment.blNo = $("#blNo").textbox("getText").toUpperCase();
        loadTable();
      }
    });

  $("#containerNo")
    .textbox("textbox")
    .bind("keydown", function (e) {
      // enter key
      if (e.keyCode == 13) {
        shipment.params.containerNo = $("#containerNo")
          .textbox("getText")
          .toUpperCase();
        loadTable();
      }
    });

  $("#opr").combobox({
    panelMaxHeight: 200,
    valueField: "dictValue",
    textField: "dictLabel",
    data: oprList,
    onSelect: function (opr) {
      if (opr.dictValue != "Chọn OPR") {
        shipment.opeCode = opr.dictValue;
      } else {
        shipment.opeCode = null;
      }
      loadTable();
    },
  });
  $("#opr").combobox("select", "Chọn OPR");
  // loadTable();
});

$("#powerDrawDateStatusSelect").combobox({
  onChange: function (serviceType) {
    shipment.params.powerDrawDateStatus = serviceType;
    search();
  }
});

function loadTable() {
  $("#dg").datagrid({
    url: PREFIX + "/shipments",
    height:
      $(document).height() - $(".main-body__search-wrapper").height() - 70,
    method: "POST",
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
          data: shipment,
        }),
        success: function (res) {
          if (res.code == 0) {
            success(res.shipments);
            $("#dg").datagrid("selectRow", 0);
          } else {
            success([]);
            loadShipmentDetails(null);
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
  return (
    '<a onclick="logisticInfo(' +
    row.logisticGroupId +
    "," +
    "'" +
    value +
    "')\"> " +
    value +
    "</a>"
  );
}
// FORMAT DATE FOR date time format dd/mm/yyyy
function formatDate(value) {
  let date = new Date(value);
  let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  let month = date.getMonth() + 1;
  let monthText = month < 10 ? "0" + month : month;
  return day + "/" + monthText + "/" + date.getFullYear();
}

function formatBlBooking(value, row) {
  if (row) {
    if (row.blNo) {
      return row.blNo;
    } else if (row.bookingNo) {
      return row.bookingNo;
    }
  }
  return "";
}

// Trigger when click a row in easy ui data grid on the left screen
function getSelected(index, row) {
  if (row) {
    shipmentSelected = row;
    rowAmount = shipmentSelected.containerAmount;
    checkList = Array(rowAmount).fill(0);
    allChecked = false;
    let title = "";
    title += "Mã Lô: " + row.id + " - ";
    title += "SL: " + row.containerAmount + " - ";

    if (row.edoFlg == "0") {
      title += 'Loại lệnh: DO - ';
      $("#deleteBtn").show();
    } else {
      title += 'Loại lệnh: eDO - ';
      $("#deleteBtn").hide();
    }

    title += "B/L No: ";
    if (row.blNo != null) {
      title += row.blNo;
    } else {
      title += "Trống";
    }
    $("#shipmentInfo").html(title);
  }
  loadShipmentDetails(shipmentSelected.id);
  loadListComment();
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
function historyRenderer(instance, td, row, col, prop, value, cellProperties) {
  let historyIcon =
    '<a id="custom" onclick="openHistoryFormCatos(' +
    row +
    ')" class="fa fa-window-restore easyui-tooltip" title="Lịch Sử Catos" aria-hidden="true" style="color: #3498db;"></a>';
  $(td).addClass("htCenter").addClass("htMiddle").html(historyIcon);
}

function historyEportRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  let historyIcon =
    '<a id="custom" onclick="openHistoryFormEport(' +
    row +
    ')" class="fa fa-history easyui-tooltip" title="Lịch Sử Eport" aria-hidden="true" style="color: #3498db;"></a>';
  $(td).addClass("htCenter").addClass("htMiddle").html(historyIcon);
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
  return shipmentSelected.serviceType == SERVICE_TYPE.dropFull
    ? renderIconsFollowServiceTypeDropFull(
      instance,
      td,
      row,
      col,
      prop,
      value,
      cellProperties
    )
    : renderIconsStatusServiceTypePickupFull(
      instance,
      td,
      row,
      col,
      prop,
      value,
      cellProperties
    );
}

function getRequestConfigIcon(row) {
  const statusResult = getStatusContFollowIndex(row);

  if (!statusResult) {
    return "";
  } else if (statusResult == CONT_SPECIAL_STATUS.YES) {
    return '<i id="verify" class="fa fa-check easyui-tooltip" title="Yêu cầu xác nhật đã được duyệt" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394"></i>';
  } else if (statusResult == CONT_SPECIAL_STATUS.CANCEL) {
    return getRequestConfigIconIsCancel(row);
  } else if (statusResult == CONT_SPECIAL_STATUS.REQ) {
    return getRequestConfigIconIsRequest(row);
  } else if (statusResult == CONT_SPECIAL_STATUS.INIT) {
    return '<i id="verify" class="fa fa-check easyui-tooltip" title="Có thể yêu cầu xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db"></i>';
  }
}

/**
 * @author Khanh
 * @description Ket qua tra ve hien cont dang cho yeu cau request cua to nao?
 * @returns {String}
 */
function getRequestConfigIconIsRequest(row) {
  let statusResult = " ";
  if (
    sourceData[row].dangerous &&
    sourceData[row].dangerous === CONT_SPECIAL_STATUS.REQ &&
    sourceData[row].oversize &&
    sourceData[row].oversize === CONT_SPECIAL_STATUS.REQ &&
    sourceData[row].frozenStatus &&
    sourceData[row].frozenStatus === CONT_SPECIAL_STATUS.REQ
  ) {
    return '<i id="verify" class="fa fa-check easyui-tooltip" title="Đang chờ yêu cầu xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f8ac59"></i>';
  }
  if (
    sourceData[row].dangerous &&
    sourceData[row].dangerous == CONT_SPECIAL_STATUS.REQ
  ) {
    statusResult += "Tổ Nguy Hiểm, ";
  }
  if (
    sourceData[row].oversize &&
    sourceData[row].oversize == CONT_SPECIAL_STATUS.REQ
  ) {
    statusResult += "Tổ Quá Khổ, ";
  }
  if (
    sourceData[row].frozenStatus &&
    sourceData[row].frozenStatus == CONT_SPECIAL_STATUS.REQ
  ) {
    statusResult += "Tổ Lạnh, ";
  }
  return `<i id="verify" class="fa fa-check easyui-tooltip" title="Đang chờ yêu cầu xác nhận từ ${statusResult.slice(
    0,
    statusResult.length - 2
  )}" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f8ac59"></i>`;
}

/**
 * @author Khanh
 * @description Ket qua tra ve hien cont đã bị từ chối bởi to nao?
 * @returns {String}
 */
function getRequestConfigIconIsCancel(row) {
  let statusResult = " ";
  if (
    sourceData[row].dangerous &&
    sourceData[row].dangerous === CONT_SPECIAL_STATUS.CANCEL &&
    sourceData[row].oversize &&
    sourceData[row].oversize === CONT_SPECIAL_STATUS.CANCEL &&
    sourceData[row].frozenStatus &&
    sourceData[row].frozenStatus === CONT_SPECIAL_STATUS.CANCEL
  ) {
    return '<i id="verify" class="fa fa-check easyui-tooltip" title="Yêu cầu xác nhận bị từ chối" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #ff0000"></i>';
  }
  if (
    sourceData[row].dangerous &&
    sourceData[row].dangerous == CONT_SPECIAL_STATUS.CANCEL
  ) {
    statusResult += "Tổ Nguy Hiểm, ";
  }
  if (
    sourceData[row].oversize &&
    sourceData[row].oversize == CONT_SPECIAL_STATUS.CANCEL
  ) {
    statusResult += "Tổ Quá Khổ, ";
  }
  if (
    sourceData[row].frozenStatus &&
    sourceData[row].frozenStatus == CONT_SPECIAL_STATUS.CANCEL
  ) {
    statusResult += "Tổ Lạnh, ";
  }
  return `<i id="verify" class="fa fa-check easyui-tooltip" title="Yêu cầu xác nhận bị từ chối từ ${statusResult.slice(
    0,
    statusResult.length - 2
  )}" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #ff0000"></i>`;
}

/**
 * Render button
 */
function btnDetailRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "wgt" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  let containerNo, sztp;

  if (sourceData && sourceData.length > 0) {
    if (sourceData.length > row && sourceData[row].id) {
      value = `<button class="btn btn-success btn-xs" onclick="openDetail('${sourceData[row].id}', '${containerNo}', '${sztp}', '${row}')"><i class="fa fa-book" style="margin: 0px 3px;"></i>Lạnh</button>`;
    } else if (containerNo && sztp) {
      value =
        '<button class="btn btn-success btn-xs" id="detailBtn ' +
        row +
        '" onclick="openDetail(' +
        null +
        ",'" +
        containerNo +
        "'," +
        "'" +
        sztp +
        '\')"><i class="fa fa-book" style="margin: 0px 3px;"></i>Lạnh</button>';
    }
  }
  $(td).html(value);
  cellProperties.readOnly = "true";
  return td;
}

function temperatureRenderer(instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (value == null) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function humidityRenderer(instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (value == null) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function ventilationRenderer(instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (value == null) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function paymentRenderer(instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (sourceData[row] == null || sourceData[row].sztp == null) {

    value = "";
    $(td).html(
      '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
      '' +
      "</div>"
    );
    return td;
  } else {
    if (!sourceData[row].payType) {
      value = "Chủ hàng thanh toán";
    } else if (sourceData[row].payType == "Before") {
      value = "Hãng tàu thanh toán trước"
    } else if (sourceData[row].payType == "After") {
      value = "Hãng tàu thanh toán sau"
    } else {
      value = '';
    }

    $(td).html(
      '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
      value +
      "</div>"
    );
    return td;
  }

}

function daySetupTemperatureRenderer(instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  const date = new Date(value);
  const month = date.getMonth() == 12 ? '00' : date.getMonth() + 1;
  const result = `${date.getDate()}/${month}/${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}`;
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function powerDrawDateRenderer(instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  const date = new Date(value);
  const month = date.getMonth() == 12 ? '01' : date.getMonth() + 1;
  const result = `${date.getDate()}/${month}/${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}`;
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function openDetail(id, containerNo, sztp, row) {
  if (!id) {
    id = 0;
  }
  detailInformationForContainerSpecial.indexSelected = row;
  $.modal.openCustomForm(
    "Khai báo chi tiết",
    `${PREFIX}/shipment-detail/${id}/cont/${containerNo}/sztp/${sztp}/detail`,
    800,
    460
  );
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
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function payTypeRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }

  if (sourceData[row].payType) {
    value = "Chủ hàng thanh toán";
  } else if (sourceData[row].payType == "Before") {
    value = "Hãng tàu thanh toán trước"
  } else if (sourceData[row].payType == "After") {
    value = "Hãng tàu thanh toán sau"
  } else {
    value = '';
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function sztpRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
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
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  if (sourceData[row] && sourceData[row].vslNm && sourceData[row].voyNo) {
    $(td).html(
      '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
      sourceData[row].vslNm +
      " - " +
      sourceData[row].voyNo +
      "</div>"
    );
  }
  return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function cargoTypeRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
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
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function orderNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
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
    fixedColumnsLeft: 4,
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
          return "Số Container";
        case 5:
          return '<span>Chi Tiết Container</span>';
        case 6:
          return '<span>Nhiệt Độ</span>';
        case 7:
          return '<span>Độ Ẩm</span>';
        case 8:
          return '<span>Thông Gió</span>';
        case 9:
          return '<span>Ngày cắm điện</span>';
        case 10:
          return '<span>Ngày rút điện</span>';
        case 11:
          return "Sztp";
        case 12:
          return "Chủ Hàng";
        case 13:
          return "Tàu - Chuyến";
        case 14:
          return "Trọng Lượng";
        case 15:
          return "Loại Hàng";
        case 16:
          return "Số Seal";
        case 17:
          return "Cảng Dỡ Hàng";
        case 18:
          return "P.T.T.T";
        case 19:
          return "Payer";
        case 20:
          return "Ghi Chú";
      }
    },
    colWidths: [
      23,
      21,
      21,
      120,
      100,
      150,
      100,
      100,
      100,
      150,
      150,
      60,
      200,
      100,
      100,
      80,
      80,
      100,
      100,
      100,
      100,
    ],
    filter: "true",
    columns: [
      {
        data: "active",
        type: "checkbox",
        renderer: checkBoxRenderer,
      },
      {
        data: "historyCatos",
        readOnly: true,
        renderer: historyRenderer,
      },
      {
        data: "historyEport",
        readOnly: true,
        renderer: historyEportRenderer,
      },
      {
        data: "status",
        readOnly: true,
        renderer: statusIconsRenderer,
      },
      {
        data: "containerNo",
        renderer: containerNoRenderer,
      },

      {
        data: "btnDetail",
        renderer: btnDetailRenderer,
      },
      ////////////
      {
        data: "temperature",
        renderer: temperatureRenderer,
      },

      {
        data: "humidity",
        renderer: humidityRenderer,
      },

      {
        data: "ventilation",
        renderer: ventilationRenderer,
      },
      {
        data: "daySetupTemperature",
        renderer: daySetupTemperatureRenderer,
      },

      {
        data: "powerDrawDate",
        renderer: powerDrawDateRenderer,
      },
      ////////////////////
      {
        data: "sztp",
        renderer: sztpRenderer,
      },

      {
        data: "consignee",
        renderer: consigneeRenderer,
      },
      {
        data: "vslNm",
        renderer: vslNmRenderer,
      },
      {
        data: "wgt",
        renderer: wgtRenderer,
      },
      {
        data: "cargoType",
        renderer: cargoTypeRenderer,
      },
      {
        data: "sealNo",
        renderer: sealNoRenderer,
      },
      {
        data: "dischargePort",
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
          if (selected[3] == 15) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Down
        case 40:
          if (selected[2] == shipmentSelected.containerAmount - 1) {
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
hot = new Handsontable(dogrid, config);

function loadShipmentDetails(id) {
  const statusPowerDrawDate = shipment.params.powerDrawDateStatus;
  if (id) {
    $.modal.loading("Đang xử lý ...");
    $.ajax({
      url:
        PREFIX +
        "/shipment/" +
        id +
        "/shipmentDetails" + "/status/" + statusPowerDrawDate,
      method: "GET",
      success: function (res) {
        $.modal.closeLoading();
        if (res.code == 0) {
          checkList = Array(rowAmount).fill(0);
          allChecked = false;
          $(".checker").prop("checked", false);
          for (let i = 0; i < checkList.length; i++) {
            $("#check" + i).prop("checked", false);
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
      },
    });
  } else {
    hot.destroy();
    configHandson();
    hot = new Handsontable(dogrid, config);
    hot.loadData([]);
    hot.render();
  }
}

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
  if (!allChecked) {
    allChecked = true;
    for (let i = 0; i < checkList.length; i++) {
      if (hot.getDataAtCell(i, 3) == null) {
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
  allChecked = true;
  for (let i = 0; i < checkList.length; i++) {
    if (hot.getDataAtCell(i, 3) != null) {
      if (checkList[i] != 1) {
        allChecked = false;
      }
    }
  }
  $(".checker").prop("checked", allChecked);
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
      // if ('N' == object["paymentStatus"]) {
      //   errorFlg = true;
      //   $.modal.alertWarning("Không thể xác nhận chứng từ gốc cho container chưa thanh toán. Vui lòng kiểm tra lại.");
      //   return false;
      // }
      shipmentDetailIds += object["id"] + ",";
    });

    if (!errorFlg) {
      if (shipmentDetailIds.length == 0) {
        $.modal.alertWarning("Bạn chưa chọn container nào.");
        errorFlg = true;
      } else {
        shipmentDetailIds = shipmentDetailIds.substring(
          0,
          shipmentDetailIds.length - 1
        );
      }
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
  $("#opr").combobox("select", "Chọn OPR");
  $("#logisticGroups").combobox("select", "0");
  $("#doStatus").combobox("select", "");
  $("#containerNo").textbox("setText", "");
  $("#blNo").textbox("setText", "");
  shipment = new Object();
  shipment.params = new Object();
  loadTable();
}

// function confirmRequestDocument() {
//   if (getDataSelectedFromTable()) {
//     layer.confirm(
//       "Xác nhận kiểm tra thông tin đúng.",
//       {
//         icon: 3,
//         title: "Xác Nhận",
//         btn: ["Đồng Ý", "Hủy Bỏ"],
//       },
//       function () {
//         $.modal.loading("Đang xử lý ...");
//         layer.close(layer.index);
//         $.ajax({
//           url: PREFIX + "/confirmation",
//           method: "POST",
//           data: {
//             shipmentDetailIds: shipmentDetailIds,
//             logisticGroupId: shipmentSelected.logisticGroupId,
//           },
//           success: function (res) {
//             $.modal.closeLoading();
//             if (res.code == 0) {
//               $.modal.alertSuccess(res.msg);

//               loadTable();
//             } else {
//               $.modal.alertError(res.msg);
//             }
//           },
//           error: function (data) {
//             $.modal.closeLoading();
//           },
//         });
//       },
//       function () {
//         // close form
//       }
//     );
//   }
// }

function rejectRequestDocument() {
  if (getDataSelectedFromTable()) {
    openReject();
  }
}

function logisticInfo(id, logistics) {
  $.modal.openLogisticInfo(
    "Thông tin liên lạc " + logistics,
    ctx + "om/support/logistics/" + id + "/info",
    null,
    470,
    function () {
      $.modal.close();
    }
  );
}

function loadListComment(shipmentCommentId) {
  let req = {
    serviceType: 1,
    shipmentId: shipmentSelected.id,
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
              date =
                createTime.substring(8, 10) +
                "/" +
                createTime.substring(5, 7) +
                "/" +
                createTime.substring(0, 4);
              time = createTime.substring(10, 19);
            }

            let resolvedBackground = "";
            if (
              (shipmentCommentId && shipmentCommentId == element.id) ||
              !element.resolvedFlg
            ) {
              resolvedBackground = 'style="background-color: #ececec;"';
              commentNumber++;
            }

            html += "<div " + resolvedBackground + ">";
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
            html +=
              "<div><span>" +
              element.content.replaceAll("#{domain}", domain) +
              "</span></div>";
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
      shipmentId: shipmentSelected.id,
      logisticGroupId: shipmentSelected.logisticGroupId,
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
  let vslCd = "EMTY";
  let voyNo = "0000";
  let containerNo = containerInfo.containerNo;
  if (containerInfo == null || !containerNo || !vslCd || !voyNo) {
    $.modal.alertWarning("Container chưa được khai báo.");
  } else {
    layer.open({
      type: 2,
      area: [1002 + "px", 500 + "px"],
      fix: true,
      maxmin: true,
      shade: 0.3,
      title: "Lịch Sử Container " + containerNo + " Catos",
      content:
        HIST_PREFIX +
        "/container/history/" +
        voyNo +
        "/" +
        vslCd +
        "/" +
        containerNo,
      btn: ["Đóng"],
      shadeClose: false,
      yes: function (index, layero) {
        layer.close(index);
      },
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
      area: [967 + "px", 500 + "px"],
      fix: true,
      maxmin: true,
      shade: 0.3,
      title:
        "Lịch Sử Container " +
        (containerInfo.containerNo != null ? containerInfo.containerNo : "") +
        " Eport",
      content: HIST_PREFIX + "/container/history/" + containerInfo.id,
      btn: ["Đóng"],
      shadeClose: false,
      yes: function (index, layero) {
        layer.close(index);
      },
    });
  }
}

/**
 * @param {none}
 * @description open model reject.js
 * @author Khanh
 */
function openReject() {
  layer.open({
    type: 2,
    area: [600 + 'px', 300 + 'px'],
    fix: true,
    maxmin: true,
    shade: 0.3,
    title: 'Khai báo lí do từ chối',
    content: PREFIX + "/reject",
    btn: ["Xác Nhận Từ Chối", "Hủy"],
    shadeClose: false,
    yes: function (index, layero) {
      confirmReject(index, layero);
    },
    cancel: function (index) {
      return true;
    }
  });
}

function confirmReject(index, layero) {
  let childLayer = layero.find("iframe")[0].contentWindow.document;
  const containerNoCheckeds = getListContainerNoFromCheked().join(", ");

  $.modal.loading("Đang xử lý ...");
  layer.close(layer.index);
  // $.ajax({
  //   url: PREFIX + "/reject",
  //   method: "POST",
  //   data: {
  //     shipmentDetailIds: shipmentDetailIds,
  //   },
  //   success: function (res) {
  //     const contentReject = $(childLayer).find("#contentReject").val();
  //     sendComment(contentReject, shipmentSelected.id, shipmentSelected.logisticGroupId, shipmentSelected.serviceType, containerNoCheckeds);
  //     if (res.code == 0) {
  //       $.modal.alertSuccess(res.msg);
  //       handleLoadTableFromModel();
  //     } else {
  //       $.modal.alertError(res.msg);
  //     }
  //   },
  //   error: function (data) {
  //     onCloseModel();
  //   },
  // });
  let shipmentDetailIdsChoosse = [];
  for (let i = 0; i < checkList.length; ++i) {
    if (checkList[i] == 1) {
      shipmentDetailIdsChoosse.push(sourceData[i].id);
    }
  }
  shipmentDetailIdsChoosse = shipmentDetailIdsChoosse.join(",");
  $.ajax({
    url: PREFIX + "/shipmentDetail/reject",
    method: "POST",
    accept: 'text/plain',
    data: { idShipmentDetails: shipmentDetailIdsChoosse },
    dataType: 'text',
    success: function (data) {
      const contentReject = $(childLayer).find("#contentReject").val();
      sendComment(contentReject, shipmentSelected.id, shipmentSelected.logisticGroupId, shipmentSelected.serviceType, containerNoCheckeds);
      loadTable();
    },
    error: function (result) {
      $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại.");
      $.modal.closeLoading();
    },
  });

}

/**
 * @param {}
 * @description Call api to add comment to server
 * @author Khanh
 */
function sendComment(contentReject, shipmentId, logisticGroupId, serviceType, containerNos) {
  let req = {
    topic: "Lí do từ chối xác nhận yêu cầu container " + containerNos,
    content: contentReject,
    shipmentId: `${shipmentId}`,
    logisticGroupId: `${logisticGroupId}`,
    serviceType: `${serviceType}`,
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


/**
 * @param {none}
 * @description handel event add coment in reject.js to load coments
 * @author Khanh
 */
function handleLoadCommentFromModelReject(shipmentCommentId) {
  loadListComment(shipmentCommentId);
}

/**
 * @param {none}
 * @description handel event load again table in reject.js to load Table
 * @author Khanh
 */
function handleLoadTableFromModel() {
  loadTable();
}

function formatServiceType(value, row) {
  switch (value) {
    case 1:
      return "Bốc Hàng";
    case 2:
      return "Hạ Rỗng";
    case 3:
      return "Bốc Rỗng";
    case 4:
      return "Hạ Hàng";
    default:
      return "";
  }
}

function getStatusContFollowIndex(index) {
  return null;
}

function getListContainerNoFromCheked() {
  let result = [];
  for (let i = 0; i < sourceData.length; ++i) {
    if (checkList[i] == 1) {
      result.push(sourceData[i].containerNo);
    }
  }
  return result;
}

function renderIconsFollowServiceTypeDropFull(
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
    sourceData[row].dischargePort &&
    sourceData[row].processStatus &&
    sourceData[row].paymentStatus &&
    sourceData[row].customStatus &&
    sourceData[row].finishStatus
  ) {
    // Command process status
    let process =
      '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';

    if (
      !getStatusContFollowIndex(row) ||
      getStatusContFollowIndex(row) == CONT_SPECIAL_STATUS.YES
    ) {
      switch (sourceData[row].processStatus) {
        case "E":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
          break;
        case "Y":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
          break;
        case "N":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
          break;
        case "D":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ hủy lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f93838;"></i>';
          break;
      }
    }

    // Payment status
    let payment =
      '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
    switch (sourceData[row].paymentStatus) {
      case "E":
        payment =
          '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Lỗi Thanh Toán" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
        break;
      case "Y":
        payment =
          '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case "N":
        if (value > 1) {
          payment =
            '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        }
        break;
    }
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
    // released status
    let released =
      '<i id="finish" class="fa fa-ship easyui-tooltip" title="Chưa Thể Giao Container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
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
    let content = "<div>";

    content += getRequestConfigIcon(row);

    content += process + payment;
    // Domestic cont: VN --> not show
    if (sourceData[row].dischargePort.substring(0, 2) != "VN") {
      content += customs;
    }
    content += released + "</div>";
    $(td).html(content);
  }
  return td;
}

// nhat 
function renderIconsStatusServiceTypePickupFull(
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
    sourceData[row].dischargePort &&
    sourceData[row].processStatus &&
    sourceData[row].paymentStatus &&
    sourceData[row].customStatus &&
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
    if (
      !getStatusContFollowIndex(row) ||
      getStatusContFollowIndex(row) == CONT_SPECIAL_STATUS.YES
    ) {
      switch (sourceData[row].processStatus) {
        case "E":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
          break;
        case "Y":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
          break;
        case "N":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
          break;
      }
    }
    // Payment status
    let payment =
      '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
    switch (sourceData[row].paymentStatus) {
      case "E":
        payment =
          '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Lỗi Thanh Toán" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
        break;
      case "Y":
        payment =
          '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case "N":
        if (value > 1) {
          payment =
            '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        }
        break;
    }
    // Do status
    let doStatus =
      '<i id="do" class="fa fa-file-text easyui-tooltip" title="Chưa Xác Nhận" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
    if (sourceData[row].doStatus == "Y") {
      doStatus =
        '<i id="do" class="fa fa-file-text easyui-tooltip" title="Đã Xác Nhận" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
    }
    // released status
    let released = "";
    // Drop full
    if (shipmentSelected.serviceType == 4) {
      released =
        '<i id="finish" class="fa fa-ship easyui-tooltip" title="Chưa thể nhận container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
      switch (sourceData[row].finishStatus) {
        case "Y":
          released =
            '<i id="finish" class="fa fa-ship easyui-tooltip" title="Đã Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
          break;
        case "N":
          if (sourceData[row].paymentStatus == "Y") {
            released =
              '<i id="finish" class="fa fa-ship easyui-tooltip" title="Có Thể Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
          }
          break;
      }
    } else {
      // Drop empty
      released =
        '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Chưa thể nhận container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
      switch (sourceData[row].finishStatus) {
        case "Y":
          released =
            '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Đã Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
          break;
        case "N":
          if (sourceData[row].paymentStatus == "Y") {
            released =
              '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Có Thể Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
          }
          break;
      }
    }

    // Return the content
    let content = "<div>";

    // if (!sourceData[row].sztp.includes("G")) {
    //   content += getRequestConfigIcon(sourceData[row].frozenStatus);
    // }
    content += getRequestConfigIcon(row);

    // Domestic cont: VN --> not show
    if (sourceData[row].dischargePort.substring(0, 2) != "VN") {
      content += customs;
    }

    content += process + payment + doStatus + released;

    /*if (sourceData[row].dischargePort.substring(0, 2) != "VN") {
        content += customs;
      }*/


    content += "</div>";
    $(td).html(content);
  }
  return td;
}
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

function confirmRequestDocument() {


  layer.confirm(
    "Xác nhận kiểm tra thông tin đúng.",
    {
      icon: 3,
      title: "Xác Nhận",
      btn: ["Đồng Ý", "Hủy Bỏ"],
    },
    function () {
      $.modal.loading("Đang xử lý ...");
      layer.close(layer.index);

      let shipmentDetailIdsChoosse = [];
      for (let i = 0; i < checkList.length; ++i) {
        if (checkList[i] == 1) {
          shipmentDetailIdsChoosse.push(sourceData[i].id);
        }
      }
      shipmentDetailIdsChoosse = shipmentDetailIdsChoosse.join(",");
      $.ajax({
        url: PREFIX + "/shipmentDetail/confirm",
        method: "POST",
        accept: 'text/plain',
        data: { idShipmentDetails: shipmentDetailIdsChoosse },
        dataType: 'text',
        success: function (data) {
          loadTable();
        },
        error: function (result) {
          $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại.");
          $.modal.closeLoading();
        },
      });
    },
    function () {
      // close form
    }
  );
}

function rejectRequest() {

}

function saveReeferSuccess() {
  $.modal.alertSuccess("Lưu thông tin thành công.");
}