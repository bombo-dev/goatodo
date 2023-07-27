package com.bombo.goatodo.domain.slack.controller;

import com.bombo.goatodo.domain.slack.service.SlackService;
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

    @PostMapping("/slack/{id}/daily/start")
    public ResponseEntity<Void> sendDailyStartMessage(@PathVariable Long id) {
        slackService.sendStartMessageToUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/slack/{id}/daily/end")
    public ResponseEntity<Void> sendDailyEndMessage(@PathVariable Long id) {
        slackService.sendEndDailyMessageToUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
