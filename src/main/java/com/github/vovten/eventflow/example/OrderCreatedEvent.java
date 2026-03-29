package com.github.vovten.eventflow.example;

import com.github.vovten.eventflow.event.Event;

public class OrderCreatedEvent implements Event {
    private Long orderId;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public Class<? extends Event> type() {
        return OrderCreatedEvent.class;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
