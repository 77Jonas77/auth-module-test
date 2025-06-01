package dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.repository;

import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByIdAndExpiresAtAfter(UUID id, Instant date);
}
