package com.poc.onesignal.infrastructure.db;

import com.poc.onesignal.domain.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {
  Optional<UserEntity> findByEmail(String email);

  default User save(User user) {
    UserEntity entity = UserEntity.fromDomain(user);
    return ((CrudRepository<UserEntity, UUID>) this).save(entity).toDomain();
  }

  default Optional<User> findDomainUserByEmail(String email) {
    return findByEmail(email).map(UserEntity::toDomain);
  }
}