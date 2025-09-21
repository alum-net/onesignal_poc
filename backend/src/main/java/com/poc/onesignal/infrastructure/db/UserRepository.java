package main.java.com.poc.onesignal.infrastructure.db;

import main.java.com.poc.onesignal.domain.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
  Optional<UserEntity> findByEmail(String email);

  default User save(User user) {
    UserEntity entity = fromDomain(user);
    return save(entity).toDomain();
  }

  default Optional<User> findByEmail(String email) {
    return findByEmail(email).map(UserEntity::toDomain);
  }

  static UserEntity fromDomain(User user) {
    UserEntity entity = new UserEntity();
    entity.setId(user.getId());
    entity.setEmail(user.getEmail());
    entity.setName(user.getName());
    entity.setLastName(user.getLastName());
    return entity;
  }
}