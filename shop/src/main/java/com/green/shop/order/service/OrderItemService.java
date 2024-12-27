package com.green.shop.order.service;


import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.mapper.ItemMapper;
import com.green.shop.order.dto.OrderItemDto;
import com.green.shop.order.eption.OutOfStockException;
import com.green.shop.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OrderItemService {

    private  final OrderMapper orderMapper;
    private  final ItemMapper itemMapper;


    //상품 주문 처리
    public void createOrderItem(Long orderId, Long itemId, int count){

        OrderItemDto orderItemDto = new OrderItemDto();
        ItemDto itemDto = itemMapper.itemSelect(itemId);

        orderItemDto.setOrderId(orderId);
        orderItemDto.setItemId(itemId);
        orderItemDto.setOrderPrice(this.getTotalPrice(itemDto.getPrice(), count));
        orderItemDto.setCount(count);

        this.removeStock(itemDto, count);

        orderMapper.insertOrderItem(orderItemDto);

    }



    //상품의 재고 감소
    public void removeStock(ItemDto itemDto, int stockNumber){

        int restStock = itemDto.getStockNumber() - stockNumber;

        if(restStock<0)
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량 : "
                                            + itemDto.getStockNumber() + ")");

        Map map = new HashMap();

        map.put("itemId", itemDto.getItemId());
        map.put("stockNumber", restStock);

        orderMapper.changeStock(map);
    }

    //상품 재고 추가
    public void addStock(Long itemId, int stockNumber){
        ItemDto itemDto = itemMapper.itemSelect(itemId);

        int restStock = itemDto.getStockNumber() + stockNumber;

        Map map = new HashMap();

        map.put("itemId", itemDto.getItemId());
        map.put("stockNumber", restStock);

        orderMapper.changeStock(map);

    }




    //상품 가격 계산하기
    public int getTotalPrice(int price, int count){
        return price * count;
    }



}



