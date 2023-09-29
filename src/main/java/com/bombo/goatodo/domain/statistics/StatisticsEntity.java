package com.bombo.goatodo.domain.statistics;


import com.bombo.goatodo.global.CreatedBaseEntity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class StatisticsEntity extends CreatedBaseEntity {

    private double percent;

    public double getPercent() {
        return percent;
    }
}
