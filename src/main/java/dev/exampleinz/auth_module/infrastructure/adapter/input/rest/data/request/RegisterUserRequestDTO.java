package dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.request;

import java.time.LocalDate;

public record RegisterUserRequestDTO(String name, String lastName, String email, String password, String username,
                                     LocalDate dateOfBirth, boolean sendBudgetReport, boolean isProfilePublic) {

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean isSendBudgetReport() {
        return sendBudgetReport;
    }

    public boolean isProfilePublic() {
        return isProfilePublic;
    }
}
