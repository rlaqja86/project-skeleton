package com.org.cloudkitchen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class OrderService {
    private final ObjectMapper objectMapper;

    private Queue<Order> orderQueue;

    @Autowired
    public OrderService (ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.orderQueue = new LinkedList<>();
    }

    @PostConstruct
    public void init() {
        try {
            List<Order> orderList = Arrays.asList(objectMapper.readValue(new ClassPathResource("data/dispatch_orders.json").getFile(), Order[].class));

            for (Order order : orderList) {
                orderQueue.add(order);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Order getOrderFromQueue() {
        return orderQueue.poll();
    }

    public Boolean isEmptyQueue() {
        return orderQueue.isEmpty();
    }
}
