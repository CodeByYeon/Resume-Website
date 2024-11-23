package inhatc.cse.spring.spring_resume_project.resume.controller;

import inhatc.cse.spring.spring_resume_project.resume.dto.ResumeFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResumeController {
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



}
