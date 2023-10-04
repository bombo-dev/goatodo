package com.goatodo.api.todo.presentation.interfaces;

import com.goatodo.api.todo.presentation.dto.TagCreateRequest;
import com.goatodo.api.todo.presentation.dto.TagDeleteRequest;
import com.goatodo.api.todo.presentation.dto.TagUpdateRequest;
import com.goatodo.application.todo.dto.request.TagServiceCreateRequest;
import com.goatodo.application.todo.dto.request.TagServiceDeleteRequest;
import com.goatodo.application.todo.dto.request.TagServiceUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class TagRequestMapper {

    public TagServiceCreateRequest toService(TagCreateRequest request) {
        return TagServiceCreateRequest.builder()
                .memberId(request.memberId())
                .name(request.name())
                .tagType(request.tagType())
                .build();
    }

    public TagServiceUpdateRequest toService(TagUpdateRequest request) {
        return TagServiceUpdateRequest.builder()
                .memberId(request.memberId())
                .name(request.name())
                .build();
    }

    public TagServiceDeleteRequest toService(TagDeleteRequest request) {
        return TagServiceDeleteRequest.builder()
                .memberId(request.memberId())
                .build();
    }
}
