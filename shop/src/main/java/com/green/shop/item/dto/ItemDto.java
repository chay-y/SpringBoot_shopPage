package com.green.shop.item.dto;

import com.green.shop.item.constant.ItemSellStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*dto 를통해 db랑 데이터를 주고받을 수 있다.*/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long itemId;
    private String itemName;
    private Integer price;
    private Integer stockNumber;
    private String itemDetail;
    private ItemSellStatus itemSellStatus;  // enum 형식으로 저장하고 사용
    private LocalDateTime regTime;
    private LocalDateTime updateTime;



}
