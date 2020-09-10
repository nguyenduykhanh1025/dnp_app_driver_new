var prefix = ctx + "logistic/vessel-changing";
var interval, currentPercent, timeout;
var dogrid = document.getElementById("container-grid"),
  hot;
var shipmentSelected, shipmentDetailIds, sourceData, currentProcessId;
var allChecked = false;
var checkList = [];
var rowAmount = 0;
var shipmentSearch = new Object();

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
  //DEFAULT SEARCH FOLLOW DATE
  loadTable();
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

  $("#main-layout").layout({
    onExpand: function (region) {
      if (region == "west") {
        hot.render();
      }
    },
  });
});

document.getElementById("bookingSearch").addEventListener("keyup", function (event) {
  event.preventDefault();
  if (event.keyCode === 13) {
    shipmentSearch.bookingNo = $("#bookingSearch").val().toUpperCase();
    loadTable();
  }
});

// LOAD SHIPMENT LIST
function loadTable(msg) {
  if (msg) {
    $.modal.alertSuccess(msg);
  }
  $("#dg").datagrid({
    url: prefix + "/shipments",
    height: window.innerHeight - 110,
    method: "post",
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
  let seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
  return day + "/" + monthText + "/" + date.getFullYear() + " " + hours + ":" + minutes + ":" + seconds;
}

// FORMAT REMARK FOR SHIPMENT LIST
function formatRemark(value) {
  return '<div class="easyui-tooltip" title="' + (value != null ? value : "Trống") + '" style="width: 80; text-align: center;"><span>' + (value != null ? value : "") + "</span></div>";
}

function handleRefresh() {
  loadTable();
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected() {
  let row = $("#dg").datagrid("getSelected");
  if (row) {
    shipmentSelected = row;
    $("#loCode").text(row.id);
    $("#taxCode").text(row.taxCode);
    $("#quantity").text(row.containerAmount);
    $("#bookingNo").text(row.bookingNo);
    loadShipmentDetail(row.id);
  }
}

// FORMAT HANDSONTABLE COLUMN
function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
  let content = "";
  if (checkList[row] == 1) {
    content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')" checked></div>';
  } else {
    content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')"></div>';
  }
  $(td)
    .attr("id", "checkbox" + row)
    .addClass("htCenter")
    .addClass("htMiddle")
    .html(content);
  return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html(value);
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle");
  $(td).html(value);
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle");
  $(td).html(value);
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle");
  $(td).html(value);
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle");
  $(td).html(value);
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function temperatureRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htRight");
  $(td).html(value);
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htRight");
  $(td).html(value);
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html(value);
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle");
  $(td).html(value);
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle");
  $(td).html(value);
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
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
    fixedColumnsLeft: 1,
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
          return "Container No";
        case 2:
          return "Kích Thước";
        case 3:
          return "Chủ Hàng";
        case 4:
          return "Hãng Tàu";
        case 5:
          return "Tàu và Chuyến";
        case 6:
          return "Nhiệt Độ";
        case 7:
          return "Trọng Lượng";
        case 8:
          return "Loại Hàng";
        case 9:
          return "Cảng Dỡ Hàng";
        case 10:
          return "Ghi Chú";
      }
    },
    // colWidths: [50, 100, 200, 200, 150, 220, 100, 100, 150, 150, 200],
    filter: "true",
    columns: [
      {
        data: "active",
        type: "checkbox",
        className: "htCenter",
        renderer: checkBoxRenderer,
      },
      {
        data: "containerNo",
        renderer: containerNoRenderer,
      },
      {
        data: "sztp",
        renderer: sizeRenderer,
      },
      {
        data: "consignee",
        renderer: consigneeRenderer,
      },
      {
        data: "opeCode",
        renderer: opeCodeRenderer,
      },
      {
        data: "vslNm",
        renderer: vslNmRenderer,
      },
      {
        data: "temperature",
        renderer: temperatureRenderer,
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
        data: "dischargePort",
        renderer: dischargePortRenderer,
      },
      {
        data: "remark",
        renderer: remarkRenderer,
      },
    ],
  };
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

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
  updateLayout();
  let tempCheck = allChecked;
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
    let cellStatus = hot.getDataAtCell(i, 1);
    if (cellStatus != null && checkList[i] != 1) {
      allChecked = false;
    }
  }
  $(".checker").prop("checked", allChecked);
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
        rowAmount = sourceData.length;
        checkList = Array(rowAmount).fill(0);
        allChecked = false;
        hot.destroy();
        configHandson();
        hot = new Handsontable(dogrid, config);
        hot.loadData(sourceData);
        hot.render();
        if (rowAmount == null || rowAmount == 0) {
          $.modal.alertWarning("Hiện tại không có container nào để đổi tàu.");
        }
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
  loadShipmentDetail(shipmentSelected.id);
}

