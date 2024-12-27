package com.green.shop.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class PageHandler {

    private int totalCnt;           //총 게시물의 개수
    private int pageSize;           //한페이지의 크기
    private int naviSize=10;        //페이지 네비게이션의 크기
    private int totalPage;          //전체 페이지의 개수
    private int page;               //현재 페이지
    private int beginPage;          //네비게이션의 첫번째 페이지 번호
    private int endPage;            //네비게이션의 마지막 페이지 번호

    //데이터타입이 boolean인 경우
    //getter ->is, setter->setter로 사용
    private boolean firstPage;      //시작 페이지인지 확인
    private boolean lastPage;       //마지막 페이지인지확인


    public PageHandler(int totalCnt, int pageSize, int page){
        this.totalCnt = totalCnt;
        this.pageSize = pageSize;
        this.page = page;

        //전체 페이지 수(totalPage)
        totalPage = (int) Math.ceil((double)totalCnt/pageSize);

        //시작페이지 번호(beginPage)

        beginPage = ((page - 1) / naviSize) * naviSize + 1;
        //마지막 페이지 번호(endPage)
        endPage = Math.min(beginPage + naviSize - 1, totalPage);

        //시작페이지인지 확인(firstPage)
        firstPage = beginPage == 1;

        //마지막 페이지인지 확인(lastPage)
        lastPage = endPage == totalPage;


    }
}
