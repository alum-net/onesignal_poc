package main.java.com.poc.onesignal.domain;

import lombok.Builder;
import lombok.Getter;
import java.util.Optional;
import java.time.LocalDateTime;

@Getter
@Builder
public class Notification {
  private String heading;
  private String content;
  private String recipientId;
  private Optional<LocalDateTime> schedule;
}