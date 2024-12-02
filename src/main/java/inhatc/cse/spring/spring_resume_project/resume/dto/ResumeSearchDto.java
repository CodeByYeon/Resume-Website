package inhatc.cse.spring.spring_resume_project.resume.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeSearchDto {
    private String searchDateType;

    private String searchBy;

    private String searchQuery = "";

}
