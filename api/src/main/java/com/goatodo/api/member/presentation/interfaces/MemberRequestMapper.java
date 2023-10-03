package com.goatodo.api.member.presentation.interfaces;

import com.goatodo.api.member.presentation.dto.MemberAccountRequest;
import com.goatodo.api.member.presentation.dto.MemberCreateRequest;
import com.goatodo.api.member.presentation.dto.MemberUpdateRequest;
import com.goatodo.api.member.presentation.dto.SlackInfoRequest;
import com.goatodo.application.member.dto.request.MemberServiceAccountRequest;
import com.goatodo.application.member.dto.request.MemberServiceCreateRequest;
import com.goatodo.application.member.dto.request.MemberServiceSlackInfoRequest;
import com.goatodo.application.member.dto.request.MemberServiceUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class MemberRequestMapper {

    private MemberRequestMapper() {

    }

    public MemberServiceSlackInfoRequest toService(SlackInfoRequest request) {
        return MemberServiceSlackInfoRequest.builder()
                .slackEmail(request.slackEmail())
                .startAlarmTime(request.startAlarmTime())
                .endAlarmTime(request.endAlarmTime())
                .build();
    }

    public MemberServiceUpdateRequest toService(MemberUpdateRequest request) {
        return MemberServiceUpdateRequest.builder()
                .nickname(request.nickname())
                .occupation(request.occupation())
                .build();
    }

    public MemberServiceCreateRequest toService(MemberCreateRequest request) {
        return MemberServiceCreateRequest.builder()
                .account(toService(request.account()))
                .password(request.password())
                .occupation(request.occupation())
                .role(request.role())
                .build();
    }

    public MemberServiceAccountRequest toService(MemberAccountRequest request) {
        return MemberServiceAccountRequest.builder()
                .email(request.email())
                .password(request.password())
                .build();
    }
}
