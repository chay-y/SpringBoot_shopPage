package com.green.shop.chart.mapper;

import com.green.shop.chart.dto.ChartDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChartMapper {

    List<ChartDto> orderCount();
}
