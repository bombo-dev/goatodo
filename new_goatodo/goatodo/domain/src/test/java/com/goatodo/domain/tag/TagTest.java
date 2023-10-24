package com.goatodo.domain.tag;

import com.goatodo.common.error.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TagTest {

    @DisplayName("태그는 수정이 가능하다.")
    @Test
    void updateTag() {
        // given
        Tag tag = createTag("태그");

        String inputTagName = "변경 할 태그";
        Tag requestTag = createTag(inputTagName);

        // when
        tag.update(requestTag);

        // then
        assertThat(tag.getName()).isEqualTo(inputTagName);
    }

    @DisplayName("태그 수정 시 동일한 이름의 태그로 수정이 불가능하다.")
    @Test
    void updateDuplicateTag() {
        // given
        String inputTagName = "태그";

        Tag tag = createTag(inputTagName);
        Tag requestTag = createTag(inputTagName);

        // when & then
        Assertions.assertThatThrownBy(() -> tag.update(requestTag))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.TAG_DUPLICATE.name());
    }

    private Tag createTag(String name) {
        return Tag.builder()
                .userId(1L)
                .name(name)
                .build();
    }
}