package inhatc.cse.spring.spring_resume_project.member;

import inhatc.cse.spring.spring_resume_project.member.dto.MemberDto;
import inhatc.cse.spring.spring_resume_project.member.entity.Member;
import inhatc.cse.spring.spring_resume_project.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member createMember() {
        MemberDto memberDto = MemberDto.builder()
                .nickname("군산 피바다")
                .email("test@test.com")
                .password("1111")
                .build();
        System.out.println(memberDto);
        Member member = Member.createMember(memberDto, passwordEncoder);
        return member;
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void saveMemberTest() {
        Member member = createMember();
        Member saveMember = memberService.saveMember(member);
        assertEquals(member.getEmail(), saveMember.getEmail());
    }
}
