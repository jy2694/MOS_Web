package kr.mos1981.mosweb.repository;

import kr.mos1981.mosweb.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
