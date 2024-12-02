package inhatc.cse.spring.spring_resume_project.resume.service;

import inhatc.cse.spring.spring_resume_project.member.entity.Member;
import inhatc.cse.spring.spring_resume_project.member.repository.MemberRepository;
import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeFileDto;
import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeFormDto;
import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeSearchDto;
import inhatc.cse.spring.spring_resume_project.resume.entity.Resume;
import inhatc.cse.spring.spring_resume_project.resume.entity.ResumeFile;
import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeFileRepository;
import inhatc.cse.spring.spring_resume_project.resume.repository.ResumeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final MemberRepository memberRepository;


    public Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public Long saveResume(ResumeFormDto resumeFormDto,
                           List<MultipartFile> resumeFileList,
                           Member member) throws Exception{
        Resume resume = resumeFormDto.createResume(member);
        resumeRepository.save(resume);

        for (int i = 0; i < resumeFileList.size(); i++) {
            ResumeFile resumeFile = new ResumeFile();
            resumeFile.setResume(resume);
            resumeFileService.saveResumeFile(resumeFile,resumeFileList.get(i));
        }
        return resume.getId();
    }

    @Transactional(readOnly = true) //이력서 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정.
    public ResumeFormDto getResumeDtl(Long resumeId){ // 게시글을 수정하는 페이지로 진입하기 위한 함수
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

    public Long updateResume(ResumeFormDto resumeFormDto, List<MultipartFile> resumeFileList)throws Exception{
        //이력서 수정
        Resume resume = resumeRepository.findById(resumeFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        resume.updateResume(resumeFormDto);

        List<Long> resumeFileIds =resumeFormDto.getResumeFileIds();
        for (int i = 0; i < resumeFileList.size(); i++) {
            resumeFileService.updateResumeFile(resumeFileIds.get(i),resumeFileList.get(i));
        }
        return resume.getId();
    }

    @Transactional(readOnly = true)
    public Page<Resume> getResumePage(ResumeSearchDto resumeSearchDto, Pageable pageable){
        return resumeRepository.getResumePage(resumeSearchDto,pageable);
    }

    public boolean canEditOrDelete(Resume resume, Member currentMember) {
        return resume.getMember().getId().equals(currentMember.getId())
                || currentMember.getRole().toString().equals("ADMIN");
    }


    public Resume getResumeById(Long resumeId) {
        return resumeRepository.findById(resumeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 이력서를 찾을 수 없습니다."));
    }

    public void deleteResume(Long resumeId) {
        Resume resume = getResumeById(resumeId);
        resumeRepository.delete(resume);
    }

    public ResumeFile getResumeFile(Long fileId) {
        return resumeFileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("해당 파일이 존재하지 않습니다."));
    }

}
