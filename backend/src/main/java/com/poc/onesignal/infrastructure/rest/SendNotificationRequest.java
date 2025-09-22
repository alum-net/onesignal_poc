package com.poc.onesignal.infrastructure.rest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendNotificationRequest {
  @NotBlank
  private String heading;
  @NotBlank
  private String content;
  private String scheduledAt;
}