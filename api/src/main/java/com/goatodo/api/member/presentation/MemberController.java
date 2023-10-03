package com.goatodo.api.member.presentation;

import com.goatodo.api.member.presentation.dto.MemberAccountRequest;
import com.goatodo.api.member.presentation.dto.MemberCreateRequest;
import com.goatodo.api.member.presentation.dto.MemberUpdateRequest;
import com.goatodo.api.member.presentation.dto.SlackInfoRequest;
import com.goatodo.api.member.presentation.interfaces.MemberRequestMapper;
import com.goatodo.application.member.MemberService;
import com.goatodo.application.member.dto.MemberResponse;
import com.goatodo.application.member.dto.MembersResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class MemberController {

    private final String SESSION_LOGIN_ID = "loginId";
    private final MemberRequestMapper memberRequestMapper;
    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Void> signUp(@Validated @RequestBody MemberCreateRequest memberCreateRequest) {

        memberService.save(memberCreateRequest.toCommand());

//        memberService.save(memberCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/members/login")
    public ResponseEntity<Void> login(@Validated @RequestBody MemberAccountRequest memberAccountRequest,
                                      HttpServletRequest httpServletRequest) {
        Long loginId = memberService.login(memberRequestMapper.toService(memberAccountRequest));

        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute(SESSION_LOGIN_ID, loginId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/members/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {

        HttpSession httpSession = httpServletRequest.getSession(false);

        if (httpSession != null) {
            httpSession.removeAttribute(SESSION_LOGIN_ID);
            httpSession.invalidate();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> findOne(@PathVariable Long id) {
        MemberResponse findMemberResponse = memberService.findOne(id);

        return ResponseEntity.status(HttpStatus.OK).body(findMemberResponse);
    }

    @GetMapping("/members")
    public ResponseEntity<MembersResponse> findAll() {
        MembersResponse findMembersResponse = memberService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(findMembersResponse);
    }

    @PatchMapping("/members/{id}/profile")
    public ResponseEntity<Void> modifyProfile(@PathVariable Long id,
                                              @Validated @RequestBody MemberUpdateRequest memberUpdateRequest) {
        memberService.updateProfile(id, memberRequestMapper.toService(memberUpdateRequest));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/members/{id}/slack")
    public ResponseEntity<Void> interLockSlack(@PathVariable Long id,
                                               @Validated @RequestBody SlackInfoRequest slackInfoRequest) {
        memberService.updateSlackInfo(id, memberRequestMapper.toService(slackInfoRequest));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("member/{id}/password")
    public ResponseEntity<Void> modifyPassword(@PathVariable Long id,
                                               @Validated @RequestBody MemberAccountRequest memberAccountRequest) {

        memberService.updatePassword(id, memberRequestMapper.toService(memberAccountRequest));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/member/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
