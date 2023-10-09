package com.goatodo.domain.user.repository;

import com.goatodo.domain.user.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {

    @Query("""
            SELECT L
            FROM Level L
            WHERE L.id = :levelId + 1
            """)
    Optional<Level> findNextLevelById(Long levelId);

    @Query("""
            SELECT L
            FROM Level L
            WHERE L.id = :levelId - 1
            """)
    Optional<Level> findPreLevelById(Long levelId);
}
