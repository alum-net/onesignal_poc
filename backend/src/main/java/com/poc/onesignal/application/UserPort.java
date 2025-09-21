package main.java.com.poc.onesignal.application;

import main.java.com.poc.onesignal.domain.User;
import main.java.com.poc.onesignal.domain.Notification;

import java.util.Optional;

public interface UserPort {
  User createUser(String email, String name, String lastName);

  Optional<User> findUserByEmail(String email);

  void sendInstantNotification(String userEmail, Notification notification);

  void scheduleNotification(String userEmail, Notification notification);
}