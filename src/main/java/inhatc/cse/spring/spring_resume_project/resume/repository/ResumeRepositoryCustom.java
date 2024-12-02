package inhatc.cse.spring.spring_resume_project.resume.repository;

import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeSearchDto;
import inhatc.cse.spring.spring_resume_project.resume.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumeRepositoryCustom {

    Page<Resume> getResumePage(ResumeSearchDto resumeSearchDto, Pageable pageable);


}
