package com.bombo.goatodo.domain.member.repository;

import com.bombo.goatodo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
