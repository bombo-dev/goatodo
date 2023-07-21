package com.bombo.goatodo.domain.todo.service.dto;

import com.bombo.goatodo.domain.todo.Tag;

public record TagResponse(
        Long id,
        Long memberId,
        String tag
) {

    public TagResponse(Tag tag, Long memberId) {
        this(tag.getId(), memberId, tag.getName());
    }

    public TagResponse(Tag tag) {
        this(tag.getId(), null, tag.getName());
    }
}
