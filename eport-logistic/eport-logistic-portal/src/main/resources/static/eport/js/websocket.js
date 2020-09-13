var stompClient = null;
const ENDPOINT = ctx + 'eport_logistic';
const DESTINATION_PREFIX = ctx + 'eport_logistic/';

(function ($) {
  $.extend({
    websocket: {
      connect: function(headers = {}, connectCallback, errorCallback){
        let socket = new SockJS(ENDPOINT);
        stompClient = Stomp.over(socket);
      
        stompClient.connect(headers, connectCallback, errorCallback);
      },
      disconnect: function(disconnectCallback, headers = {}){
        if (stompClient != null){
          stompClient.disconnect(disconnectCallback, headers);
        }
      },
      subscribe: function(destination, callback, headers = {}){
        if (stompClient == null){
          return;
        }
        return stompClient.subscribe(DESTINATION_PREFIX + destination, callback, headers);
      },
      client: stompClient
    }
  });
})(jQuery);