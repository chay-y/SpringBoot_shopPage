package com.green.shop;


import com.green.shop.item.constant.ItemSellStatus;
import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Test
    @DisplayName("상품 저장 테스트")
    public void  createItemTest(){
        for(int i=1; i<=20; i++) {

            ItemDto item = new ItemDto();
            item.setItemName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품의 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL); //열거형안의 내용만 저장가능
            item.setStockNumber(100);
        }

//        int result = itemService.itemInsert(item);
//
//        assertThat(result).isEqualTo(1);

    }


    @Test
    @DisplayName("전체 목록 조회 테스트")
    public void itemListAllTest(){
        List<ItemDto> list = itemService.itemListAll();
        System.out.println(list);
        assertThat(list).isNotEmpty();

    }


}
