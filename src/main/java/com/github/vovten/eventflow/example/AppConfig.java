package com.github.vovten.eventflow.example;

import com.github.vovten.eventflow.channel.InternalEventChannel;
import com.github.vovten.eventflow.dispatcher.EventDispatcher;
import com.github.vovten.eventflow.dispatcher.UnifiedEventDispatcher;
import com.github.vovten.eventflow.publisher.EventPublisher;
import com.github.vovten.eventflow.publisher.EventPublisherBuilder;
import com.github.vovten.eventflow.registry.EventHandlerRegistry;
import com.github.vovten.eventflow.registry.SpringEventListenerRegistry;
import com.github.vovten.eventflow.transport.DefaultQueueProvider;
import com.github.vovten.eventflow.transport.QueueProvider;
import com.github.vovten.eventflow.transport.incoming.InMemoryIncomingEventTransport;
import com.github.vovten.eventflow.transport.outgoing.InMemoryOutgoingEventTransport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Bean
    public EventPublisher eventPublisher() {
        var internalChannel = new InternalEventChannel(
                new InMemoryOutgoingEventTransport(queueProvider().getQueue("in-memory"))
        );
        return EventPublisherBuilder.channels(internalChannel).build();
    }

    @Bean
    public EventDispatcher eventDispatcher(ApplicationContext appContext) {
        UnifiedEventDispatcher eventDispatcher = new UnifiedEventDispatcher(
                Executors.newSingleThreadExecutor(),
                springHandlerRegistry(appContext),
                List.of(new InMemoryIncomingEventTransport(queueProvider().getQueue("in-memory")))
        );
        eventDispatcher.start();
        return eventDispatcher;
    }

    @Bean
    public EventHandlerRegistry springHandlerRegistry(ApplicationContext appContext) {
        return new SpringEventListenerRegistry(
                appContext, "com.github.vovten.eventflow.example"
        );
    }

    @Bean
    public QueueProvider queueProvider(){
        return new DefaultQueueProvider(100);
    }
}
