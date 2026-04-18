package edu.classproject.tracking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrackingRepositoryTest {

    private InMemoryTrackingRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTrackingRepository();
    }

    @Test
    void happyPath_eventsReturnedSortedByOccurredAt() {
        TrackingEvent first = new TrackingEvent(
                "order-1", "PLACED", "Order placed",
                Instant.parse("2024-01-01T10:00:00Z")
        );

        TrackingEvent second = new TrackingEvent(
                "order-1", "CONFIRMED", "Order confirmed",
                Instant.parse("2024-01-01T11:00:00Z")
        );

        TrackingEvent third = new TrackingEvent(
                "order-1", "OUT_FOR_DELIVERY", "On the way",
                Instant.parse("2024-01-01T12:00:00Z")
        );

        repository.save(second);
        repository.save(third);
        repository.save(first);

        List<TrackingEvent> timeline = repository.findByOrderId("order-1");

        assertEquals(3, timeline.size());
        assertEquals("PLACED", timeline.get(0).status());
        assertEquals("CONFIRMED", timeline.get(1).status());
        assertEquals("OUT_FOR_DELIVERY", timeline.get(2).status());
    }

    @Test
    void unknownOrderId_returnsEmptyList() {
        List<TrackingEvent> timeline = repository.findByOrderId("nonexistent-order");

        assertNotNull(timeline);
        assertTrue(timeline.isEmpty());
    }

    @Test
    void multipleEvents_sameOrder_allPresentAndSorted() {
        TrackingEvent e1 = new TrackingEvent(
                "order-2", "PLACED", "Order placed",
                Instant.parse("2024-01-01T08:00:00Z")
        );

        TrackingEvent e2 = new TrackingEvent(
                "order-2", "PREPARING", "Being prepared",
                Instant.parse("2024-01-01T09:00:00Z")
        );

        TrackingEvent e3 = new TrackingEvent(
                "order-2", "READY_FOR_PICKUP", "Ready",
                Instant.parse("2024-01-01T10:00:00Z")
        );

        TrackingEvent e4 = new TrackingEvent(
                "order-2", "OUT_FOR_DELIVERY", "On the way",
                Instant.parse("2024-01-01T11:00:00Z")
        );

        TrackingEvent e5 = new TrackingEvent(
                "order-2", "DELIVERED", "Delivered",
                Instant.parse("2024-01-01T12:00:00Z")
        );

        repository.save(e3);
        repository.save(e1);
        repository.save(e5);
        repository.save(e2);
        repository.save(e4);

        List<TrackingEvent> timeline = repository.findByOrderId("order-2");

        assertEquals(5, timeline.size());
        assertEquals("PLACED", timeline.get(0).status());
        assertEquals("PREPARING", timeline.get(1).status());
        assertEquals("READY_FOR_PICKUP", timeline.get(2).status());
        assertEquals("OUT_FOR_DELIVERY", timeline.get(3).status());
        assertEquals("DELIVERED", timeline.get(4).status());
    }
}