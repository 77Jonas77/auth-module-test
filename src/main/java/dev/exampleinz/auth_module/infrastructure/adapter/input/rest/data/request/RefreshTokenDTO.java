package dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.request;

import java.util.UUID;

public record RefreshTokenDTO(UUID refreshTokenId) {

    public UUID getRefreshTokenId() {
        return refreshTokenId;
    }

}
