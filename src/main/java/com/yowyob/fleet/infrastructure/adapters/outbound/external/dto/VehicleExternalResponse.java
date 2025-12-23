package com.yowyob.fleet.infrastructure.adapters.outbound.external.dto;

import java.util.UUID;

/**
 * Technical data received from the external Vehicle Service.
 */
public record VehicleExternalResponse(
    UUID id,
    String licensePlate,
    String brand,
    String model,
    Integer manufacturingYear,
    String type,
    String color
) {}