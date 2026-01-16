package com.yowyob.fleet.domain.model;

import java.util.UUID;

public record Driver(
    UUID userId,
    String licenceNumber,
    Boolean status,
    UUID assignedVehicleId
) {}