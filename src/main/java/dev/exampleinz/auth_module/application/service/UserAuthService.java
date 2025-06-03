package dev.exampleinz.auth_module.application.service;

import dev.exampleinz.auth_module.application.port.input.LoginUserInputPort;
import dev.exampleinz.auth_module.application.port.input.LogoutUserInputPort;
import dev.exampleinz.auth_module.application.port.input.RefreshTokenInputPort;
import dev.exampleinz.auth_module.application.port.input.RegisterUserInputPort;
import dev.exampleinz.auth_module.application.port.output.RefreshTokenOutputPort;
import dev.exampleinz.auth_module.application.port.output.UserOutputPort;
import dev.exampleinz.auth_module.domain.model.RefreshToken;
import dev.exampleinz.auth_module.domain.model.User;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response.AuthenticationResponseDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.RefreshTokenEntity;
import dev.exampleinz.auth_module.infrastructure.adapter.shared.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
public class UserAuthService implements RegisterUserInputPort, LoginUserInputPort, UserDetailsService, LogoutUserInputPort, RefreshTokenInputPort {

    private UserOutputPort userOutputPort;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private RefreshTokenOutputPort refreshTokenRepository;

    public UserAuthService(UserOutputPort userOutputPort, JwtUtils jwtUtils, @Lazy AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RefreshTokenOutputPort refreshTokenRepository) {
        this.userOutputPort = userOutputPort;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    @Transactional
    public AuthenticationResponseDTO login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        if (authentication.isAuthenticated()) {
            String accessToken = jwtUtils.generateAccessToken(email);
            User user = userOutputPort.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            return getAuthenticationResponseDTO(accessToken, user);
        } else {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed for user: " + email);
        }
    }

    public AuthenticationResponseDTO handleOAuthLogin(String email) {
        User user = userOutputPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtUtils.generateAccessToken(email);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plusMillis(jwtUtils.getRefreshTokenExpirationMs()));
        refreshTokenRepository.save(refreshToken);

        return new AuthenticationResponseDTO(accessToken, refreshToken.getId());
    }

    private AuthenticationResponseDTO getAuthenticationResponseDTO(String accessToken, User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plusMillis(jwtUtils.getRefreshTokenExpirationMs()));
        refreshTokenRepository.save(refreshToken);

        return new AuthenticationResponseDTO(accessToken, refreshToken.getId());
    }

    @Override
    public User register(User user) { //todo: exceptions customization?
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (userOutputPort.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        if (userOutputPort.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User with this username already exists");
        }
        //another validation can be added here - depending on gui validations
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userOutputPort.save(user);
    }

    @Override
    public AuthenticationResponseDTO refreshToken(UUID refreshTokenId) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByIdAndExpiresAtAfter(refreshTokenId, Instant.now())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired refresh token"));

        String newAccessToken = jwtUtils.generateAccessToken(refreshToken.getUser().getEmail());
        return new AuthenticationResponseDTO(newAccessToken, refreshToken.getId());
    }

    @Override
    public void revokeRefreshToken(UUID refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userOutputPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
