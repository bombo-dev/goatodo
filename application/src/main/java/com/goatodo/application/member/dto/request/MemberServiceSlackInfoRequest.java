package com.goatodo.application.member.dto.request;

import com.goatodo.domain.member.SlackInfo;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record MemberServiceSlackInfoRequest(
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
