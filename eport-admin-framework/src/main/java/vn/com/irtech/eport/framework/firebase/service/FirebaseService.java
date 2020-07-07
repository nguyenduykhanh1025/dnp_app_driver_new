package vn.com.irtech.eport.framework.firebase.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;

@Service
public class FirebaseService {
  public int sendNotification(String title, String content, List<String> receiverTokens) throws FirebaseMessagingException {
	  MulticastMessage message = MulticastMessage.builder()
			  .putData("title", title)
			  .putData("content", content)
			  .addAllTokens(receiverTokens)
			  .build();
	  BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
	  return response.getSuccessCount();
  }
  
}
