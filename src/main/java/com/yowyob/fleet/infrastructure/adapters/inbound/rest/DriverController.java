package com.yowyob.fleet.infrastructure.adapters.inbound.rest;

import com.yowyob.fleet.domain.model.Driver;
import com.yowyob.fleet.domain.ports.in.ManageDriverUseCase;
import com.yowyob.fleet.infrastructure.adapters.inbound.rest.dto.DriverRegistrationRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
@Tag(name = "Drivers", description = "Operations for Driver Management")
public class DriverController {

    private final ManageDriverUseCase driverUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new Driver (Remote Auth + Local Profile)")
    public Mono<Driver> register(@RequestBody DriverRegistrationRequest request) {
        return driverUseCase.registerDriver(request);
    }

    @GetMapping
    @Operation(summary = "List all drivers", description = "Retrieve all drivers with optional status filtering")
    public Flux<Driver> list(
        @Parameter(
            name = "status", 
            description = "Filter by active (true) or inactive (false) status", 
            in = ParameterIn.QUERY, 
            schema = @Schema(type = "boolean")
        ) 
        @RequestParam(required = false) Boolean status
    ) {
        return driverUseCase.getAllDrivers(status);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get driver by ID")
    public Mono<Driver> get(
        @Parameter(
            name = "userId",
            description = "Unique identifier of the driver",
            in = ParameterIn.PATH,
            schema = @Schema(type = "string", format = "uuid")
        )
        @PathVariable UUID userId
    ) {
        return driverUseCase.getDriverById(userId);
    }

    @PostMapping("/{userId}/assign-vehicle")
    @Operation(summary = "Assign a vehicle to a driver")
    public Mono<Void> assign(@PathVariable UUID userId, @RequestBody VehicleAssignRequest req) {
        return driverUseCase.assignVehicle(userId, req.vehicleId());
    }

    @PostMapping("/{userId}/unassign-vehicle")
    @Operation(summary = "Remove vehicle assignment from driver")
    public Mono<Void> unassign(@PathVariable UUID userId) {
        return driverUseCase.unassignVehicle(userId);
    }

    public record VehicleAssignRequest(UUID vehicleId) {}
}