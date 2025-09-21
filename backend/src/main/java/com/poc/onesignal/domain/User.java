package main.java.com.poc.onesignal.domain;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
public class User {
  private UUID id;
  private String email;
  private String name;
  private String lastName;
}