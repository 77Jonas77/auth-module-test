package dev.exampleinz.auth_module.infrastructure.adapter.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.exampleinz.auth_module.application.port.output.RefreshTokenOutputPort;
import dev.exampleinz.auth_module.application.port.output.UserOutputPort;
import dev.exampleinz.auth_module.application.service.UserAuthService;
import dev.exampleinz.auth_module.domain.model.RefreshToken;
import dev.exampleinz.auth_module.domain.model.User;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response.AuthenticationResponseDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.RefreshTokenEntity;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.repository.RefreshTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserAuthService authService;

    public OAuth2AuthenticationSuccessHandler(@Lazy UserAuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        AuthenticationResponseDTO authResponse = authService.handleOAuthLogin(email);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println("\n" + response + "\n");
        objectMapper.writeValue(response.getWriter(), authResponse);

    }


}
