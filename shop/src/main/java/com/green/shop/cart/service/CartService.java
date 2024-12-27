package com.green.shop.cart.service;

import com.green.shop.cart.dto.CartDetailDto;
import com.green.shop.cart.dto.CartDto;
import com.green.shop.cart.dto.CartItemDto;
import com.green.shop.cart.dto.CartOrderDto;
import com.green.shop.cart.form.CartForm;
import com.green.shop.cart.mapper.CartMapper;
import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.mapper.ItemMapper;
import com.green.shop.member.dto.MemberDto;
import com.green.shop.member.mapper.MemberMapper;
import com.green.shop.order.constant.OrderStatus;
import com.green.shop.order.form.OrderForm;
import com.green.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;
    private final ItemMapper itemMapper;
    private final MemberMapper memberMapper;
    private final OrderService orderService;

    //카트에 상품 추가
    public Long addCart(CartForm cartForm, String id){

        //로그인ID로 기본키인 memberId를 검색
        Long memberId = memberMapper.findMemberId(id);

        if(memberId == null){
            throw new IllegalArgumentException("유효하지 않은 ID입니다.");
        }

        //로그인한 사용자의 장바구니가 존재하는지 확인
        CartDto cart = createCartIfNotExists(memberId);

        //상품을 장바구니에 추가
        return addItemToCart(cartForm, cart);
    }

    //카트 존재 여부 확인
    private CartDto createCartIfNotExists(Long memberId){

        //cart가 존재하는지 확인
        CartDto cart = cartMapper.findMemberCart(memberId);

        //존재하는 카트가 없으면 카트를 새로 생성
        if (cart == null){
            CartDto cartDto = new CartDto();
            cartDto.setMemberId(memberId);

            cartMapper.insertCart(cartDto);

            cart = cartMapper.findMemberCart(memberId);
        }
        return cart;
    }


    //장바구니에 넣을 상품 조회 확인
    private Long addItemToCart(CartForm cartForm, CartDto cartDto){

        //상품 조회
        ItemDto item = itemMapper.itemSelect(cartForm.getItemId());

        Map<String, Object> map = new HashMap<>();
        map.put("itemId", item.getItemId());
        map.put("cartId", cartDto.getCartId());


        //상품이 장바구니에 들어있는지 확인
        CartItemDto cartItemDto = cartMapper.findItemInCart(map);

        //장바구니에 상품이 없으면 새로 추가
        if(cartItemDto == null){

            cartItemDto = new CartItemDto();

            cartItemDto.setCartId(cartDto.getCartId());
            cartItemDto.setItemId(item.getItemId());
            cartItemDto.setCount(cartForm.getCount());

            cartMapper.insertCartItem(cartItemDto);
        }else {
            //장바구니에 상품이 있으면 기존 수량을 변경
            //저장된 장바구니 수량에 추가하는 값을 더하여 장바구니 수량을 변경
            cartItemDto.setCount(cartForm.getCount()+ cartItemDto.getCount());

            map.put("count", cartItemDto.getCount());
            map.put("cartItemId", cartItemDto.getCartItemId());
            cartMapper.updateCount(map);
        }
        return cartItemDto.getCartId();


        //CartItemDto의 CartId를 리턴

    }

        //장바구니 물건 조회
        public List<CartDetailDto> getCartList(String id){
            Long memberId = memberMapper.findMemberId(id);

            List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

            //현재 로그인한 회원의 장바구니 조회
            CartDto cart = cartMapper.findMemberCart(memberId);

            //장바구니에 상품을 하나도 안담았으면
            //장바구니가 존재하지 않으므로 비어있는 리스트 반환
            if(cart == null)
                return cartDetailDtoList;

            //장바구니에 담겨있는 상품의 정보를 조회
            cartDetailDtoList = cartMapper.findCartDetail(cart.getCartId());

            return cartDetailDtoList;

        }






        //로그인한 회원과 장바구니 주인이 동일한지 확인
        public  boolean validateCartItem(Long cartItemId, String id){

            //로그인한 회원에 대한 조회
            Long loginMemberId = memberMapper.findMemberId(id);

            //장바구니의 회원 조회
            Long curMemberId = cartMapper.findMemberId(cartItemId);

            //결과 리턴
            if(loginMemberId == curMemberId) return true;
            else return false;

        }


        //화면에서 변경한 상품수를 DB에도 적용
        public void updateCartItemCount(Long cartItemId, int count){

            CartItemDto cartItem = cartMapper.findCartItem(cartItemId);

            if (cartItem == null){
                throw new IllegalArgumentException("상품이 존재하지 않습니다.");
            }

            Map map = new HashMap();
            map.put("count", count);
            map.put("cartItemId", cartItemId);

            cartMapper.updateCount(map);
        }



        public int deleteCartItem(Long cartItemId){

            CartItemDto cartItem = cartMapper.findCartItem(cartItemId);

            if (cartItem == null)
                throw new IllegalArgumentException("상품이 존재하지 않습니다.");


            return cartMapper.deleteCartItem(cartItemId);

        }

        //장바구니의 상품을 주문(cart->orderItem)
        public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String id){
            List<OrderForm> orderFormList = new ArrayList<>();

            for(CartOrderDto cartOrderDto: cartOrderDtoList){
                CartItemDto cartItemDto =
                        cartMapper.findCartItem(cartOrderDto.getCartItemId());

                OrderForm orderForm = new OrderForm();

                orderForm.setItemId(cartItemDto.getItemId());
                orderForm.setCount(cartItemDto.getCount());

                orderFormList.add(orderForm);
            }

            Long orderId = orderService.cartOrders(orderFormList, id);

            for(CartOrderDto cartOrderDto: cartOrderDtoList){
                cartMapper.deleteCartItem(cartOrderDto.getCartItemId());
            }
            return orderId;




        }

}















