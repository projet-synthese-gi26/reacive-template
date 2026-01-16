package com.yowyob.fleet.domain.ports.out;

import com.yowyob.fleet.domain.model.Driver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface DriverPersistencePort {
    Mono<Driver> save(Driver driver);
    Mono<Driver> findById(UUID userId);
    Flux<Driver> findAll(Boolean status);
    Mono<Void> updateVehicleAssignment(UUID userId, UUID vehicleId);
}