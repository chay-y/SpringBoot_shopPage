package com.green.shop.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemImgDto {

    private Long itemImgId;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private Long itemId;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

}
