package com.goatodo.domain.member;

import com.goatodo.common.util.RegexPattern;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class SlackInfo {

    @Pattern(regexp = RegexPattern.EMAIL_REGEX,
            message = "적절한 이메일 양식대로 입력해주세요. ex) goatodo@example.com")
    @Column(name = "slack_email")
    private String slackEmail;

    @Column(name = "start_alarm_time")
    private LocalTime startAlarmTime;

    @Column(name = "end_alarm_time")
    private LocalTime endAlarmTime;

    @Builder
    public SlackInfo(String slackEmail, LocalTime startAlarmTime, LocalTime endAlarmTime) {
        this.slackEmail = slackEmail;
        this.startAlarmTime = startAlarmTime;
        this.endAlarmTime = endAlarmTime;
    }
}