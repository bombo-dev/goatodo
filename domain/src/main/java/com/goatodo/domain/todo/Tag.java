package com.goatodo.domain.todo;

import com.goatodo.domain.base.BaseEntity;
import com.goatodo.domain.member.Member;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tag_type", nullable = false)
    private TagType tagType;

    @Builder
    public Tag(Member member, String name, TagType tagType) {
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

    public void changeTag(String name) {
        this.name = name;
    }
}
