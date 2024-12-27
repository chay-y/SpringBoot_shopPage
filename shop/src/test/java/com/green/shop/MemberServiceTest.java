package com.green.shop;

import com.green.shop.member.constant.Role;
import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.service.MemberSecurityService;
import com.green.shop.member.service.MemberService;
import org.hamcrest.collection.IsIn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService  memberService;

    @Autowired
    private MemberSecurityService memberSecurityService;


    //웹 계층을 테스트하기 위해 사용
    //실제 웹에 접속하지 않았지만 접속된 것 처럼 서블릿 환경을 구현해준다.
    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("회원가입 테스트")
    public void insertMemberTest(){
        MemberDto member = new MemberDto();
        member.setId("test3");
        member.setPassword("1234");
        member.setName("이자바");
        member.setEmail("test3@naver.com");
        member.setAddress("부산");
        member.setRole(Role.USER);

        int result = memberService.insertMember(member);
        System.out.println(member);

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("중복테스트")
    public void overlapTest(){
        MemberDto memberDto = new MemberDto();

        memberDto.setId("test2");
        memberDto.setEmail("test22@naver.com");

        Throwable th = null;


        try{
            memberService.insertMember(memberDto);
        }catch (IllegalStateException e){
         th= e;
        }

        System.out.println(th.getMessage());


        assertThat(th.getMessage()).isIn("중복된 아이디","이미 가입환 회원");


    }



    @Test
    @DisplayName("로그인 테스트")
    public void loginMemberTest() throws Exception{
        String id="test8";
        String password = "1234";

        //HTTP 요청을 모방하여 컨트롤러의 동작을 테스트하는데 사용
        mockMvc.perform(formLogin().userParameter("id") //로그인 시 사용하는 사용자의 이름
                        .loginProcessingUrl("/members/login")   //로그인 처리 URL
                        .user(id)   //로그인 요청에 사용되는 아이디와 비밀번호를 설정
                        .password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated()); //테스트 결과를 검증


        //로그아웃 요청
        //HTTP 응답코드
        //100번대: 정보전달용 / 200번대: 성공 / 300번대: 리디렉션 / 400번대: 클라이언트 오류 / 500번대: 서버오류
        mockMvc.perform(MockMvcRequestBuilders.get("/members/logout"))
                .andExpect(status().is3xxRedirection());    //로그아웃 후 300번대 상태를 전달받는것을 기대

        //로그아웃을 처리한 후 로그인되지 않은 상태인지 확인
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated()); //인증되지 않은 상태임을 기대
    }


















}
