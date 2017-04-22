package com.xjsaber.spring.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by xjsaber on 2017/4/22.
 *
 */
@Aspect
public class Audience {

    /**
     * 表演之前
     */
    @Before("execution(* com.xjsaber.spring.aop.Performance.perform(..))")
    public void silenceCellPhones(){

        System.out.println("Silencing cell phones");
    }

    /**
     * 表演之前
     */
    @Before("execution(* com.xjsaber.spring.aop.Performance.perform(..))")
    public void taskSeats() {

        System.out.println("Taking seats");;
    }

    /**
     * 表演之后
     */
    @AfterReturning("execution(* com.xjsaber.spring.aop.Performance.perform(..))")
    public void applause() {

        System.out.println("CLAP CLAP CLAP!!!");;
    }

    /**
     * 表演失败之后
     */
    @AfterThrowing("execution(* com.xjsaber.spring.aop.Performance.perform(..))")
    public void demandRefund() {

        System.out.println("Demanding a refund");
    }
}