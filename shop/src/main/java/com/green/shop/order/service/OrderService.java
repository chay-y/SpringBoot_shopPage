package com.green.shop.order.service;


import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.mapper.MemberMapper;
import com.green.shop.order.constant.OrderStatus;
import com.green.shop.order.dto.OrderDto;
import com.green.shop.order.dto.OrderHistDto;
import com.green.shop.order.form.OrderForm;
import com.green.shop.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class) //예외가 발생되면 롤백하겠다.
public class OrderService {

    private final OrderMapper orderMapper;
    private final MemberMapper memberMapper;
    private final OrderItemService orderItemService;



    //주문처리
    //form -> orderDto
    public Long createOrder(OrderForm orderForm, String id) throws Exception{

        OrderDto orderDto = new OrderDto();

        MemberDto memberDto = memberMapper.loginMember(id);
        orderDto.setMemberId(memberDto.getMemberId());
        orderDto.setOrderStatus(OrderStatus.ORDER);

        orderMapper.insertOrder(orderDto); //orders테이블에 주문 추가

        orderItemService.createOrderItem(orderDto.getOrderId(), orderForm.getItemId(), orderForm.getCount());

        return orderDto.getOrderId();
    }




    public List<OrderHistDto> orderSelect(Map map){
        return orderMapper.orderSelect(map);
    }

    public int orderCount(Map map){
        return orderMapper.orderCount(map);
    }



    @Transactional(readOnly = true) //읽기전용 처리하겠다
    //로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 확인
    public  boolean validateOrder(Long orderId, String id){

        //현재 회원 정보를 조회
        Long loginId = memberMapper.findMemberId(id);


        //주문 내역에서 id찾기
        Long memberId = orderMapper.orderMemberId(orderId);


        //같은 사람이면 true, 아니면 false 출력
        if (loginId == memberId) return true;
        else return false;

    }

    //주문 취소
    public Long cancelOrder(Long orderId) throws  Exception {

        //주문상품에 대한 정보를 가져오기
        OrderHistDto orderHistDto = orderMapper.findOrder(orderId);

        //취소된 주문수량만큼 재고를 증가시키기
        orderItemService.addStock(orderHistDto.getItemId(), orderHistDto.getCount());

        //주문 상태를 변경하기
        orderMapper.cancelOrder(orderId);

        return orderId;

    }


    //장바구니의 물건 주문
    public Long cartOrders(List<OrderForm> orderFormList, String id){
        MemberDto member = memberMapper.loginMember(id);

        OrderDto orderDto = new OrderDto();
        orderDto.setMemberId(member.getMemberId());
        orderDto.setOrderStatus(OrderStatus.ORDER);

        orderMapper.insertOrder(orderDto);

        for(OrderForm orderForm : orderFormList){
            orderItemService.createOrderItem(orderDto.getOrderId(),
                                                    orderForm.getItemId(), orderForm.getCount());
        }

        return orderDto.getOrderId();

    }

























}
