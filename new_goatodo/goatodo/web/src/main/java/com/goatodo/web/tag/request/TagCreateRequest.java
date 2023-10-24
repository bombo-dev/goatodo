package com.goatodo.web.tag.request;

import com.goatodo.application.tag.request.TagCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TagCreateRequest(
        @NotNull(message = "userId는 null 일 수 없습니다.")
        Long userId,

        @NotBlank(message = "name은 공백이거나 null 일 수 없습니다.")
        String name
) {

    public TagCreateServiceRequest toService() {
        return TagCreateServiceRequest.builder()
                .name(name)
                .build();
    }
}
