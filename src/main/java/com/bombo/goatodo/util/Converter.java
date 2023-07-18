package com.bombo.goatodo.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Converter {

    private Converter() {

    }

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
