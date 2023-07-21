package com.bombo.goatodo.domain.todo.service;

import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.repository.MemberRepository;
import com.bombo.goatodo.domain.todo.Tag;
import com.bombo.goatodo.domain.todo.TagType;
import com.bombo.goatodo.domain.todo.controller.dto.TagCreateRequest;
import com.bombo.goatodo.domain.todo.controller.dto.TagDeleteRequest;
import com.bombo.goatodo.domain.todo.controller.dto.TagUpdateRequest;
import com.bombo.goatodo.domain.todo.repository.TagRepository;
import com.bombo.goatodo.domain.todo.service.dto.TagResponse;
import com.bombo.goatodo.domain.todo.service.dto.TagResponses;
import com.bombo.goatodo.global.error.ErrorCode;
import com.bombo.goatodo.global.exception.DuplicateException;
import com.bombo.goatodo.global.exception.NotExistIdRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("memberCategoryService")
public class MemberTagService implements TagService {

    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;

    @Override
    public TagResponse save(TagCreateRequest tagCreateRequest) {
        validateDuplicatedCategory(tagCreateRequest);

        if (isCommonCategory(tagCreateRequest.tagType())) {
            Tag savedTag = saveCommonCategory(tagCreateRequest);
            return new TagResponse(savedTag);
        }

        Tag savedTag = savedMemberCategory(tagCreateRequest);
        return new TagResponse(savedTag);
    }

    private Tag saveCommonCategory(TagCreateRequest tagCreateRequest) {
        Tag tag = Tag.builder()
                .name(tagCreateRequest.name())
                .build();

        return tagRepository.save(tag);
    }

    private Tag savedMemberCategory(TagCreateRequest tagCreateRequest) {
        Member findMember = memberRepository.findById(tagCreateRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Tag tag = Tag.builder()
                .member(findMember)
                .name(tagCreateRequest.name())
                .build();

        return tagRepository.save(tag);
    }

    @Override
    public TagResponses findCategoriesForSelecting(Long memberId) {
        List<TagResponse> findSelectingCategories = tagRepository.findSelectingCategory(memberId)
                .stream()
                .map(TagResponse::new)
                .toList();

        return new TagResponses(findSelectingCategories);
    }

    @Override
    public TagResponses findCategoriesByMember(Long memberId) {
        List<TagResponse> findCategories = tagRepository.findByMember_Id(memberId)
                .stream()
                .map(TagResponse::new)
                .toList();

        return new TagResponses(findCategories);
    }

    @Override
    public void updateCategory(TagUpdateRequest tagUpdateRequest) {

        if (isCommonCategory(tagUpdateRequest.tagType())) {
            Tag findTag = tagRepository.findById(tagUpdateRequest.id())
                    .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

            findTag.changeTag(tagUpdateRequest.name());
            return;
        }

        Tag findTag = tagRepository.findById(tagUpdateRequest.id())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Member findMember = memberRepository.findById(tagUpdateRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        if (findTag.isOwnTag(findMember)) {
            findTag.changeTag(tagUpdateRequest.name());
            return;
        } else {
            throw new IllegalStateException("적절한 권한이 없어 태그 변경을 수행 할 수 없습니다.");
        }
    }

    @Override
    public void deleteCategory(TagDeleteRequest tagDeleteRequest) {
        if (isCommonCategory(tagDeleteRequest.tagType())) {
            Tag findTag = tagRepository.findById(tagDeleteRequest.id())
                    .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

            tagRepository.delete(findTag);
            return;
        }

        Tag findTag = tagRepository.findById(tagDeleteRequest.id())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));
        Member findMember = memberRepository.findById(tagDeleteRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        if (findTag.isOwnTag(findMember)) {
            tagRepository.delete(findTag);
        }

    }

    private void validateDuplicatedCategory(TagCreateRequest tagCreateRequest) {
        validateDuplicatedCommonCategory(tagCreateRequest);
        validateDuplicatedMemberCategory(tagCreateRequest);
    }

    private boolean isCommonCategory(TagType tagType) {
        return tagType == TagType.COMMON;
    }

    private void validateDuplicatedCommonCategory(TagCreateRequest tagCreateRequest) {
        if (isCommonCategory(tagCreateRequest.tagType())) {
            tagRepository.existSameCommonCategory(tagCreateRequest.name())
                    .ifPresent((category -> {
                        throw new DuplicateException(ErrorCode.CATEGORY_DUPLICATE_TAG);
                    }));
        }
    }

    private void validateDuplicatedMemberCategory(TagCreateRequest tagCreateRequest) {
        if (!isCommonCategory(tagCreateRequest.tagType())) {
            tagRepository.existSameCommonCategory(tagCreateRequest.name())
                    .ifPresent((category -> {
                        throw new DuplicateException(ErrorCode.CATEGORY_DUPLICATE_TAG);
                    }));

            tagRepository.existSameMemberCategory(tagCreateRequest.memberId(), tagCreateRequest.name())
                    .ifPresent((category -> {
                        throw new DuplicateException(ErrorCode.CATEGORY_DUPLICATE_TAG);
                    }));
        }
    }
}
