package com.goatodo.application.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goatodo.domain.member.Member;
import com.goatodo.domain.member.repository.MemberRepository;
import com.goatodo.domain.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class SlackService {

    private final String DAILY_MESSAGE = "todo 리스트를 작성 할 시간입니다.";
    private final String SLACK_POST_MESSAGE_URL = "https://slack.com/api/chat.postMessage";
    private final String SLACK_FIND_ID_BY_EMAIL = "https://slack.com/api/users.lookupByEmail";

    @Value("${slackBotToken}")
    private String slackToken;

    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;

    public SlackService(MemberRepository memberRepository, TodoRepository todoRepository) {
        this.memberRepository = memberRepository;
        this.todoRepository = todoRepository;
    }

    public void sendStartMessageToUser(Long memberId) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        String url = SLACK_POST_MESSAGE_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + slackToken);
        headers.add("Content-type", "application/json; charset=utf-8");
        String authenticationSlackId = getSlackIdByEmail(findMember.getSlackInfo().getSlackEmail());
        String lifeQuotes = LifeQuotesStorage.randomOf();
        String responseMsg = createResponseMsg(lifeQuotes);
        SlackDailyResponse slackDailyResponse = new SlackDailyResponse(authenticationSlackId, responseMsg);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestBody = objectMapper.writeValueAsString(slackDailyResponse);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url, HttpMethod.POST, requestEntity, String.class);
            log.info("body : {} ", responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String createResponseMsg(String lifeQuotes) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(DAILY_MESSAGE).append("\n");
        stringBuffer.append("오늘의 명언").append("\n");
        stringBuffer.append(lifeQuotes);
        return stringBuffer.toString();
    }

    public void sendEndDailyMessageToUser(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        String url = SLACK_POST_MESSAGE_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + slackToken);
        headers.add("Content-type", "application/json; charset=utf-8");

        String authenticationSlackId = getSlackIdByEmail(findMember.getSlackInfo().getSlackEmail());
        double dailyTodoAchievementRate = getDailyTodoAchievementRate(memberId);
        String dailyMessage = createDailyEndMessage(dailyTodoAchievementRate);
        SlackDailyResponse slackDailyResponse = new SlackDailyResponse(authenticationSlackId, dailyMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestBody = objectMapper.writeValueAsString(slackDailyResponse);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url, HttpMethod.POST, requestEntity, String.class);

            log.info("body : {} ", responseEntity.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String createDailyEndMessage(double dailyTodoAchievementRate) {
        return "금일 투두리스트의 성취율은 : " + dailyTodoAchievementRate + "% 입니다.";
    }

    private double getDailyTodoAchievementRate(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endTime = now.withHour(23).withMinute(59).withSecond(59);

        List<Todo> dailyTodoList = todoRepository.findAllByMember_idAndDateBetween(memberId, startTime, endTime);

        double achievementRate = dailyTodoList.stream()
                .map(Todo::getCompleteStatus)
                .mapToDouble(CompleteStatus::getScore)
                .average()
                .orElse(0);

        return Math.round(achievementRate * 100) / 100.0;
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
