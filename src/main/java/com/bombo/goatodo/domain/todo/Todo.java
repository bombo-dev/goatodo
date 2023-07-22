package com.bombo.goatodo.domain.todo;

import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.global.BaseEntity;
import com.bombo.goatodo.util.BooleanToYNConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Todo 작성 시 회원은 필수 입니다.")
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull(message = "Todo 작성 시 태그는 필수 입니다.")
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Tag tag;

    @NotBlank(message = "Todo 작성 시 제목은 공백이거나 null 이면 안됩니다.")
    @Size(max = 20, message = "Todo의 제목의 길이는 20자를 초과 할 수 없습니다.")
    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Size(max = 50, message = "Todo의 설명은 50자를 초과 할 수 없습니다.")
    @Column(name = "description", length = 50)
    private String description;

    @NotNull(message = "Todo 진행 상태는 null 일 수 없습니다.")
    @Enumerated
    @Column(name = "complete_status", nullable = false)
    private CompleteStatus completeStatus;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "is_active", length = 1, nullable = false)
    private boolean isActive;

    @Builder
    public Todo(@NotNull Member member,
                @NotNull Tag tag,
                @NotBlank String title,
                @NotNull CompleteStatus completeStatus,
                String description,
                boolean isActive) {
        this.member = member;
        this.tag = tag;
        this.title = title;
        this.description = description;
        this.completeStatus = completeStatus;
        this.isActive = isActive;
    }
}