package com.goatodo.api.todo.presentation;

import com.bombo.goatodo.domain.todo.controller.dto.TagCreateRequest;
import com.bombo.goatodo.domain.todo.service.AdminTagService;
import com.bombo.goatodo.domain.todo.service.dto.TagsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminTagController {

    private final AdminTagService adminTagService;

    @PostMapping("/tag")
    public ResponseEntity<Void> saveTag(@Validated @RequestBody TagCreateRequest tagCreateRequest) {
        adminTagService.save(tagCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/tags")
    public ResponseEntity<TagsResponse> findSelectTags() {
        TagsResponse findTagsResponse = adminTagService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(findTagsResponse);
    }
}
