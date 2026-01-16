package com.yowyob.fleet.infrastructure.mappers;

import com.yowyob.fleet.domain.model.Driver;
import com.yowyob.fleet.infrastructure.adapters.outbound.persistence.entity.DriverEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    DriverEntity toEntity(Driver domain);
    Driver toDomain(DriverEntity entity);
}