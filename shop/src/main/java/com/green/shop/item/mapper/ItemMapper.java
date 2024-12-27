package com.green.shop.item.mapper;

import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.dto.ItemImgDto;
import com.green.shop.item.dto.ItemMainDto;
import com.green.shop.item.form.ItemForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemMapper {
    int itemInsert(ItemDto itemDto);

    int itemImgInsert(ItemImgDto itemImgDto);

    List<ItemDto> itemListAll();


    /*resultType = ItemDto*/
    ItemDto itemSelect(Long itemId);

    /*sql문으로 넘겨주는게 여러개라서 list */
    List<ItemImgDto> itemImgSelect(Long itemId);


    ItemImgDto itemImgIdSelect(Long itemImgId);

    int itemUpdate(ItemDto itemDto);

    int itemImgUpdate(ItemImgDto itemImgDto);

    List<ItemDto> itemListPage(Map map);

    int countAdminItems(Map map);

    List<ItemMainDto> mainSelect(Map map);


}
