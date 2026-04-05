package com.custom;

import com.github.vovten.eventflow.channel.BroadcastEventChannel;
import com.github.vovten.eventflow.channel.EventChannel;
import com.github.vovten.eventflow.event.AbstractTraceableEvent;
import com.github.vovten.eventflow.event.Event;

import java.util.List;

/**
 * Event representing a broadcast order creation.
 * This event is routed through the broadcast event channel to all subscribers.
 * Extends AbstractTraceableEvent to support event tracing capabilities.
 */
public class BroadCastOrderCreatedEvent extends AbstractTraceableEvent {
    private Long orderId;

    public BroadCastOrderCreatedEvent() {
    }

    public BroadCastOrderCreatedEvent(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public Class<? extends Event> type() {
        return BroadCastOrderCreatedEvent.class;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public List<Class<? extends EventChannel>> channels() {
        return List.of(BroadcastEventChannel.class);
    }

}
