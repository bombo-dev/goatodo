package com.bombo.goatodo.domain.category.repository;

import com.bombo.goatodo.domain.category.Category;
import com.bombo.goatodo.domain.member.Account;
import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.Occupation;
import com.bombo.goatodo.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
        Category category = Category.builder()
                .tag("수업")
                .build();

        // when
        Category savedCategory = categoryRepository.save(category);
        Optional<Category> findCategory = categoryRepository.findById(savedCategory.getId());

        // then
        Assertions.assertThat(findCategory).isNotEmpty();
        Assertions.assertThat(findCategory.get().getMember()).isNull();
        Assertions.assertThat(findCategory.get()).isEqualTo(savedCategory);
    }

    @Test
    @DisplayName("회원만의 카테고리를 생성 할 수 있다.")
    void createCategoryWithMemberTest() {
        // given
        Member savedMember = memberRepository.save(member);
        Category category = Category.builder()
                .tag("수업")
                .member(savedMember)
                .build();

        // when
        Category savedCategory = categoryRepository.save(category);
        Optional<Category> findCategory = categoryRepository.findById(savedCategory.getId());

        // then
        Assertions.assertThat(findCategory).isNotEmpty();
        Assertions.assertThat(findCategory.get().getMember()).isNotNull();
        Assertions.assertThat(findCategory.get()).isEqualTo(savedCategory);
    }

    @Test
    @DisplayName("전체 카테고리 정보를 조회할 수 있다.")
    void findAllCategoryTest() {
        // given
        Category categoryA = Category.builder()
                .tag("수업")
                .build();

        Category categoryB = Category.builder()
                .tag("스터디")
                .build();

        // when
        categoryRepository.save(categoryA);
        categoryRepository.save(categoryB);

        List<Category> findCategories = categoryRepository.findAll();

        // then
        Assertions.assertThat(findCategories).hasSize(2);
    }

    @Test
    @DisplayName("공통 카테고리를 조회 할 수 있다.")
    void findCategoryWithMemberTest() {
        // given
        Category categoryA = Category.builder()
                .tag("수업")
                .build();

        Category categoryB = Category.builder()
                .tag("스터디")
                .build();

        Member savedMember = memberRepository.save(member);
        Category categoryC = Category.builder()
                .tag("수업")
                .member(savedMember)
                .build();

        // when
        categoryRepository.save(categoryA);
        categoryRepository.save(categoryB);
        categoryRepository.save(categoryC);
        List<Category> findCommonCategories = categoryRepository.findByMember_IdIsNull();

        // then
        Assertions.assertThat(findCommonCategories).isNotEmpty();
        Assertions.assertThat(findCommonCategories).hasSize(2);
    }

    @Test
    @DisplayName("회원이 선택할 카테고리를 조회 할 수 있다.")
    void findCategoryForSelectTest() {
        // given
        Category categoryA = Category.builder()
                .tag("수업")
                .build();

        Category categoryB = Category.builder()
                .tag("스터디")
                .build();

        Member savedMember = memberRepository.save(member);
        Category categoryC = Category.builder()
                .tag("일상")
                .member(savedMember)
                .build();

        // when
        categoryRepository.save(categoryA);
        categoryRepository.save(categoryB);
        categoryRepository.save(categoryC);
        List<Category> findCommonCategories = categoryRepository.findSelectingCategory(savedMember.getId());

        // then
        Assertions.assertThat(findCommonCategories).isNotEmpty();
        Assertions.assertThat(findCommonCategories).hasSize(3);
    }

    @Test
    @DisplayName("회원이 생성한 카테고리를 삭제 할 수 있다.")
    void deleteCategoryTag() {
        // given
        Member savedMember = memberRepository.save(member);
        Category category = Category.builder()
                .tag("일상")
                .member(savedMember)
                .build();

        // when
        Category savedCategory = categoryRepository.save(category);

        categoryRepository.delete(savedCategory);
        Optional<Category> findCategory = categoryRepository.findById(savedCategory.getId());

        // then
        Assertions.assertThat(findCategory).isEmpty();
    }
}