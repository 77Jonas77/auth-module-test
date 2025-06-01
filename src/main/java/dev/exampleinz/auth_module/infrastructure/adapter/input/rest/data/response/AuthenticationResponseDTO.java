package dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response;

import java.util.UUID;

public record AuthenticationResponseDTO(String accessToken, UUID refreshToken) {

    public AuthenticationResponseDTO(String accessToken, UUID refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public UUID getRefreshToken() {
        return refreshToken;
    }
}
