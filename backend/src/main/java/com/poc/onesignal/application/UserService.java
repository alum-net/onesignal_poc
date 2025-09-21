package main.java.com.poc.onesignal.application;

import main.java.com.poc.onesignal.domain.User;
import main.java.com.poc.onesignal.domain.Notification;
import main.java.com.poc.onesignal.infrastructure.db.UserRepository;
import main.java.com.poc.onesignal.infrastructure.onesignal.OneSignalAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserPort {
  private final UserRepository userRepository;
  private final OneSignalAdapter oneSignalAdapter;

  @Override
  public User createUser(String email, String name, String lastName) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("User with this email already exists.");
    }
    User newUser = User.builder()
        .id(UUID.randomUUID())
        .email(email)
        .name(name)
        .lastName(lastName)
        .build();
    return userRepository.save(newUser);
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public void sendInstantNotification(String userEmail, Notification notification) {
    Optional<User> userOptional = findUserByEmail(userEmail);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      Notification instantNotification = Notification.builder()
          .heading(notification.getHeading())
          .content(notification.getContent())
          .recipientId(user.getId().toString())
          .schedule(Optional.empty())
          .build();
      oneSignalAdapter.send(instantNotification);
    } else {
      throw new IllegalArgumentException("User not found.");
    }
  }

  @Override
  public void scheduleNotification(String userEmail, Notification notification) {
    Optional<User> userOptional = findUserByEmail(userEmail);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      Notification scheduledNotification = Notification.builder()
          .heading(notification.getHeading())
          .content(notification.getContent())
          .recipientId(user.getId().toString())
          .schedule(notification.getSchedule())
          .build();
      oneSignalAdapter.send(scheduledNotification);
    } else {
      throw new IllegalArgumentException("User not found.");
    }
  }
}