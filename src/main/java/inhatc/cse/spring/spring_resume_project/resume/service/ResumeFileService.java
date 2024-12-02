package inhatc.cse.spring.spring_resume_project.resume.service;

import inhatc.cse.spring.spring_resume_project.resume.entity.ResumeFile;
import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeFileRepository;
import jakarta.persistence.EntityNotFoundException;
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

        if(!StringUtils.isEmpty(oriFileName)){ //파일 첨부 공간이 비어 있지 않은 경우라면
            fileName = fileService.uploadResumeFile(resumeFileLocation,oriFileName,resumeFileName.getBytes()); //파일 이름을 저장하고

            fileUrl = "/resumeProject/resumeFile/" + fileName; //파일경로를 저장한다.
        }
        resumeFile.updateResumeFile(oriFileName,fileName,fileUrl); //파일 공간 비어있건 안비어있건 정보를 담고
        resumeFileRepository.save(resumeFile); //저장한다

    }

    public void updateResumeFile(Long resumeFileId,MultipartFile resumeFile) throws Exception{
        if(!resumeFile.isEmpty()){
            ResumeFile savedResume = resumeFileRepository.findById(resumeFileId)
                    .orElseThrow(EntityNotFoundException::new);
            if(!StringUtils.isEmpty(savedResume.getFileName())){
                fileService.deleteFile(resumeFileLocation+"/"+savedResume.getFileName());
            }

            String oriFileName = resumeFile.getOriginalFilename();
            String fileName = fileService.uploadResumeFile(resumeFileLocation,oriFileName,resumeFile.getBytes());
            String fileUrl = "/files/resume" + fileName;
            savedResume.updateResumeFile(oriFileName,fileName,fileUrl);
        }
    }

}
