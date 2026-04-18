package edu.classproject.tracking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTrackingRepository implements TrackingRepository {

    private final Map<String, List<TrackingEvent>> store = new HashMap<>();

    @Override
    public void save(TrackingEvent event) {
        store.computeIfAbsent(event.orderId(), k -> new ArrayList<>()).add(event);
    }

    @Override
    public List<TrackingEvent> findByOrderId(String orderId) {
        return store.getOrDefault(orderId, new ArrayList<>())
                .stream()
                .sorted(Comparator.comparing(TrackingEvent::occurredAt))
                .toList();
    }
}