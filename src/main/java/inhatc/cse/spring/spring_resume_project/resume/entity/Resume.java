package inhatc.cse.spring.spring_resume_project.resume.entity;

import inhatc.cse.spring.spring_resume_project.common.entity.BaseEntity;
import inhatc.cse.spring.spring_resume_project.member.entity.Member;
import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeFormDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resume extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//오토 인크리먼트
    @Column(name = "resume_id")//DB에 저장될 때 숫자
    private Long id;// 기본 키로 만들어 생성

    @ManyToOne(fetch = FetchType.LAZY) //한 명의 멤버가 여러 개의 글을 쓸 수 있기 때문에
    @JoinColumn(name = "member_id") // Member의 ID부분을 조인해서 쓰겠다는 뜻
    private Member member;

    private String nickname;

    private String title;  //제목

    private String contents;  //내용

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "resume",
            cascade = CascadeType.ALL, orphanRemoval = true) // 파일을 여러개 올릴 수 있으니 연결해주고, 게시글 삭제되면 파일도 삭제되도록 설정함
    private List<ResumeFile> resumeFileList = new ArrayList<>();

    public void updateResume(ResumeFormDto resumeFormDto){
        this.title = resumeFormDto.getTitle();
        this.contents = resumeFormDto.getContents();
    }

}
