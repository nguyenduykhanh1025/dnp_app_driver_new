var stompClient = null;
const ENDPOINT = '/eport_websocket';

(function ($) {
  $.extend({
    websocket: {
      // Connect to websocket server
      connect: function(headers = {}, connectCallback, errorCallback){
        var socket = new SockJS(ENDPOINT);
        stompClient = Stomp.over(socket);
      
        stompClient.connect(headers, connectCallback, errorCallback);
      },

      // Subcribe message from websocket server
      subscribe: function(destination, callback, headers = {}){
        if (stompClient == null){
          console.error("Please connect to websocket server before subcribe!");
          return;
        }
        stompClient.subscribe(destination, callback, headers);
      },
      client: stompClient
    }
  });
})(jQuery);