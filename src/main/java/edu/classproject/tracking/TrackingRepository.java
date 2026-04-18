package edu.classproject.tracking;

import java.util.List;

public interface TrackingRepository {
    void save(TrackingEvent event);
    List<TrackingEvent> findByOrderId(String orderId);
}