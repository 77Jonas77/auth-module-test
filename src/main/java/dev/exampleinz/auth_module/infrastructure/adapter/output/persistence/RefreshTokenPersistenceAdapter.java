package dev.exampleinz.auth_module.infrastructure.adapter.output.persistence;

import dev.exampleinz.auth_module.application.port.output.RefreshTokenOutputPort;
import dev.exampleinz.auth_module.domain.model.RefreshToken;
import dev.exampleinz.auth_module.domain.model.User;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.RefreshTokenEntity;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.UserJpaEntity;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenPersistenceAdapter implements RefreshTokenOutputPort {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserPersistenceMapper userPersistenceMapper;
    private final UserPersistenceAdapter userPersistenceAdapter;

    public RefreshTokenPersistenceAdapter(RefreshTokenRepository refreshTokenRepository, UserPersistenceMapper userPersistenceMapper, UserPersistenceAdapter userPersistenceAdapter) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userPersistenceMapper = userPersistenceMapper;
        this.userPersistenceAdapter = userPersistenceAdapter;
    }

    @Override
    public Optional<RefreshToken> findByIdAndExpiresAtAfter(UUID id, Instant date) {
        Optional<RefreshTokenEntity> entityOpt = refreshTokenRepository.findByIdAndExpiresAtAfter(id, date);
        return entityOpt.map(this::mapToDomain);
    }

    @Override
    public void deleteById(UUID uuid) {
        refreshTokenRepository.deleteById(uuid);
    }

    @Override
    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = mapToEntity(refreshToken);

        UserJpaEntity userJpaEntity = userPersistenceAdapter.findById(entity.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + entity.getUser().getId()));

        entity.setUser(userJpaEntity);

        RefreshTokenEntity savedEntity = refreshTokenRepository.save(entity);

        return mapToDomain(savedEntity);
    }

    private RefreshToken mapToDomain(RefreshTokenEntity entity) {
        return new RefreshToken(
                entity.getId(),
                userPersistenceMapper.toDomain(entity.getUser()),
                entity.getCreatedAt(),
                entity.getExpiresAt()
        );
    }

    private RefreshTokenEntity mapToEntity(RefreshToken token) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setId(token.getId());
        entity.setUser(userPersistenceMapper.toEntity(token.getUser()));
        entity.setCreatedAt(token.getCreatedAt());
        entity.setExpiresAt(token.getExpiresAt());
        return entity;
    }
}
