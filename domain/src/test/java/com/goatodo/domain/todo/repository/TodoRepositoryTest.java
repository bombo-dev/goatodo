package com.goatodo.domain.todo.repository;

import com.goatodo.domain.todo.CompleteStatus;
import com.goatodo.domain.todo.Tag;
import com.goatodo.domain.todo.TagType;
import com.goatodo.domain.todo.Todo;
import com.goatodo.domain.user.Account;
import com.goatodo.domain.user.OccupationType;
import com.goatodo.domain.user.Role;
import com.goatodo.domain.user.User;
import com.goatodo.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ComponentScan(basePackages = {"com.goatodo.domain"})
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TagRepository tagRepository;

    private static User user;
    private static Tag tag;

    @BeforeAll
    static void beforeAll() {
        user = initMember();
        tag = initTag();
    }

    @Test
    @DisplayName("Todo 를 정상적으로 생성 할 수 있다.")
    void saveTodoTest() {
        // given
        User savedUser = userRepository.save(user);
        Tag savedTag = tagRepository.save(tag);

        Todo todo = Todo.builder()
                .member(savedUser)
                .tag(savedTag)
                .title("코딩 테스트")
                .description("프로그래머스 문제 풀이")
                .isActive(true)
                .completeStatus(CompleteStatus.READY)
                .build();

        // when
        Todo savedTodo = todoRepository.save(todo);
        Optional<Todo> findTodo = todoRepository.findById(savedTodo.getId());

        // then
        Assertions.assertThat(findTodo).isNotEmpty();
        Assertions.assertThat(findTodo.get().getTag().getName()).isEqualTo("공부");
        Assertions.assertThat(findTodo.get().getUser().getNickname()).isEqualTo("고투두");
    }

    @Test
    @DisplayName("Todo를 정상적으로 단 건 조회 할 수 있다.")
    void findTodoTest() {
        // given
        User savedUser = userRepository.save(user);
        Tag savedTag = tagRepository.save(tag);

        Todo todo = Todo.builder()
                .member(savedUser)
                .tag(savedTag)
                .title("코딩 테스트")
                .description("프로그래머스 문제 풀이")
                .isActive(true)
                .completeStatus(CompleteStatus.READY)
                .build();

        // when
        Todo savedTodo = todoRepository.save(todo);
        Optional<Todo> findTodo = todoRepository.findById(savedTodo.getId());

        // then
        Assertions.assertThat(findTodo).isNotEmpty();
        Assertions.assertThat(findTodo.get().getTag().getName()).isEqualTo("공부");
        Assertions.assertThat(findTodo.get().getUser().getNickname()).isEqualTo("고투두");
    }

    @Test
    @DisplayName("Todo를 전체 조회 할 수 있다.")
    void findAllTodoTest() {
        // given
        Account account = Account.builder()
                .email("email@email.com")
                .password("password1234!")
                .build();

        User adminUser = User.builder()
                .account(account)
                .nickname("운영자")
                .occupation(OccupationType.EMPLOYEE)
                .role(Role.ADMIN)
                .build();

        Tag commonTag = Tag.builder()
                .member(adminUser)
                .tagType(TagType.COMMON)
                .name("스터디")
                .build();

        User savedUserA = userRepository.save(user);
        User savedUserB = userRepository.save(adminUser);
        Tag savedTagA = tagRepository.save(tag);
        Tag savedTagB = tagRepository.save(commonTag);

        Todo todoA = Todo.builder()
                .member(savedUserA)
                .tag(savedTagA)
                .title("코딩 테스트")
                .description("프로그래머스 문제 풀이")
                .isActive(true)
                .completeStatus(CompleteStatus.READY)
                .build();

        Todo todoB = Todo.builder()
                .member(savedUserB)
                .tag(savedTagB)
                .title("디자인 패턴 스터디")
                .description("chapter 10, chapter 11")
                .isActive(true)
                .completeStatus(CompleteStatus.READY)
                .build();

        // when
        todoRepository.save(todoA);
        todoRepository.save(todoB);
        List<Todo> findTodos = todoRepository.findAll();

        // then
        Assertions.assertThat(findTodos).isNotEmpty();
        Assertions.assertThat(findTodos).hasSize(2);
    }

    @Test
    @DisplayName("회원이 생성한 Todo 만을 조회 할 수 있다.")
    void findTodoByMemberIdTest() {
        // given
        Account account = Account.builder()
                .email("email@email.com")
                .password("password1234!")
                .build();

        User adminUser = User.builder()
                .account(account)
                .nickname("운영자")
                .occupation(OccupationType.EMPLOYEE)
                .role(Role.ADMIN)
                .build();

        Tag commonTag = Tag.builder()
                .member(adminUser)
                .tagType(TagType.COMMON)
                .name("스터디")
                .build();

        User savedUserA = userRepository.save(user);
        User savedUserB = userRepository.save(adminUser);
        Tag savedTagA = tagRepository.save(tag);
        Tag savedTagB = tagRepository.save(commonTag);

        Todo todoA = Todo.builder()
                .member(savedUserA)
                .tag(savedTagA)
                .title("코딩 테스트")
                .description("프로그래머스 문제 풀이")
                .isActive(true)
                .completeStatus(CompleteStatus.READY)
                .build();

        Todo todoB = Todo.builder()
                .member(savedUserB)
                .tag(savedTagB)
                .title("디자인 패턴 스터디")
                .description("chapter 10, chapter 11")
                .isActive(true)
                .completeStatus(CompleteStatus.READY)
                .build();

        // when
        todoRepository.save(todoA);
        todoRepository.save(todoB);
        List<Todo> findTodos = todoRepository.findAllByMember_Id(savedUserA.getId());

        // then
        Assertions.assertThat(findTodos).isNotEmpty();
        Assertions.assertThat(findTodos).hasSize(1);
    }

    @Test
    @DisplayName("당일 생성한 Todo만을 조회 할 수 있다.")
    void findTodoDateIsToday() {
        // given
        User savedUser = userRepository.save(user);
        Tag savedTag = tagRepository.save(tag);
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime startToday = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endToday = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        Todo todoA = Todo.builder()
                .member(savedUser)
                .tag(savedTag)
                .title("하루의 시작 약속")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        Todo todoB = Todo.builder()
                .member(savedUser)
                .tag(savedTag)
                .title("하루가 끝나기 바로 전 약속")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        Todo todoC = Todo.builder()
                .member(savedUser)
                .tag(savedTag)
                .title("어제 약속")
                .completeStatus(CompleteStatus.COMPLETE)
                .isActive(true)
                .build();

        Todo savedTodoA = todoRepository.save(todoA);
        savedTodoA.changeCreatedAtForTest(startToday);
        Todo savedTodoB = todoRepository.save(todoB);
        savedTodoB.changeCreatedAtForTest(endToday);
        Todo savedTodoC = todoRepository.save(todoC);
        savedTodoC.changeCreatedAtForTest(yesterday);

        todoRepository.save(savedTodoA);
        todoRepository.save(savedTodoB);
        todoRepository.save(savedTodoC);

        // when
        List<Todo> findTodayTodoList = todoRepository.findAllByMember_idAndDateBetween(savedUser.getId(), startToday, endToday);

        // then
        Assertions.assertThat(findTodayTodoList).hasSize(2);
        Assertions.assertThat(findTodayTodoList).contains(todoA, todoB);
    }

    @Test
    @DisplayName("어제 생성한 Todo만을 조회 할 수 있다.")
    void findTodoDateIsYesterday() {
        // given
        User savedUser = userRepository.save(user);
        Tag savedTag = tagRepository.save(tag);
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime startToday = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endToday = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        LocalDateTime startYesterday = yesterday.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endYesterday = yesterday.withHour(23).withMinute(59).withSecond(59);

        Todo todoA = Todo.builder()
                .member(savedUser)
                .tag(savedTag)
                .title("하루의 시작 약속")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        Todo todoB = Todo.builder()
                .member(savedUser)
                .tag(savedTag)
                .title("하루가 끝나기 바로 전 약속")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        Todo todoC = Todo.builder()
                .member(savedUser)
                .tag(savedTag)
                .title("어제 약속")
                .completeStatus(CompleteStatus.COMPLETE)
                .isActive(true)
                .build();

        Todo savedTodoA = todoRepository.save(todoA);
        savedTodoA.changeCreatedAtForTest(startToday);
        Todo savedTodoB = todoRepository.save(todoB);
        savedTodoB.changeCreatedAtForTest(endToday);
        Todo savedTodoC = todoRepository.save(todoC);
        savedTodoC.changeCreatedAtForTest(yesterday);

        todoRepository.save(savedTodoA);
        todoRepository.save(savedTodoB);
        todoRepository.save(savedTodoC);

        // when
        List<Todo> findYesterDayTodoList = todoRepository.findAllByMember_idAndDateBetween(savedUser.getId(), startYesterday, endYesterday);

        // then
        Assertions.assertThat(findYesterDayTodoList).hasSize(1);
        Assertions.assertThat(findYesterDayTodoList).contains(todoC);
    }

    @Test
    @DisplayName("Todo를 정상적으로 삭제 할 수 있다.")
    void deleteTodoTest() {
        // given
        User savedUser = userRepository.save(user);
        Tag savedTag = tagRepository.save(tag);

        Todo todo = Todo.builder()
                .member(savedUser)
                .tag(savedTag)
                .title("코딩 테스트")
                .description("프로그래머스 문제 풀이")
                .isActive(true)
                .completeStatus(CompleteStatus.READY)
                .build();

        Todo savedTodo = todoRepository.save(todo);
        // when
        todoRepository.delete(savedTodo);
        List<Todo> findTodoList = todoRepository.findAll();

        // then
        Assertions.assertThat(findTodoList).isEmpty();
    }

    static User initMember() {
        Account account = Account.builder()
                .email("goatodo@naver.com")
                .password("password1234!")
                .build();

        return User.builder()
                .account(account)
                .occupation(OccupationType.GENERAL)
                .nickname("고투두")
                .role(Role.ADMIN)
                .build();
    }


    static Tag initTag() {
        return Tag.builder()
                .tagType(TagType.COMMON)
                .name("공부")
                .build();
    }
}