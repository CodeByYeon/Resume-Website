package inhatc.cse.spring.spring_resume_project.member.entity;

import inhatc.cse.spring.spring_resume_project.common.entity.BaseEntity;
import inhatc.cse.spring.spring_resume_project.member.constant.Role;
import inhatc.cse.spring.spring_resume_project.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//오토 인크리먼트 만드는것임
    @Column(name = "member_id")//DB에 저장될 때 이름
    private Long id;// 기본 키로 만들어 생성

    @Column(unique = true, length = 30) //유니크 키, 30자가 최대치임
    private String nickname;

    @Column(nullable = false, length = 30) //널 가능한지, 30자가 최대치임
    private String name;

    @Column(nullable = false,length = 200) //암호화 걸리면 엄청 길어지기 때문에 200자
    private String password;

    @Column(unique = true, length = 50) //유니크 키로 설정하는 어노테이션
    private String email;

    @Enumerated(EnumType.STRING)//Enum이라는게 숫자를 가독성 좋게 만드는건데 문자열형태로 저장하게 만들어주는 코드임
    private Role role;

    public static Member createMember(MemberDto memberDto, PasswordEncoder passwordEncoder){

        Member member = Member.builder()
                .nickname(memberDto.getNickname())
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .role(Role.USER) //기본 유저의 권한을 유저로 설정한다
                .build();

        return member;
    }
}
