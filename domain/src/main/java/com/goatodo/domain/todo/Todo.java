package com.goatodo.domain.todo;

import com.goatodo.domain.base.BaseEntity;
import com.goatodo.domain.member.Member;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Column(name = "description", length = 50)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "complete_status", nullable = false)
    private CompleteStatus completeStatus;

    @Column(name = "is_active", length = 1, nullable = false)
    private boolean isActive;

    @Builder
    public Todo(Member member,
                Tag tag,
                String title,
                CompleteStatus completeStatus,
                String description,
                boolean isActive) {
        this.member = member;
        this.tag = tag;
        this.title = title;
        this.description = description;
        this.completeStatus = completeStatus;
        this.isActive = isActive;
    }

    public Long getMemberId() {
        return this.member.getId();
    }

    public boolean getActive() {
        return this.isActive;
    }

    public boolean isOwnTodo(Member member) {
        return this.member.isSameMember(member.getAccount());
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
