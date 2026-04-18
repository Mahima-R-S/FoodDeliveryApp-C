package edu.classproject.tracking;

import java.util.List;

public class TrackingServiceImpl implements TrackingService {

    private final TrackingRepository repository;

    public TrackingServiceImpl(TrackingRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("TrackingRepository must not be null");
        }
        this.repository = repository;
    }

    @Override
    public void addEvent(TrackingEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("TrackingEvent must not be null");
        }
        if (event.orderId() == null || event.orderId().isBlank()) {
            throw new IllegalArgumentException("TrackingEvent.orderId must not be null or blank");
        }
        repository.save(event);
    }

    @Override
    public List<TrackingEvent> timeline(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId must not be null or blank");
        }
        return repository.findByOrderId(orderId);
    }
}