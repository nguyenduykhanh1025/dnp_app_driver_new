const PREFIX = ctx + "mc/plan/request";

// Init screen
$(document).ready(function () {
  loadTable();

  // CONNECT WEB SOCKET
  connectToWebsocketServer();

  $('#btnSearch').click(function(){
    loadTable();
  });

  $('#btnRefresh').click(function(){
    loadTable();
  });
});

// Init data for table
function loadTable() {
  $("#dg").datagrid({
    url: PREFIX + "/list",
    method: "GET",
    height: document.documentElement.clientHeight - 140,
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    pagination: true,
    fitColumns: true,
    rownumbers: true,
    pageSize: 50,
    nowrap: false,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        data: {
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          serviceType: $("select[name='params[serviceType]']").val(),
          contNo: $("input[name='params[contNo]']").val(),
          sztp: $("input[name='params[sztp]']").val(),
          vslNm: $("input[name='params[vslNm]']").val(),
          voyNo: $("input[name='params[voyNo]']").val(),
          dischargePort: $("input[name='params[dischargePort]']").val(),
          consignee: $("input[name='params[consignee]']").val(),
          opeCode: $("input[name='params[opeCode]']").val()
        },
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

function formatServiceType(value, row, index) {
  if (row.shipment === null) {
    return "";
  }
  if (row.shipment.serviceType == 2) {
    return '<span class="text-warning">Empty in</span>';
  }
  if (row.shipment.serviceType == 4) {
    return '<span class="text-info">Full in</span>';
  }
  return "";
}

function formatContNo(value, row, index) {
  return row.containerNo;
}

function formatSztp(value, row, index) {
  return row.shipmentDetail.sztp;
}

function formatVslNm(value, row, index) {
  return row.shipmentDetail.vslNm;
}

function formatVoyNo(value, row, index) {
  return row.shipmentDetail.voyNo;
}

function formatDischargePort(value, row, index) {
  return row.shipmentDetail.dischargePort;
}

function formatOpeCode(value, row, index) {
  return row.shipmentDetail.opeCode;
}

function formatConsignee(value, row, index) {
  return row.shipmentDetail.consignee;
}

function formatAction(value, row, index) {
  var actions = [];
  actions.push(
    '<button type="button" class="btn btn-primary btn-xs" onclick="edit(\'' +
      row.id +
      "')\">Nhập tọa độ</button>"
  );
  return actions.join(" ");
}

// Open edit popup
function edit(pickupHistoryId) {
  const options = {
    url: ctx + "mc/plan/edit/" + pickupHistoryId,
    title: "Lập tọa độ cho container",
    skin: "custom-modal",
    btn: ["OK"],
    height: 420,
    yes: function (index, layero) {
      let iframeWin = layero.find("iframe")[0];
      iframeWin.contentWindow.submitHandler(index, layero);
    },
  };
  $.modal.openOptions(options);
}

function connectToWebsocketServer() {
  // Connect to WebSocket Server.
  $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
  $.websocket.subscribe("mc/plan/request", onMessageReceived);
}

function onMessageReceived(payload) {
  let audio = document.getElementById("alertAudio");
  if (audio !== null) {
    audio.play();
  }

  let message = JSON.parse(payload.body);

  edit(message);

  $.notification.notify(
    "Yêu cầu nhập tọa độ cho container",
    "Nhấn vào đây để nhập!"
  );
}

function onError(error) {
  console.error(
    "Could not connect to WebSocket server. Please refresh this page to try again!"
  );
}

function search(){
  loadTable();
}
