package com.poc.onesignal.infrastructure.db;

import com.poc.onesignal.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {
  @Id
  @JdbcTypeCode(SqlTypes.VARCHAR)
  private UUID id;
  private String email;
  private String name;
  private String lastName;

  public User toDomain() {
    return User.builder()
        .id(this.id)
        .email(this.email)
        .name(this.name)
        .lastName(this.lastName)
        .build();
  }

  public static UserEntity fromDomain(User user) {
    UserEntity entity = new UserEntity();
    entity.setId(user.getId());
    entity.setEmail(user.getEmail());
    entity.setName(user.getName());
    entity.setLastName(user.getLastName());
    return entity;
  }
}