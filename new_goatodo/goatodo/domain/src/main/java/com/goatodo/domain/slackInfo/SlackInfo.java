package com.goatodo.domain.slackInfo;

import com.goatodo.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "slack_infos")
@Entity
public class SlackInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slack_email", nullable = false, unique = true)
    private String slackEmail;

    @Column(name = "start_alarm_time", nullable = false)
    private LocalTime startAlarmTime;

    @Column(name = "end_alarm_time", nullable = false)
    private LocalTime endAlarmTime;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Builder
    public SlackInfo(String slackEmail, LocalTime startAlarmTime, LocalTime endAlarmTime) {
        this.slackEmail = slackEmail;
        this.startAlarmTime = startAlarmTime;
        this.endAlarmTime = endAlarmTime;
    }
}
