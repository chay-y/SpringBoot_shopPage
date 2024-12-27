package com.green.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

//인증되지 않은 사용자가 접근을 시도할 때 적절한 응답을 생성하여 클라이언트에게 전달
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    //인증이 필요한 메서드에 접근하면 자동호출되며,
    //인증예외가 발생한 경우에 실행
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        //HTTP 응답을 생성하고 상태코드를 설정
        //Unauthorized : 인증되지 않은 요청
        //SC_UNAUTHORIZED  : 권한이 없음을 나타내는 상태코드(401)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
