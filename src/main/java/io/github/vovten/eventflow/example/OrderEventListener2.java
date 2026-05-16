package io.github.vovten.eventflow.example;

import com.custom.BroadCastOrderCreatedEvent;
import io.github.vovten.eventflow.EventListener;
import io.github.vovten.eventflow.event.Envelope;
import io.github.vovten.eventflow.example.event.EnvelopeDomainOrderEvent;
import io.github.vovten.eventflow.example.event.ExternalOrderCreatedEvent;
import io.github.vovten.eventflow.example.event.InternalOrderCreatedEvent;
import io.github.vovten.eventflow.example.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Event listener for order-related events.
 * Handles internal, external, and broadcast order created events.
 * Each event type is processed and logged to the console.
 */
@Component
public class OrderEventListener2 {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener2.class);

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
    @EventListener(EnvelopeDomainOrderEvent.class)
    public void onEvent(Envelope<EnvelopeDomainOrderEvent> event) {
        EnvelopeDomainOrderEvent payload = event.payload();
        String msg = String.format("Envelope DomainOrderEvent with ID=%s processed", payload.orderId());
        logger.info(msg);
    }

    /**
     * Handles order created events.
     *
     * @param event the broadcast order created event
     */
    @EventListener
    public void onEvent(OrderCreatedEvent event) {
        if (event.orderId() != 5) {
            throw new IllegalArgumentException("Не корректный ID события");
        }
        String msg = String.format("DomainOrderEvent with ID=%s processed", event.orderId());
        logger.info(msg);
    }
}
