package com.goatodo.application.user;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.NotExistIdRequestException;
import com.goatodo.domain.todo.CompleteStatus;
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
    public void changeExperience(Long userId, int exp, CompleteStatus before, CompleteStatus after) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Level level = levelRepository.findById(user.getLevelId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        if (after.isComplete()) {
            user.requireExp(exp, level.getRequiredExperience());
        }

        if (before.isComplete()) {
            user.rollbackExp(exp, level.getPreExperience());
        }

        changeLevel(user);
    }

    private void changeLevel(User user) {

        if (user.isLevelUp()) {
            Level level = levelRepository.findNextLevelById(user.getLevelId())
                    .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

            user.changeLevel(level.getId());
            return;
        }

        if (user.isLevelDown()) {
            Level level = levelRepository.findPreLevelById(user.getLevelId())
                    .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

            user.changeLevel(level.getId());
        }
    }
}
