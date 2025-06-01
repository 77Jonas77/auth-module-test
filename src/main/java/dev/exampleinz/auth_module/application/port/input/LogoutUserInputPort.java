package dev.exampleinz.auth_module.application.port.input;

import java.util.UUID;

public interface LogoutUserInputPort {
    void revokeRefreshToken(UUID refreshTokenId);
}
