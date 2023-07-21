package com.bombo.goatodo.domain.tag.service;

import com.bombo.goatodo.domain.member.Occupation;
import com.bombo.goatodo.domain.member.Role;
import com.bombo.goatodo.domain.member.controller.dto.MemberCreateRequest;
import com.bombo.goatodo.domain.member.service.DefaultMemberService;
import com.bombo.goatodo.domain.member.service.dto.MemberResponse;
import com.bombo.goatodo.domain.todo.Tag;
import com.bombo.goatodo.domain.todo.TagType;
import com.bombo.goatodo.domain.todo.controller.dto.TagCreateRequest;
import com.bombo.goatodo.domain.todo.controller.dto.TagDeleteRequest;
import com.bombo.goatodo.domain.todo.controller.dto.TagUpdateRequest;
import com.bombo.goatodo.domain.todo.repository.TagRepository;
import com.bombo.goatodo.domain.todo.service.AdminTagService;
import com.bombo.goatodo.domain.todo.service.MemberTagService;
import com.bombo.goatodo.domain.todo.service.dto.TagResponse;
import com.bombo.goatodo.domain.todo.service.dto.TagResponses;
import com.bombo.goatodo.global.exception.DuplicateException;
import com.bombo.goatodo.global.exception.NotExistIdRequestException;
import com.bombo.goatodo.global.exception.RoleException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class MemberTagServiceTest {

    @Autowired
    private MemberTagService memberTagService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private AdminTagService adminTagService;

    @Autowired
    private DefaultMemberService defaultMemberService;

    private static MemberCreateRequest normalMemberCreateRequest;
    private static MemberCreateRequest adminMemberCreateRequest;

    @BeforeAll
    static void beforeAll() {
        normalMemberCreateRequest = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "닉네임",
                Occupation.GENERAL,
                Role.NORMAL
        );

        adminMemberCreateRequest = new MemberCreateRequest(
                "admin",
                "naver.com",
                "password1234",
                "운영자",
                Occupation.GENERAL,
                Role.ADMIN
        );
    }

    @Test
    @DisplayName("회원이 이미 생성한 이름의 태그를 생성하면 예외가 발생한다.")
    void saveSameTagNameEx() {
        // given
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);

        TagCreateRequest tagCreateRequest = new TagCreateRequest(savedNormalMember.id(), "공부", TagType.INDIVIDUAL);
        memberTagService.save(tagCreateRequest);

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberTagService.save(tagCreateRequest))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("중복된 이름의 태그는 생성 할 수 없습니다.");
    }

    @Test
    @DisplayName("회원이 이미 공용으로 생성된 태그와 똑같은 이름의 태그를 생성하면 예외가 발생한다.")
    void saveSameCommonTagNameEx() {
        // given
        MemberResponse savedAdminMember = defaultMemberService.save(adminMemberCreateRequest);
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);

        TagCreateRequest commonTagCreateRequest = new TagCreateRequest(savedAdminMember.id(), "스터디", TagType.COMMON);
        adminTagService.save(commonTagCreateRequest);

        // when
        TagCreateRequest normalTagCreateRequest = new TagCreateRequest(savedNormalMember.id(), "스터디", TagType.INDIVIDUAL);

        // then
        Assertions.assertThatThrownBy(() -> memberTagService.save(normalTagCreateRequest))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("중복된 이름의 태그는 생성 할 수 없습니다.");
    }

    @Test
    @DisplayName("일반 회원이 공용 태그를 생성하려고 시도하면 예외가 발생한다.")
    void saveCommonTagByNormalMemberEx() {
        // given
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);
        TagCreateRequest commonTagCreateRequest = new TagCreateRequest(savedNormalMember.id(), "스터디", TagType.COMMON);

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberTagService.save(commonTagCreateRequest))
                .isInstanceOf(RoleException.class)
                .hasMessage("생성 할 수 있는 권한이 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 회원 아이디가 전달되면 예외가 발생한다.")
    void saveTagNotExistIdEx() {
        // given
        TagCreateRequest commonTagCreateRequest = new TagCreateRequest(-1L, "스터디", TagType.INDIVIDUAL);

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberTagService.save(commonTagCreateRequest))
                .isInstanceOf(NotExistIdRequestException.class)
                .hasMessage("잘못된 ID가 전달되었습니다.");
    }

    @Test
    @DisplayName("일반 회원은 공용 태그를 생성 할 수 있다.")
    void saveIndividualTag() {
        // given
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);
        TagCreateRequest tagCreateRequest = new TagCreateRequest(savedNormalMember.id(), "스터디", TagType.INDIVIDUAL);

        // when
        TagResponse savedTagResponse = memberTagService.save(tagCreateRequest);

        // then
        Assertions.assertThat(savedTagResponse.name()).isEqualTo("스터디");
        Assertions.assertThat(savedTagResponse.tagType()).isEqualTo(TagType.INDIVIDUAL);
    }

    @Test
    @DisplayName("회원이 선택하기 위한 태그를 조회 할 수 있다.")
    void findForSelectingTag() {
        // given
        MemberResponse savedAdminMember = defaultMemberService.save(adminMemberCreateRequest);
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);

        TagCreateRequest commonTagCreateRequest = new TagCreateRequest(savedAdminMember.id(), "공부", TagType.COMMON);
        TagCreateRequest individualTagCreateRequest = new TagCreateRequest(savedNormalMember.id(), "스터디", TagType.INDIVIDUAL);

        TagResponse savedCommonTag = adminTagService.save(commonTagCreateRequest);
        TagResponse savedIndividualTag = memberTagService.save(individualTagCreateRequest);

        // when
        TagResponses findSelectingTags = memberTagService.findTagsForSelecting(savedNormalMember.id());
        List<TagResponse> tagResponses = findSelectingTags.getTagResponseList();

        // then
        Assertions.assertThat(tagResponses).hasSize(2);
        Assertions.assertThat(tagResponses).containsExactly(savedCommonTag, savedIndividualTag);
    }

    @Test
    @DisplayName("회원이 생성한 태그만 조회 할 수 있다.")
    void findByMember() {
        // given
        MemberResponse savedAdminMember = defaultMemberService.save(adminMemberCreateRequest);
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);

        TagCreateRequest commonTagCreateRequest = new TagCreateRequest(savedAdminMember.id(), "공부", TagType.COMMON);
        TagCreateRequest individualTagCreateRequest = new TagCreateRequest(savedNormalMember.id(), "스터디", TagType.INDIVIDUAL);

        TagResponse savedCommonTag = adminTagService.save(commonTagCreateRequest);
        TagResponse savedIndividualTag = memberTagService.save(individualTagCreateRequest);

        // when
        TagResponses findCreateTags = memberTagService.findTagsByMember(savedNormalMember.id());
        List<TagResponse> tagResponses = findCreateTags.getTagResponseList();

        // then
        Assertions.assertThat(tagResponses).hasSize(1);
        Assertions.assertThat(tagResponses).containsExactly(savedIndividualTag);
    }

    @Test
    @DisplayName("일반 회원이 공통 태그를 수정하려고 시도하면 예외가 발생한다.")
    void changeCommonTagByNormalMemberEx() {
        // given
        MemberResponse savedAdminMember = defaultMemberService.save(adminMemberCreateRequest);
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);

        TagCreateRequest commonTagCreateRequest = new TagCreateRequest(savedAdminMember.id(), "공부", TagType.COMMON);
        TagResponse savedCommonTag = adminTagService.save(commonTagCreateRequest);

        TagUpdateRequest tagUpdateRequest = new TagUpdateRequest(
                savedCommonTag.id(),
                savedNormalMember.id(),
                "약속",
                savedCommonTag.tagType());

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberTagService.updateTag(tagUpdateRequest))
                .isInstanceOf(RoleException.class)
                .hasMessage("수정 할 수 있는 권한이 없습니다.");
    }

    @Test
    @DisplayName("다른 회원이 작성한 태그를 수정하려고 시도하면 예외가 발생한다.")
    void changeTagByNotOwnMemberEx() {
        // given
        MemberResponse savedAdminMember = defaultMemberService.save(adminMemberCreateRequest);
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);

        TagCreateRequest individualTagCreateRequest = new TagCreateRequest(savedAdminMember.id(), "공부", TagType.INDIVIDUAL);
        TagResponse savedIndividualTag = memberTagService.save(individualTagCreateRequest);

        TagUpdateRequest tagUpdateRequest = new TagUpdateRequest(
                savedIndividualTag.id(),
                savedNormalMember.id(),
                "약속",
                savedIndividualTag.tagType());

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberTagService.updateTag(tagUpdateRequest))
                .isInstanceOf(RoleException.class)
                .hasMessage("수정 할 수 있는 권한이 없습니다.");
    }

    @Test
    @DisplayName("자신이 작성한 태그이면 수정이 가능하다.")
    void changeTagNameByOwnMember() {
        // given
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);

        TagCreateRequest individualTagCreateRequest = new TagCreateRequest(savedNormalMember.id(), "공부", TagType.INDIVIDUAL);
        TagResponse savedIndividualTag = memberTagService.save(individualTagCreateRequest);

        TagUpdateRequest tagUpdateRequest = new TagUpdateRequest(
                savedIndividualTag.id(),
                savedNormalMember.id(),
                "약속",
                savedIndividualTag.tagType());

        // when
        memberTagService.updateTag(tagUpdateRequest);
        Tag findTag = tagRepository.findById(savedIndividualTag.id()).get();

        // then
        Assertions.assertThat(findTag.getName()).isEqualTo("약속");
    }

    @Test
    @DisplayName("일반 유저는 공통 태그를 삭제 할 수 없다.")
    void deleteTagByNormalMemberEx() {
        // given
        MemberResponse savedAdminMember = defaultMemberService.save(adminMemberCreateRequest);
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);

        TagCreateRequest commonTagCreateRequest = new TagCreateRequest(savedAdminMember.id(), "공부", TagType.COMMON);
        TagResponse savedCommonTag = adminTagService.save(commonTagCreateRequest);

        TagDeleteRequest tagDeleteRequest = new TagDeleteRequest(
                savedCommonTag.id(),
                savedNormalMember.id(),
                savedCommonTag.tagType());

        // when


        // then
        Assertions.assertThatThrownBy(() -> memberTagService.deleteTag(tagDeleteRequest))
                .isInstanceOf(RoleException.class)
                .hasMessage("삭제 할 수 있는 권한이 없습니다.");
    }

    @Test
    @DisplayName("자기 자신이 만든 태그가 아니면 삭제 할 수 없다.")
    void deleteTagByNotOwnMemberEx() {
        // given
        MemberResponse savedAdminMember = defaultMemberService.save(adminMemberCreateRequest);
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);

        TagCreateRequest individualTagCreateRequest = new TagCreateRequest(savedAdminMember.id(), "공부", TagType.INDIVIDUAL);
        TagResponse savedIndividualTag = memberTagService.save(individualTagCreateRequest);

        TagDeleteRequest tagDeleteRequest = new TagDeleteRequest(
                savedIndividualTag.id(),
                savedNormalMember.id(),
                savedIndividualTag.tagType());

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberTagService.deleteTag(tagDeleteRequest))
                .isInstanceOf(RoleException.class)
                .hasMessage("삭제 할 수 있는 권한이 없습니다.");
    }

    @Test
    @DisplayName("자기 자신이 만든 태그라면 삭제가 가능하다.")
    void deleteTagByOwnMember() {
        // given
        MemberResponse savedNormalMember = defaultMemberService.save(normalMemberCreateRequest);

        TagCreateRequest individualTagCreateRequest = new TagCreateRequest(savedNormalMember.id(), "공부", TagType.INDIVIDUAL);
        TagResponse savedIndividualTag = memberTagService.save(individualTagCreateRequest);

        TagDeleteRequest tagDeleteRequest = new TagDeleteRequest(
                savedIndividualTag.id(),
                savedNormalMember.id(),
                savedIndividualTag.tagType());

        // when
        memberTagService.deleteTag(tagDeleteRequest);
        Optional<Tag> findTag = tagRepository.findById(savedIndividualTag.id());

        // then
        Assertions.assertThat(findTag).isEmpty();
    }
}