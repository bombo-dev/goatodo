package com.bombo.goatodo.domain.todo.service.dto;

import java.util.Collections;
import java.util.List;

public record TagResponses(
        List<TagResponse> tagResponseList
) {

    public List<TagResponse> getCategoryResponseList() {
        return Collections.unmodifiableList(tagResponseList);
    }
}
