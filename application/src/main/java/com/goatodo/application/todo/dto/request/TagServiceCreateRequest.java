package com.goatodo.application.todo.dto.request;

import com.goatodo.domain.tag.Tag;
import lombok.Builder;

@Builder
public record TagServiceCreateRequest(
        Long userId,
        String name
) {
    public Tag toEntity() {
        return Tag.builder()
                .userId(userId)
                .name(name())
                .build();
    }
}
