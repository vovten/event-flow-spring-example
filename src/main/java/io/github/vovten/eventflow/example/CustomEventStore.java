package io.github.vovten.eventflow.example;

import io.github.vovten.eventflow.lifecycle.store.EventStatus;
import io.github.vovten.eventflow.lifecycle.store.EventStore;
import io.github.vovten.eventflow.lifecycle.store.StoredEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Custom event store
 */
@Component
public class CustomEventStore implements EventStore {
    @Override
    public String getType() {
        return "custom";
    }

    @Override
    public void save(StoredEvent event) {
        System.out.println("-------- Event saved");
    }

    @Override
    public void updateStatus(UUID eventId, EventStatus status, String errorDetails) {
        System.out.println("-------- Event updated");
    }

    @Override
    public List<StoredEvent> findByStatus(EventStatus status, Instant before) {
        return List.of();
    }

    @Override
    public Optional<StoredEvent> findById(UUID eventId) {
        return Optional.empty();
    }
}
