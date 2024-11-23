package inhatc.cse.spring.spring_resume_project.resume.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileService {
    public String uploadResume(String uploadPath,String originalFileName,
                             byte[] fileData) throws IOException {
        UUID uuid = UUID.randomUUID(); //파일명을 랜덤하게 바꿔주는 것
        String ext =originalFileName.substring(originalFileName.lastIndexOf("."),originalFileName.length());
        String savedFileName = uuid.toString() + ext; //파일 저장 시 파일이름 + 확장자 식으로 저장
        String fileUploadFullPath = uploadPath + "/" + savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullPath);
        fos.write(fileData);
        fos.close();

        return savedFileName;

    }
    public void deleteFile(String filePath){
        File deleteFile = new File(filePath);
        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제했습니다.");
        }else {
            log.info(filePath + "파일이 존재하지 않습니다.");
        }
    }
}
