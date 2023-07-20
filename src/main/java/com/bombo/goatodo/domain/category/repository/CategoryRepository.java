package com.bombo.goatodo.domain.category.repository;

import com.bombo.goatodo.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.member IS NULL")
    List<Category> findByMember_IdIsNull();

    @Query("SELECT c FROM Category c WHERE c.member IS NULL OR c.member.id = :id")
    List<Category> findSelectingCategory(Long id);
}
