package com.github.vovten.eventflow.example;

import com.github.vovten.eventflow.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @EventListener
    public void onEvent(OrderCreatedEvent event) {
        System.out.println("Order created: " + event.getOrderId());
    }
}
