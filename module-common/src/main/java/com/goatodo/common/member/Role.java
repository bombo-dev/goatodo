package com.goatodo.common.member;

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
