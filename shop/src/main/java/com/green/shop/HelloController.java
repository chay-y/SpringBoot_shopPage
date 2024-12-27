package com.green.shop;

import com.green.shop.config.PageHandler;
import com.green.shop.item.dto.ItemMainDto;
import com.green.shop.item.dto.ItemSearchDto;
import com.green.shop.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HelloController {

    private final ItemService itemService;
    private final PageHandler pageHandler;


    @GetMapping("/")
    public String main(@RequestParam(value = "page", required = false) Integer page,
                       @RequestParam(value = "searchText", required = false) String searchText,
                       @ModelAttribute("itemSearchDto") ItemSearchDto itemSearchDto,
                       Model model) {


        //페이징처리 계산
        int pg = 6;
        if (page == null) page = 1;

        Map map = new HashMap();

        map.put("page", page * pg - pg);
        map.put("pageSize", pg);


        //검색어가 있으면 텍스트와 기준은 상품명으로 설정
//        if(searchText != null){
//            itemSearchDto.setSearchText(searchText);
//            itemSearchDto.setSearchBy("itemName");
//        }

        if(searchText ==null ){
            searchText = "";
        }
        itemSearchDto.setSearchText(searchText);
        itemSearchDto.setSearchBy("itemName");



        map.put("itemSearchDto", itemSearchDto);

        //페이지 핸들러 설정
        int totalCnt = itemService.countAdminItems(map);

        PageHandler pageHandler = new PageHandler(totalCnt, pg, page);

        System.out.println("pageHandler : " + pageHandler);

        //모델에 추가하여 전달
        List<ItemMainDto> items = itemService.mainSelect(map);

        model.addAttribute("items", items);
        model.addAttribute("pageHandler", pageHandler);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage",5);

        return "index";


    }
}












