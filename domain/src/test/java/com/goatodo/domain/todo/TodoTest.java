package com.goatodo.domain.todo;

import com.goatodo.domain.user.User;
import com.goatodo.domain.user.UserFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TodoTest {

    @Test
    @DisplayName("필수 필드를 입력하지 않으면 예외가 발생한다.")
    void todoEntityNullField() {
        //given


        // when & then
        Assertions.assertThatThrownBy(() -> {
            Todo.builder()
                    .title("title")
                    .tag(TodoFactory.tag())
                    .completeStatus(CompleteStatus.READY)
                    .description("description")
                    .difficulty(Difficulty.EASY)
                    .build();
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("필수 필드가 정확하게 입력되면 예외가 발생하지 않는다.")
    void todoEntityNonNullField() {
        //given


        // when & then
        Assertions.assertThatCode(TodoFactory::todo)
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Todo의 진행 상태를 같은 상태로 업데이트하면 예외가 발생한다.")
    void todoCompleteStatusSameUpdate() {
        //given
        Todo todo = TodoFactory.todo();

        //when & then
        Assertions.assertThatThrownBy(() -> todo.changeCompleteStatus(CompleteStatus.READY))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("진행 상태를 같은 상태로 변경 할 수 없습니다. " + CompleteStatus.READY.getStatusName());
    }

    @ParameterizedTest
    @EnumSource(value = CompleteStatus.class, mode = EnumSource.Mode.EXCLUDE, names = {"READY"})
    @DisplayName("현재 상태가 아닌 상태로만 Todo의 업데이트가 가능하다.")
    void todoCompleteStatusUpdate(CompleteStatus completeStatus) {
        //given
        Todo todo = TodoFactory.todo();

        //when & then
        Assertions.assertThatCode(() -> todo.changeCompleteStatus(completeStatus))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @EnumSource(value = Difficulty.class)
    @DisplayName("이미 생성된 todo의 내용을 업데이트가 가능하다.")
    void todoUpdate(Difficulty difficulty) {
        //given
        User user = UserFactory.user();
        Todo todo = TodoFactory.todo();

        Tag tag = TodoFactory.tag("새로운 태그");
        Todo inputTodo = Todo.createTodo(user.getId(), tag, "새로운 타이틀", "새로운 내용", difficulty);

        //when
        todo.updateTodo(inputTodo);

        //then
        Assertions.assertThat(todo.getTag().getName()).isEqualTo("새로운 태그");
        Assertions.assertThat(todo.getTitle()).isEqualTo("새로운 타이틀");
        Assertions.assertThat(todo.getDescription()).isEqualTo("새로운 내용");
        Assertions.assertThat(todo.getDifficulty()).isEqualTo(difficulty);
    }
}