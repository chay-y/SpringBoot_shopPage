package com.green.shop;


import com.green.shop.item.constant.ItemSellStatus;
import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.dto.ItemImgDto;
import com.green.shop.item.mapper.ItemMapper;
import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.mapper.MemberMapper;
import com.green.shop.order.constant.OrderStatus;
import com.green.shop.order.dto.OrderHistDto;
import com.green.shop.order.form.OrderForm;
import com.green.shop.order.mapper.OrderMapper;
import com.green.shop.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ItemMapper itemMapper;

    private MemberDto memberDto;
    private ItemDto itemDto;
    private Long orderId;

    //테스트 할때마다 숫자 바꾸기 0,1,2
    int i =0;

    @BeforeEach     //테스트 메서드가 실행되기 전에 무조건 자동 실행
    void setUp() throws Exception{
        String uniqueId = "tester" + i;
        String uniqueEmail = "testUser" + i + "@naver.com";

        memberDto = new MemberDto();
        memberDto.setId(uniqueId);
        memberDto.setEmail(uniqueEmail);
        memberDto.setPassword("1234");
        memberDto.setName("김그린");
        memberMapper.insertMember(memberDto);

        System.out.println(memberDto);

        itemDto = new ItemDto();
        itemDto.setItemName("물건");
        itemDto.setPrice(10000);
        itemDto.setStockNumber(100);
        itemDto.setItemDetail("상세설명");
        itemDto.setItemSellStatus(ItemSellStatus.SELL);
        itemMapper.itemInsert(itemDto);

        System.out.println(itemDto);

        ItemImgDto itemImgDto = new ItemImgDto();
        itemImgDto.setItemId(itemDto.getItemId());
        itemImgDto.setImgName("물건1");
        itemImgDto.setRepImgYn("Y");

        itemMapper.itemImgInsert(itemImgDto);

        OrderForm orderForm = new OrderForm();
        orderForm.setItemId(itemDto.getItemId());
        orderForm.setCount(2);

        orderId = orderService.createOrder(orderForm, memberDto.getId());

    }


    @Test
    @DisplayName("주문 테스트")
    public void order() throws Exception{
        OrderForm orderForm = new OrderForm();
        orderForm.setItemId(36L);
        orderForm.setCount(5);

        Long orderId = orderService.createOrder(orderForm, "ad1");
    }


    @Test
    public void CancerOrderTest() throws Exception{
        int beforeCount = itemDto.getStockNumber();

        Long canceledOrderId = orderService.cancelOrder(orderId);

        assertThat(canceledOrderId).isNotNull();

        OrderHistDto orderHistDto = orderMapper.findOrder(canceledOrderId);

        assertThat(OrderStatus.CANCEL).isEqualTo(orderHistDto.getOrderStatus());
        assertThat(itemDto.getStockNumber()).isEqualTo(beforeCount);

    }




}


