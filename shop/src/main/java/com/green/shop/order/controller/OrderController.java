package com.green.shop.order.controller;


import com.green.shop.config.PageHandler;
import com.green.shop.member.service.MemberService;
import com.green.shop.order.dto.OrderHistDto;
import com.green.shop.order.form.OrderForm;
import com.green.shop.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller           //@Controller + @ResponseBody = @RestController 뷰가아닌 자바객체로 리턴한다
@RequiredArgsConstructor
public class OrderController {

    private  final OrderService orderService;
    private  final  MemberService memberService;

    //비동기로 처리하기위해
    //@ResponseBody : 뷰가 아닌 데이터를 전송하고자 할 때 사용하는 어노테이션
    //                 자바 객체를 HTTP요청의 body로 전달

    //@RequestBody : HTTP요청의 body에 담긴 내용을 JSON 형태의 자바 객체로 변환하여 전달

    //Principal : 스프링 시큐리티에서 제공하는 객체.
    //              로그인 되어 있는 상태라면 로그인 계정에 대한 정보를 담고 있는 객체

    //ResponseEntity : HTTP응답을 표현하는 스프링의 클래스
    //                  응답헤더, 본문의 양식에 맞춰서 반환

    //ResponseEntity 사용법
    //1. 상태코드만 전송
    //ResponseEntity <>(HttpStatus.OK);
    //2. 메시지와 상태코드 전송
    //ResponseEntity <>("success", HttpStatus.OK);
    //3. 객체와 상태코드 전송
    //ResponseEntity <>(message, HttpStatus.OK);
    //4. 헤더와 상태코드 전송
    //ResponseEntity <>(header, HttpStatus.OK);

    @PostMapping("/order") //Valid 유효성검사 , bindingREsult 넘겨받을것이다. ,요청한 바디에 전달하겠따
    public ResponseEntity order(@RequestBody @Valid OrderForm orderForm,
                                              BindingResult bindingResult,
                                              Principal principal){

        //오류가 있다면 오류 메세지와 함께 ResponseEntity객체에 담아서 전달
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }


        //로그인 유저의 정보를 조회 (Principal 선언되어있어야함)
        String id = principal.getName();

        Long orderId;

        //화면에서 넘어오는 주문 정보와 회원의 id를 이용해서 주문을 실행
        try {
            orderId = orderService.createOrder(orderForm, id);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        //정상적으로 동작이 되었다면
        //결과값으로 생성된 주문번호와 요청이 성공했다는 HTTP응답 상태 코드를 반환
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(Model model,
                            @PathVariable(value = "page", required = false) Integer page,
                            @ModelAttribute("orderHistDto") OrderHistDto orderHistDto,
                            Principal principal){
        //한 화면에 표시될 데이터의 개수(pageSize)
        //만약 페이지번호가 없으면 페이지는 1로 설정
        int ps=4;
        Map map = new HashMap();
        if(page == null) page = 1;

        map.put("page", page * ps - ps);
        map.put("pageSize", ps);


        //로그인한 회원의 아이디를 확인 전달
        Long memberId = memberService.findMemberId(principal.getName());
        map.put("memberId", memberId);


        //페이징처리를 위해 주문 건수 계산
        int totalCnt = orderService.orderCount(map);

        //PageHandler 사용(페이징 처리)
        PageHandler pageHandler = new PageHandler(totalCnt, ps, page);

        List<OrderHistDto> orderHist = orderService.orderSelect(map);

        //모델에 추가
        model.addAttribute("page", page);
        model.addAttribute("orderHist", orderHist);
        model.addAttribute("pageHandler", pageHandler);

        //order/orderHist.html로 전달
        return "order/orderHist";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId,
                                      Principal principal){ //Principal principal 로그인한 사용자 아이디 받아오기

        // 주문자와 취소하는 사람이 동일인인지 확인
        // HttpStatus.FORBIDDEN : 상태코드 403 ->클라이언트가 요청한 리소스에 대해 접근 권한이 없음을 의미
        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        //주문취소
        try {
            orderService.cancelOrder(orderId);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);

    }


}











