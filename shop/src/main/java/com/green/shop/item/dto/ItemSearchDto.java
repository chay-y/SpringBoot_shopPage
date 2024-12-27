package com.green.shop.item.dto;

import com.green.shop.item.constant.ItemSellStatus;
import lombok.Data;

@Data
public class ItemSearchDto {

    //날짜
    private String searchDateType;

    //상품의상태
    private ItemSellStatus searchSellStatus;

    //상품명 또는 상세설명
    private String searchBy;

    //검색어
    private String searchText;

}
