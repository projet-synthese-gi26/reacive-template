package com.yowyob.fleet.infrastructure.mappers;

import com.yowyob.fleet.domain.model.Fleet;
import com.yowyob.fleet.infrastructure.adapters.inbound.rest.dto.FleetRequest;
import com.yowyob.fleet.infrastructure.adapters.inbound.rest.dto.FleetResponse;
import com.yowyob.fleet.infrastructure.adapters.outbound.persistence.entity.FleetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface FleetMapper {

    @Mapping(target = "fleetManagerId", source = "managerUserId")
    @Mapping(target = "createdAt", source = "creationDate")
    @Mapping(target = "phoneNumber", ignore = true) 
    FleetEntity toEntity(Fleet domain);
    
    @Mapping(target = "managerUserId", source = "fleetManagerId")
    @Mapping(target = "creationDate", source = "createdAt")
    @Mapping(target = "vehicleCount", ignore = true)
    Fleet toDomain(FleetEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "vehicleCount", ignore = true)
    Fleet toDomain(FleetRequest request);

    FleetResponse toResponse(Fleet domain);

    /**
     * Custom mapping: LocalDate to Instant (Domain to Entity)
     * Sets the time to the start of the day in UTC.
     */
    default Instant map(LocalDate date) {
        return date == null ? null : date.atStartOfDay(ZoneOffset.UTC).toInstant();
    }

    /**
     * Custom mapping: Instant to LocalDate (Entity to Domain)
     * Extracts the date part from the UTC instant.
     */
    default LocalDate map(Instant instant) {
        return instant == null ? null : LocalDate.ofInstant(instant, ZoneOffset.UTC);
    }
}