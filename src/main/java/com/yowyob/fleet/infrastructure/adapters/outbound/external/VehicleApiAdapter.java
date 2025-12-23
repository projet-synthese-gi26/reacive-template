package com.yowyob.fleet.infrastructure.adapters.outbound.external;

import com.yowyob.fleet.domain.model.Vehicle;
import com.yowyob.fleet.domain.ports.out.ExternalVehiclePort;
import com.yowyob.fleet.infrastructure.adapters.outbound.external.client.VehicleApiClient;
import com.yowyob.fleet.infrastructure.adapters.outbound.external.dto.VehicleExternalResponse; // AJOUTE CET IMPORT
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VehicleApiAdapter implements ExternalVehiclePort {

    private final VehicleApiClient vehicleApiClient;

    @Override
    public Mono<Vehicle> getExternalVehicleInfo(UUID vehicleId) {
        return vehicleApiClient.getVehicleById(vehicleId)
                .map(ext -> new Vehicle(
                        ext.id(),
                        null, // fleetId will be populated by the application service
                        ext.licensePlate(),
                        ext.brand(),
                        ext.model(),
                        ext.manufacturingYear(),
                        ext.type(),
                        ext.color(),
                        null, null, null // Local parameters to be filled later
                ))
                .onErrorResume(e -> {
                    System.err.println("⚠️ External Vehicle Service Error: " + e.getMessage());
                    return Mono.empty();
                });
    }
}