// GET CHECKED SHIPMENT DETAIL LIST
function getDataSelectedFromTable() {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  let cleanedGridData = [];
  for (let i = 0; i < checkList.length; i++) {
    if (checkList[i] == 1 && Object.keys(myTableData[i]).length > 0) {
      cleanedGridData.push(myTableData[i]);
    }
  }

  let opecode, vessel, voyage;
  if (cleanedGridData.length > 0) {
    opecode = cleanedGridData[0].opeCode;
    vessel = cleanedGridData[0].vslNm;
    voyage = cleanedGridData[0].voyNo;
  }
  shipmentDetailIds = "";
  $.each(cleanedGridData, function (index, object) {
    if (opecode != object["opeCode"]) {
      $.modal.alertError("Hãng tàu không được khác nhau!");
      errorFlg = true;
      return false;
    } else if (vessel != object["vslNm"]) {
      $.modal.alertError("Tàu và Chuyến không được khác nhau!");
      errorFlg = true;
      return false;
    } else if (voyage != object["voyNo"]) {
      $.modal.alertError("Số chuyến không được khác nhau!");
      errorFlg = true;
      return false;
    }
    opecode = object["opeCode"];
    vessel = object["vslNm"];
    voyage = object["voyNo"];
    shipmentDetailIds += object["id"] + ",";
  });

  // Get result in "selectedList" letiable
  if (shipmentDetailIds.length == 0) {
    $.modal.alert("Bạn chưa chọn container.");
    errorFlg = true;
  } else {
    shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length - 1);
  }
  if (errorFlg) {
    return false;
  }
  return true;
}

function finishForm(result) {
  if (result.code == 0) {
    $.modal.alertSuccess(result.msg);
  } else {
    $.modal.alertError(result.msg);
  }
  reloadShipmentDetail();
}

function changeVessel() {
  if (getDataSelectedFromTable()) {
    $.modal.openCustomForm("Đổi Tàu/Chuyến", prefix + "/shipment-detail-ids/" + shipmentDetailIds + "/form", 800, 380);
  }
}

function otp(vessel) {
  $.modal.openCustomForm("Xác Thực OTP", prefix + "/otp/shipment-detail-ids/" + shipmentDetailIds + "/vessel/" + vessel, 600, 350);
}

function finishVerifyForm(result) {
  if (result.code == 0 || result.code == 301) {
    // $.modal.loading(result.msg);
    currentProcessId = result.processId;
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

function connectToWebsocketServer() {
  // Connect to WebSocket Server.
  $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
  console.log("Connect socket.");
  currentSubscription = $.websocket.subscribe(currentProcessId + "/response", onMessageReceived);
}

function onError(error) {
  console.log(error);
  $.modal.alertError("Could not connect to WebSocket server. Please refresh this page to try again!");
  setTimeout(() => {
    hideProgress();
    $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
  }, 1000);
  //$.modal.closeLoading();
}

function onMessageReceived(payload) {
  clearTimeout(timeout);
  setProgressPercent((currentPercent = 100));
  setTimeout(() => {
    hideProgress();

    let message = JSON.parse(payload.body);

    reloadShipmentDetail();

    if (message.code == 0) {
      $.modal.alertSuccess(message.msg);
    } else {
      $.ajax({
        url: prefix + "/process-order/" + currentProcessId + "/containers/failed",
        method: "GET",
      }).done(function (res) {
        if (res.code == 0) {
          $.modal.alertWarning(res.msg);
        } else {
          $.modal.alertError("Có lỗi xảy ra, vui lòng liên hệ admin.");
        }
      });
    }

    // Close loading
    //$.modal.closeLoading();

    // Unsubscribe destination
    if (currentSubscription) {
      currentSubscription.unsubscribe();
    }

    // Close websocket connection
    $.websocket.disconnect(onDisconnected);
  }, 1000);
}

function onDisconnected() {
  console.log("Disconnected socket.");
}

function showProgress(title) {
  $(".progress-wrapper").show();
  $(".dim-bg").show();
  $("#titleProgress").text(title);
  $(".percent-text").text("0%");
  currentPercent = 0;
  interval = setInterval(function () {
    if (currentPercent <= 99) {
      setProgressPercent(++currentPercent);
    }
    if (currentPercent >= 99) {
      clearInterval(interval);
    }
  }, 1000);
}

function setProgressPercent(percent) {
  $("#progressBar").prop("aria-valuenow", percent);
  $("#progressBar").css("width", percent + "%");
  $(".percent-text").text(percent + "%");
}

function hideProgress() {
  $(".progress-wrapper").hide();
  $(".dim-bg").hide();
  currentPercent = 0;
  $(".percent-text").text("0%");
  setProgressPercent(0);
}
