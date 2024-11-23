package inhatc.cse.spring.spring_resume_project.resume.service;

import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeDto;
import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;

    public void uploadResume(String uploadPath,String originalFileName,
                             byte[] fileData) throws IOException {
        UUID uuid = UUID.randomUUID(); //파일명을 랜덤하게 바꿔주는 것
        String ext =originalFileName.substring(originalFileName.lastIndexOf("."),originalFileName.length());
        String savedFileName = uuid.toString() + ext; //파일 저장 시 파일이름 + 확장자 식으로 저장
        String fileUploadFullPath = uploadPath + "/" + savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullPath);
        fos.write(fileData);
        fos.close();
    }

}
