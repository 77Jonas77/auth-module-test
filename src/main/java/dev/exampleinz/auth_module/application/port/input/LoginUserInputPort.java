package dev.exampleinz.auth_module.application.port.input;

import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response.AuthenticationResponseDTO;

public interface LoginUserInputPort {
    AuthenticationResponseDTO login(String login, String password);
}
