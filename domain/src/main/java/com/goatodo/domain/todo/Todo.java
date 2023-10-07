package com.goatodo.domain.todo;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.RoleException;
import com.goatodo.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Column(name = "description", length = 50)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CompleteStatus completeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @Builder
    public Todo(Long userId,
                Tag tag,
                String title,
                CompleteStatus completeStatus,
                Difficulty difficulty,
                String description
    ) {
        this.userId = userId;
        this.tag = tag;
        this.title = title;
        this.difficulty = difficulty;
        this.description = description;
        this.completeStatus = completeStatus;
    }

    public static Todo createTodo(
            Long userId,
            Tag tag,
            String title,
            String description,
            Difficulty difficulty
    ) {
        return Todo.builder()
                .userId(userId)
                .tag(tag)
                .title(title)
                .description(description)
                .difficulty(difficulty)
                .completeStatus(CompleteStatus.READY)
                .build();
    }

    public void validOwn(Long userId, ErrorCode errorCode) {
        if (!userId.equals(this.userId)) {
            throw new RoleException(errorCode);
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
}
