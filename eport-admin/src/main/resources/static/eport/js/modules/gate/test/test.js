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
    } else {
      $('#container1').html("empty");
    }

    if (message.containerNo2) {
      $('#container2').html(message.containerNo2);
    } else {
      $('#container2').html("empty");
    }

    if (message.truckNo) {
      $('#truckNoDetection').html(message.truckNo);
    } else {
      $('#truckNoDetection').html("empty");
    }

    if (message.chassisNo) {
      $('#chassisNoDetection').html(message.chassisNo);
    } else {
      $('#chassisNoDetection').html("empty");
    }
  }
}

function onError(error) {
  console.error(
    "Could not connect to WebSocket server. Please refresh this page to try again!"
  );
}

function loadYardPosition() {
  let url = '';
  if ($("#blNo").val()) {
    url = PREFIX + "/blNo/" + $("#blNo").val() + "/yardPosition";
  } else if ($("#refNo").val()) {
    url = PREFIX + "/jobOrder/" + $("#refNo").val() + "/yardPosition";
  }
  if (url) {
    $.ajax({
      cache: true,
      type: "GET",
      url: url,
      async: false,
      error: function (request) {
        $.modal.closeLoading();
        $.modal.alertError("System error");
      },
      success: function (result) {
        $.modal.closeLoading();
        if (result.code == web_status.SUCCESS) {
          $.modal.msgSuccess("Thành công!");
          showYardPosition(result.bayList);
        } else if (result.code == web_status.WARNING) {
          $.modal.msgWarning(result.msg);
        } else {
          $.modal.msgError(result.msg);
        }
      },
    });
  } else {
    $.modal.alertWarning("Bạn chưa nhập thông tin.");
  }
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

function showYardPosition(bayList) {
  var index = 0;
  console.log(bayList);
  bayList.forEach(function (bay) {
    let level = 1;
    let str = '<div class="bayPosition">';
    let bayName = "";
    for (let col = 0; col < bay[0].length; col++) {
      if (col > 5 && level == 1) {
        level++;
        str += '</div><div style="margin-bottom: 10px;"><b>' + bayName + '</b></div>';
        if (bayName) {
          $(".contListPosition").append(str);
        }
        str = '<div class="bayPosition">';
        bayName = "";
      }
      str += '<div class="columnDiv">';
      for (let row = 4; row >= 0; row--) {
        if (bay[row][col] != null) {
          bayName = bay[row][col].block + "-" + bay[row][col].bay;

          // Position is empty
          if (bay[row][col].containerNo == null) {
            str += '<div id="cell' + bay[row][col].id + '" class="cellDiv" style="background-color: #dbcfcf;>CONT</div>';

            // Container must be make into an order
          } else {

            str += '<div style="background-color: #72ecea;" class="cellDiv">' + bay[row][col].containerNo + '</div>';

          } 
        } else {
          str += '<div class="cellDivDisable"></div>';
        }
      }
      str += '</div>';
    }
    str += '</div><div style="margin-bottom: 10px;"><b>' + bayName + '</b></div>';
    $(".contListPosition").append(str);
    index++;
  });
}

function checkIn() {
  let reqData = {
    gateId: $('#gateId').val(),
    truckNo: $('#truckNo').val(),
    chassisNo: $("#chassisNo").val(),
    containerNo1: $("#containerSend1").val(),
    containerNo2: $("#containerSend2").val(),
    wgt : $("#wgt").val()
  };
  $.modal.loading("Đang xử lý...");
  $.ajax({
    cache: true,
    type: "POST",
    url: "http://app.danangport.com/api/gate/detection",
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

