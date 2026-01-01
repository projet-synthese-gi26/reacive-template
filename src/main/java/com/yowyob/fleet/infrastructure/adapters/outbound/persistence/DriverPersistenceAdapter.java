package com.yowyob.fleet.infrastructure.adapters.outbound.persistence;

import com.yowyob.fleet.domain.model.Driver;
import com.yowyob.fleet.domain.ports.out.DriverPersistencePort;
import com.yowyob.fleet.infrastructure.adapters.outbound.persistence.repository.DriverR2dbcRepository;
import com.yowyob.fleet.infrastructure.mappers.DriverMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DriverPersistenceAdapter implements DriverPersistencePort {

    private final DriverR2dbcRepository repository;
    private final DriverMapper mapper;

    @Override
    public Mono<Driver> save(Driver driver) {
        return repository.save(mapper.toEntity(driver))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Driver> findById(UUID userId) {
        return repository.findById(userId)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Driver> findAll(Boolean status) {
        if (status != null) {
            return repository.findByStatus(status).map(mapper::toDomain);
        }
        return repository.findAll().map(mapper::toDomain);
    }

    @Override
    public Mono<Void> updateVehicleAssignment(UUID userId, UUID vehicleId) {
        return repository.findById(userId)
                .flatMap(entity -> {
                    entity.setAssignedVehicleId(vehicleId);
                    return repository.save(entity);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Chauffeur non trouvé")))
                .then();
    }
}