package com.bombo.goatodo.domain.todo.service.dto;

import java.util.Collections;
import java.util.List;

public record TodosResponse(
        List<TodoResponse> todoResponseList
) {

    public List<TodoResponse> getTodoResponseList() {
        return Collections.unmodifiableList(todoResponseList);
    }
}
