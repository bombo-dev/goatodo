package com.goatodo.api.todo.presentation.dto;

import com.goatodo.domain.todo.TagType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TagCreateRequest(
        @NotNull
        Long memberId,

        @NotBlank(message = "카테고리는 공백 일 수 없습니다.")
        @Size(max = 20, message = "카테고리는 20자 이내여야 합니다.")
        String name,

        @NotNull(message = "태그 타입은 null 일 수 없습니다.")
        TagType tagType
) {

}
