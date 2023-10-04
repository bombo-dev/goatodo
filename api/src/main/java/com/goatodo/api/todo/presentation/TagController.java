package com.goatodo.api.todo.presentation;

import com.goatodo.api.todo.presentation.dto.TagCreateRequest;
import com.goatodo.api.todo.presentation.dto.TagDeleteRequest;
import com.goatodo.api.todo.presentation.dto.TagUpdateRequest;
import com.goatodo.api.todo.presentation.interfaces.TagRequestMapper;
import com.goatodo.application.todo.MemberTagService;
import com.goatodo.application.todo.dto.TagsResponse;
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
    private final TagRequestMapper tagRequestMapper;

    @PostMapping("/tags")
    public ResponseEntity<Void> saveTag(@Validated @RequestBody TagCreateRequest request) {
        memberTagService.save(tagRequestMapper.toService(request));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<TagsResponse> findTags(@PathVariable Long tagId) {
        TagsResponse tagsResponse = memberTagService.findTagsByMember(tagId);

        return ResponseEntity.status(HttpStatus.OK).body(tagsResponse);
    }

    @GetMapping("/tags/{tagId}/select")
    public ResponseEntity<TagsResponse> findSelectTags(@PathVariable Long tagId) {
        TagsResponse tagsResponse = memberTagService.findTagsForSelecting(tagId);

        return ResponseEntity.status(HttpStatus.OK).body(tagsResponse);
    }

    @PatchMapping("/tags/{tagId}")
    public ResponseEntity<Void> updateTag(@PathVariable Long tagId, @Validated @RequestBody TagUpdateRequest request) {
        memberTagService.updateTag(tagId, tagRequestMapper.toService(request));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId, @Validated @RequestBody TagDeleteRequest request) {
        memberTagService.deleteTag(tagId, tagRequestMapper.toService(request));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
