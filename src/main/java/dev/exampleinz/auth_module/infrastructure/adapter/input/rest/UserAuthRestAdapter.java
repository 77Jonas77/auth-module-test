package dev.exampleinz.auth_module.infrastructure.adapter.input.rest;

import dev.exampleinz.auth_module.application.service.UserAuthService;
import dev.exampleinz.auth_module.domain.model.User;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.request.AuthenticationRequestDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.request.LogutRequestDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.request.RefreshTokenDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.request.RegisterUserRequestDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response.AuthenticationResponseDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response.RegisterUserResponseDTO;
import dev.exampleinz.auth_module.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserAuthRestAdapter {

    private UserAuthService userAuthService;
    private UserRestMapper userRestMapper;

    public UserAuthRestAdapter(UserAuthService userAuthService, UserRestMapper userRestMapper) {
        this.userAuthService = userAuthService;
        this.userRestMapper = userRestMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDTO> register(@RequestBody RegisterUserRequestDTO registerUserRequestDTO){
        User user = userRestMapper.RegisterToDomain(registerUserRequestDTO);
        user = userAuthService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRestMapper.toRegisterResponse(user));
    }

    //TODO: Expired token validation and error handling
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        try {
            AuthenticationResponseDTO responseDTO = userAuthService.login(authenticationRequestDTO.getEmail(), authenticationRequestDTO.getPassword());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        try {
            AuthenticationResponseDTO responseDTO = userAuthService.refreshToken(refreshTokenDTO.getRefreshTokenId());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogutRequestDTO logutRequestDTO) {
        try {
            userAuthService.revokeRefreshToken(logutRequestDTO.getRefreshTokenId());
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
