package inhatc.cse.spring.spring_resume_project.fileUpload;

import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeFileRepository;
import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeRepository;
import inhatc.cse.spring.spring_resume_project.resume.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ResumeServiceTest {

    @Autowired
    ResumeService resumeService;

    @Autowired
    ResumeRepository resumeRepository;

    @Autowired
    ResumeFileRepository resumeFileRepository;


}
