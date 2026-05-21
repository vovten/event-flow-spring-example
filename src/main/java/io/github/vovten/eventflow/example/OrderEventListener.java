package io.github.vovten.eventflow.example;

import io.github.vovten.eventflow.EventListener;
import com.custom.BroadCastOrderCreatedEvent;
import io.github.vovten.eventflow.EventSubscriber;
import io.github.vovten.eventflow.event.Envelope;
import io.github.vovten.eventflow.event.Event;
import io.github.vovten.eventflow.example.event.OrderCreatedEvent;
import io.github.vovten.eventflow.example.event.EnvelopeDomainOrderEvent;
import io.github.vovten.eventflow.example.event.ExternalOrderCreatedEvent;
import io.github.vovten.eventflow.example.event.InternalOrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Event listener for order-related events.
 * Handles internal, external, and broadcast order created events.
 * Each event type is processed and logged to the console.
 */
@Component
public class OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    @EventListener
    public void onEvent(InternalOrderCreatedEvent event) {
        String msg = String.format("InternalOrderCreatedEvent with ID=%s processed", event.getOrderId());
        logger.info(msg);
    }

    /**
     * Handles external order created events.
     *
     * @param event the external order created event
     */
    @EventListener
    public void onEvent(ExternalOrderCreatedEvent event) {
        String msg = String.format("ExternalOrderCreatedEvent with ID=%s processed", event.getOrderId());
        logger.info(msg);
    }

    /**
     * Handles broadcast order created events.
     *
     * @param event the broadcast order created event
     */
    @EventListener
    public void onEvent(BroadCastOrderCreatedEvent event) {
        String msg = String.format("BroadCastOrderCreatedEvent with ID=%s processed", event.getOrderId());
        logger.info(msg);
    }

    /**
     * Handles order created events.
     *
     * @param event the broadcast order created event
     */
    @EventListener(OrderCreatedEvent.class)
    public void onEvent(Envelope<OrderCreatedEvent> event) {
        OrderCreatedEvent payload = event.payload();
        String msg = String.format("Envelope DomainOrderEvent with ID=%s processed", payload.orderId());
        logger.info(msg);
    }

//    /**
//     * Handles order created events.
//     *
//     * @param event the broadcast order created event
//     */
//    @EventListener
//    public void onEvent(OrderCreatedEvent event) {
//        String msg = String.format("OrderCreatedEvent with ID=%s processed", event.orderId());
//        logger.info(msg);
//    }
}
