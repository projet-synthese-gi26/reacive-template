package com.yowyob.fleet.infrastructure.adapters.outbound.external.client;

import com.yowyob.fleet.infrastructure.adapters.outbound.external.dto.VehicleExternalResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;
import java.util.UUID;

@HttpExchange("/vehicles")
public interface VehicleApiClient {

    @GetExchange("/{id}")
    Mono<VehicleExternalResponse> getVehicleById(@PathVariable UUID id);
}