package com.goatodo.domain.tag;

import com.goatodo.common.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagTest {

    @DisplayName("자기 자신이 만든 태그가 아니라면 예외가 발생한다.")
    @Test
    void validOwnWithInconsistentUserId() {
        // given
        Long userId = 1L;
        Tag tag = createTag(userId);

        Long clientId = 2L;

        // when & then
        assertThatThrownBy(() -> tag.validOwn(clientId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.FORBIDDEN_EXCEPTION.name() + "- userId : " + clientId);
    }

    @DisplayName("자기 자신이 만든 태그라면 정상동작한다.")
    @Test
    void validOwnConsistentUserId() {
        // given
        Long userId = 1L;
        Tag tag = createTag(userId);

        // when & then
        assertThatCode(() -> tag.validOwn(userId))
                .doesNotThrowAnyException();
    }

    private Tag createTag(Long userId) {
        return Tag.builder()
                .userId(userId)
                .name("태그 이름")
                .build();
    }
}