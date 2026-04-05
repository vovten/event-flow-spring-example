package com.github.vovten.eventflow.example.event;

import com.github.vovten.eventflow.channel.EventChannel;
import com.github.vovten.eventflow.channel.ExternalEventChannel;
import com.github.vovten.eventflow.event.AbstractTraceableEvent;
import com.github.vovten.eventflow.event.Event;

import java.util.List;

/**
 * Event representing an external order creation.
 * This event is routed through the external event channel for external system integration.
 * Extends AbstractTraceableEvent to support event tracing capabilities.
 */
public class ExternalOrderCreatedEvent extends AbstractTraceableEvent {
    private Long orderId;

    public ExternalOrderCreatedEvent() {
    }

    public ExternalOrderCreatedEvent(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public Class<? extends Event> type() {
        return ExternalOrderCreatedEvent.class;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public List<Class<? extends EventChannel>> channels() {
        return List.of(ExternalEventChannel.class);
    }
}