package com.goatodo.application.slack.dto;

public record SlackDailyResponse(
        String channel,
        String text
) {
}
