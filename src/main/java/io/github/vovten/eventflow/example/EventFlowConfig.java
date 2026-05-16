package io.github.vovten.eventflow.example;

import io.github.vovten.eventflow.channel.BroadcastEventChannel;
import io.github.vovten.eventflow.channel.ExternalEventChannel;
import io.github.vovten.eventflow.channel.InternalEventChannel;
import io.github.vovten.eventflow.dispatcher.EventDispatcher;
import io.github.vovten.eventflow.dispatcher.EventDispatcherBuilder;
import io.github.vovten.eventflow.event.Event;
import io.github.vovten.eventflow.publisher.EventPublisher;
import io.github.vovten.eventflow.publisher.SpringEventPublisherBuilder;
import io.github.vovten.eventflow.registry.EventHandlerRegistry;
import io.github.vovten.eventflow.serialization.EventSerializerFactory;
import io.github.vovten.eventflow.transport.incoming.KafkaInTransport;
import io.github.vovten.eventflow.transport.incoming.LocalQueueInTransport;
import io.github.vovten.eventflow.transport.outgoing.BroadcastKafkaOutTransport;
import io.github.vovten.eventflow.transport.outgoing.KafkaOutTransport;
import io.github.vovten.eventflow.transport.outgoing.LocalQueueOutTransport;
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
//@Configuration
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
    public EventSerializerFactory serializerFactory() {
        var serializer = new MyJsonSerializer();
        var serializerFactory = new EventSerializerFactory();
        serializerFactory.register(serializer);
        return serializerFactory;
    }

    /**
     * Creates and configures the event publisher with multiple event channels.
     * Sets up internal, external, and broadcast channels with appropriate transports.
     * The publisher is configured with retry, silent, transactional, and async capabilities.
     *
     * @return configured event publisher
     */
    @Bean
    public EventPublisher eventPublisher(EventSerializerFactory serializerFactory) {
        var intChannel = new InternalEventChannel(new LocalQueueOutTransport(eventQueue));
        var serializer = serializerFactory.getByName("myJson");
        var transport = new KafkaOutTransport("localhost:9092", "test", serializer);
        var broadcastTransport = new BroadcastKafkaOutTransport("localhost:9092", "test2", serializer);
        var extChannel = new ExternalEventChannel(transport);
        var broadcastChannel = new BroadcastEventChannel(broadcastTransport);
        return SpringEventPublisherBuilder.create(intChannel, extChannel, broadcastChannel)
                .retryable()
                .transactional()
                .buildAndLog();
    }

    /**
     * Creates and configures the event dispatcher.
     * Sets up local and Kafka transports with idempotent processing and thread pool execution.
     *
     * @return configured and started event dispatcher
     */
    @Bean
    public EventDispatcher eventDispatcher(EventHandlerRegistry eventHandlerRegistry,
                                           EventSerializerFactory serializerFactory) {
        var localTransport = new LocalQueueInTransport(eventQueue);
        var kafkaTransport = new KafkaInTransport("localhost:9092", "test2",
                "group", serializerFactory);
        EventDispatcher dispatcher = EventDispatcherBuilder.create()
                .addTransports(localTransport, kafkaTransport)
                .idempotent()
                .executor(Executors.newFixedThreadPool(5))
                .handlerRegistry(eventHandlerRegistry)
                .buildAndLog();
        dispatcher.start(dispatcher::dispatch);
        return dispatcher;
    }
}
