package com.goatodo.common.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class SlackInfo {

    @Column(name = "slack_email")
    private String slackEmail;

    @Column(name = "start_alarm_time")
    private LocalTime startAlarmTime;

    @Column(name = "end_alarm_time")
    private LocalTime endAlarmTime;

    public SlackInfo(String slackEmail, LocalTime startAlarmTime, LocalTime endAlarmTime) {
        this.slackEmail = slackEmail;
        this.startAlarmTime = startAlarmTime;
        this.endAlarmTime = endAlarmTime;
    }
}
