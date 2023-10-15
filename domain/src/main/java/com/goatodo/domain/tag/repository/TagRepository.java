package com.goatodo.domain.tag.repository;

import com.goatodo.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE t.member IS NULL")
    List<Tag> findByMember_IdIsNull();

    @Query("SELECT t FROM Tag t WHERE t.member.id = :memberId")
    List<Tag> findByMember_Id(Long memberId);

    @Query("SELECT t FROM Tag t WHERE t.member IS NULL OR t.member.id = :memberId")
    List<Tag> findSelectingTag(Long memberId);

    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.member m WHERE (m.id IS NULL OR m.id = :memberId) AND t.name = :name")
    Optional<Tag> findByMember_IdAndName(Long memberId, String name);

    @Query("SELECT t FROM Tag t WHERE t.member IS NULL AND t.name = :name")
    Optional<Tag> existSameCommonTag(String name);
}
