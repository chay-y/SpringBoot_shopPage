package com.green.shop.item.service;

import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.dto.ItemImgDto;
import com.green.shop.item.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemMapper itemMapper;

    private final FileService fileService;

    public void saveItemImg(ItemImgDto itemImgDto, MultipartFile itemImgFile)
                                    throws Exception{

        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        System.out.println(itemImgDto);

        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation,
                                                oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        itemImgDto.setImgName(imgName);
        itemImgDto.setOriImgName(oriImgName);
        itemImgDto.setImgUrl(imgUrl);

        //상품 이미지 입력처리
        itemMapper.itemImgInsert(itemImgDto);

    }


    public void itemImgUpdate (Long itemImgId, MultipartFile itemImgFile) throws Exception{

        if(!itemImgFile.isEmpty()) {
            //이미지 읽어오기
            ItemImgDto savedItemImg = itemMapper.itemImgIdSelect(itemImgId);


            //기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            //새로운 이미지 저장
            String oriImgName = itemImgFile.getOriginalFilename();
            assert oriImgName != null;
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());

            String imgUrl = "/images/item/" + imgName;

            savedItemImg.setOriImgName(oriImgName);
            savedItemImg.setImgName(imgName);
            savedItemImg.setImgUrl(imgUrl);

            itemMapper.itemImgUpdate(savedItemImg);
        }
    }
}



