package com.github.vovten.eventflow.example;

import com.github.vovten.eventflow.event.AbstractTraceableEvent;
import com.github.vovten.eventflow.event.Event;

public class OrderCreatedEvent extends AbstractTraceableEvent {
    private final Long orderId;

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
}
