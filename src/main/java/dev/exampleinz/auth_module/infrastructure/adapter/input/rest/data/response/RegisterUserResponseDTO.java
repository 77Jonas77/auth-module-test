package dev.exampleinz.auth_module.infrastructure.adapter.input.rest.data.response;

import java.time.LocalDate;

public record RegisterUserResponseDTO(String name, String lastName, String email, String username,
                                     LocalDate dateOfBirth, int experience, int money, boolean sendBudgetReport, boolean isProfilePublic, boolean isEmailVerified) {

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getExperience() {
        return experience;
    }

    public int getMoney() {
        return money;
    }

    public boolean isSendBudgetReport() {
        return sendBudgetReport;
    }

    public boolean isProfilePublic() {
        return isProfilePublic;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

}
