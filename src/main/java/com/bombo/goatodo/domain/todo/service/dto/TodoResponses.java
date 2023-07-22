package com.bombo.goatodo.domain.todo.service.dto;

import java.util.Collections;
import java.util.List;

public record TodoResponses(
        List<TodoResponse> todoResponseList
) {

    public List<TodoResponse> getTodoResponseList() {
        return Collections.unmodifiableList(todoResponseList);
    }
}
