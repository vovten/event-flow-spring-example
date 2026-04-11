package io.github.vovten.eventflow.example.event;

import io.github.vovten.eventflow.channel.EventChannel;
import io.github.vovten.eventflow.channel.InternalEventChannel;
import io.github.vovten.eventflow.event.Event;
import io.github.vovten.eventflow.channel.EventChannel;

import java.util.List;

/**
 * Event representing an internal order creation.
 * This event is routed through the internal event channel and is not exposed externally.
 */
public class InternalOrderCreatedEvent implements Event {
    private Long orderId;

    public InternalOrderCreatedEvent() {
    }

    public InternalOrderCreatedEvent(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public Class<? extends Event> type() {
        return InternalOrderCreatedEvent.class;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public List<Class<? extends EventChannel>> channels() {
        return List.of(InternalEventChannel.class);
    }

}
