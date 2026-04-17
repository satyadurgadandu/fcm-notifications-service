package in.satya.fcmnotification.config;

import java.io.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FirebaseConfig {
	
	
	@PostConstruct
    public void initFirebase() {
        try {
            // Only initialize once
            if (FirebaseApp.getApps().isEmpty()) {

                // Reads firebase-service-account.json from src/main/resources/
                InputStream serviceAccount =
                    new ClassPathResource("firebase-service-account.json").getInputStream();

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                
                 log.info("✅ Firebase initialized successfully!");
                System.out.println("✅ Firebase initialized successfully!");

                
            }
        } catch (IOException e) {
            System.out.println("❌ Firebase init failed. Did you add firebase-service-account.json to src/main/resources?");

            log.error("❌ Firebase init failed. Did you add firebase-service-account.json to src/main/resources?");
            throw new RuntimeException("Firebase initialization failed: " + e.getMessage());
        }
    }

}
