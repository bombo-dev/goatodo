package com.goatodo.api.member.presentation.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record SlackInfoRequest(

        @NotBlank
        String slackEmail,

        @NotNull
        LocalTime startAlarmTime,

        @NotNull
        LocalTime endAlarmTime
) {
}
