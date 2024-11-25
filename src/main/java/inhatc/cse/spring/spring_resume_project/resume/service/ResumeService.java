package inhatc.cse.spring.spring_resume_project.resume.service;

import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeFileDto;
import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeFormDto;
import inhatc.cse.spring.spring_resume_project.resume.entity.Resume;
import inhatc.cse.spring.spring_resume_project.resume.entity.ResumeFile;
import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeFileRepository;
import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @Transactional(readOnly = true) //이력서 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정.
    public ResumeFormDto getResumeDtl(Long resumeId){
        List<ResumeFile> resumeFileList = resumeFileRepository.findByResumeIdOrderByIdAsc(resumeId);
        List<ResumeFileDto> resumeFileDtoList = new ArrayList<>();
        for(ResumeFile resumeFile : resumeFileList){
            ResumeFileDto resumeFileDto = ResumeFileDto.of(resumeFile);
            resumeFileDtoList.add(resumeFileDto);
        }

        Resume resume = resumeRepository.findById(resumeId).orElseThrow(EntityNotFoundException::new);
        ResumeFormDto resumeFormDto = ResumeFormDto.of(resume);
        resumeFormDto.setResumeFileDtoList(resumeFileDtoList);
        return resumeFormDto;
    }
}
