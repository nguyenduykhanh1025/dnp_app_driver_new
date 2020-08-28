const PREFIX = ctx + "gate/test"
const WEB_SOCKET_TOPIC = "gate/detection/monitor";

$(".main-body").height($(window).height() - 10);
$(".easyui-layout").height($(window).height() - 15);
// $(".grey-background").height($(document).innerHeight() - 100);
$(window).resize(function () {
  $(".main-body").height($(window).height() - 10);
  $(".easyui-layout").height($(window).height() - 15);
});


$(".main-body").layout();

$(".left-side__collapse").click(function () {
  $("#main-layout").layout("collapse", "west");
  setTimeout(() => {
    hot.render();
  }, 200);
});

$(".right-side__collapse").click(function() {
  $('#right-layout').layout('collapse','south');
})

$('#dg').datagrid({
  height: rightHeight - 60
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
    receiveOption: $("#receiveOption").prop("checked"),
    yardPosition1: $("#yardPosition1").val(),
    yardPosition2: $("#yardPosition2").val(),
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


function loadTable() {
  $('#dg').datagrid({
    url: PREFIX + "/pickupList",
    method: "POST",
    height: rightHeight - 60,
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    pagination: false,
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
        contentType: "application/json",
        data: JSON.stringify({
          truckNo: $('#truckNo').val(),
          chassisNo: $("#chassisNo").val(),
        }),
        success: function (data) {
          if (data.code == 0) {
            success(data.pickupList);
          } else if (data.code == 301) {
            $.modal.alertWarning(data.msg);
          } else {
            $.modal.alertError(data.msg);
          }
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function searchPickup() {
  loadTable();
}

function formatServiceType(value, row) {
  if (row && row.shipment && row.shipment.serviceType) {
    switch (row.shipment.serviceType) {
      case 1:
        return 'Bốc Hàng';
      case 2:
        return 'Hạ Rỗng';
      case 3:
        return 'Bốc Rỗng';
      case 4:
        return 'Hạ Hàng';
    }
  } 
  return '';
}

function passDataDetection() {
  if ($("#truckNoDetection").text() && 'empty' != $("#truckNoDetection").text()) {
    $("#truckNo").val($("#truckNoDetection").text());
  }
  
  if ($("#chassisNoDetection").text() && 'empty' != $("#chassisNoDetection").text()) {
    $("#chassisNo").val($("#chassisNoDetection").text());
  }
  
  if ($("#container1").text() && 'empty' != $("#container1").text()) {
    $("#containerSend1").val($("#container1").text());
  }

  if ($("#container2").text() && 'empty' != $("#container2").text()) {
    $("#containerSend2").val($("#container2").val());
  }
}