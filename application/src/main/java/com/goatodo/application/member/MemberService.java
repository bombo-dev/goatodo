package com.goatodo.application.member;

import com.goatodo.application.member.dto.MemberResponse;
import com.goatodo.application.member.dto.MembersResponse;
import com.goatodo.application.member.dto.request.MemberServiceAccountRequest;
import com.goatodo.application.member.dto.request.MemberServiceCreateRequest;
import com.goatodo.application.member.dto.request.MemberServiceSlackInfoRequest;
import com.goatodo.application.member.dto.request.MemberServiceUpdateRequest;
import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.NotExistIdRequestException;
import com.goatodo.domain.member.Account;
import com.goatodo.domain.member.Member;
import com.goatodo.domain.member.SlackInfo;
import com.goatodo.domain.member.exception.InvalidEmailOrPasswordException;
import com.goatodo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    @Transactional
    public Long save(MemberServiceCreateRequest request) {
        Member member = request.toEntity();
        memberValidator.validateDuplicatedMember(member);

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    public Long login(MemberServiceAccountRequest request) {
        Member member = memberRepository.findByAccount_Email(request.email())
                .orElseThrow(() -> new InvalidEmailOrPasswordException(ErrorCode.MEMBER_LOGIN_FAILED));

        Account account = request.toVO();
        member.validLoginInfo(account);

        return member.getId();
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
    public void updatePassword(Long memberId, MemberServiceAccountRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        member.changePassword(request.email(), request.password());
    }

    @Transactional
    public void updateProfile(Long id, MemberServiceUpdateRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        memberValidator.validateDuplicatedNickname(request.nickname());
        member.changeProfile(request.nickname(), request.occupation());
    }

    @Transactional
    public void updateSlackInfo(Long id, MemberServiceSlackInfoRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        SlackInfo slackInfo = request.toVO();
        member.interLockSlack(slackInfo);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        memberRepository.delete(member);
    }
}
