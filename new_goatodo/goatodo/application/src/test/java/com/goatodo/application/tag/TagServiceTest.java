package com.goatodo.application.tag;

import com.goatodo.application.IntegrationApplicationTest;
import com.goatodo.application.tag.request.TagCreateServiceRequest;
import com.goatodo.application.tag.request.TagUpdateServiceRequest;
import com.goatodo.application.tag.response.TagResponse;
import com.goatodo.common.error.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class TagServiceTest extends IntegrationApplicationTest {

    @Autowired
    private TagService tagService;

    @DisplayName("유저가 새로운 태그를 생성 할 수 있다.")
    @Test
    void createTag() {
        // given
        Long userId = 1L;
        TagCreateServiceRequest request = createTagCreateServiceRequest("태그");

        // when
        TagResponse response = tagService.createTag(userId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response)
                .extracting( "userId", "name")
                .containsExactly( 1L, "태그");
    }

    @DisplayName("유저는 같은 이름의 중복된 태그를 생성 할 수 없다.")
    @Test
    void createDuplicateTag() {
        // given
        Long userId = 1L;
        TagCreateServiceRequest request = createTagCreateServiceRequest("태그");
        tagService.createTag(userId, request);

        // when & then
        Assertions.assertThatThrownBy(() -> tagService.createTag(userId, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.TAG_DUPLICATE.name());
    }

    @DisplayName("유저는 태그를 수정 할 수 있다.")
    @Test
    void updateTag() {
        // given
        Long userId = 1L;
        TagCreateServiceRequest createRequest = createTagCreateServiceRequest("태그");
        TagResponse response = tagService.createTag(userId, createRequest);

        String updateTagName = "변경 된 태그";
        TagUpdateServiceRequest updateRequest = createTagUpdateServiceRequest(updateTagName);

        // when
        TagResponse updatedTagResponse = tagService.update(response.tagId(), updateRequest);

        // then
        assertThat(updatedTagResponse)
                .extracting("userId", "name")
                .containsExactly(1L, updateTagName);
    }

    @DisplayName("유저는 유저가 만든 이미 존재하는 태그의 이름으로 태그를 수정 할 수 없다.")
    @Test
    void updateDuplicateTag() {
        // given
        Long userId = 1L;
        String tagName = "태그";

        TagCreateServiceRequest createRequest = createTagCreateServiceRequest(tagName);
        TagResponse response = tagService.createTag(userId, createRequest);

        TagUpdateServiceRequest updateRequest = createTagUpdateServiceRequest(tagName);

        // when & then
        assertThatThrownBy(() -> tagService.update(response.tagId(), updateRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ErrorCode.TAG_DUPLICATE.name());
    }

    private TagCreateServiceRequest createTagCreateServiceRequest(String name) {
        return TagCreateServiceRequest.builder()
                .name(name)
                .build();
    }

    private TagUpdateServiceRequest createTagUpdateServiceRequest(String name) {
        return TagUpdateServiceRequest.builder()
                .userId(1L)
                .name(name)
                .build();
    }
}