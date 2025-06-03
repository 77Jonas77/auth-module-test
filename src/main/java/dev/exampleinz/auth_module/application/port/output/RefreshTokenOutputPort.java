package dev.exampleinz.auth_module.application.port.output;

import dev.exampleinz.auth_module.domain.model.RefreshToken;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.RefreshTokenEntity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenOutputPort {
    Optional<RefreshToken> findByIdAndExpiresAtAfter(UUID id, Instant date);
    void deleteById(UUID uuid);
    RefreshToken save(RefreshToken refreshToken);
}
