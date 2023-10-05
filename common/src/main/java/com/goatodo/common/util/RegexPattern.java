package com.goatodo.common.util;

import java.util.regex.Pattern;

public final class RegexPattern {

    private RegexPattern() {
    }

    public final static String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*()_+,.:;<=>?\\[\\]^{|}~]{8,20}$";
    public final static String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public final static String DOMAIN_NAME_REGEX = "^(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(\\.\\w{2,})?$";

    public final static Pattern PASSWORD_MATCHER = Pattern.compile(PASSWORD_REGEX);
    public final static Pattern EMAIL_MATCHER = Pattern.compile(PASSWORD_REGEX);
}
