package com.org.cloudkitchen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class OrderDispatcher {
    private final OrderService orderService;

    @Autowired
    public OrderDispatcher(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostConstruct
    public void init() {
        dispatch();
    }

    private void dispatch() {
        Runnable runnable = new Runnable() {
            public void run() {
                if (!orderService.isEmptyQueue()) {
                    Order order = orderService.getOrderFromQueue();
                    System.out.println("order detail : id :" + order.getId() + " name : " + order.getName() + " prepTime : " + order.getPrepTime());
                    order = orderService.getOrderFromQueue();
                    System.out.println("order detail : id :" + order.getId() + " name : " + order.getName() + " prepTime : " + order.getPrepTime());
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
    }

}
