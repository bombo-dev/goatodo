package com.bombo.goatodo.domain.category;

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

    @NotBlank
    @Size(min = 2, max = 20)
    @Column(name = "tag", length = 20, nullable = false)
    private String tag;

    @Builder
    public Category(Long id, Member member, String tag) {
        this.id = id;
        this.member = member;
        this.tag = tag;
    }
}
