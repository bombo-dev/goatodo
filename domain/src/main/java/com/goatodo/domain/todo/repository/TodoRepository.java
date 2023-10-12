package com.goatodo.domain.todo.repository;

import com.goatodo.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t WHERE t.userId = :userId")
    List<Todo> findAllByMember_Id(Long userId);

    @Query("SELECT t FROM Todo t WHERE t.userId = :userId AND t.createdAt BETWEEN :startLocalDateTime AND :endLocalDateTime")
    List<Todo> findAllByMember_idAndDateBetween(Long userId, LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime);
}
