package com.goatodo.domain.tag.repository;

import com.goatodo.domain.IntegrationRepositoryTest;
import com.goatodo.domain.tag.Tag;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryTest extends IntegrationRepositoryTest {

    @DisplayName("유저 아이디와 태그 이름으로 태그의 조회가 가능하다.")
    @Test
    void readTagWithUserIdAndTagName() {
        // given
        Long userId = 1L;
        Tag tag = Tag.createTag(userId, "태그");
        tagRepository.save(tag);

        // when
        Optional<Tag> findTag = tagRepository.findByUserIdAndName(userId, "태그");

        // then
        assertThat(findTag).isNotEmpty();
    }
}