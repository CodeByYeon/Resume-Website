package inhatc.cse.spring.spring_resume_project.resume.controller;

import inhatc.cse.spring.spring_resume_project.member.entity.Member;
import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeFormDto;
import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeSearchDto;
import inhatc.cse.spring.spring_resume_project.resume.entity.Resume;
import inhatc.cse.spring.spring_resume_project.resume.entity.ResumeFile;
import inhatc.cse.spring.spring_resume_project.resume.service.ResumeFileService;
import inhatc.cse.spring.spring_resume_project.resume.service.ResumeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @Value("${resumeFileLocation}")
    private String resumeFileLocation;


    @GetMapping("/")
    public String home(){
        return "home/home";
    }

    @GetMapping({"/resume/list","/resume/list/{page}"})
    public String resumeList(ResumeSearchDto resumeSearchDto, Model model,
                             @PathVariable("page")Optional<Integer> page){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Resume> resumes = resumeService.getResumePage(resumeSearchDto,pageable);
        model.addAttribute("resumes",resumes);
        model.addAttribute("resumeSearchDto",resumeSearchDto);
        model.addAttribute("maxPage",5);
        return "resume/list";
    }

    @GetMapping("/resume/upload")
    public String resumeUploadForm(Model model){
        model.addAttribute("resumeFormDto",new ResumeFormDto());
        return "resume/upload";
    }

    @PostMapping("/resume/upload")
    public String resumeUpload(@Valid ResumeFormDto resumeFormDto,
                               BindingResult bindingResult, Model model,
                               @RequestParam("resumeFile")List<MultipartFile> resumeFileList,
                               @AuthenticationPrincipal UserDetails userDetails){
        if(bindingResult.hasErrors()){
            return "resume/upload";
        }
        if(resumeFileList.get(0).isEmpty() && resumeFormDto.getId() == null){
            model.addAttribute("errorMessage","첫번째 파일은 필수로 첨부해야 합니다.");
            return "resume/upload";
        }
        try{

            Member member = resumeService.findMemberByEmail(userDetails.getUsername());
            resumeService.saveResume(resumeFormDto, resumeFileList, member);
        }catch (Exception e){
            model.addAttribute("errorMessage","이력서 등록 중 에러가 발생하였습니다.");
            return "resume/upload";
        }
        return "redirect:/resume/list";
    }

    @GetMapping("/resume/modify/{resumeId}")
    public String resumeDtl(@PathVariable("resumeId") Long resumeId, Model model,
                            @AuthenticationPrincipal UserDetails userDetails){ //이력서 수정 페이지로 진입하는 함수
        Resume resume = resumeService.getResumeById(resumeId);
        Member currentMember =resumeService.findMemberByEmail(userDetails.getUsername());
        if(!resumeService.canEditOrDelete(resume,currentMember)){
            model.addAttribute("errorMessage", "본인이 작성한 글만 수정하거나 삭제할 수 있습니다.");
            return "redirect:/resume/list";
        }else {
            try {
                ResumeFormDto resumeFormDto = resumeService.getResumeDtl(resumeId); //조회한 이력서 데이터를 모델에 담아서 뷰로 전달.
                model.addAttribute("resumeFormDto", resumeFormDto);
            } catch (EntityNotFoundException e) { // 이력서 엔티티가 존재하지 않을 경우 에러메시지를 담아서 이력서 리스트 페이지로 이동
                model.addAttribute("errorMessage", "존재하지 않는 글 입니다.");
                model.addAttribute("resumeFormDto", new ResumeFormDto());
                return "resume/list";
            }
        }
        return "resume/upload";
    }

    @PostMapping("/resume/modify/{resumeId}")
    public String resumeUpdate(@Valid ResumeFormDto resumeFormDto,BindingResult bindingResult,
                               @RequestParam("resumeFile") List<MultipartFile> resumeFileList,Model model){ // 이력서 수정 후 수정하기 요청을 보냈을 때
        if(bindingResult.hasErrors()){
            return "resume/upload";
        }
        if(resumeFileList.get(0).isEmpty() && resumeFormDto.getId() == null){
            model.addAttribute("errorMessage","첫 번째 파일은 반드시 첨부하여야 합니다.");
            return "resume/upload";
        }
        try{
            resumeService.updateResume(resumeFormDto,resumeFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","게시글 수정 중 오류가 발생했습니다.");
            return "resume/upload";
        }
        return "redirect:/resume/list";
    }

    @PostMapping("/resume/delete/{resumeId}")
    public String deleteResume(@PathVariable("resumeId") Long resumeId,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        try {
            Resume resume = resumeService.getResumeById(resumeId);
            Member currentMember = resumeService.findMemberByEmail(userDetails.getUsername());

            // 권한 확인
            if (!resumeService.canEditOrDelete(resume, currentMember)) {
                model.addAttribute("errorMessage", "본인이 작성한 글만 삭제할 수 있습니다.");
                return "redirect:/resume/list";
            }

            // 삭제 처리
            resumeService.deleteResume(resumeId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "삭제 중 오류가 발생했습니다.");
            return "redirect:/resume/list";
        }
        return "redirect:/resume/list";
    }

    @DeleteMapping("/resume/delete/{resumeId}")
    public String delete(@PathVariable("resumeId") Long resumeId,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        try {
            Resume resume = resumeService.getResumeById(resumeId);
            Member currentMember = resumeService.findMemberByEmail(userDetails.getUsername());

            if (!resumeService.canEditOrDelete(resume, currentMember)) {
                model.addAttribute("errorMessage", "본인이 작성한 글만 삭제할 수 있습니다.");
                return "redirect:/resume/list";
            }

            resumeService.deleteResume(resumeId);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "삭제 중 오류가 발생했습니다.");
            return "redirect:/resume/list";
        }
        return "redirect:/resume/list";
    }


    @GetMapping("/resume/view/{resumeId}") //세부 내용 조회 메소드
    public String viewResume(@PathVariable("resumeId") Long resumeId,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        try {
            Resume resume = resumeService.getResumeById(resumeId);
            Member currentMember = resumeService.findMemberByEmail(userDetails.getUsername());

            // 수정/삭제 권한 확인
            boolean canEdit = resumeService.canEditOrDelete(resume, currentMember);
            model.addAttribute("resume", resume);
            model.addAttribute("canEdit", canEdit);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 글입니다.");
            return "redirect:/resume/list";
        }
        return "resume/view";
    }
    @GetMapping("/resume/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        // 이력서 파일 조회
        ResumeFile resumeFile = resumeService.getResumeFileById(id);
        if (resumeFile == null || resumeFile.getFileUrl() == null) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        try {
            // 파일 경로 조합
            Path filePath = Paths.get(resumeFileLocation).resolve(resumeFile.getFileUrl()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new FileNotFoundException("파일이 존재하지 않거나 읽을 수 없습니다: " + filePath.toString());
            }

            // 파일 이름 가져오기
            String fileName = filePath.getFileName().toString();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("파일 다운로드 중 문제가 발생했습니다.", e);
        }
    }


}
