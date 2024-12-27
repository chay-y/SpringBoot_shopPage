package com.green.shop;

import com.green.shop.config.PageHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PageHandlerTest {

    @Test
    @DisplayName("게시글 개수 25개")
    public void test1(){
        PageHandler ph = new PageHandler(250, 10, 1);

        System.out.println("ph " + ph);

        assertThat(ph.getBeginPage()).isEqualTo(1);
        assertThat(ph.getEndPage()).isEqualTo(10);
        assertThat(ph.isFirstPage()).isTrue();
        assertThat(ph.isLastPage()).isFalse();
    }


    @Test
    @DisplayName("게시글 개수 400개")
    public void test2(){
        PageHandler ph = new PageHandler(400, 10, 11);

        System.out.println("ph " + ph);

        assertThat(ph.getBeginPage()).isEqualTo(11);
        assertThat(ph.getEndPage()).isEqualTo(20);
        assertThat(ph.isFirstPage()).isFalse();
        assertThat(ph.isLastPage()).isFalse();
    }

    @Test
    @DisplayName("게시글 개수 123개, 현재페이지번호11")
    public void test3(){
        PageHandler ph = new PageHandler(123, 10,11);

        System.out.println("ph " + ph);

        assertThat(ph.getBeginPage()).isEqualTo(11);
        assertThat(ph.getEndPage()).isEqualTo(13);
        assertThat(ph.isFirstPage()).isFalse();
        assertThat(ph.isLastPage()).isTrue();
    }

    @Test
    @DisplayName("게시글 개수 0개, 현재페이지번호0")
    public void test4(){
        PageHandler ph = new PageHandler(0, 10,0);

        System.out.println("ph " + ph);

        assertThat(ph.getBeginPage()).isEqualTo(1);
        assertThat(ph.getEndPage()).isEqualTo(0);
        assertThat(ph.isFirstPage()).isTrue();
        assertThat(ph.isLastPage()).isTrue();
    }



}
