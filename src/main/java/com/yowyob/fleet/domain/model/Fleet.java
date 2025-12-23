package com.yowyob.fleet.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public record Fleet(
    UUID id,
    String name,
    LocalDate creationDate,
    UUID managerUserId,
    Integer vehicleCount
) {}