package inhatc.cse.spring.spring_resume_project.member.service;

import inhatc.cse.spring.spring_resume_project.member.entity.Member;
import inhatc.cse.spring.spring_resume_project.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        Member newMember = memberRepository.save(member);
        log.info("저장된 신규 회원 : " + newMember);
        return newMember;
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> mem = memberRepository.findByEmail(member.getEmail());
        if(mem.isPresent()){
            Member m = mem.get();
            System.out.println(m);
            throw new IllegalStateException("이미 가입된 회원입니다."); //예외처리 발생 시 경고문을 출력하고 다음 코드를 실행하지 않음
        }


    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다." + email));

        log.info(member.toString());

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
