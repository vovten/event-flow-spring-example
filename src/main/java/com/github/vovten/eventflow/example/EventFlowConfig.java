package com.github.vovten.eventflow.example;

import com.github.vovten.eventflow.channel.ExternalEventChannel;
import com.github.vovten.eventflow.channel.InternalEventChannel;
import com.github.vovten.eventflow.dispatcher.EventDispatcher;
import com.github.vovten.eventflow.dispatcher.EventDispatcherBuilder;
import com.github.vovten.eventflow.event.Event;
import com.github.vovten.eventflow.publisher.EventPublisher;
import com.github.vovten.eventflow.publisher.SpringEventPublisherBuilder;
import com.github.vovten.eventflow.registry.EventHandlerRegistry;
import com.github.vovten.eventflow.registry.SpringEventHandlerRegistryBuilder;
import com.github.vovten.eventflow.transport.incoming.KafkaInTransport;
import com.github.vovten.eventflow.transport.incoming.LocalQueueInTransport;
import com.github.vovten.eventflow.transport.outgoing.KafkaOutTransport;
import com.github.vovten.eventflow.transport.outgoing.LocalQueueOutTransport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

// Uncomment for manual configuration testing and disable event-flow auto-configuration in application.yaml
//@Configuration
public class EventFlowConfig {

    private final LinkedBlockingDeque<Event> eventQueue = new LinkedBlockingDeque<>(100);

    @Bean
    public EventHandlerRegistry eventHandlerRegistry(ApplicationContext applicationContext) {
        return SpringEventHandlerRegistryBuilder.create(applicationContext)
                .withAnnotationListeners("com.github.vovten.eventflow.example")
                .withInterfaceListeners()
                .buildAndLog();
    }

    @Bean
    public EventPublisher eventPublisher() {
        var intChannel = new InternalEventChannel(new LocalQueueOutTransport(eventQueue));
        var extChannel = new ExternalEventChannel(new KafkaOutTransport("localhost:9092", "test"));
        return SpringEventPublisherBuilder.channels(intChannel, extChannel)
                .retryable()
                .silent()
                .transactional()
                .buildAndLog();
    }

    @Bean
    EventDispatcher eventDispatcher(EventHandlerRegistry eventHandlerRegistry) {
        var localTransport = new LocalQueueInTransport(eventQueue);
        var kafkaTransport = new KafkaInTransport("localhost:9092", "test", "group");
        EventDispatcher dispatcher = EventDispatcherBuilder.builder()
                .addTransports(localTransport, kafkaTransport)
                .idempotent()
                .executor(Executors.newFixedThreadPool(5))
                .handlerRegistry(eventHandlerRegistry)
                .build();
        dispatcher.start();
        return dispatcher;
    }
}
