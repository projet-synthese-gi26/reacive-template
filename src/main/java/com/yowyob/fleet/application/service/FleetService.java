package com.yowyob.fleet.application.service;

import com.yowyob.fleet.domain.model.Fleet;
import com.yowyob.fleet.domain.ports.in.ManageFleetUseCase;
import com.yowyob.fleet.domain.ports.out.FleetRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FleetService implements ManageFleetUseCase {

    private final FleetRepositoryPort fleetRepository;

    @Override
    public Mono<Fleet> createFleet(Fleet fleet) {
        // Business logic: ensure creation date is set if not provided
        Fleet fleetToSave = new Fleet(
                fleet.id(),
                fleet.name(),
                fleet.creationDate() != null ? fleet.creationDate() : LocalDate.now(),
                fleet.managerUserId(),
                0
        );
        return fleetRepository.save(fleetToSave);
    }

    @Override
    public Mono<Fleet> getFleetById(UUID id) {
        return fleetRepository.findById(id);
    }

    @Override
    public Flux<Fleet> getAllFleets() {
        return fleetRepository.findAll();
    }

    @Override
    public Mono<Fleet> updateFleet(UUID id, Fleet fleet) {
        return fleetRepository.findById(id)
                .flatMap(existingFleet -> {
                    Fleet updated = new Fleet(
                            id,
                            fleet.name(),
                            existingFleet.creationDate(), // Keep original creation date
                            fleet.managerUserId(),
                            existingFleet.vehicleCount()
                    );
                    return fleetRepository.save(updated);
                });
    }

    @Override
    public Mono<Fleet> patchFleet(UUID id, Fleet fleet) {
        return fleetRepository.findById(id)
                .flatMap(existing -> {
                    // Logic: Update only non-null fields from the request
                    Fleet patched = new Fleet(
                            id,
                            fleet.name() != null ? fleet.name() : existing.name(),
                            existing.creationDate(),
                            fleet.managerUserId() != null ? fleet.managerUserId() : existing.managerUserId(),
                            existing.vehicleCount()
                    );
                    return fleetRepository.save(patched);
                });
    }

    @Override
    public Mono<Void> deleteFleet(UUID id) {
        return fleetRepository.deleteById(id);
    }
}