package com.github.vovten.eventflow.example;

import com.github.vovten.eventflow.event.Event;
import com.github.vovten.eventflow.serialization.EventSerializer;
import com.github.vovten.eventflow.util.EventUtils;
import org.springframework.stereotype.Component;

/**
 * Custom JSON serializer for event serialization.
 * Implements the EventSerializer interface to provide JSON-based serialization
 * and deserialization of events using the event-flow library's utilities.
 */
@Component
public class MyJsonSerializer implements EventSerializer {

    @Override
    public byte[] serialize(Event event) {
        return event.asJson().getBytes();
    }

    @Override
    public <T extends Event> T deserialize(byte[] data, Class<T> eventType) {
        return EventUtils.fromJson(new String(data), eventType);
    }

    @Override
    public byte getCode() {
        return 3;
    }

    @Override
    public String getName() {
        return "myJson";
    }
}
