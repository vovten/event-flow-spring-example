package io.github.vovten.eventflow.example;

import io.github.vovten.eventflow.EventSubscriber;
import io.github.vovten.eventflow.event.Event;
import io.github.vovten.eventflow.example.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderEventSubscriber implements EventSubscriber {
    private static final Logger log = LoggerFactory.getLogger(OrderEventSubscriber.class);

    @Override
    public List<Class<?>> events() {
        return List.of(OrderCreatedEvent.class);
    }

    @Override
    public void onEvent(Event event) {
        log.info("Получено событие в подписчике: {}", event);
    }
}
