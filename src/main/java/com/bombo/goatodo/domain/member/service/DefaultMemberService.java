package com.bombo.goatodo.domain.member.service;

import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.controller.dto.MemberAccountRequest;
import com.bombo.goatodo.domain.member.controller.dto.MemberCreateRequest;
import com.bombo.goatodo.domain.member.controller.dto.MemberUpdateRequest;
import com.bombo.goatodo.domain.member.repository.MemberRepository;
import com.bombo.goatodo.domain.member.service.dto.MemberResponse;
import com.bombo.goatodo.domain.member.service.dto.MembersResponse;
import com.bombo.goatodo.global.error.ErrorCode;
import com.bombo.goatodo.global.exception.DuplicateException;
import com.bombo.goatodo.global.exception.NotExistIdRequestException;
import com.bombo.goatodo.global.exception.RoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultMemberService implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public MemberResponse save(MemberCreateRequest memberCreateRequest) {
        Member requestMember = memberCreateRequest.toEntity();
        validateDuplicatedMember(requestMember);
        Member savedMember = memberRepository.save(requestMember);
        return new MemberResponse(savedMember);
    }

    @Override
    public MembersResponse findAll() {
        List<Member> findMembers = memberRepository.findAll();

        List<MemberResponse> memberResponseList = findMembers.stream()
                .map(MemberResponse::new)
                .toList();

        return new MembersResponse(memberResponseList);
    }

    @Override
    public MemberResponse findOne(Long id) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        return new MemberResponse(findMember);
    }

    @Transactional
    @Override
    public void updatePassword(MemberAccountRequest memberAccountRequest) {
        Member findMember = memberRepository.findById(memberAccountRequest.id())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        if (findMember.isSamePassword(memberAccountRequest.password())) {
            throw new DuplicateException(ErrorCode.MEMBER_DUPLICATE_PASSWORD);
        }

        if (!findMember.isSameEmail(memberAccountRequest.email())) {
            throw new RoleException(ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);
        }
        findMember.changePassword(memberAccountRequest.password());
    }

    @Transactional
    @Override
    public void updateProfile(MemberUpdateRequest memberUpdateRequest) {
        validateDuplicatedNickname(memberUpdateRequest.nickname());

        Member findMember = memberRepository.findById(memberUpdateRequest.id())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        findMember.changeProfile(memberUpdateRequest.nickname(), memberUpdateRequest.occupation());
    }

    @Transactional
    @Override
    public void deleteMember(Long id) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        memberRepository.delete(findMember);
    }

    private void validateDuplicatedMember(Member requestMember) {
        validateDuplicatedNickname(requestMember.getNickname());
        validateDuplicatedEmail(requestMember.getAccount().getEmail());
    }

    private void validateDuplicatedEmail(String email) {
        memberRepository.findByAccount_Email(email)
                .ifPresent(member -> {
                    throw new DuplicateException(ErrorCode.MEMBER_DUPLICATE_EMAIL);
                });
    }

    private void validateDuplicatedNickname(String nickname) {
        memberRepository.findByNickname(nickname)
                .ifPresent(member -> {
                    throw new DuplicateException(ErrorCode.MEMBER_DUPLICATE_NICKNAME);
                });
    }
}
