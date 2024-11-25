package inhatc.cse.spring.spring_resume_project.resume.repository;

import inhatc.cse.spring.spring_resume_project.resume.entity.ResumeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeFileRepository extends JpaRepository<ResumeFile,Long> {
    List<ResumeFile> findByResumeIdOrderByIdAsc(Long resumeId);

}
