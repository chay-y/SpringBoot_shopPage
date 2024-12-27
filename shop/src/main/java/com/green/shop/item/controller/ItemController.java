package com.green.shop.item.controller;

import com.green.shop.config.PageHandler;
import com.green.shop.item.dto.ItemDto;
import com.green.shop.item.dto.ItemSearchDto;
import com.green.shop.item.form.ItemForm;
import com.green.shop.item.mapper.ItemMapper;
import com.green.shop.item.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;


    @GetMapping("/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemForm", new ItemForm());
        return "/item/itemForm";
    }


    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemForm itemForm,
                          BindingResult bindingResult,
                          Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemForm.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품이미지는 필수 입력값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.itemInsert(itemForm, itemImgFileList);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping("/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) { //변수에 item Id 대입하기위해

        try {

            //조회한 상품 데이터를 가져옴
            ItemForm itemForm = itemService.getItemDtl(itemId);
            model.addAttribute("itemForm", itemForm);

        } catch (NullPointerException e) {
            //상품이 존재하지 않으면 '존재하지 않는 상품입니다.'를 메세지로 표시
            model.addAttribute("errorMessage", "존재하지 않는 상풉입니다.");
            model.addAttribute("itemForm", new ItemForm()); //새로운객체로 나오게되면 비어있는채로 나온다.

        }

        //가져온 내용을 전달
        return "item/itemForm";
    }


    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){

        ItemForm itemForm = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemForm);

        return "item/itemDtl";

    }



    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemForm itemForm,
                             BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemForm.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemForm, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            e.printStackTrace();
            return "item/itemForm";
        }
        return "redirect:/";
    }

    //required=false -> page의 값이 필수가 아니도록 설정
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemListPage(@PathVariable(value = "page", required = false) Integer page,
                               Model model, @ModelAttribute("itemSearchDto") ItemSearchDto itemSearchDto){


        int ps = 3;    //pageSize 생성할페이지수

        Map map = new HashMap();

        // /admin/items로 접속한 경우 페이지번호를 1로 설정
        if (page==null) page=1;

        map.put("page", page* ps - ps); //sql문의 offset
        map.put("pageSize", ps);
        map.put("itemSearchDto", itemSearchDto);

        int totalCnt = itemService.countAdminItems(map); //전체 게시글 개수
        PageHandler pageHandler = new PageHandler(totalCnt, ps, page);

        List<ItemDto> items = itemService.itemListPage(map);

        model.addAttribute("items", items);
        model.addAttribute("pageHandler", pageHandler);

        return "item/itemMng";
    }




}




