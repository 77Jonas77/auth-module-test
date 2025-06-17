package dev.exampleinz.auth_module.application.port.input;

import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response.AuthenticationResponseDTO;

import javax.naming.AuthenticationException;

public interface HandleOAuthCodeExchange {
    public AuthenticationResponseDTO handleOAuthCodeExchange(String code, String codeVerifier) throws AuthenticationException;
}
