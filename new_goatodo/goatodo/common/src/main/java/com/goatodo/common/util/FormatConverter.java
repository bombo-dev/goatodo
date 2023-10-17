package com.goatodo.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FormatConverter {

    private FormatConverter() {

    }

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
