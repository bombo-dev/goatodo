package com.goatodo.common.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class FormatConverterTest {

    @Test
    @DisplayName("LocalDateTime을 yyyy-MM-dd HH:mm:ss 형태로 변환할 수 있다.")
    void convertLocalDateTimeToString() {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2023, 7, 22, 13, 30, 22);

        // when
        String convertedLocalDateTime = FormatConverter.convertLocalDateTimeToString(localDateTime);

        // then
        Assertions.assertThat(convertedLocalDateTime).isEqualTo("2023-07-22 13:30:22");
    }

}