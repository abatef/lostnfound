package com.hatla2y.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            FileInputStream fis =
                    new FileInputStream("src/main/resources/firebase-config.json");
            FirebaseOptions opts = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(fis))
                    .build();
            FirebaseApp.initializeApp(opts, FirebaseApp.DEFAULT_APP_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
