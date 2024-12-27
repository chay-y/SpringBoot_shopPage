package com.green.shop.member.controller;

import com.green.shop.member.constant.Role;
import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.form.MemberJoinForm;
import com.green.shop.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members") // 컨트롤러 자체 맵핑
@RequiredArgsConstructor // 의존성 주입 방법 중 생성자 주입을 임의의 코드 없이 자동으로 설정
public class MemberController {

//    @Autowired 의존성 주입방법1

    private final MemberService memberService;



    // /members/new 로 접속하면 memberJoinForm.html이 출력되기를 원해요

    @GetMapping("/new") //경로여러단계 가능, 검사는안하고 경로만 지정
    public String memberForm(Model model, HttpServletRequest request){

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        System.out.println(token.getHeaderName()+"="+token.getToken());

        model.addAttribute("memberJoinForm", new MemberJoinForm()); //유효성검사를위해 MemberJoinForm

        return "member/memberJoinForm";
    }

    //회원가입
    //RedirectAttributes : 특정 속성을 리다이렉트 되는 뷰 페이지에 사용하도록 전달 -> 전다이안되면 model을 이용하게된다.
    @PostMapping("/new") // 같은 /new/ 지만 포스트로 걸린다면,
    public String newMember(@Valid MemberJoinForm memberJoinForm,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes rttr) {
                            //@valid - 유효성 검사를위해 달아줌, BindingResult - 오류가발생하면 이곳에 저정하겠다.(순서는 valid뒤에와야함)

        //bindingResult 에  에러가 발생한다면
        if(bindingResult.hasErrors()){
            return "member/memberJoinForm";
        }

        try {
            //회원가입

            MemberDto dto = new MemberDto();

            dto.setId(memberJoinForm.getId());
            dto.setPassword(memberJoinForm.getPassword());
            dto.setName(memberJoinForm.getName());
            dto.setEmail(memberJoinForm.getEmail());
            dto.setAddress(memberJoinForm.getAddress());
            dto.setRole(Role.USER); // USER : 지금은 사용자라는뜻 ,ADMIN : 관리자

            memberService.insertMember(dto);

            //insert 동작 일어난후
            //addFlash 일회성
            rttr.addFlashAttribute("resultMessage", "회원가입환영"); //화면에 메세지 전달

            System.out.println(memberJoinForm.toString());


        }catch (IllegalStateException e){ //상태에 대한 에러가 발생한다면
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberJoinForm"; //원래 회원가입 페이지로 이동
        }
        return "redirect:/"; //메인페이지
    }

    //로그인
    @GetMapping("/login")
    public String loginForm(){
        return "/member/memberLoginForm"; //템플릿에있는
    }

    //로그인 에러 발생했을때
    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }
}
