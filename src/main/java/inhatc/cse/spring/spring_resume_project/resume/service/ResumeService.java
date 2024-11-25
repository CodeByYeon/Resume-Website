package inhatc.cse.spring.spring_resume_project.resume.service;

import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeFormDto;
import inhatc.cse.spring.spring_resume_project.resume.entity.Resume;
import inhatc.cse.spring.spring_resume_project.resume.entity.ResumeFile;
import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeFileRepository;
import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeFileService resumeFileService;
    private final ResumeFileRepository resumeFileRepository;

    public Long saveResume(ResumeFormDto resumeFormDto, List<MultipartFile> resumeFileList) throws Exception{
        Resume resume = resumeFormDto.createResume();
        resumeRepository.save(resume);

        for (int i = 0; i < resumeFileList.size(); i++) {
            ResumeFile resumeFile = new ResumeFile();
            resumeFile.setResume(resume);
            resumeFileService.saveResumeFile(resumeFile,resumeFileList.get(i));
        }
        return resume.getId();
    }
}
