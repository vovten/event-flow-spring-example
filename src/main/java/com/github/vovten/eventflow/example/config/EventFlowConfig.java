package com.github.vovten.eventflow.example.config;

import com.github.vovten.eventflow.channel.BroadcastEventChannel;
import com.github.vovten.eventflow.channel.ExternalEventChannel;
import com.github.vovten.eventflow.channel.InternalEventChannel;
import com.github.vovten.eventflow.dispatcher.EventDispatcher;
import com.github.vovten.eventflow.dispatcher.EventDispatcherBuilder;
import com.github.vovten.eventflow.event.Event;
import com.github.vovten.eventflow.example.MyJsonSerializer;
import com.github.vovten.eventflow.publisher.EventPublisher;
import com.github.vovten.eventflow.publisher.SpringEventPublisherBuilder;
import com.github.vovten.eventflow.registry.EventHandlerRegistry;
import com.github.vovten.eventflow.registry.SpringEventHandlerRegistryBuilder;
import com.github.vovten.eventflow.serialization.EventSerializerFactory;
import com.github.vovten.eventflow.transport.incoming.KafkaInTransport;
import com.github.vovten.eventflow.transport.incoming.LocalQueueInTransport;
import com.github.vovten.eventflow.transport.outgoing.BroadcastKafkaOutTransport;
import com.github.vovten.eventflow.transport.outgoing.KafkaOutTransport;
import com.github.vovten.eventflow.transport.outgoing.LocalQueueOutTransport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Configuration class for setting up the Event Flow infrastructure.
 * Configures event channels, publishers, dispatchers, and handler registries.
 * Supports internal, external, and broadcast event channels with various transports.
 * 
 * Uncomment @Configuration annotation and disable auto-configuration in application.yaml
 * for manual configuration testing.
 */
//@Configuration  // Uncomment for manual configuration testing and disable event-flow auto-configuration in application.yaml
public class EventFlowConfig {

    private final LinkedBlockingDeque<Event> eventQueue = new LinkedBlockingDeque<>(100);

    /**
     * Creates and configures the event handler registry.
     * Scans the specified package for annotation-based and interface-based event listeners.
     *
     * @param applicationContext the Spring application context
     * @return configured event handler registry
     */
    @Bean
    public EventHandlerRegistry eventHandlerRegistry(ApplicationContext applicationContext) {
        return SpringEventHandlerRegistryBuilder.create(applicationContext)
                .withAnnotationListeners("com.github.vovten.eventflow.example")
                .withInterfaceListeners()
                .buildAndLog();
    }

    /**
     * Creates and configures the event publisher with multiple event channels.
     * Sets up internal, external, and broadcast channels with appropriate transports.
     * The publisher is configured with retry, silent, transactional, and async capabilities.
     *
     * @return configured event publisher
     */
    @Bean
    public EventPublisher eventPublisher() {
        var serializer = new MyJsonSerializer();
        EventSerializerFactory.register(serializer);
        var intChannel = new InternalEventChannel(new LocalQueueOutTransport(eventQueue));
        var transport = new KafkaOutTransport("localhost:9092", "test");
        var broadcastTransport = new BroadcastKafkaOutTransport("localhost:9092", "test", serializer);
        var extChannel = new ExternalEventChannel(transport);
        var broadcastChannel = new BroadcastEventChannel(broadcastTransport);
        return SpringEventPublisherBuilder.create(intChannel, extChannel, broadcastChannel)
                .retryable()
                .silent()
                .transactional()
                .async(Executors.newFixedThreadPool(2))
                .buildAndLog();
    }

    /**
     * Creates and configures the event dispatcher.
     * Sets up local and Kafka transports with idempotent processing and thread pool execution.
     *
     * @param eventHandlerRegistry the event handler registry to use for dispatching events
     * @return configured and started event dispatcher
     */
    @Bean
    EventDispatcher eventDispatcher(EventHandlerRegistry eventHandlerRegistry) {
        var localTransport = new LocalQueueInTransport(eventQueue);
        var kafkaTransport = new KafkaInTransport("localhost:9092", "test", "group");
        EventDispatcher dispatcher = EventDispatcherBuilder.create()
                .addTransports(localTransport, kafkaTransport)
                .idempotent()
                .executor(Executors.newFixedThreadPool(5))
                .handlerRegistry(eventHandlerRegistry)
                .build();
        dispatcher.start(dispatcher::dispatch);
        return dispatcher;
    }
}
