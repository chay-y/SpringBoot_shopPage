package com.green.shop.cart.controller;


import com.green.shop.cart.dto.CartDetailDto;
import com.green.shop.cart.dto.CartOrderDto;
import com.green.shop.cart.form.CartForm;
import com.green.shop.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity order(@RequestBody @Valid CartForm cartForm,
                                BindingResult bindingResult,
                                Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError: fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }


        String id = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartForm, id);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public String orderHist(Principal principal, Model model){

        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName());

        model.addAttribute("cartItems", cartDetailDtoList);

        return "cart/cartList";
    }



    @PatchMapping("/cartItem/{cartItemId}")
    public ResponseEntity updateCartItem(@PathVariable("cartItemId")Long cartItemId,
                                         int count,
                                         Principal principal){

        if(count <=0){
            return new ResponseEntity<String>("최소 주문수량은 1개입니다.", HttpStatus.BAD_REQUEST);
        } else if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수량 변경 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);

    }


    @DeleteMapping("/cartItem/{cartItemId}")
    public ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId,
                                         Principal principal){

        if(!cartService.validateCartItem(cartItemId, principal.getName()))
            return new ResponseEntity<String>("상품 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);

        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }


    //카트의 상품을 주문하기
    @PostMapping("/cart/orders")
    public ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto,
                                        Principal principal){
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0)
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.BAD_REQUEST);

        for(CartOrderDto cartOrder : cartOrderDtoList){
            if(!cartService.validateCartItem(
                    cartOrder.getCartItemId(), principal.getName())){
                return  new ResponseEntity<String> ("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }

        Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }



}












