package dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.mapper;

import dev.exampleinz.auth_module.domain.model.User;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public User toDomain(UserJpaEntity userJpaEntity) {
        return new User(
                userJpaEntity.getId(),
                userJpaEntity.getName(),
                userJpaEntity.getLastName(),
                userJpaEntity.getEmail(),
                userJpaEntity.getPassword(),
                userJpaEntity.getUsername(),
                userJpaEntity.getDateOfBirth(),
                userJpaEntity.getExperience(),
                userJpaEntity.getMoney(),
                userJpaEntity.isSendBudgetReport(),
                userJpaEntity.isProfilePublic(),
                userJpaEntity.isEmailVerified(),
                userJpaEntity.isFullDataProvided()
        );
    }

    public UserJpaEntity toEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setUsername(user.getUsername());
        entity.setDateOfBirth(user.getDateOfBirth());
        entity.setExperience(user.getExperience());
        entity.setMoney(user.getMoney());
        entity.setSendBudgetReport(user.isSendBudgetReport());
        entity.setProfilePublic(user.isProfilePublic());
        entity.setEmailVerified(user.isEmailVerified());
        entity.setFullDataProvided(user.isFullDataProvided());
        return entity;
    }

}
