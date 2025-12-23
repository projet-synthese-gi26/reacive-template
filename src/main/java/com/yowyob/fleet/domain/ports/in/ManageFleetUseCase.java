package com.yowyob.fleet.domain.ports.in;

import com.yowyob.fleet.domain.model.Fleet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface ManageFleetUseCase {
    Mono<Fleet> createFleet(Fleet fleet);
    Mono<Fleet> getFleetById(UUID id);
    Flux<Fleet> getAllFleets();
    Mono<Fleet> updateFleet(UUID id, Fleet fleet);
    Mono<Fleet> patchFleet(UUID id, Fleet fleet);
    Mono<Void> deleteFleet(UUID id);
}