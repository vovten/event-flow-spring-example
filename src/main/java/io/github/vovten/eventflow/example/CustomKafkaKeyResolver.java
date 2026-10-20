package io.github.vovten.eventflow.example;

import io.github.vovten.eventflow.event.Envelope;
import io.github.vovten.eventflow.event.Event;
import io.github.vovten.eventflow.example.event.ExternalOrderCreatedEvent;
import io.github.vovten.eventflow.transport.outgoing.EventKeyResolver;
import org.springframework.stereotype.Component;

@Component
public class CustomKafkaKeyResolver implements EventKeyResolver {
    @Override
    public String resolveKey(Event event) {
        if (event instanceof Envelope<?> env
                && env.payload() instanceof ExternalOrderCreatedEvent extOrderCreatedEvent
                && extOrderCreatedEvent.getOrderId() != null) {
            return "custom-" + extOrderCreatedEvent.getOrderId();
        }
        // null — round-robin, according to the EventKeyResolver contract
        return null;
    }
}
