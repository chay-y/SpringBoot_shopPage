package com.green.shop.chart.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ChartDto {

    private Date orderDate;
    private int count;
}
