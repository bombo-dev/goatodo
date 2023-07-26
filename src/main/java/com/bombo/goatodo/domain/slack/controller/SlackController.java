package com.bombo.goatodo.domain.slack.controller;

import com.bombo.goatodo.domain.slack.service.SlackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SlackController {

    private final SlackService slackService;

    @PostMapping("/slack/{id}")
    public ResponseEntity<Void> sendDailyMessage(@PathVariable Long id) {
        try {
            slackService.sendStartMessageToUser(id);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
