package com.bombo.goatodo.domain.tag.repository;

import com.bombo.goatodo.domain.member.Account;
import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.Occupation;
import com.bombo.goatodo.domain.member.repository.MemberRepository;
import com.bombo.goatodo.domain.todo.Tag;
import com.bombo.goatodo.domain.todo.TagType;
import com.bombo.goatodo.domain.todo.repository.TagRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TagRepository tagRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        Account account = Account.builder()
                .email("email@email.com")
                .password("password1234")
                .build();

        member = Member.builder()
                .account(account)
                .nickname("닉네임")
                .occupation(Occupation.GENERAL)
                .build();
    }

    @Test
    @DisplayName("공통 카테고리를 만들어 낼 수 있다.")
    void createCategoryNonMemberTest() {
        // given
        Tag tag = Tag.builder()
                .name("수업")
                .build();

        // when
        Tag savedTag = tagRepository.save(tag);
        Optional<Tag> findCategory = tagRepository.findById(savedTag.getId());

        // then
        Assertions.assertThat(findCategory).isNotEmpty();
        Assertions.assertThat(findCategory.get().getMember()).isNull();
        Assertions.assertThat(findCategory.get()).isEqualTo(savedTag);
    }

    @Test
    @DisplayName("회원만의 카테고리를 생성 할 수 있다.")
    void createCategoryWithMemberTest() {
        // given
        Member savedMember = memberRepository.save(member);
        Tag tag = Tag.builder()
                .name("수업")
                .member(savedMember)
                .build();

        // when
        Tag savedTag = tagRepository.save(tag);
        Optional<Tag> findCategory = tagRepository.findById(savedTag.getId());

        // then
        Assertions.assertThat(findCategory).isNotEmpty();
        Assertions.assertThat(findCategory.get().getMember()).isNotNull();
        Assertions.assertThat(findCategory.get()).isEqualTo(savedTag);
    }

    @Test
    @DisplayName("전체 카테고리 정보를 조회할 수 있다.")
    void findAllCategoryTest() {
        // given
        Tag tagA = Tag.builder()
                .name("수업")
                .tagType(TagType.COMMON)
                .build();

        Tag tagB = Tag.builder()
                .name("스터디")
                .tagType(TagType.COMMON)
                .build();

        // when
        tagRepository.save(tagA);
        tagRepository.save(tagB);

        List<Tag> findCategories = tagRepository.findAll();

        // then
        Assertions.assertThat(findCategories).hasSize(2);
    }

    @Test
    @DisplayName("공통 카테고리를 조회 할 수 있다.")
    void findCategoryWithMemberTest() {
        // given
        Tag tagA = Tag.builder()
                .name("수업")
                .tagType(TagType.COMMON)
                .build();

        Tag tagB = Tag.builder()
                .name("스터디")
                .tagType(TagType.COMMON)
                .build();

        Member savedMember = memberRepository.save(member);
        Tag tagC = Tag.builder()
                .name("수업")
                .member(savedMember)
                .tagType(TagType.INDIVIDUAL)
                .build();

        // when
        tagRepository.save(tagA);
        tagRepository.save(tagB);
        tagRepository.save(tagC);
        List<Tag> findCommonCategories = tagRepository.findByMember_IdIsNull();

        // then
        Assertions.assertThat(findCommonCategories).isNotEmpty();
        Assertions.assertThat(findCommonCategories).hasSize(2);
    }

    @Test
    @DisplayName("동일한 공통 카테고리를 조회 할 수 있다.")
    void findCommonCategoryWithTagTest() {
        // given
        Tag tagA = Tag.builder()
                .name("수업")
                .tagType(TagType.COMMON)
                .build();

        Tag tagB = Tag.builder()
                .name("스터디")
                .tagType(TagType.COMMON)
                .build();

        Member savedMember = memberRepository.save(member);
        Tag tagC = Tag.builder()
                .name("수업")
                .member(savedMember)
                .tagType(TagType.INDIVIDUAL)
                .build();

        // when
        tagRepository.save(tagA);
        tagRepository.save(tagB);
        tagRepository.save(tagC);

        Optional<Tag> findCategory = tagRepository.existSameCommonCategory("수업");

        // then
        Assertions.assertThat(findCategory).isNotEmpty();
    }

    @Test
    @DisplayName("회원이 선택할 카테고리를 조회 할 수 있다.")
    void findCategoryForSelectTest() {
        // given
        Tag tagA = Tag.builder()
                .name("수업")
                .tagType(TagType.COMMON)
                .build();

        Tag tagB = Tag.builder()
                .name("스터디")
                .tagType(TagType.COMMON)
                .build();

        Member savedMember = memberRepository.save(member);
        Tag tagC = Tag.builder()
                .name("수업")
                .member(savedMember)
                .tagType(TagType.INDIVIDUAL)
                .build();

        // when
        tagRepository.save(tagA);
        tagRepository.save(tagB);
        tagRepository.save(tagC);
        List<Tag> findCommonCategories = tagRepository.findSelectingCategory(savedMember.getId());

        // then
        Assertions.assertThat(findCommonCategories).isNotEmpty();
        Assertions.assertThat(findCommonCategories).hasSize(3);
    }

    @Test
    @DisplayName("회원이 생성한 카테고리를 조회 할 수 있다.")
    void findCategoriesByMember() {
        // given
        Member savedMember = memberRepository.save(member);

        Tag tagA = Tag.builder()
                .name("수업")
                .tagType(TagType.COMMON)
                .build();

        Tag tagB = Tag.builder()
                .name("스터디")
                .tagType(TagType.COMMON)
                .build();

        Tag tagC = Tag.builder()
                .name("일상")
                .member(savedMember)
                .tagType(TagType.INDIVIDUAL)
                .build();

        Tag tagD = Tag.builder()
                .name("청소")
                .member(savedMember)
                .tagType(TagType.INDIVIDUAL)
                .build();

        tagRepository.save(tagA);
        tagRepository.save(tagB);
        tagRepository.save(tagC);
        tagRepository.save(tagD);

        // when
        List<Tag> findCategories = tagRepository.findByMember_Id(savedMember.getId());

        // then
        Assertions.assertThat(findCategories).hasSize(2);

    }

    @Test
    @DisplayName("회원이 생성한 카테고리를 아이디와 이름으로 조회 할 수 있다.")
    void findCategoryByTagAndMemberIdTest() {
        // given
        Member savedMember = memberRepository.save(member);
        Tag tag = Tag.builder()
                .name("일상")
                .member(savedMember)
                .tagType(TagType.INDIVIDUAL)
                .build();

        // when
        tagRepository.save(tag);
        Optional<Tag> findCategory = tagRepository.existSameMemberCategory(savedMember.getId(), tag.getName());

        // then
        Assertions.assertThat(findCategory).isNotEmpty();
    }

    @Test
    @DisplayName("회원이 생성한 카테고리를 삭제 할 수 있다.")
    void deleteCategoryTag() {
        // given
        Member savedMember = memberRepository.save(member);
        Tag tag = Tag.builder()
                .name("일상")
                .member(savedMember)
                .tagType(TagType.INDIVIDUAL)
                .build();

        // when
        Tag savedTag = tagRepository.save(tag);

        tagRepository.delete(savedTag);
        Optional<Tag> findCategory = tagRepository.findById(savedTag.getId());

        // then
        Assertions.assertThat(findCategory).isEmpty();
    }
}