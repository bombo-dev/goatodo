package com.bombo.goatodo.domain.todo;

import com.bombo.goatodo.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @NotBlank(message = "카테고리는 공백 일 수 없습니다.")
    @Size(max = 20, message = "카테고리는 20자 이내여야 합니다.")
    @Column(name = "tag", length = 20, nullable = false)
    private String tag;

    @Builder
    public Category(Long id, Member member, String tag) {
        this.id = id;
        this.member = member;
        this.tag = tag;
    }
}
