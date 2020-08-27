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