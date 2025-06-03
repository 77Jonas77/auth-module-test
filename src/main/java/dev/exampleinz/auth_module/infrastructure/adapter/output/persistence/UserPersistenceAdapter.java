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
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userJpaRepository.findFirstByEmail(email))
                .map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userJpaRepository.findFirstByUsername(username))
                .map(userPersistenceMapper::toDomain);
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
