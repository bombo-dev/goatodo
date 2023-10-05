package com.goatodo.application.todo.dto;


import com.goatodo.common.util.FormatConverter;
import com.goatodo.domain.todo.Todo;

public record TodoResponse(
        Long id,
        String title,
        String description,
        String tagName,
        String status,
        String createdAt,
        String updatedAt,
        boolean isActive
) {

    public TodoResponse(Todo todo) {
        this(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getTag().getName(),
                todo.getCompleteStatus().getStatusName(),
                FormatConverter.convertLocalDateTimeToString(todo.getCreatedAt()),
                FormatConverter.convertLocalDateTimeToString(todo.getUpdatedAt()),
                todo.getIsActive()
        );
    }
}
