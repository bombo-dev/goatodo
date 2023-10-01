package com.goatodo.common.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t WHERE t.member.id = :memberId")
    List<Todo> findAllByMember_Id(Long memberId);

    @Query("SELECT t FROM Todo t WHERE t.member.id = :memberId AND t.createdAt BETWEEN :startLocalDateTime AND :endLocalDateTime")
    List<Todo> findAllByMember_idAndDateBetween(Long memberId, LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime);
}
