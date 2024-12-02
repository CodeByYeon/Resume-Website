package inhatc.cse.spring.spring_resume_project.resume.controller;

import inhatc.cse.spring.spring_resume_project.member.entity.Member;
import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeFormDto;
import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeSearchDto;
import inhatc.cse.spring.spring_resume_project.resume.entity.Resume;
import inhatc.cse.spring.spring_resume_project.resume.service.ResumeFileService;
import inhatc.cse.spring.spring_resume_project.resume.service.ResumeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

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
    public String resumeDtl(@PathVariable("resumeId") Long resumeId, Model model){ //이력서 수정 페이지로 진입하는 함수
        try{
            ResumeFormDto resumeFormDto = resumeService.getResumeDtl(resumeId); //조회한 이력서 데이터를 모델에 담아서 뷰로 전달.
            model.addAttribute("resumeFormDto",resumeFormDto);
        }catch (EntityNotFoundException e){ // 이력서 엔티티가 존재하지 않을 경우 에러메시지를 담아서 이력서 리스트 페이지로 이동
            model.addAttribute("errorMessage", "존재하지 않는 글 입니다.");
            model.addAttribute("resumeFormDto",new ResumeFormDto());
            return "resume/list";
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
        return "redirect:/";
    }

}
