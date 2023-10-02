package com.goatodo.domain.todo.response;

import com.bombo.goatodo.domain.todo.Todo;
import com.bombo.goatodo.util.FormatConverter;

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
                todo.getActive()
        );
    }
}
