package dev.exampleinz.auth_module.infrastructure.adapter.output.persistence;

import dev.exampleinz.auth_module.application.port.output.UserOutputPort;
import dev.exampleinz.auth_module.domain.model.User;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.UserJpaEntity;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserPersistenceAdapter implements UserOutputPort {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository, UserPersistenceMapper userPersistenceMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = userPersistenceMapper.toEntity(user);
        UserJpaEntity userCreated = userJpaRepository.save(userJpaEntity);
        return userPersistenceMapper.toDomain(userCreated);
    }

    @Override
    public Optional<UserJpaEntity> findByEmail(String email) {
        UserJpaEntity userJpaEntity = userJpaRepository.findFirstByEmail(email);
        return Optional.ofNullable(userJpaEntity);
    }

    @Override
    public Optional<UserJpaEntity> findByUsername(String username) {
        UserJpaEntity userJpaEntity = userJpaRepository.findFirstByUsername(username);
        return Optional.ofNullable(userJpaEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

}
