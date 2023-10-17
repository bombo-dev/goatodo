package com.goatodo.domain.occupation.repository;

import com.goatodo.domain.occupation.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OccupationRepository extends JpaRepository<Occupation, Long> {
}
