package com.yowyob.fleet.domain.model;

import java.util.UUID;

public record Vehicle(
    UUID id,
    UUID fleetId,
    String licensePlate,
    String brand,
    String model,
    Integer manufacturingYear,
    String type,
    String color,
    VehicleParameters.Financial financialParameters,
    VehicleParameters.Maintenance maintenanceParameters,
    VehicleParameters.Operational operationalParameters
) {}