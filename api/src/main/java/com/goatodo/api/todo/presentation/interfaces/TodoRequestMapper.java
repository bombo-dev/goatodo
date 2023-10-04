package com.goatodo.api.todo.presentation.interfaces;

import com.goatodo.api.todo.presentation.dto.*;
import com.goatodo.application.todo.dto.request.*;
import org.springframework.stereotype.Component;

@Component
public class TodoRequestMapper {

    public TodoServiceCreateRequest toService(TodoCreateRequest request) {
        return TodoServiceCreateRequest.builder()
                .memberId(request.memberId())
                .tagId(request.tagId())
                .title(request.title())
                .description(request.description())
                .build();
    }

    public TodoServiceReadRequest toService(TodoReadRequest request) {
        return TodoServiceReadRequest.builder()
                .memberId(request.memberId())
                .build();
    }

    public TodoServiceUpdateRequest toService(TodoUpdateRequest request) {
        return TodoServiceUpdateRequest.builder()
                .memberId(request.memberId())
                .tagId(request.tagId())
                .title(request.title())
                .description(request.description())
                .build();
    }

    public TodoServiceCompleteStatusUpdateRequest toService(TodoCompleteStatusUpdateRequest request) {
        return TodoServiceCompleteStatusUpdateRequest.builder()
                .memberId(request.memberId())
                .completeStatus(request.completeStatus())
                .build();
    }

    public TodoServiceDeleteRequest toService(TodoDeleteRequest request) {
        return TodoServiceDeleteRequest.builder()
                .memberId(request.memberId())
                .build();
    }
}
