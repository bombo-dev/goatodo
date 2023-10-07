package com.goatodo.domain.retrospect;

import com.goatodo.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "retrospects",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_USER_IDAND_NAME",
                        columnNames = {"user_id", "name"}
                )
        })
@Entity
public class Retrospect extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", length = 35, nullable = false)
    private String title;

    @Lob
    private String content;

    @Column(name = "write_date", nullable = false)
    private LocalDate writeDate;

    @Builder
    public Retrospect(Long userId, String title, String content, LocalDate writeDate) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(title);
        Objects.requireNonNull(content);
        Objects.requireNonNull(writeDate);
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.writeDate = writeDate;
    }
}
