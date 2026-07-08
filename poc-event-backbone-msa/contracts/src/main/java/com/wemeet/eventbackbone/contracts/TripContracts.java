package com.wemeet.eventbackbone.contracts;

/** 공유 contracts — TMS 소유. */
public final class TripContracts {
    private TripContracts() {}

    @EventContract(type = "tms.cmd.create_trip", version = 1)
    public record CreateTrip(String orderId, String amount, String currency) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }
    @EventContract(type = "tms.cmd.cancel_trip", version = 1)
    public record CancelTrip(String tripId) implements DomainEvent {
        @Override public String aggregateId() { return tripId; }
    }

    @EventContract(type = "tms.trip.dispatched", version = 1)
    public record TripDispatched(String tripId, String orderId, String carrierId) implements DomainEvent {
        @Override public String aggregateId() { return tripId; }
    }
    @EventContract(type = "tms.trip.creation_failed", version = 1)
    public record TripCreationFailed(String orderId, String reason) implements DomainEvent {
        @Override public String aggregateId() { return orderId; }
    }
}
