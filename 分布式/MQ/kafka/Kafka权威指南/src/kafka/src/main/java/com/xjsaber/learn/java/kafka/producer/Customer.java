package com.xjsaber.learn.java.kafka.producer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xjsaber
 */
@Slf4j
@Data
public class Customer {

    private String customerId;
    private String customerName;

    public Customer(){

    }

    public Customer(String id, String name){
        this.customerId = id;
        this.customerName = name;
    }

    public String getId(){
        return this.customerId;
    }

    public String getName(){
        return this.customerName;
    }

    public void setId(String id){
        this.customerId = id;
    }

    public void setName(String name){
        this.customerName = name;
    }
}
