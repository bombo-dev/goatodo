package com.bombo.goatodo.common;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class CreatedBaseEntity {

    @CreatedDate
    protected LocalDateTime createdAt;

    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
