package com.green.shop.chart.controller;

import com.green.shop.chart.dto.ChartDto;
import com.green.shop.chart.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

    @GetMapping("/chart")
    public String orderHist(Model model){

        List<ChartDto> orderCounts = chartService.orderCount();
        model.addAttribute("orderCounts", orderCounts);
        return "chart";
    }


    @GetMapping("/chart/data")
    @ResponseBody   //수행한 결과를 json형태로 리턴
    public List<ChartDto> getOrderCounts(){
        return chartService.orderCount();
    }







}
