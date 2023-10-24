package com.goatodo.application.tag.request;

import com.goatodo.domain.tag.Tag;
import lombok.Builder;

@Builder
public record TagUpdateServiceRequest(
        Long userId,
        String name
) {


    public Tag toEntity() {
        return Tag.builder()
                .userId(userId)
                .name(name)
                .build();
    }
}
