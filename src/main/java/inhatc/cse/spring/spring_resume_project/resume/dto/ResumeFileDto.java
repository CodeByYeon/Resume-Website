package inhatc.cse.spring.spring_resume_project.resume.dto;

import inhatc.cse.spring.spring_resume_project.resume.entity.ResumeFile;
import lombok.*;
import org.modelmapper.ModelMapper;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeFileDto {

    private Long id;


    private String fileName;    //저장된 파일 이름

    private String oriFileName; //원본 파일 이름

    private String fileUrl;     //파일 저장 경로

    private static ModelMapper modelMapper = new ModelMapper();

    public static ResumeFileDto of(ResumeFile resumeFile){
        return modelMapper.map(resumeFile, ResumeFileDto.class);
    }

}
