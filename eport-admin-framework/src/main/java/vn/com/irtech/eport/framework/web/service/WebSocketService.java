package vn.com.irtech.eport.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

	@Autowired
    private SimpMessageSendingOperations messagingTemplate;
	
	@Value("${websocket.message-broker}")
	private String messageBroker;
	
	public void sendMessage(String destination, Object payload) {
		logger.info("Websocket - send message to: " + destination);
		messagingTemplate.convertAndSend(this.messageBroker + destination, payload);
	}
	
}
