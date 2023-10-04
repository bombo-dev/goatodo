package com.goatodo.application.todo.dto.request;

import com.goatodo.domain.member.Member;
import com.goatodo.domain.todo.Tag;
import com.goatodo.domain.todo.TagType;
import lombok.Builder;

@Builder
public record TagServiceCreateRequest(
        Long memberId,
        String name,
        TagType tagType
) {
    public Tag toEntity(Member member) {
        return Tag.builder()
                .member(member)
                .name(name())
                .tagType(tagType())
                .build();
    }
}
