package com.goatodo.domain.todo;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.RoleException;
import com.goatodo.domain.base.BaseEntity;
import com.goatodo.domain.todo.exception.NotActiveException;
import com.goatodo.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Todo 작성 시 회원은 필수 입니다.")
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private User user;

    @NotNull(message = "Todo 작성 시 태그는 필수 입니다.")
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @NotBlank(message = "Todo 작성 시 제목은 공백이거나 null 이면 안됩니다.")
    @Size(max = 20, message = "Todo의 제목의 길이는 20자를 초과 할 수 없습니다.")
    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Size(max = 50, message = "Todo의 설명은 50자를 초과 할 수 없습니다.")
    @Column(name = "description", length = 50)
    private String description;

    @NotNull(message = "Todo 진행 상태는 null 일 수 없습니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "complete_status", nullable = false)
    private CompleteStatus completeStatus;

    @Column(name = "is_active", length = 1, nullable = false)
    private boolean isActive;

    @Builder
    public Todo(User user,
                Tag tag,
                String title,
                CompleteStatus completeStatus,
                String description,
                boolean isActive) {
        this.user = user;
        this.tag = tag;
        this.title = title;
        this.description = description;
        this.completeStatus = completeStatus;
        this.isActive = isActive;
    }

    public static Todo createTodo(User user, Tag tag, String title, String description) {
        return Todo.builder()
                .member(user)
                .tag(tag)
                .title(title)
                .description(description)
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();
    }

    public Long getMemberId() {
        return user.getId();
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void validOwn(Long memberId, ErrorCode errorCode) {
        if (!memberId.equals(user.getId())) {
            throw new RoleException(errorCode);
        }
    }

    public void validActive() {
        if (!isActive) {
            throw new NotActiveException(ErrorCode.NOT_ACTIVE);
        }
    }

    public void updateTodo(Todo todo) {
        this.tag = todo.getTag();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
    }

    public void changeCompleteStatus(CompleteStatus completeStatus) {
        this.completeStatus = completeStatus;
    }

    public void changeCreatedAtForTest(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
