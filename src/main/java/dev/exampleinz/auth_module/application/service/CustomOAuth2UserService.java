package dev.exampleinz.auth_module.application.service;

import dev.exampleinz.auth_module.application.port.output.UserOutputPort;
import dev.exampleinz.auth_module.domain.model.User;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.entity.UserJpaEntity;
import dev.exampleinz.auth_module.infrastructure.adapter.output.persistence.mapper.UserPersistenceMapper;
import dev.exampleinz.auth_module.infrastructure.adapter.shared.JwtUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Random;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserOutputPort userOutputPort;

    public CustomOAuth2UserService(UserOutputPort userOutputPort) {
        this.userOutputPort = userOutputPort;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String,Object> attributes = oAuth2User.getAttributes();
        //mozna dodac metode od sprawdzania poprawnosci danych usera
        String email = (String) attributes.get("email");
        if(userOutputPort.existsByEmail(email)){
            throw new OAuth2AuthenticationException("Email already exists: " + email);
        }
        String name = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String username = name + lastName;

        while(userOutputPort.existsByUsername(username)){
            username += new Random().nextInt(1000);
        }
        String finalUsername = username;

        boolean isEmailVerified = (boolean) attributes.getOrDefault("email_verified", false);
        userOutputPort.findByEmail(email).orElseGet( () -> {
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
            return userOutputPort.save(newUser);
        });
        return oAuth2User;
    }

}
