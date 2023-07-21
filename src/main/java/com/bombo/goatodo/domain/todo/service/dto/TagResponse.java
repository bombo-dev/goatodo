package com.bombo.goatodo.domain.todo.service.dto;

import com.bombo.goatodo.domain.todo.Tag;
import com.bombo.goatodo.domain.todo.TagType;

public record TagResponse(
        Long id,
        Long memberId,
        String name,
        TagType tagType
) {

    public TagResponse(Tag tag) {
        this(tag.getId(), tag.getMemberId(), tag.getName(), tag.getTagType());
    }
}
