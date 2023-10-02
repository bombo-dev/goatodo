package com.goatodo.domain.slack.response;

public record SlackDailyResponse(
        String channel,
        String text
) {
}
