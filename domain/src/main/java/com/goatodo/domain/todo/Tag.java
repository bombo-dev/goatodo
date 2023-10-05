package com.goatodo.domain.todo;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.RoleException;
import com.goatodo.domain.base.BaseEntity;
import com.goatodo.domain.user.User;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;

    @NotBlank(message = "카테고리는 공백 일 수 없습니다.")
    @Size(max = 20, message = "카테고리는 20자 이내여야 합니다.")
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @NotNull(message = "태그 타입은 null 일 수 없습니다.")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "tag_type", nullable = false)
    private TagType tagType;

    @Builder
    public Tag(User user, String name, TagType tagType) {
        this.user = user;
        this.name = name;
        this.tagType = tagType;
    }

    public static Tag createTag(User user, String name, TagType tagType) {
        return Tag.builder()
                .member(user)
                .name(name)
                .tagType(tagType)
                .build();
    }


    public void validOwn(Long memberId, ErrorCode errorCode) {
        if (memberId.equals(user.getId())) {
            throw new RoleException(errorCode);
        }
    }

    public void validRole(ErrorCode errorCode) {
        if (user != null && user.isNormal() && tagType.isCommonType()) {
            throw new RoleException(errorCode);
        }
    }

    public boolean isCommonCategory() {
        return tagType.isCommonType();
    }

    public Long getMemberId() {
        if (user != null) {
            return user.getId();
        }

        return null;
    }

    public void changeTag(String name) {
        this.name = name;
    }
}
