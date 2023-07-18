package com.bombo.goatodo.domain.member.repository;

import com.bombo.goatodo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.nickname = :nickname WHERE m.id = :id")
    void updateNickname(String nickname, Long id);
}
