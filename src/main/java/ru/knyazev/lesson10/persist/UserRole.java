package ru.knyazev.lesson10.persist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole {

    private final String username;

    private final String roleName;

    public UserRole(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }
}
