package inhatc.cse.spring.spring_resume_project.resume.controller;

import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeFormDto;
import inhatc.cse.spring.spring_resume_project.resume.service.ResumeFileService;
import inhatc.cse.spring.spring_resume_project.resume.service.ResumeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping("/")
    public String home(){
        return "home/home";
    }

    @GetMapping("/resume/list")
    public String resumeList(){
        return "resume/list";
    }

    @GetMapping("/resume/upload")
    public String resumeUpload(Model model){
        model.addAttribute("resumeFormDto",new ResumeFormDto());
        return "resume/upload";
    }

    @PostMapping("/resume/upload")
    public String resumeUpload(@Valid ResumeFormDto resumeFormDto, BindingResult bindingResult, Model model, @RequestParam("resumeFile")List<MultipartFile> resumeFileList){
        if(bindingResult.hasErrors()){
            return "resume/upload";
        }
        if(resumeFileList.get(0).isEmpty() && resumeFormDto.getId() == null){
            model.addAttribute("errorMessage","첫번째 파일은 필수로 첨부해야 합니다.");
        }
        try{
            resumeService.saveResume(resumeFormDto, resumeFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","이력서 등록 중 에러가 발생하였습니다.");
            return "resume/upload";
        }
        return "redirect:/";
    }

    @GetMapping("/resume/{resumeId}")
    public String resumeDtl(@PathVariable("resumeId") Long resumeId, Model model){
        try{
            ResumeFormDto resumeFormDto = resumeService.getResumeDtl(resumeId);
            model.addAttribute("resumeFormDto",resumeFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 글 입니다.");
            model.addAttribute("resumeFormDto",new ResumeFormDto());
            return "resume/list";
        }
        return "resume/upload";
    }


}
