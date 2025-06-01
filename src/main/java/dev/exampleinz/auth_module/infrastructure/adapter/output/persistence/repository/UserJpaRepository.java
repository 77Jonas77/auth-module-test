package dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.repository;

import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    UserJpaEntity findFirstByEmail(String email);

    UserJpaEntity findFirstByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}