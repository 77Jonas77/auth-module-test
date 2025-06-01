package dev.exampleinz.auth_module.application.port.input;

import dev.exampleinz.auth_module.domain.model.User;

public interface RegisterUserInputPort {
    User register(User user);
}
