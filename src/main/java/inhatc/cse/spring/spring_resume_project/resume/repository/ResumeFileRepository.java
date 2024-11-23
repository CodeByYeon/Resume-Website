package inhatc.cse.spring.spring_resume_project.resume.repository;

import inhatc.cse.spring.spring_resume_project.resume.entity.ResumeFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeFileRepository extends JpaRepository<ResumeFile,Long> {
}
