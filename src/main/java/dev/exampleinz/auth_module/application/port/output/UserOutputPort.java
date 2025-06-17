package dev.exampleinz.auth_module.application.port.output;


import dev.exampleinz.auth_module.domain.model.User;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.UserJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserOutputPort {
    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<UserJpaEntity> findById(UUID id);
}
