package com.goatodo.domain.user;

public enum Role {
    ADMIN,
    NORMAL;

    public boolean isAdmin() {
        return this == Role.ADMIN;
    }

    public boolean isNormal() {
        return this == Role.NORMAL;
    }
}
