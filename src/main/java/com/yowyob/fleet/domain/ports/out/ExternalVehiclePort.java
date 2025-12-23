package com.yowyob.fleet.domain.ports.out;

import com.yowyob.fleet.domain.model.Vehicle;
import reactor.core.publisher.Mono;
import java.util.UUID;

/**
 * Port used by the domain to fetch technical details from the external Vehicle Service.
 */
public interface ExternalVehiclePort {
    /**
     * Fetches details (brand, model, etc.) for a specific vehicle ID.
     */
    Mono<Vehicle> getExternalVehicleInfo(UUID vehicleId);
}