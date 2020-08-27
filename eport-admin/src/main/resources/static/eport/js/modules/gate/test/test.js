const PREFIX = ctx + "gate/test"
const WEB_SOCKET_TOPIC = "gate/detection/monitor";

$(".main-body").layout();

$(".left-side__collapse").click(function () {
  $("#main-layout").layout("collapse", "west");
  setTimeout(() => {
    hot.render();
  }, 200);
});

connectToWebsocketServer();

function connectToWebsocketServer() {
  // Connect to WebSocket Server.
  $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
  $.websocket.subscribe(WEB_SOCKET_TOPIC, onMessageReceived);
}

function onMessageReceived(payload) {
  let message = JSON.parse(payload.body);

  if (message) {
    if (message.containerNo1) {
      $('#container1').html(message.containerNo1);
    }

    if (message.containerNo2) {
      $('#container2').html(message.containerNo2);
    }

    if (message.truckNo) {
      $('#truckNoDetection').html(message.truckNo);
    }

    if (message.chassisNo) {
      $('#chassisNoDetection').html(message.chassisNo);
    }
  }
}

function onError(error) {
  console.error(
    "Could not connect to WebSocket server. Please refresh this page to try again!"
  );
}

function loadYardPosition() {
  console.log('loadYardPosition');
}

function gateIn() {
  let reqData = {
    truckNo: $('#truckNo').val(),
    chassisNo: $("#chassisNo").val(),
    gatePass: $("#gatePass").val(),
    weight: $("#wgt").val(),
    containerSend1: $("#containerSend1").val(),
    containerSend2: $("#containerSend2").val(),
    blNo: $("#blNo").val(),
    refNo: $("#refNo").val(),
    containerFlg: $('input[name="containerFlg"]:checked').val(),
    containerReceive1: $("#containerReceive1").val(),
    containerReceive2: $("#containerReceive2").val(),
    containerAmount: $("#containerAmount").val(),
    logisticGroupId: $("#logistics").val(),
    driverId: $("#drivers").val(),
    sendOption: $("#sendOption").prop("checked"),
    receiveOption: $("#receiveOption").prop("checked")
  };
  $.modal.loading("Đang tạo dữ liệu mẫu gate-in...");
  $.ajax({
    cache: true,
    type: "POST",
    url: PREFIX + "/gateIn",
    contentType: "application/json",
    data: JSON.stringify(reqData),
    async: false,
    error: function (request) {
      $.modal.closeLoading();
      $.modal.alertError("System error");
    },
    success: function (result) {
      $.modal.closeLoading();
      if (result.code == web_status.SUCCESS) {
        $.modal.alert("Thành công!");
      } else if (result.code == web_status.WARNING) {
        $.modal.alertWarning(result.msg);
      } else {
        $.modal.alertError(result.msg);
      }
    },
  });

}

$("#logistics").combobox({
  valueField: 'id',
  textField: 'groupName',
  url: PREFIX + '/logistics',
  onSelect: function (logistic) {
    var url = PREFIX + '/logistic/' + logistic.id + '/drivers';
    $('#drivers').combobox({
      valueField: 'id',
      textField: 'mobileNumber',
      url: url
    });
  }
});