package dev.exampleinz.auth_module.application.port.input;

import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response.AuthenticationResponseDTO;

import java.util.UUID;

public interface RefreshTokenInputPort {
    AuthenticationResponseDTO refreshToken(UUID refreshTokenId);
}
