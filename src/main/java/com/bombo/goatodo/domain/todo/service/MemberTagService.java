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
import com.bombo.goatodo.global.exception.RoleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberTagService {

    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;

    @Transactional
    public TagResponse save(TagCreateRequest tagCreateRequest) {
        validateRole(tagCreateRequest.tagType(), ErrorCode.CREATE_REQUEST_IS_FORBIDDEN);
        validateDuplicatedTag(tagCreateRequest.memberId(), tagCreateRequest.name());

        Member findMember = memberRepository.findById(tagCreateRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Tag tag = Tag.builder()
                .member(findMember)
                .name(tagCreateRequest.name())
                .tagType(tagCreateRequest.tagType())
                .build();

        Tag savedTag = tagRepository.save(tag);
        return new TagResponse(savedTag);
    }

    public TagResponses findTagsForSelecting(Long memberId) {
        List<TagResponse> findSelectingCategories = tagRepository.findSelectingTag(memberId)
                .stream()
                .map(TagResponse::new)
                .toList();

        return new TagResponses(findSelectingCategories);
    }

    public TagResponses findTagsByMember(Long memberId) {
        List<TagResponse> findCategories = tagRepository.findByMember_Id(memberId)
                .stream()
                .map(TagResponse::new)
                .toList();

        return new TagResponses(findCategories);
    }

    public TagResponse updateTag(TagUpdateRequest tagUpdateRequest) {
        validateRole(tagUpdateRequest.tagType(), ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);

        Tag findTag = tagRepository.findById(tagUpdateRequest.id())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Member findMember = memberRepository.findById(tagUpdateRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        validateOwner(findTag, findMember, ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);
        findTag.changeTag(tagUpdateRequest.name());
        return new TagResponse(findTag);
    }

    public void deleteTag(TagDeleteRequest tagDeleteRequest) {
        Tag findTag = tagRepository.findById(tagDeleteRequest.id())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));
        Member findMember = memberRepository.findById(tagDeleteRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        validateOwner(findTag, findMember, ErrorCode.DELETE_REQUEST_IS_FORBIDDEN);
        tagRepository.delete(findTag);
    }

    private void validateRole(TagType tagType, ErrorCode errorCode) {
        if (tagType.isCommonType()) {
            throw new RoleException(errorCode);
        }
    }

    private void validateOwner(Tag findTag, Member findMember, ErrorCode errorCode) {
        if (!findTag.isOwnTag(findMember)) {
            throw new RoleException(errorCode);
        }
    }

    private void validateDuplicatedTag(Long memberId, String name) {
        tagRepository.existSameMemberTag(memberId, name)
                .ifPresent((tag) -> {
                    throw new DuplicateException(ErrorCode.TAG_DUPLICATE);
                });
    }
}
