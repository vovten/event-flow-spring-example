package io.github.vovten.eventflow.example;

import com.custom.BroadCastOrderCreatedEvent;
import io.github.vovten.eventflow.EventListener;
import io.github.vovten.eventflow.EventSubscriber;
import io.github.vovten.eventflow.event.Envelope;
import io.github.vovten.eventflow.event.Event;
import io.github.vovten.eventflow.example.event.DomainOrderEvent;
import io.github.vovten.eventflow.example.event.EnvelopeDomainOrderEvent;
import io.github.vovten.eventflow.example.event.ExternalOrderCreatedEvent;
import io.github.vovten.eventflow.example.event.InternalOrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Event listener for order-related events.
 * Handles internal, external, and broadcast order created events.
 * Each event type is processed and logged to the console.
 */
@Component
public class OrderEventListener implements EventSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    @Value("${spring.output.ansi.enabled:detect}")
    private AnsiOutput.Enabled springOutputAnsiEnabled;

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
    @EventListener(DomainOrderEvent.class)
    public void onEvent2(Envelope<DomainOrderEvent> event) {
        String msg = String.format("DomainOrderEvent with ID=%s processed", event.eventId());
        logger.info(msg);
    }

    @Override
    public List<Class<?>> events() {
        return List.of(DomainOrderEvent.class);
    }

    @Override
    public void onEvent(Event event) {
        Object payload = ((Envelope) event).payload();
        System.out.println("Event for subscriber: " + event);
    }
}
