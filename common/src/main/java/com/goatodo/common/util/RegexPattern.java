package com.goatodo.common.util;

public final class RegexPattern {

    public final static String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*()_+,.:;<=>?\\[\\]^{|}~]{8,20}$";
    public final static String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public final static String DOMAIN_NAME_REGEX = "^(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(\\.\\w{2,})?$";
}