package com.goatodo.application.tag;

import com.goatodo.application.tag.request.TagCreateServiceRequest;
import com.goatodo.application.tag.response.TagResponse;
import com.goatodo.domain.tag.Tag;
import com.goatodo.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagValidator tagValidator;

    public TagResponse createTag(Long userId, TagCreateServiceRequest request) {
        Tag tag = Tag.createTag(userId, request.name());
        tagValidator.duplicateTag(tag);

        tagRepository.save(tag);

        return new TagResponse(tag);
    }
}
