package com.goatodo.domain.tag;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.domain.base.BaseEntity;
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
                        columnNames = {"user_id", "name"}
                )
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Builder
    public Tag(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public static Tag createTag(Long userId, String name) {
        return Tag.builder()
                .userId(userId)
                .name(name)
                .build();
    }


    public void validOwn(Long userId) {
        if (!userId.equals(this.userId)) {
            throw new IllegalStateException(ErrorCode.FORBIDDEN_EXCEPTION.name() + "- userId : " + userId);
        }
    }

    public void changeName(String name) {
        this.name = name;
    }
}
