package dev.exampleinz.auth_module.infrastructure.adapter.output.persistence;

import dev.exampleinz.auth_module.application.port.output.RefreshTokenOutputPort;
import dev.exampleinz.auth_module.domain.model.RefreshToken;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.RefreshTokenEntity;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.repository.RefreshTokenRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenPersistenceAdapter implements RefreshTokenOutputPort {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenPersistenceAdapter(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Optional<RefreshToken> findByIdAndExpiresAtAfter(UUID id, Instant date) {
//        return refreshTokenRepository.findByIdAndExpiresAtAfter(id,date);
        return null;
    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return null;
    }
}
