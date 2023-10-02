package com.goatodo.application.todo.dto;

import java.util.Collections;
import java.util.List;

public record TagsResponse(
        List<TagResponse> tagResponseList
) {

    public List<TagResponse> getTagResponseList() {
        return Collections.unmodifiableList(tagResponseList);
    }
}
