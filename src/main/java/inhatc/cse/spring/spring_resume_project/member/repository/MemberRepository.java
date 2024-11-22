package inhatc.cse.spring.spring_resume_project.member.repository;

import inhatc.cse.spring.spring_resume_project.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);

}
