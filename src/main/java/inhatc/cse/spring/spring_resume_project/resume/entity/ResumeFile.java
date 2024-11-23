package inhatc.cse.spring.spring_resume_project.resume.entity;

import inhatc.cse.spring.spring_resume_project.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeFile extends BaseEntity {
    @Id //기본 키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 숫자 증가 = autoincrement
    @Column(name = "resume_file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String fileName;    //저장된 파일 이름

    private String oriFileName; //원본 파일 이름

    private String fileUrl;     //파일 저장 경로

    public void updateResumeFile(String oriFileName, String fileName, String fileUrl){
        this.oriFileName = oriFileName;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

}
