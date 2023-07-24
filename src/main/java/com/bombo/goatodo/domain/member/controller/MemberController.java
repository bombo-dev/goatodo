package com.bombo.goatodo.domain.member.controller;

import com.bombo.goatodo.domain.member.controller.dto.MemberAccountRequest;
import com.bombo.goatodo.domain.member.controller.dto.MemberCreateRequest;
import com.bombo.goatodo.domain.member.controller.dto.MemberUpdateRequest;
import com.bombo.goatodo.domain.member.service.MemberService;
import com.bombo.goatodo.domain.member.service.dto.MemberResponse;
import com.bombo.goatodo.domain.member.service.dto.MembersResponse;
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
    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<Void> signUp(@Validated @RequestBody MemberCreateRequest memberCreateRequest) {
        memberService.save(memberCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/member/login")
    public ResponseEntity<Void> login(@Validated @RequestBody MemberAccountRequest memberAccountRequest,
                                      HttpServletRequest httpServletRequest) {
        Long loginId = memberService.login(memberAccountRequest);

        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute(SESSION_LOGIN_ID, loginId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/member/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {

        HttpSession httpSession = httpServletRequest.getSession(false);

        if (httpSession != null) {
            httpSession.removeAttribute(SESSION_LOGIN_ID);
            httpSession.invalidate();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<MemberResponse> findOne(@PathVariable Long id) {
        MemberResponse findMemberResponse = memberService.findOne(id);

        return ResponseEntity.status(HttpStatus.OK).body(findMemberResponse);
    }

    @GetMapping("/members")
    public ResponseEntity<MembersResponse> findAll() {
        MembersResponse findMembersResponse = memberService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(findMembersResponse);
    }

    @PostMapping("/member/{id}/modify/profile")
    public ResponseEntity<Void> modifyProfile(@PathVariable Long id,
                                              @Validated @RequestBody MemberUpdateRequest memberUpdateRequest) {
        memberService.updateProfile(id, memberUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("member/{id}/modify/password")
    public ResponseEntity<Void> modifyPassword(@PathVariable Long id,
                                               @Validated @RequestBody MemberAccountRequest memberAccountRequest) {

        memberService.updatePassword(id, memberAccountRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/member/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
