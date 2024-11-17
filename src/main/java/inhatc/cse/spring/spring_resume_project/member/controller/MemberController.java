package inhatc.cse.spring.spring_resume_project.member.controller;

import inhatc.cse.spring.spring_resume_project.member.dto.MemberDto;
import inhatc.cse.spring.spring_resume_project.member.entity.Member;
import inhatc.cse.spring.spring_resume_project.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping(value = "/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 패스워드를 확인해주세요");
        return "member/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        log.info("==> logout");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/";
    }

    @GetMapping("/add")
    public String newMember(Model model){
        model.addAttribute("memberDto", new MemberDto());
        return "member/add";
    }

    @PostMapping("/add")
    public String memberNew(@Valid MemberDto memberDto,
                            BindingResult bindingResult,
                            Model model){
        if(bindingResult.hasErrors()){
            return "member/add";
        }
        try {
            Member member = Member.createMember(memberDto, passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "member/add";
        }
        return "redirect:/";
    }
}
