package ru.skypro.homework.dto;

public class UserDetailsDTO {
    private final String username;
    private final String password;

    private final Integer userId;
    private final Role role;

    public UserDetailsDTO(String username, String password, Integer userId, Role role) {
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

}
