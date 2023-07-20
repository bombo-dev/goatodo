package com.bombo.goatodo.domain.todo.repository;

import com.bombo.goatodo.domain.todo.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE t.member IS NULL")
    List<Tag> findByMember_IdIsNull();

    @Query("SELECT t FROM Tag t WHERE t.member.id = :id")
    List<Tag> findByMember_Id(Long id);

    @Query("SELECT t FROM Tag t WHERE t.member IS NULL OR t.member.id = :id")
    List<Tag> findSelectingCategory(Long id);

    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.member m WHERE m.id = :id AND t.name = :name")
    Optional<Tag> existSameMemberCategory(Long id, String name);

    @Query("SELECT t FROM Tag t WHERE t.member IS NULL AND t.name = :name")
    Optional<Tag> existSameCommonCategory(String name);
}
