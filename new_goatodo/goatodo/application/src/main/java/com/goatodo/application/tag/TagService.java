package com.goatodo.application.tag;

import com.goatodo.application.tag.request.TagCreateServiceRequest;
import com.goatodo.application.tag.request.TagUpdateServiceRequest;
import com.goatodo.application.tag.response.TagResponse;
import com.goatodo.common.error.ErrorCode;
import com.goatodo.domain.tag.Tag;
import com.goatodo.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagValidator tagValidator;

    public TagResponse createTag(
            Long userId,
            TagCreateServiceRequest request
    ) {
        Tag tag = Tag.createTag(userId, request.name());
        tagValidator.duplicateTag(tag);

        tagRepository.save(tag);

        return new TagResponse(tag);
    }

    public TagResponse update(
            Long tagId,
            TagUpdateServiceRequest request
    ) {
        Tag requestTag = request.toEntity();
        tagValidator.duplicateTag(requestTag);

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 tagId가 입력되었습니다. id : {}", tagId);
                    return new NoSuchElementException(ErrorCode.NOT_EXIST_ID_REQUEST.name());
                });
        tag.update(requestTag);

        return new TagResponse(tag);
    }
}
