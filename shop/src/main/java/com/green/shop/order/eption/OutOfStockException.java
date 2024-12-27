package com.green.shop.order.eption;

public class OutOfStockException extends RuntimeException{

    public OutOfStockException(String message){
        super(message);
    }

}
