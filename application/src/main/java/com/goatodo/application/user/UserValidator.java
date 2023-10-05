package com.goatodo.application.user;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.DuplicateException;
import com.goatodo.domain.user.User;
import com.goatodo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;

    public void validateDuplicatedMember(User user) {
        validateDuplicatedEmail(user.getAccount().getEmail());
        validateDuplicatedNickname(user.getNickname());
    }

    public void validateDuplicatedEmail(String email) {
        userRepository.findByAccount_Email(email)
                .ifPresent(member -> {
                    throw new DuplicateException(ErrorCode.MEMBER_DUPLICATE_EMAIL);
                });
    }

    public void validateDuplicatedNickname(String nickname) {
        userRepository.findByNickname(nickname)
                .ifPresent(member -> {
                    throw new DuplicateException(ErrorCode.MEMBER_DUPLICATE_NICKNAME);
                });
    }
}
