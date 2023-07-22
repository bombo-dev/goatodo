package com.bombo.goatodo.domain.todo;

import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.global.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uniqueMemberNameType",
                        columnNames = {"name", "tag_type"}
                )
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @NotBlank(message = "카테고리는 공백 일 수 없습니다.")
    @Size(max = 20, message = "카테고리는 20자 이내여야 합니다.")
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @NotNull(message = "태그 타입은 null 일 수 없습니다.")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "tag_type", nullable = false)
    private TagType tagType;

    @Builder
    public Tag(Member member, String name, @NotNull TagType tagType) {
        this.member = member;
        this.name = name;
        this.tagType = tagType;
    }

    public boolean isOwnTag(Member member) {
        return this.member.isSameMember(member.getAccount());
    }

    public boolean isCommonCategory() {
        return this.tagType == TagType.COMMON;
    }

    public Long getMemberId() {
        if (this.member != null) {
            return this.member.getId();
        }

        return null;
    }

    public void changeTag(String tag) {
        this.name = tag;
    }
}
