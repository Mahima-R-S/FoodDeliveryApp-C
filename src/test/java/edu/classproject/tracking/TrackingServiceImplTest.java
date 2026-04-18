package edu.classproject.tracking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrackingServiceImplTest {

    private TrackingService service;

    @BeforeEach
    void setUp() {
        service = new TrackingServiceImpl(new InMemoryTrackingRepository());
    }


    @Test
    void addEvent_thenTimeline_returnsSingleEvent() {
        TrackingEvent event = new TrackingEvent(
                "ORDER-1", "ORDER_PLACED", "Order received", Instant.now());

        service.addEvent(event);

        List<TrackingEvent> result = service.timeline("ORDER-1");
        assertEquals(1, result.size());
        assertEquals("ORDER_PLACED", result.get(0).status());
    }

    // ── Edge / failure cases ──────────────────────────────────────

    @Test
    void timeline_unknownOrderId_returnsEmptyList() {
        List<TrackingEvent> result = service.timeline("ORDER-DOES-NOT-EXIST");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void addEvent_nullEvent_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.addEvent(null));
    }

    @Test
    void timeline_nullOrderId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.timeline(null));
    }

    @Test
    void timeline_blankOrderId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.timeline("   "));
    }
}