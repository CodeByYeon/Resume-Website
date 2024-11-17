package inhatc.cse.spring.spring_resume_project.member.service;

import inhatc.cse.spring.spring_resume_project.member.entity.Member;
import inhatc.cse.spring.spring_resume_project.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        Member newMember = memberRepository.save(member);
        return newMember;
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> mem = memberRepository.findByEmail(member.getEmail());
    }
}
