package com.goatodo.application.user;

import com.goatodo.application.user.dto.UserResponse;
import com.goatodo.application.user.dto.UsersResponse;
import com.goatodo.application.user.dto.request.UserServiceAccountRequest;
import com.goatodo.application.user.dto.request.UserServiceCreateRequest;
import com.goatodo.application.user.dto.request.UserServiceSlackInfoRequest;
import com.goatodo.application.user.dto.request.UserServiceUpdateRequest;
import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.NotExistIdRequestException;
import com.goatodo.domain.user.Account;
import com.goatodo.domain.user.User;
import com.goatodo.domain.user.exception.InvalidEmailOrPasswordException;
import com.goatodo.domain.user.repository.UserRepository;
import com.goatodo.domain.user.slackinfo.SlackInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Transactional
    public Long save(UserServiceCreateRequest request) {
        User user = request.toEntity();
        userValidator.validateDuplicatedMember(user);

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public Long login(UserServiceAccountRequest request) {
        User user = userRepository.findByAccount_Email(request.email())
                .orElseThrow(() -> new InvalidEmailOrPasswordException(ErrorCode.MEMBER_LOGIN_FAILED));

        Account account = request.toVO();
        user.validMatchPassword(account);

        return user.getId();
    }

    public UsersResponse findAll() {
        List<User> findUsers = userRepository.findAll();

        List<UserResponse> userResponseList = findUsers.stream()
                .map(UserResponse::new)
                .toList();

        return new UsersResponse(userResponseList);
    }

    public UserResponse findOne(Long id) {
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        return new UserResponse(findUser);
    }

    @Transactional
    public void updatePassword(Long memberId, UserServiceAccountRequest request) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        user.changePassword(request.email(), request.password());
    }

    @Transactional
    public void updateProfile(Long id, UserServiceUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        userValidator.validateDuplicatedNickname(request.nickname());
        user.changeProfile(request.nickname(), request.occupationType());
    }

    @Transactional
    public void updateSlackInfo(Long id, UserServiceSlackInfoRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        SlackInfo slackInfo = request.toVO();
        user.interLockSlack(slackInfo);
    }

    @Transactional
    public void deleteMember(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        userRepository.delete(user);
    }
}
