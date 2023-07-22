package com.bombo.goatodo.domain.todo.service;

import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.repository.MemberRepository;
import com.bombo.goatodo.domain.todo.Tag;
import com.bombo.goatodo.domain.todo.TagType;
import com.bombo.goatodo.domain.todo.controller.dto.TagCreateRequest;
import com.bombo.goatodo.domain.todo.controller.dto.TagDeleteRequest;
import com.bombo.goatodo.domain.todo.controller.dto.TagUpdateRequest;
import com.bombo.goatodo.domain.todo.repository.TagRepository;
import com.bombo.goatodo.global.error.ErrorCode;
import com.bombo.goatodo.global.exception.DuplicateException;
import com.bombo.goatodo.global.exception.NotExistIdRequestException;
import com.bombo.goatodo.global.exception.RoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminTagService {

    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;

    public Long save(TagCreateRequest tagCreateRequest) {
        validateDuplicatedCategory(tagCreateRequest.name());
        Member findMember = memberRepository.findById(tagCreateRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));
        validateRole(findMember);

        Tag tag = Tag.builder()
                .name(tagCreateRequest.name())
                .tagType(TagType.COMMON)
                .build();

        Tag savedTag = tagRepository.save(tag);

        return savedTag.getId();
    }

    public void updateCategory(TagUpdateRequest tagUpdateRequest) {
        validateDuplicatedCategory(tagUpdateRequest.name());
        Member findMember = memberRepository.findById(tagUpdateRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));
        validateRole(findMember);

        Tag findTag = tagRepository.findById(tagUpdateRequest.id())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        findTag.changeTag(tagUpdateRequest.name());
    }

    public void deleteCategory(TagDeleteRequest tagDeleteRequest) {
        Member findMember = memberRepository.findById(tagDeleteRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));
        validateRole(findMember);

        Tag findTag = tagRepository.findById(tagDeleteRequest.id())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        tagRepository.delete(findTag);
    }

    private void validateRole(Member member) {
        if (member.isNormal()) {
            throw new RoleException(ErrorCode.CREATE_REQUEST_IS_FORBIDDEN);
        }
    }

    private void validateDuplicatedCategory(String name) {
        tagRepository.existSameCommonTag(name)
                .ifPresent((tag) -> {
                    throw new DuplicateException(ErrorCode.TAG_DUPLICATE);
                });
    }
}
