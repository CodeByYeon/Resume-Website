package inhatc.cse.spring.spring_resume_project.resume.controller;

import org.springframework.stereotype.Controller;
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

    @GetMapping("/resume/add")
    public String resumeAdd(){
        return "resume/add";
    }


}
