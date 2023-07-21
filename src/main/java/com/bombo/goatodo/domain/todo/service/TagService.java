package com.bombo.goatodo.domain.todo.service;

import com.bombo.goatodo.domain.todo.controller.dto.TagCreateRequest;
import com.bombo.goatodo.domain.todo.controller.dto.TagDeleteRequest;
import com.bombo.goatodo.domain.todo.controller.dto.TagUpdateRequest;
import com.bombo.goatodo.domain.todo.service.dto.TagResponse;
import com.bombo.goatodo.domain.todo.service.dto.TagResponses;

public interface TagService {

    TagResponse save(TagCreateRequest tagCreateRequest);

    TagResponses findCategoriesForSelecting(Long memberId);

    TagResponses findCategoriesByMember(Long memberId);

    void updateCategory(TagUpdateRequest tagUpdateRequest);

    void deleteCategory(TagDeleteRequest tagDeleteRequest);
}
