package main.java.com.poc.onesignal.infrastructure.rest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class SendNotificationRequest {
  @NotBlank
  private String heading;
  @NotBlank
  private String content;
  private String scheduledAt;
}