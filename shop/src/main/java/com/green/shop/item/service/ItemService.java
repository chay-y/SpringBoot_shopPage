package com.green.shop.item.service;

import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.dto.ItemImgDto;
import com.green.shop.item.dto.ItemMainDto;
import com.green.shop.item.form.ItemForm;
import com.green.shop.item.mapper.ItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class) //모든 예외에 롤백작성하겠다.
public class ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemImgService itemImgService;

    //아이템추가
    public Long itemInsert(ItemForm itemForm, List<MultipartFile> itemImgFileList)
                                        throws  Exception{

        //form을 dto로 변환
        ItemDto itemDto = makeItem(itemForm);

        //상품을 등록
        itemMapper.itemInsert(itemDto);

        //이미지 등록
        for(int i=0; i<itemImgFileList.size(); i++){
            ItemImgDto itemImgDto = new ItemImgDto();

            //상품이미지에 상품의 아이디만 먼저 저장
            itemImgDto.setItemId(itemDto.getItemId());

            //첫번째 이미지를 대표이미지로 설정
            if(i==0) itemImgDto.setRepImgYn("Y");
            else itemImgDto.setRepImgYn("N");

            itemImgService.saveItemImg(itemImgDto, itemImgFileList.get(i));
        }

        return itemDto.getItemId();
    }

    //아이템 이미지 추가
    public int itemImgInsert(ItemImgDto itemImgDto){
        return itemMapper.itemImgInsert(itemImgDto);
    }

    //전체목록조회
    public List<ItemDto> itemListAll(){
        return itemMapper.itemListAll();
    }


    //상품 검색하고 폼에 나타내기
    public ItemForm getItemDtl(Long itemId){


        //상품 조회
        ItemDto itemDto = itemMapper.itemSelect(itemId);

        //만약 상품이 존재하지 않으면 NullPointerException 을 발생시킴
        if(itemDto == null){
            throw  new NullPointerException("상품이 존재 하지 않습니다.");
        }

        //읽어온 내용을 폼의 형식으로 변환(화면에 표현하기위해)
        ItemForm itemForm = makeItemForm(itemDto);

        //이미지를 설정
        itemForm.setItemImgList(itemMapper.itemImgSelect(itemId));


        //폼의 형태로 리턴
        return itemForm;
    }

    public Long updateItem(ItemForm itemForm, List<MultipartFile> itemImgFileList) throws Exception{

        ItemDto itemDto = makeItem(itemForm);

        int result = itemMapper.itemUpdate(itemDto);
        List<Long> itemImgIds = itemForm.getItemImgIds();

        for(int i=0; i<itemImgFileList.size(); i++){
            ItemImgDto itemImgDto = new ItemImgDto();
            itemImgService.itemImgUpdate(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return itemDto.getItemId();
    }

    @Transactional(readOnly = true) //읽기전용으로 사용, insert/update/delete 작업 실행시 예외발생
    public List<ItemMainDto> mainSelect(Map map){
        return itemMapper.mainSelect(map);
    }

    private ItemDto makeItem(ItemForm itemForm){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(itemForm.getId());
        itemDto.setItemName(itemForm.getItemName());
        itemDto.setPrice(itemForm.getPrice());
        itemDto.setStockNumber(itemForm.getStockNumber());
        itemDto.setItemDetail(itemForm.getItemDetail());
        itemDto.setItemSellStatus(itemForm.getItemSellStatus());

        return itemDto;
    }

    private ItemForm makeItemForm(ItemDto itemDto){
        ItemForm itemForm = new ItemForm();
        itemForm.setId(itemDto.getItemId());
        itemForm.setItemName(itemDto.getItemName());
        itemForm.setPrice(itemDto.getPrice());
        itemForm.setStockNumber(itemDto.getStockNumber());
        itemForm.setItemDetail(itemDto.getItemDetail());
        itemForm.setItemSellStatus(itemDto.getItemSellStatus());

        return itemForm;
    }

    public List<ItemDto> itemListPage(Map map){
        return itemMapper.itemListPage(map);

    }

    public int countAdminItems(Map map){
        return itemMapper.countAdminItems(map);
    }






}
