package com.goatodo.api.member.presentation.dto;

import com.bombo.goatodo.domain.member.SlackInfo;

import java.time.LocalTime;

public record SlackInfoRequest(
        String slackEmail,
        LocalTime startAlarmTime,
        LocalTime endAlarmTime
) {

    public SlackInfo toSlackInfo() {
        return new SlackInfo(slackEmail, startAlarmTime, endAlarmTime);
    }
}
