package com.green.shop.cart.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartForm {

    @NotNull(message = "상품 아이디는 필수 입력값입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 1개 이상 담아주시와요.")
    private int count;
}
