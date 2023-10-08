package com.goatodo.domain.user;

import com.goatodo.domain.todo.Difficulty;
import com.goatodo.domain.todo.Todo;
import com.goatodo.domain.todo.TodoFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class UserTest {
    
    @DisplayName("todo를 완료하면 경험치를 획득하고 레벨업 경험치 충족 시 레벨업을 한다.")
    @ParameterizedTest
    @MethodSource("difficultyAndExpAndLevelUp")
    void requiredTodoExpLevelUp(Difficulty difficulty, int exp, boolean isLevelUp) {
        //given
        Todo todo = TodoFactory.todo(difficulty);
        User user = UserFactory.user();

        //when
        user.requireExp(todo);

        //then
        Assertions.assertThat(user.getExperience()).isEqualTo(exp);
        Assertions.assertThat(user.getIsLevelUp()).isEqualTo(isLevelUp);
    }

    static Stream<Arguments> difficultyAndExpAndLevelUp() {
        return Stream.of(
                arguments(Difficulty.VERY_EASY, 5, false),
                arguments(Difficulty.EASY, 7, false),
                arguments(Difficulty.NORMAL, 0, true),
                arguments(Difficulty.HARD, 3, true),
                arguments(Difficulty.VERY_HARD, 10, true)
        );
    }
}