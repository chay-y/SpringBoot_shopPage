package com.green.shop.chart.service;


import com.green.shop.chart.dto.ChartDto;
import com.green.shop.chart.mapper.ChartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final ChartMapper chartMapper;

    public List<ChartDto> orderCount(){
        List<ChartDto> list = chartMapper.orderCount();
        return list;
    }


}
