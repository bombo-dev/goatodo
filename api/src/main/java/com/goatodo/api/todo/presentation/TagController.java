package com.goatodo.api.todo.presentation;

import com.bombo.goatodo.domain.todo.controller.dto.TagCreateRequest;
import com.bombo.goatodo.domain.todo.controller.dto.TagDeleteRequest;
import com.bombo.goatodo.domain.todo.controller.dto.TagUpdateRequest;
import com.bombo.goatodo.domain.todo.service.MemberTagService;
import com.bombo.goatodo.domain.todo.service.dto.TagsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TagController {

    private final MemberTagService memberTagService;

    @PostMapping("/tag")
    public ResponseEntity<Void> saveTag(@Validated @RequestBody TagCreateRequest tagCreateRequest) {
        memberTagService.save(tagCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<TagsResponse> findTags(@PathVariable Long id) {
        TagsResponse findTagsResponse = memberTagService.findTagsByMember(id);

        return ResponseEntity.status(HttpStatus.OK).body(findTagsResponse);
    }

    @GetMapping("/tags/{id}/select")
    public ResponseEntity<TagsResponse> findSelectTags(@PathVariable Long id) {
        TagsResponse findTagsResponse = memberTagService.findTagsForSelecting(id);

        return ResponseEntity.status(HttpStatus.OK).body(findTagsResponse);
    }

    @PostMapping("/tag/{id}")
    public ResponseEntity<Void> updateTag(@PathVariable Long id, @Validated @RequestBody TagUpdateRequest tagUpdateRequest) {
        memberTagService.updateTag(id, tagUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/tag/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id, @Validated @RequestBody TagDeleteRequest tagDeleteRequest) {
        memberTagService.deleteTag(id, tagDeleteRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
