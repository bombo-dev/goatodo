package com.bombo.goatodo.domain.todo.repository;

import com.bombo.goatodo.domain.todo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.member IS NULL")
    List<Category> findByMember_IdIsNull();

    @Query("SELECT c FROM Category c WHERE c.member IS NULL OR c.member.id = :id")
    List<Category> findSelectingCategory(Long id);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.member m WHERE m.id = :id AND c.tag = :tag")
    Optional<Category> findByMemberIdAndTag(Long id, String tag);
}
