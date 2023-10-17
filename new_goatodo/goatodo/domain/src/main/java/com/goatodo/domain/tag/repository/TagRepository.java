package com.goatodo.domain.tag.repository;

import com.goatodo.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE t.userId IS NULL")
    List<Tag> findByMember_IdIsNull();

    @Query("SELECT t FROM Tag t WHERE t.userId = :userId")
    List<Tag> findByMember_Id(Long userId);

    @Query("SELECT t FROM Tag t WHERE t.userId IS NULL OR t.userId = :userId")
    List<Tag> findSelectingTag(Long userId);

    @Query("SELECT t FROM Tag t WHERE t.userId IS NULL AND t.name = :name")
    Optional<Tag> existSameCommonTag(String name);
}
