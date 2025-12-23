package com.yowyob.fleet.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FleetRequest(
    @NotBlank String name,
    @NotNull UUID managerUserId
) {}