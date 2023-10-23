package com.goatodo.application.tag;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.domain.tag.Tag;
import com.goatodo.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Component
public class TagValidator {

    private final TagRepository tagRepository;

    public void duplicateTag(Tag tag) {
        tagRepository.findByUserIdAndName(tag.getUserId(), tag.getName())
                .ifPresent((findTag) -> {
                    log.warn("태그 생성 중 중복된 태그가 생성되었습니다. userId : {}, name : {}", tag.getUserId(), tag.getName());
                    throw new IllegalStateException(ErrorCode.TAG_DUPLICATE.name());
                });
    }
}
