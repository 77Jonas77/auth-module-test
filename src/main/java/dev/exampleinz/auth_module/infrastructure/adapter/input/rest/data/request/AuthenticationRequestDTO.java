package dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.request;

public record AuthenticationRequestDTO(String email, String password) {

    public AuthenticationRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
