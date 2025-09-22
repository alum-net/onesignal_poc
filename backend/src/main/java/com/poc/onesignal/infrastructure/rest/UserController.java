package com.poc.onesignal.infrastructure.rest;

import com.poc.onesignal.application.UserPort;
import com.poc.onesignal.domain.User;
import com.poc.onesignal.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserPort userPort;

  @PostMapping
  public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
    try {
      User user = userPort.createUser(request.getEmail(), request.getName(), request.getLastName());
      return new ResponseEntity<>(user, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
  }

  @PostMapping("/{email}/send-notification")
  public ResponseEntity<Void> sendInstantNotification(@PathVariable String email,
      @Valid @RequestBody SendNotificationRequest request) {
    try {
      Notification notification = Notification.builder()
          .heading(request.getHeading())
          .content(request.getContent())
          .schedule(Optional.empty())
          .build();
      userPort.sendInstantNotification(email, notification);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/{email}/schedule-notification")
  public ResponseEntity<Void> scheduleNotification(@PathVariable String email,
      @Valid @RequestBody SendNotificationRequest request) {
    try {
      LocalDateTime scheduledTime = LocalDateTime.parse(request.getScheduledAt(),
          DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      Notification notification = Notification.builder()
          .heading(request.getHeading())
          .content(request.getContent())
          .schedule(Optional.of(scheduledTime))
          .build();
      userPort.scheduleNotification(email, notification);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/exists/{email}")
  public ResponseEntity<Map<String, Boolean>> checkUserExists(@PathVariable String email) {
    boolean exists = userPort.findUserByEmail(email).isPresent();
    Map<String, Boolean> response = new HashMap<>();
    response.put("exists", exists);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}