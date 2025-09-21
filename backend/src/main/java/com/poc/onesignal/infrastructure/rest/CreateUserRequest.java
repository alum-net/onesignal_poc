package main.java.com.poc.onesignal.infrastructure.rest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String name;
  @NotBlank
  private String lastName;
}