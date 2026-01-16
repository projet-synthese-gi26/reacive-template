package com.yowyob.fleet.infrastructure.mappers;

import com.yowyob.fleet.domain.model.Vehicle;
import com.yowyob.fleet.domain.model.VehicleParameters;
import com.yowyob.fleet.infrastructure.adapters.outbound.persistence.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleLocalMapper {

    // Mapping to Pivot Table (vehicles)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fleetId", source = "fleetId")
    @Mapping(target = "driverId", ignore = true) // Will be handled during assignment task
    @Mapping(target = "zoneId", ignore = true)   // Nouveau champ dans l'entité, ignoré pour l'instant
    @Mapping(target = "licensePlate", source = "licensePlate")
    // NOTE: 'userId' a été supprimé ici car supprimé de l'entité
    VehicleLocalEntity toVehicleEntity(Vehicle domain);

    // Mapping to Financial Table
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicleId", source = "id")
    @Mapping(target = "insuranceNumber", source = "financialParameters.insuranceNumber")
    @Mapping(target = "insuranceExpiredAt", source = "financialParameters.insuranceExpiryDate")
    @Mapping(target = "registeredAt", source = "financialParameters.registrationDate")
    @Mapping(target = "purchasedAt", source = "financialParameters.purchaseDate")
    @Mapping(target = "depreciationRate", source = "financialParameters.depreciationRate")
    @Mapping(target = "costPerKm", source = "financialParameters.costPerKm")
    FinancialParameterEntity toFinancialEntity(Vehicle domain);

    // Mapping to Maintenance Table
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicleId", source = "id")
    @Mapping(target = "lastMaintenanceAt", source = "maintenanceParameters.lastMaintenanceDate")
    @Mapping(target = "nextMaintenanceAt", source = "maintenanceParameters.nextMaintenanceDue")
    @Mapping(target = "engineStatus", source = "maintenanceParameters.engineStatus")
    @Mapping(target = "batteryHealth", expression = "java(domain.maintenanceParameters() != null && domain.maintenanceParameters().batteryHealth() != null ? String.valueOf(domain.maintenanceParameters().batteryHealth()) : null)") 
    @Mapping(target = "maintenanceStatus", source = "maintenanceParameters.maintenanceStatus")
    MaintenanceParameterEntity toMaintenanceEntity(Vehicle domain);

    // Domain reconstruction (Helper)
    default Vehicle toDomain(VehicleLocalEntity v, FinancialParameterEntity f, MaintenanceParameterEntity m) {
        if (v == null) return null;
        return new Vehicle(
            v.getId(),
            v.getFleetId(),
            v.getLicensePlate(), // Récupération locale
            null, null, null, null, null, // Remote data (brand, model...) ne sont pas stockés localement sauf licensePlate
            f == null ? null : new VehicleParameters.Financial(
                f.getInsuranceNumber(), f.getInsuranceExpiredAt(), f.getRegisteredAt(), f.getPurchasedAt(), f.getDepreciationRate(), f.getCostPerKm()
            ),
            m == null ? null : new VehicleParameters.Maintenance(
                m.getLastMaintenanceAt(), m.getNextMaintenanceAt(), m.getEngineStatus(), 
                m.getBatteryHealth() != null ? tryParseInt(m.getBatteryHealth()) : null, 
                m.getMaintenanceStatus()
            ),
            null
        );
    }

    default Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}