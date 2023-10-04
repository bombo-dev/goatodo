package com.goatodo.application.todo;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.DuplicateException;
import com.goatodo.domain.todo.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TagValidator {

    private final TagRepository tagRepository;

    public void validateDuplicatedTag(Long memberId, String name) {
        tagRepository.findByMember_IdAndName(memberId, name)
                .ifPresent((tag) -> {
                    throw new DuplicateException(ErrorCode.TAG_DUPLICATE);
                });
    }

    public void validateDuplicatedCommonTag(String name) {
        tagRepository.existSameCommonTag(name)
                .ifPresent((tag) -> {
                    throw new DuplicateException(ErrorCode.TAG_DUPLICATE);
                });
    }
}
