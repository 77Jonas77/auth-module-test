package dev.exampleinz.auth_module.infrastructure.adapter.input.rest.mapper;

import dev.exampleinz.auth_module.domain.model.User;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.request.RegisterUserRequestDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response.AuthenticationResponseDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response.RegisterUserResponseDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserRestMapper {

    public User RegisterToDomain(RegisterUserRequestDTO registerUserRequestDTO) {
        return new User(
                registerUserRequestDTO.getName(),
                registerUserRequestDTO.getLastName(),
                registerUserRequestDTO.getEmail(),
                registerUserRequestDTO.getPassword(),
                registerUserRequestDTO.getUsername(),
                registerUserRequestDTO.getDateOfBirth(),
                registerUserRequestDTO.isSendBudgetReport(),
                registerUserRequestDTO.isProfilePublic()
        );
    }

    public RegisterUserResponseDTO toRegisterResponse(User user) {
        return new RegisterUserResponseDTO(
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getDateOfBirth(),
                user.getExperience(),
                user.getMoney(),
                user.isSendBudgetReport(),
                user.isProfilePublic(),
                user.isEmailVerified()
        );
    }

    public AuthenticationResponseDTO toAuthenticationResponse(String accessToken, UUID refreshToken) {
        return new AuthenticationResponseDTO(
                accessToken,
                refreshToken
        );
    }
}
