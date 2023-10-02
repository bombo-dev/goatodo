package com.goatodo.domain.statistics.repository;

import com.goatodo.domain.statistics.StatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<StatisticsEntity, Long> {
}
