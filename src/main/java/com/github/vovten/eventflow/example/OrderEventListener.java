package com.github.vovten.eventflow.example;

import com.github.vovten.eventflow.EventListener;
import com.custom.BroadCastOrderCreatedEvent;
import com.github.vovten.eventflow.example.event.ExternalOrderCreatedEvent;
import com.github.vovten.eventflow.example.event.InternalOrderCreatedEvent;
import org.springframework.stereotype.Component;

/**
 * Event listener for order-related events.
 * Handles internal, external, and broadcast order created events.
 * Each event type is processed and logged to the console.
 */
@Component
public class OrderEventListener {

    @EventListener
    public void onEvent(InternalOrderCreatedEvent event) {
        String msg = String.format("InternalOrderCreatedEvent with ID=%s processed", event.getOrderId());
        System.out.println(msg);
    }

    /**
     * Handles external order created events.
     *
     * @param event the external order created event
     */
    @EventListener
    public void onEvent(ExternalOrderCreatedEvent event) {
        String msg = String.format("ExternalOrderCreatedEvent with ID=%s processed", event.getOrderId());
        System.out.println(msg);
    }

    /**
     * Handles broadcast order created events.
     *
     * @param event the broadcast order created event
     */
    @EventListener
    public void onEvent(BroadCastOrderCreatedEvent event) {
        String msg = String.format("BroadCastOrderCreatedEvent with ID=%s processed", event.getOrderId());
        System.out.println(msg);
    }
}
