package io.github.vovten.eventflow.example.event;

import io.github.vovten.eventflow.channel.ExternalEventChannel;
import io.github.vovten.eventflow.channel.InternalEventChannel;
import io.github.vovten.eventflow.event.DomainEvent;

@DomainEvent(channels = {InternalEventChannel.class, ExternalEventChannel.class})
public record DomainOrderEvent(long orderId) {
}
