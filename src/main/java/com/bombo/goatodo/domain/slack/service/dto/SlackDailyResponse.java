package com.bombo.goatodo.domain.slack.service.dto;

public record SlackDailyResponse(
        String channel,
        String text
) {
}
