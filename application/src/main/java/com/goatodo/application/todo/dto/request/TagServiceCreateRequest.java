package com.goatodo.application.todo.dto.request;

import com.goatodo.domain.todo.Tag;
import com.goatodo.domain.todo.TagType;
import com.goatodo.domain.user.User;
import lombok.Builder;

@Builder
public record TagServiceCreateRequest(
        Long userId,
        String name,
        TagType tagType
) {
    public Tag toEntity(User user) {
        return Tag.builder()
                .user(user)
                .name(name())
                .tagType(tagType())
                .build();
    }
}
