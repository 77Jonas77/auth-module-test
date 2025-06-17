package dev.exampleinz.auth_module.application.service;

import dev.exampleinz.auth_module.application.port.input.*;
import dev.exampleinz.auth_module.application.port.output.RefreshTokenOutputPort;
import dev.exampleinz.auth_module.application.port.output.UserOutputPort;
import dev.exampleinz.auth_module.domain.model.RefreshToken;
import dev.exampleinz.auth_module.domain.model.User;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response.AuthenticationResponseDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.shared.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.naming.AuthenticationException;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class UserAuthService implements RegisterUserInputPort, LoginUserInputPort, UserDetailsService, LogoutUserInputPort, RefreshTokenInputPort, HandleOAuthCodeExchange {

    private final UserOutputPort userOutputPort;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenOutputPort refreshTokenRepository;
    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleClientRedirectUri;

    public UserAuthService(UserOutputPort userOutputPort, JwtUtils jwtUtils, @Lazy AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RefreshTokenOutputPort refreshTokenRepository, WebClient.Builder webClient) {
        this.userOutputPort = userOutputPort;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.webClient = webClient.build();
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
            User user = userOutputPort.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
            String accessToken = jwtUtils.generateAccessToken(email);
            return getAuthenticationResponseDTO(accessToken, user);
        } else {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed for user: " + email);
        }
    }

    private AuthenticationResponseDTO getAuthenticationResponseDTO(String accessToken, User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plusMillis(jwtUtils.getRefreshTokenExpirationMs()));
        RefreshToken createdRefreshToken  = refreshTokenRepository.save(refreshToken);

        return new AuthenticationResponseDTO(accessToken, createdRefreshToken.getId());
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
        user.setFullDataProvided(true);

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

    public AuthenticationResponseDTO handleOAuthCodeExchange(String code, String codeVerifier) throws AuthenticationException {
        Map<String, String> tokenResponse = exchangeCodeForToken(code, codeVerifier);
        String accessTokenFromOAuth = tokenResponse.get("access_token");

        Map<String, Object> userInfo = fetchUserInfo(accessTokenFromOAuth);

        String email = (String) userInfo.get("email");
        User user = userOutputPort.findByEmail(email)
                .orElseGet(() -> registerNewUser(userInfo));

        if(user==null) {
            throw new AuthenticationException("Something went wrong. Sorry!");
        }
        String accessToken = jwtUtils.generateAccessToken(email);
        return getAuthenticationResponseDTO(accessToken, user);
    }

    private User registerNewUser(Map<String, Object> userInfo) {
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("given_name");
        String lastName = (String) userInfo.get("family_name");
        String username = name + lastName;

        while (userOutputPort.existsByUsername(username)) {
            username += new Random().nextInt(1000);
        }
        String finalUsername = username;

        boolean isEmailVerified = (boolean) userInfo.getOrDefault("email_verified", false);

        return userOutputPort.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setLastName(lastName);
            newUser.setUsername(finalUsername);
            newUser.setExperience(0);
            newUser.setMoney(0);
            newUser.setSendBudgetReport(false);
            newUser.setProfilePublic(false);
            newUser.setEmailVerified(isEmailVerified);
            newUser.setFullDataProvided(false);
            return userOutputPort.save(newUser);
        });
    }

    private Map<String, Object> fetchUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://openidconnect.googleapis.com/v1/userinfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    private Map<String, String> exchangeCodeForToken(String code, String codeVerifier){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type","authorization_code");
        formData.add("code", code);
        formData.add("redirect_uri", googleClientRedirectUri);
        formData.add("client_id", googleClientId);
        formData.add("code_verifier", codeVerifier);
        formData.add("client_secret", googleClientSecret);

        return webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }
}
