package inhatc.cse.spring.spring_resume_project.resume.dto;

import inhatc.cse.spring.spring_resume_project.resume.entity.Resume;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumeFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 사항입니다.")
    private String contents;

    private List<ResumeFileDto> resumeFileDtoList = new ArrayList<>();  //이력서 파일 리스트

    private List<Long> resumeFileIds = new ArrayList<>();               //이력서들의 ID를 저장

    private static ModelMapper modelMapper = new ModelMapper();

    public Resume createResume(){
        return modelMapper.map(this,Resume.class);
    }

    public ResumeFormDto of(Resume resume){
        return modelMapper.map(this,ResumeFormDto.class);
    }




}
