# 20-Day Implementation Plan: Smart Campus JAX-RS API

> [!WARNING]
> Ensure strictly NO Spring Boot usage and NO external databases per assignment instructions.

## Phase 1: Setup, Architecture, & Discovery (Part 1)
- **Day 1: Project Reset & Core Models.** 
  - Initialize clean Maven project with JAX-RS (Jersey) and Grizzly. 
  - Create the exact `Room`, `Sensor`, and `SensorReading` POJOs. 
- **Day 2: In-Memory Data Store.**
  - Create Singleton `DataStore` using `ConcurrentHashMap`.
- **Day 3: Application Configuration & Discovery Endpoint.**
  - Implement `Application` class at `@ApplicationPath("/api/v1")`.
  - Create `DiscoveryResource` endpoint (`GET /api/v1`).
- **Day 4: Report Draft (Part 1).**
  - Write answers for JAX-RS lifecycle and HATEOAS in `README.md`.

## Phase 2: Room Management (Part 2)
- **Day 5: Room Creation & Retrieval.**
  - Implement `RoomResource` `GET /` and `POST /`.
- **Day 6: Room Detailed Retrieval.**
  - Implement `GET /{roomId}`.
- **Day 7: Room Deletion with Safety Logic.**
  - Implement `DELETE /{roomId}` (block if sensors assigned).
- **Day 8: Report Draft (Part 2).**
  - Answer full object vs ID and Idempotent DELETE.

## Phase 3: Sensor Operations & Filtering (Part 3)
- **Day 9: Sensor Registration & Integrity Validation.**
  - Implement `SensorResource` `POST /` requiring valid Room relation.
- **Day 10: Filtered Sensor Retrieval.**
  - Implement `GET /` with `@QueryParam("type")` filtering.
- **Day 11: Report Draft (Part 3).**
  - Answer `@Consumes` error handling and Query vs Path params.

## Phase 4: Sub-Resources & Deep Nesting (Part 4)
- **Day 12: Sub-Resource Locator Pattern Setup.**
  - Create `SensorReadingResource` and locator method in `SensorResource`.
- **Day 13: Sensor Reading Operations.**
  - Implement `POST /` and `GET /` handling for historical readings.
- **Day 14: Cross-Resource State Updates & Draft Report.**
  - Update parent `Sensor.currentValue` on reading POST. Answer locator pattern report.

## Phase 5: Error Handling & Observability (Part 5)
- **Day 15: Conflict Error Handling.**
  - Create `RoomNotEmptyException` + Mapper (HTTP 409).
- **Day 16: Validation & Custom Error Handling.**
  - Create `LinkedResourceNotFoundException` + Mapper (HTTP 422). Answer 422 vs 404 report.
- **Day 17: Security & Global Safety Net.**
  - Create `SensorUnavailableException` (403) and `ThrowableMapper` (500). Answer stack trace risks report.
- **Day 18: API Request/Response Logging.**
  - Implement `ContainerRequestFilter` and `ContainerResponseFilter` using Java Logger. Answer filters report.

## Phase 6: Final Polish & Delivery
- **Day 19: Documentation Complete.**
  - Finalize `README.md` with build/run instructions and 5 `curl` samples.
- **Day 20: Video Recording & Final Testing.**
  - Manual testing using Postman.
  - Record mandatory 10-minute video presentation.
