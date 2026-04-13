## Synopsis — Team 13: Live Tracking Timeline

### Overview

Team 13 is responsible for the **Live Tracking Timeline** module of a framework-free Java food delivery application. The module records and retrieves an ordered timeline of milestone events for a delivery order — from placement through delivery — and exposes this as a clean interface consumed by other modules (e.g., Notifications, Dispatch).


### Architecture & Design

The module follows **interface-driven design** as mandated by the project's extension rules: all inter-module dependencies go through interfaces, not concrete implementations.

**Key classes and their roles:**

- **`TrackingService` (Interface)** — The public contract of the module. Exposes two methods: `addEvent(event: TrackingEvent): void` and `timeline(orderId: String): List<TrackingEvent>`. All external modules depend only on this interface.

- **`TrackingServiceImpl` (Class)** — The concrete implementation. Validates all inputs (null event, blank orderId) and delegates persistence to a `TrackingRepository`. Returns the sorted timeline to the caller.

- **`TrackingEvent` (Record)** — An immutable value object holding `orderId`, `status`, `message`, and `occurredAt` as fields. Being a Java record enforces immutability — tracking events cannot be modified after creation.

- **`TrackingRepository` (Interface)** — The persistence contract of the module. Exposes `save(event: TrackingEvent): void` and `findByOrderId(orderId: String): List<TrackingEvent>`. Owned by this team.

- **`InMemoryTrackingRepository` (Class)** — The concrete persistence implementation using a `HashMap<String, List<TrackingEvent>>` where the key is `orderId`. Retrieves events sorted ascending by `occurredAt` on every read.

---

### Supported Milestones

| Status | Meaning |
|---|---|
| `ORDER_PLACED` | Order received by the system |
| `PAYMENT_CONFIRMED` | Payment authorised |
| `PREPARING` | Restaurant started preparing the order |
| `OUT_FOR_DELIVERY` | Driver picked up the order |
| `DELIVERED` | Order delivered to customer |

Any non-null String is accepted as a status so that future milestones can be added without changing this module's code.

---

### Responsibilities

- Implement and test all classes shown in the class diagram.
- Provide an in-memory `TrackingRepository` implementation (the interface is team-owned).
- Ensure `TrackingServiceImpl` correctly validates inputs and delegates to the repository.
- Sort timeline events by `occurredAt` ascending on every retrieval.
- Return an empty list (never null or exception) when an unknown `orderId` is queried.
- Wire `TrackingServiceImpl` into `DemoApplication` with a full 5-milestone demo scenario.

---

### Design Decisions

- **Sort on read, not on write** — Events are stored in insertion order and sorted in `findByOrderId`. This keeps writes simple and flexible.
- **Caller sets the timestamp** — `TrackingServiceImpl` does not call `Instant.now()`. The event is stored exactly as provided, making tests deterministic.
- **No orderId existence check** — This module does not validate whether an orderId exists in the Order module. This avoids a circular dependency and keeps the module self-contained.
- **Constructor injection** — `TrackingServiceImpl` receives its `TrackingRepository` through the constructor, making it fully testable and the repository swappable without changing service code.

---

### Integration Points

| Depends on | Provided by |
|---|---|
| `orderId` (used as key for events) | Order module (Team 9) |
| Milestone triggers (addEvent calls) | Dispatch module (Team 12) |

| Exposes | Consumed by |
|---|---|
| `TrackingService` interface | Notification module (Team 14) |
| `List<TrackingEvent>` timeline | Support module (Team 16), Admin Analytics (Team 17) |

---

### Tech Stack

Java 17+, Maven, JUnit 5, in-memory repositories — no external frameworks.
