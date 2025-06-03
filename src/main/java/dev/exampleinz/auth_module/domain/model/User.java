package dev.exampleinz.auth_module.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private LocalDate dateOfBirth;
    private int experience;
    private int money;
    private boolean sendBudgetReport;
    private boolean isProfilePublic;
    private boolean isEmailVerified;

    public User() {
    }

    public User(String name, String lastName, String email, String password, String username,
                LocalDate dateOfBirth, boolean sendBudgetReport, boolean isProfilePublic) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.experience = 0;
        this.money = 0;
        this.sendBudgetReport = sendBudgetReport;
        this.isProfilePublic = isProfilePublic;
        this.isEmailVerified = false;
    }

    public User(UUID id, String name, String lastName, String email, String password, String username, LocalDate dateOfBirth, int experience, int money, boolean sendBudgetReport, boolean isProfilePublic, boolean isEmailVerified) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.experience = experience;
        this.money = money;
        this.sendBudgetReport = sendBudgetReport;
        this.isProfilePublic = isProfilePublic;
        this.isEmailVerified = isEmailVerified;
    }

    public UUID getId() {
        return id;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setSendBudgetReport(boolean sendBudgetReport) {
        this.sendBudgetReport = sendBudgetReport;
    }

    public void setProfilePublic(boolean profilePublic) {
        isProfilePublic = profilePublic;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }
}
