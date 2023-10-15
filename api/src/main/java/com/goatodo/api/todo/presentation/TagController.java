package com.goatodo.api.todo.presentation;

import com.goatodo.api.todo.presentation.dto.TagCreateRequest;
import com.goatodo.api.todo.presentation.dto.TagDeleteRequest;
import com.goatodo.api.todo.presentation.dto.TagUpdateRequest;
import com.goatodo.api.todo.presentation.interfaces.TagRequestMapper;
import com.goatodo.application.tag.TagService;
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

    private final TagService tagService;
    private final TagRequestMapper tagRequestMapper;

    @PostMapping("/tags")
    public ResponseEntity<Void> saveTag(@Validated @RequestBody TagCreateRequest request) {
        tagService.save(tagRequestMapper.toService(request));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<TagsResponse> findTags(@PathVariable Long tagId) {
        TagsResponse tagsResponse = tagService.findTagsByMember(tagId);

        return ResponseEntity.status(HttpStatus.OK).body(tagsResponse);
    }

    @GetMapping("/tags/{tagId}/select")
    public ResponseEntity<TagsResponse> findSelectTags(@PathVariable Long tagId) {
        TagsResponse tagsResponse = tagService.findTagsForSelecting(tagId);

        return ResponseEntity.status(HttpStatus.OK).body(tagsResponse);
    }

    @PatchMapping("/tags/{tagId}")
    public ResponseEntity<Void> updateTag(@PathVariable Long tagId, @Validated @RequestBody TagUpdateRequest request) {
        tagService.updateTag(tagId, tagRequestMapper.toService(request));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId, @Validated @RequestBody TagDeleteRequest request) {
        tagService.deleteTag(tagId, tagRequestMapper.toService(request));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
