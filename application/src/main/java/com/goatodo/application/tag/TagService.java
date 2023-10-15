package com.goatodo.application.tag;

import com.goatodo.application.todo.TagValidator;
import com.goatodo.application.todo.dto.TagResponse;
import com.goatodo.application.todo.dto.TagsResponse;
import com.goatodo.application.todo.dto.request.TagServiceCreateRequest;
import com.goatodo.application.todo.dto.request.TagServiceDeleteRequest;
import com.goatodo.application.todo.dto.request.TagServiceUpdateRequest;
import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.NotExistIdRequestException;
import com.goatodo.domain.tag.Tag;
import com.goatodo.domain.tag.repository.TagRepository;
import com.goatodo.domain.user.User;
import com.goatodo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TagValidator tagValidator;

    @Transactional
    public Long save(TagServiceCreateRequest request) {
        tagValidator.validateDuplicatedTag(request.userId(), request.name());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Tag tag = Tag.createTag(user.getId(), request.name());
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

        tag.validOwn(request.userId());

        tag.changeName(request.name());
    }

    @Transactional
    public void deleteTag(Long id, TagServiceDeleteRequest request) {

        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        tag.validOwn(request.userId());

        tagRepository.delete(tag);
    }
}
