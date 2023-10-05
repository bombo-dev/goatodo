package com.goatodo.api.user.presentation.interfaces;

import com.goatodo.api.user.presentation.dto.SlackInfoRequest;
import com.goatodo.api.user.presentation.dto.UserAccountRequest;
import com.goatodo.api.user.presentation.dto.UserCreateRequest;
import com.goatodo.api.user.presentation.dto.UserUpdateRequest;
import com.goatodo.application.user.dto.request.UserServiceAccountRequest;
import com.goatodo.application.user.dto.request.UserServiceCreateRequest;
import com.goatodo.application.user.dto.request.UserServiceSlackInfoRequest;
import com.goatodo.application.user.dto.request.UserServiceUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {

    private UserRequestMapper() {

    }

    public UserServiceSlackInfoRequest toService(SlackInfoRequest request) {
        return UserServiceSlackInfoRequest.builder()
                .slackEmail(request.slackEmail())
                .startAlarmTime(request.startAlarmTime())
                .endAlarmTime(request.endAlarmTime())
                .build();
    }

    public UserServiceUpdateRequest toService(UserUpdateRequest request) {
        return UserServiceUpdateRequest.builder()
                .nickname(request.nickname())
                .occupation(request.occupation())
                .build();
    }

    public UserServiceCreateRequest toService(UserCreateRequest request) {
        return UserServiceCreateRequest.builder()
                .account(toService(request.account()))
                .password(request.password())
                .occupation(request.occupation())
                .role(request.role())
                .build();
    }

    public UserServiceAccountRequest toService(UserAccountRequest request) {
        return UserServiceAccountRequest.builder()
                .email(request.email())
                .password(request.password())
                .build();
    }
}
