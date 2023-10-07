package com.goatodo.application.todo;

import com.goatodo.application.todo.dto.TagResponse;
import com.goatodo.application.todo.dto.TagsResponse;
import com.goatodo.application.todo.dto.request.TagServiceCreateRequest;
import com.goatodo.application.todo.dto.request.TagServiceDeleteRequest;
import com.goatodo.application.todo.dto.request.TagServiceUpdateRequest;
import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.NotExistIdRequestException;
import com.goatodo.domain.todo.Tag;
import com.goatodo.domain.todo.repository.TagRepository;
import com.goatodo.domain.user.User;
import com.goatodo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserTagService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TagValidator tagValidator;

    @Transactional
    public Long save(TagServiceCreateRequest request) {
        tagValidator.validateDuplicatedTag(request.userId(), request.name());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Tag tag = Tag.createTag(user, request.name(), request.tagType());
        tag.validRole(ErrorCode.CREATE_REQUEST_IS_FORBIDDEN);

        Tag savedTag = tagRepository.save(tag);
        return savedTag.getId();
    }

    public TagsResponse findTagsForSelecting(Long memberId) {
        List<TagResponse> findSelectingCategories = tagRepository.findSelectingTag(memberId)
                .stream()
                .map(TagResponse::new)
                .toList();

        return new TagsResponse(findSelectingCategories);
    }

    public TagsResponse findTagsByMember(Long memberId) {
        List<TagResponse> findCategories = tagRepository.findByMember_Id(memberId)
                .stream()
                .map(TagResponse::new)
                .toList();

        return new TagsResponse(findCategories);
    }

    @Transactional
    public void updateTag(Long tagId, TagServiceUpdateRequest request) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        tag.validRole(ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);
        tag.validOwn(request.userId(), ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);

        tag.changeName(request.name());
    }

    @Transactional
    public void deleteTag(Long id, TagServiceDeleteRequest request) {

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        tag.validRole(ErrorCode.DELETE_REQUEST_IS_FORBIDDEN);
        tag.validOwn(request.userId(), ErrorCode.DELETE_REQUEST_IS_FORBIDDEN);

        tagRepository.delete(tag);
    }
}
