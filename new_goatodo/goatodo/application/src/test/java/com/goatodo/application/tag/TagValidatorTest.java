package com.goatodo.application.tag;

import com.goatodo.application.IntegrationApplicationTest;
import com.goatodo.common.error.ErrorCode;
import com.goatodo.domain.tag.Tag;
import com.goatodo.domain.tag.repository.TagRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

class TagValidatorTest extends IntegrationApplicationTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagValidator tagValidator;

    @DisplayName("같은 유저가 똑같은 이름의 태그를 생성하면 예외가 발생한다.")
    @Test
    void createDuplicateTag() {
        // given
        Long userId = 1L;
        String tagName = "태그";

        Tag tag = Tag.createTag(userId, tagName);
        tagRepository.save(tag);

        // when & then
        Assertions.assertThatThrownBy(() -> tagValidator.duplicateTag(tag))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.TAG_DUPLICATE.name());

    }
}