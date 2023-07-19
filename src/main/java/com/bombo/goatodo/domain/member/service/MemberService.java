package com.bombo.goatodo.domain.member.service;

import com.bombo.goatodo.domain.member.controller.dto.MemberAccountRequest;
import com.bombo.goatodo.domain.member.controller.dto.MemberCreateRequest;
import com.bombo.goatodo.domain.member.controller.dto.MemberUpdateRequest;
import com.bombo.goatodo.domain.member.service.dto.MemberResponse;
import com.bombo.goatodo.domain.member.service.dto.MembersResponse;

public interface MemberService {

    MemberResponse save(MemberCreateRequest memberCreateRequest);

    MemberResponse findOne(Long id);

    MembersResponse findAll();

    void updatePassword(MemberAccountRequest memberAccountRequest);

    void updateProfile(MemberUpdateRequest memberUpdateRequest);
}
