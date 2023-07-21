package com.bombo.goatodo.domain.todo.controller.dto;

import com.bombo.goatodo.domain.todo.TagType;
import jakarta.validation.constraints.NotNull;

public record TagDeleteRequest(
        @NotNull(message = "태그 id는 null 일 수 없습니다.")
        Long id,

        Long memberId,

        @NotNull(message = "태그 타입은 null 일 수 없습니다.")
        TagType tagType
) {
}
