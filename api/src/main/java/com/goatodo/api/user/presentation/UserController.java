package com.goatodo.api.user.presentation;

import com.goatodo.api.user.presentation.dto.SlackInfoRequest;
import com.goatodo.api.user.presentation.dto.UserAccountRequest;
import com.goatodo.api.user.presentation.dto.UserCreateRequest;
import com.goatodo.api.user.presentation.dto.UserUpdateRequest;
import com.goatodo.api.user.presentation.interfaces.UserRequestMapper;
import com.goatodo.application.user.UserService;
import com.goatodo.application.user.dto.UserResponse;
import com.goatodo.application.user.dto.UsersResponse;
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
public class UserController {

    private final String SESSION_LOGIN_ID = "loginId";
    private final UserRequestMapper userRequestMapper;
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<Void> signUp(@Validated @RequestBody UserCreateRequest request) {

        userService.save(userRequestMapper.toService(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/users/login")
    public ResponseEntity<Void> login(@Validated @RequestBody UserAccountRequest request,
                                      HttpServletRequest httpServletRequest) {
        Long loginId = userService.login(userRequestMapper.toService(request));

        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute(SESSION_LOGIN_ID, loginId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/users/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {

        HttpSession httpSession = request.getSession(false);

        if (httpSession != null) {
            httpSession.removeAttribute(SESSION_LOGIN_ID);
            httpSession.invalidate();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> findOne(@PathVariable Long id) {
        UserResponse findUserResponse = userService.findOne(id);

        return ResponseEntity.status(HttpStatus.OK).body(findUserResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<UsersResponse> findAll() {
        UsersResponse findUsersResponse = userService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(findUsersResponse);
    }

    @PatchMapping("/users/{id}/profile")
    public ResponseEntity<Void> modifyProfile(@PathVariable Long id,
                                              @Validated @RequestBody UserUpdateRequest request) {
        userService.updateProfile(id, userRequestMapper.toService(request));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/users/{id}/slack")
    public ResponseEntity<Void> interLockSlack(@PathVariable Long id,
                                               @Validated @RequestBody SlackInfoRequest request) {
        userService.updateSlackInfo(id, userRequestMapper.toService(request));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("users/{id}/password")
    public ResponseEntity<Void> modifyPassword(@PathVariable Long id,
                                               @Validated @RequestBody UserAccountRequest request) {

        userService.updatePassword(id, userRequestMapper.toService(request));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
