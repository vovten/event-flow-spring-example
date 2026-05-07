package io.github.vovten.eventflow.example.event;

import io.github.vovten.eventflow.channel.ExternalEventChannel;
import io.github.vovten.eventflow.channel.InternalEventChannel;
import io.github.vovten.eventflow.event.annotation.Event;

@Event(channels = {InternalEventChannel.class, ExternalEventChannel.class})
public record EnvelopeDomainOrderEvent(long orderId) {
}
