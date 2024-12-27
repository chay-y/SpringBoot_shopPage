package com.green.shop;


import com.green.shop.cart.dto.CartDto;
import com.green.shop.cart.dto.CartItemDto;
import com.green.shop.cart.form.CartForm;
import com.green.shop.cart.mapper.CartMapper;
import com.green.shop.cart.service.CartService;
import com.green.shop.item.constant.ItemSellStatus;
import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.dto.ItemImgDto;
import com.green.shop.item.mapper.ItemMapper;
import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.mapper.MemberMapper;
import com.green.shop.order.form.OrderForm;
import com.green.shop.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@Transactional(rollbackFor = Exception.class) //exception 발견되면 롤백
public class CartServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ItemMapper itemMapper;

    private CartItemDto cartItemDto;
    private MemberDto memberDto;
    private ItemDto itemDto;
    private CartDto cartDto;


    int i = 111;

    @BeforeEach
        //테스트 메서드가 실행되기 전에 무조건 자동 실행
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


        cartItemDto = new CartItemDto();
        cartItemDto.setItemId(itemDto.getItemId());
        cartItemDto.setCount(3);

    }




    @Test
    public void testAddCart() throws Exception{

        CartForm cartForm = new CartForm();
        cartForm.setItemId(itemDto.getItemId());
        cartForm.setCount(1);

        //장바구니에 물건 추가
        Long cartId = cartService.addCart(cartForm, memberDto.getId());
        assertThat(cartId).isNotNull();

        //장바구니에 동일한 상품을 추가해서 수량이 증가하는지 확인
        cartService.addCart(cartForm, memberDto.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("cartId", cartId);
        map.put("itemId", itemDto.getItemId());

        //장바구니의 수량을 확인. 수량이 2개인지 확인
        CartItemDto cartItem = cartMapper.findItemInCart(map);
        assertThat(cartItem).isNotNull();
        assertThat(cartItem.getCount()).isEqualTo(2);

    }




}




















