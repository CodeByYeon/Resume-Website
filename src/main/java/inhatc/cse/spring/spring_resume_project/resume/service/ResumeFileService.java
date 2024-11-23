package inhatc.cse.spring.spring_resume_project.resume.service;

import inhatc.cse.spring.spring_resume_project.resume.entity.ResumeFile;
import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeFileService {
    @Value(value = "${resumeFileLocation}")
    private String resumeFileLocation;

    private final ResumeFileRepository resumeFileRepository;

    private final FileService fileService;

    public void saveResumeFile(ResumeFile resumeFile, MultipartFile resumeFileName) throws IOException{
        String oriFileName = resumeFileName.getOriginalFilename();
        String fileName = "";
        String fileUrl = "";

        if(!StringUtils.isEmpty(oriFileName)){
            fileName = fileService.uploadResume(resumeFileLocation,oriFileName,resumeFileName.getBytes());

            fileUrl = "/files/resume/" + fileName;

            resumeFile.updateResumeFile(oriFileName,fileName,fileUrl);

            resumeFileRepository.save(resumeFile);
        }

    }


}
