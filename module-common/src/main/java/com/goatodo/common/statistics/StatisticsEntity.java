package com.goatodo.common.statistics;


import com.goatodo.common.base.CreatedBaseEntity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class StatisticsEntity extends CreatedBaseEntity {

    private double percent;

}
