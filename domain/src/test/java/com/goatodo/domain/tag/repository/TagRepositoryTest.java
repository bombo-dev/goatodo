package com.goatodo.domain.tag.repository;

import com.goatodo.domain.todo.Tag;
import com.goatodo.domain.todo.TagType;
import com.goatodo.domain.todo.repository.TagRepository;
import com.goatodo.domain.user.Account;
import com.goatodo.domain.user.Occupation;
import com.goatodo.domain.user.Role;
import com.goatodo.domain.user.User;
import com.goatodo.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ComponentScan(basePackages = {"com.goatodo.domain"})
class TagRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    private User user;

    @BeforeEach
    void setUp() {
        Account account = Account.builder()
                .email("email@email.com")
                .password("password1234")
                .build();

        user = User.builder()
                .account(account)
                .nickname("닉네임")
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();
    }

    @Test
    @DisplayName("공통 태그를 만들어 낼 수 있다.")
    void createTagNonMemberTest() {
        // given
        Tag tag = Tag.builder()
                .name("수업")
                .tagType(TagType.COMMON)
                .build();

        // when
        Tag savedTag = tagRepository.save(tag);
        Optional<Tag> findTag = tagRepository.findById(savedTag.getId());

        // then
        Assertions.assertThat(findTag).isNotEmpty();
        Assertions.assertThat(findTag.get().getUser()).isNull();
        Assertions.assertThat(findTag.get()).isEqualTo(savedTag);
    }

    @Test
    @DisplayName("회원만의 태그를 생성 할 수 있다.")
    void createTagWithMemberTest() {
        // given
        User savedUser = userRepository.save(user);
        Tag tag = Tag.builder()
                .name("수업")
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        // when
        Tag savedTag = tagRepository.save(tag);
        Optional<Tag> findTag = tagRepository.findById(savedTag.getId());

        // then
        Assertions.assertThat(findTag).isNotEmpty();
        Assertions.assertThat(findTag.get().getUser()).isNotNull();
        Assertions.assertThat(findTag.get()).isEqualTo(savedTag);
    }

    @Test
    @DisplayName("전체 태그 정보를 조회할 수 있다.")
    void findAllTagTest() {
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
    @DisplayName("공통 태그를 조회 할 수 있다.")
    void findTagWithMemberTest() {
        // given
        Tag tagA = Tag.builder()
                .name("수업")
                .tagType(TagType.COMMON)
                .build();

        Tag tagB = Tag.builder()
                .name("스터디")
                .tagType(TagType.COMMON)
                .build();

        User savedUser = userRepository.save(user);
        Tag tagC = Tag.builder()
                .name("수업")
                .member(savedUser)
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
    @DisplayName("동일한 공통 태그를 조회 할 수 있다.")
    void findCommonTagWithTagTest() {
        // given
        Tag tagA = Tag.builder()
                .name("수업")
                .tagType(TagType.COMMON)
                .build();

        Tag tagB = Tag.builder()
                .name("스터디")
                .tagType(TagType.COMMON)
                .build();

        User savedUser = userRepository.save(user);
        Tag tagC = Tag.builder()
                .name("수업")
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        // when
        tagRepository.save(tagA);
        tagRepository.save(tagB);
        tagRepository.save(tagC);

        Optional<Tag> findTag = tagRepository.existSameCommonTag("수업");

        // then
        Assertions.assertThat(findTag).isNotEmpty();
    }

    @Test
    @DisplayName("회원이 선택할 태그를 조회 할 수 있다.")
    void findTagForSelectTest() {
        // given
        Tag tagA = Tag.builder()
                .name("수업")
                .tagType(TagType.COMMON)
                .build();

        Tag tagB = Tag.builder()
                .name("스터디")
                .tagType(TagType.COMMON)
                .build();

        User savedUser = userRepository.save(user);
        Tag tagC = Tag.builder()
                .name("수업")
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        // when
        tagRepository.save(tagA);
        tagRepository.save(tagB);
        tagRepository.save(tagC);
        List<Tag> findCommonCategories = tagRepository.findSelectingTag(savedUser.getId());

        // then
        Assertions.assertThat(findCommonCategories).isNotEmpty();
        Assertions.assertThat(findCommonCategories).hasSize(3);
    }

    @Test
    @DisplayName("회원이 생성한 태그를 조회 할 수 있다.")
    void findCategoriesByMember() {
        // given
        User savedUser = userRepository.save(user);

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
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        Tag tagD = Tag.builder()
                .name("청소")
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        tagRepository.save(tagA);
        tagRepository.save(tagB);
        tagRepository.save(tagC);
        tagRepository.save(tagD);

        // when
        List<Tag> findCategories = tagRepository.findByMember_Id(savedUser.getId());

        // then
        Assertions.assertThat(findCategories).hasSize(2);
    }

    @Test
    @DisplayName("회원의 태그 생성 시 중복 된 공통 태그를 확인 할 수 있다.")
    void findDuplicatedCommonTagTest() {
        // given
        User savedUser = userRepository.save(user);

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
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        Tag tagD = Tag.builder()
                .name("청소")
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        tagRepository.save(tagA);
        tagRepository.save(tagB);
        tagRepository.save(tagC);
        tagRepository.save(tagD);

        // when
        Optional<Tag> findTag = tagRepository.findByMember_IdAndName(savedUser.getId(), "수업");

        // then
        Assertions.assertThat(findTag).isNotEmpty();
    }

    @Test
    @DisplayName("회원의 태그 생성 시 중복 된 회원 태그를 확인 할 수 있다.")
    void findDuplicatedMemberTagTest() {
        // given
        User savedUser = userRepository.save(user);

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
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        Tag tagD = Tag.builder()
                .name("청소")
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        tagRepository.save(tagA);
        tagRepository.save(tagB);
        tagRepository.save(tagC);
        tagRepository.save(tagD);

        // when
        Optional<Tag> findTag = tagRepository.findByMember_IdAndName(savedUser.getId(), "일상");

        // then
        Assertions.assertThat(findTag).isNotEmpty();
    }

    @Test
    @DisplayName("회원이 생성한 태그를 아이디와 이름으로 조회 할 수 있다.")
    void findTagByTagAndMemberIdTest() {
        // given
        User savedUser = userRepository.save(user);
        Tag tag = Tag.builder()
                .name("일상")
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        // when
        tagRepository.save(tag);
        Optional<Tag> findtag = tagRepository.findByMember_IdAndName(savedUser.getId(), tag.getName());

        // then
        Assertions.assertThat(findtag).isNotEmpty();
    }

    @Test
    @DisplayName("회원이 생성한 태그를 삭제 할 수 있다.")
    void deleteTagTag() {
        // given
        User savedUser = userRepository.save(user);
        Tag tag = Tag.builder()
                .name("일상")
                .member(savedUser)
                .tagType(TagType.INDIVIDUAL)
                .build();

        // when
        Tag savedTag = tagRepository.save(tag);

        tagRepository.delete(savedTag);
        Optional<Tag> findTag = tagRepository.findById(savedTag.getId());

        // then
        Assertions.assertThat(findTag).isEmpty();
    }
}