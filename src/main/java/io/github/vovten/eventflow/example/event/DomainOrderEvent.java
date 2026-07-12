package io.github.vovten.eventflow.example.event;

import io.github.vovten.eventflow.channel.ExternalEventChannel;
import io.github.vovten.eventflow.channel.InternalEventChannel;
import io.github.vovten.eventflow.event.annotation.Event;
import io.github.vovten.eventflow.lifecycle.EventLifecycle;

@Event(channels = {InternalEventChannel.class, ExternalEventChannel.class}, lifecycle = EventLifecycle.PERSISTED)
public record DomainOrderEvent(long orderId) {
}
