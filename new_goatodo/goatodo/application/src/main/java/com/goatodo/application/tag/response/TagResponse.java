package com.goatodo.application.tag.response;

import com.goatodo.domain.tag.Tag;

public record TagResponse(
        Long tagId,
        Long userId,
        String name
) {

    public TagResponse(Tag tag) {
        this(tag.getId(), tag.getUserId(), tag.getName());
    }
}
