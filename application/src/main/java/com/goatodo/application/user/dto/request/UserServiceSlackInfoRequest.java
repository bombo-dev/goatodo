package com.goatodo.application.user.dto.request;

import com.goatodo.domain.user.SlackInfo;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record UserServiceSlackInfoRequest(
        String slackEmail,
        LocalTime startAlarmTime,
        LocalTime endAlarmTime
) {

    public SlackInfo toVO() {
        return SlackInfo.builder()
                .slackEmail(slackEmail)
                .startAlarmTime(startAlarmTime)
                .endAlarmTime(endAlarmTime)
                .build();
    }
}
