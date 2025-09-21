package main.java.com.poc.onesignal.infrastructure.onesignal;

import main.java.com.poc.onesignal.domain.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class OneSignalAdapter {

    private final String oneSignalAppId;
    private final String oneSignalRestApiKey;
    private final RestTemplate restTemplate;

    public OneSignalAdapter(@Value("${onesignal.appId}") String appId,
            @Value("${onesignal.restApiKey}") String restApiKey) {
        this.oneSignalAppId = appId;
        this.oneSignalRestApiKey = restApiKey;
        this.restTemplate = new RestTemplate();
    }

    public void send(DomainNotification domainNotification) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + oneSignalRestApiKey);

        Map<String, Object> payload = new HashMap<>();
        payload.put("app_id", oneSignalAppId);

        Map<String, String> contents = new HashMap<>();
        contents.put("en", domainNotification.getContent());
        payload.put("contents", contents);

        Map<String, String> headings = new HashMap<>();
        headings.put("en", domainNotification.getHeading());
        payload.put("headings", headings);

        // Target the user by their external_id (the user's UUID)
        payload.put("include_external_user_ids", Collections.singletonList(domainNotification.getRecipientId()));

        domainNotification.getSchedule().ifPresent(schedule -> {
            // OneSignal API expects a timestamp string
            payload.put("send_after", schedule.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'GMT'")));
        });

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://onesignal.com/api/v1/notifications",
                    entity,
                    String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Notification sent successfully: " + response.getBody());
            } else {
                System.err.println("Failed to send notification. Status code: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while sending notification: " + e.getMessage());
        }
    }
}