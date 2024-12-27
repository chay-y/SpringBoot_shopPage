package com.green.shop.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//WebMvcConfigurer 인터페이스 : SpringMVC를 사용자가 직접 구성할 수 있도록 하는 인터페이스
@Configuration  //@Configuration : @Component를 포함하고 있으며 외부 라이브러리나 클래스를 bean으로 등록하고자 할 때 사용
public class WebMvcConfig implements WebMvcConfigurer {

    //application.yml에 설정한  'uploadPath'값을 읽어오기
    @Value("${uploadPath}") //스프링프레임워크 임폴트
    String uploadPath;


    //정적 리소스(css, js, img)에 대한 요청을 처리하는 방법을 작성
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //브라우저에 입력하는 url이 /images로 시작하는 경우
        //uploadpath에 설정한 폴더를 기준으로 파일을 읽어오도록 설정
        registry.addResourceHandler("/images/**").addResourceLocations(uploadPath);
    }
}
