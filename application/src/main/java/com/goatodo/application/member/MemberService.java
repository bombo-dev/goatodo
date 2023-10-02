package com.goatodo.application.member;

import com.bombo.goatodo.domain.member.Account;
import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.SlackInfo;
import com.bombo.goatodo.domain.member.controller.dto.MemberAccountRequest;
import com.bombo.goatodo.domain.member.controller.dto.MemberCreateRequest;
import com.bombo.goatodo.domain.member.controller.dto.MemberUpdateRequest;
import com.bombo.goatodo.domain.member.controller.dto.SlackInfoRequest;
import com.bombo.goatodo.domain.member.exception.InvalidEmailOrPasswordException;
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
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(MemberCreateRequest memberCreateRequest) {
        Member requestMember = memberCreateRequest.toEntity();
        validateDuplicatedMember(requestMember);

        Member savedMember = memberRepository.save(requestMember);
        return savedMember.getId();
    }

    public Long login(MemberAccountRequest memberAccountRequest) {
        Member findMember = memberRepository.findByAccount_Email(memberAccountRequest.email())
                .orElseThrow(() -> new InvalidEmailOrPasswordException(ErrorCode.MEMBER_LOGIN_FAILED));

        Account inputAccount = memberAccountRequest.toAccount();
        if (!findMember.isSameMember(inputAccount)) {
            throw new InvalidEmailOrPasswordException(ErrorCode.MEMBER_LOGIN_FAILED);
        }

        return findMember.getId();
    }

    public MembersResponse findAll() {
        List<Member> findMembers = memberRepository.findAll();

        List<MemberResponse> memberResponseList = findMembers.stream()
                .map(MemberResponse::new)
                .toList();

        return new MembersResponse(memberResponseList);
    }

    public MemberResponse findOne(Long id) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        return new MemberResponse(findMember);
    }

    @Transactional
    public void updatePassword(Long memberId, MemberAccountRequest memberAccountRequest) {
        Member findMember = memberRepository.findById(memberId)
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
    public void updateProfile(Long id, MemberUpdateRequest memberUpdateRequest) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        validateDuplicatedNickname(memberUpdateRequest.nickname());
        findMember.changeProfile(memberUpdateRequest.nickname(), memberUpdateRequest.occupation());
    }

    @Transactional
    public void updateSlackInfo(Long id, SlackInfoRequest slackInfoRequest) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        SlackInfo slackInfo = slackInfoRequest.toSlackInfo();
        findMember.interLockSlack(slackInfo);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        memberRepository.delete(findMember);
    }

    private void validateDuplicatedMember(Member requestMember) {
        validateDuplicatedEmail(requestMember.getAccount().getEmail());
        validateDuplicatedNickname(requestMember.getNickname());
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
