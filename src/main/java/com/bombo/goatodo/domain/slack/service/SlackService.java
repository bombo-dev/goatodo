package com.bombo.goatodo.domain.slack.service;

import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.repository.MemberRepository;
import com.bombo.goatodo.domain.slack.service.dto.SlackDailyResponse;
import com.bombo.goatodo.global.error.ErrorCode;
import com.bombo.goatodo.global.exception.NotExistIdRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SlackService {

    private final String DAILY_MESSAGE = "todo 리스트를 작성 할 시간입니다.";
    private final String SLACK_POST_MESSAGE_URL = "https://slack.com/api/chat.postMessage";
    private final String SLACK_FIND_ID_BY_EMAIL = "https://slack.com/api/users.lookupByEmail";

    @Value("${slackBotToken}")
    private String slackToken;

    private final MemberRepository memberRepository;

    public SlackService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void sendStartMessageToUser(Long id) throws JsonProcessingException {

        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        String url = SLACK_POST_MESSAGE_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + slackToken);
        headers.add("Content-type", "application/json; charset=utf-8");
        String authenticationSlackId = getSlackIdByEmail(findMember.getSlackInfo().getSlackEmail());
        SlackDailyResponse slackDailyResponse = new SlackDailyResponse(authenticationSlackId, DAILY_MESSAGE);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(slackDailyResponse);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, String.class);

        HttpStatus httpStatus = (HttpStatus) responseEntity.getStatusCode();
        int status = httpStatus.value();
        String response = responseEntity.getBody();
        System.out.println("Response status: " + status);
        log.info("response : {}", response);
    }

    private String getSlackIdByEmail(String slackEmail) {
        String url = SLACK_FIND_ID_BY_EMAIL;
        url += "?email=" + slackEmail;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + slackToken);
        headers.add("Content-type", "application/x-www-form-urlencoded");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        JSONObject jsonObject = new JSONObject(responseEntity.getBody());
        JSONObject userProfile = jsonObject.getJSONObject("user");
        log.info("JsonObject : {}", userProfile);
        return (String) userProfile.get("id");
    }
}
