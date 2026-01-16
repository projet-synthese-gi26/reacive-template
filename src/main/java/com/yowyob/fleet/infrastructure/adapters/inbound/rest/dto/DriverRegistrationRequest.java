package com.yowyob.fleet.infrastructure.adapters.inbound.rest.dto;

import java.util.List;

public record DriverRegistrationRequest(
    // Infos TraMaSys
    String username,
    String password,
    String email,
    String phone,
    String firstName,
    String lastName,
    
    // Infos Métier (Locales)
    String licenceNumber
) {}