package com.goatodo.web.tag;

import com.goatodo.application.tag.TagService;
import com.goatodo.application.tag.response.TagResponse;
import com.goatodo.web.global.response.ApiResponse;
import com.goatodo.web.tag.request.TagCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
@RestController
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<ApiResponse<TagResponse>> createTag(@Valid @RequestBody TagCreateRequest request) {
        TagResponse response = tagService.createTag(request.userId(), request.toService());
        return ResponseEntity.created(URI.create("/api/v1/tags/" + response.tagId()))
                .body(ApiResponse.created(response));
    }
}
