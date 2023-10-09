package com.goatodo.application.user;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.NotExistIdRequestException;
import com.goatodo.domain.user.Level;
import com.goatodo.domain.user.User;
import com.goatodo.domain.user.repository.LevelRepository;
import com.goatodo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LevelService {

    private final LevelRepository levelRepository;
    private final UserRepository userRepository;

    @Transactional
    public void changeLevel(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        if (user.getIsLevelUp()) {
            Level level = levelRepository.findNextLevelById(user.getLevel().getId())
                    .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

            user.changeLevel(level);
            return;
        }

        if (user.getIsLevelDown()) {
            Level level = levelRepository.findPreLevelById(user.getLevel().getId())
                    .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

            user.changeLevel(level);
        }
    }
}
