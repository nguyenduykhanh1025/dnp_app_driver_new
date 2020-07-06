package vn.com.irtech.eport.framework.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import vn.com.irtech.eport.system.service.ISysConfigService;

@Service
public class FirebaseConfig {
	@Autowired
	private ISysConfigService configService;
	
	@PostConstruct
    public void initialize() {
        try {
        	String firebaseKey = configService.selectConfigByKey("firebase.credental");
        	if (firebaseKey == null) throw new Exception("Firebase key null!");
        	
        	InputStream inputStream = new ByteArrayInputStream(firebaseKey.getBytes("UTF-8"));
			FirebaseOptions options = new FirebaseOptions.Builder()
			  .setCredentials(GoogleCredentials.fromStream(inputStream))
			  .setDatabaseUrl(configService.selectConfigByKey("firebase.url"))
			  .build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

    }
}
