package com.poc.onesignal.domain;

import lombok.Builder;
import lombok.Getter;
import java.util.Optional;
import java.time.OffsetDateTime;

@Getter
@Builder
public class Notification {
  private String heading;
  private String content;
  private String recipientId;
  private Optional<OffsetDateTime> schedule;
}