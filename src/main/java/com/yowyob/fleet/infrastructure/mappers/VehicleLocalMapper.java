package com.yowyob.fleet.infrastructure.mappers;

import com.yowyob.fleet.domain.model.Vehicle;
import com.yowyob.fleet.domain.model.VehicleParameters;
import com.yowyob.fleet.infrastructure.adapters.outbound.persistence.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class VehicleLocalMapper {

    // --- Vers ENTITY ---

    @Mapping(target = "id", source = "id")
    @Mapping(target = "fleetId", source = "fleetId")
    @Mapping(target = "licensePlate", source = "licensePlate")
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "model", source = "model")
    @Mapping(target = "manufacturingYear", source = "manufacturingYear")
    @Mapping(target = "color", source = "color")
    @Mapping(target = "currentDriverId", ignore = true)
    @Mapping(target = "vehicleTypeId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    public abstract VehicleLocalEntity toVehicleEntity(Vehicle domain);

    // --- Vers ENTITY (Paramètres) ---

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicleId", source = "id")
    @Mapping(target = "insuranceNumber", source = "financialParameters.insuranceNumber")
    @Mapping(target = "insuranceExpiredAt", source = "financialParameters.insuranceExpiryDate")
    @Mapping(target = "registeredAt", source = "financialParameters.registrationDate")
    @Mapping(target = "purchasedAt", source = "financialParameters.purchaseDate")
    @Mapping(target = "depreciationRate", source = "financialParameters.depreciationRate")
    @Mapping(target = "costPerKm", source = "financialParameters.costPerKm")
    public abstract FinancialParameterEntity toFinancialEntity(Vehicle domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicleId", source = "id")
    @Mapping(target = "lastMaintenanceAt", source = "maintenanceParameters.lastMaintenanceDate")
    @Mapping(target = "nextMaintenanceAt", source = "maintenanceParameters.nextMaintenanceDue")
    @Mapping(target = "engineStatus", source = "maintenanceParameters.engineStatus")
    @Mapping(target = "batteryHealth", expression = "java(mapBatteryHealth(domain))")
    @Mapping(target = "maintenanceStatus", source = "maintenanceParameters.maintenanceStatus")
    public abstract MaintenanceParameterEntity toMaintenanceEntity(Vehicle domain);

    // Helper pour éviter les expressions complexes dans l'annotation
    protected String mapBatteryHealth(Vehicle domain) {
        if (domain.maintenanceParameters() != null && domain.maintenanceParameters().batteryHealth() != null) {
            return String.valueOf(domain.maintenanceParameters().batteryHealth());
        }
        return null;
    }

    // --- Vers DOMAINE (Méthode manuelle) ---

    public Vehicle toDomain(VehicleLocalEntity v, FinancialParameterEntity f, MaintenanceParameterEntity m) {
        if (v == null) return null;

        return new Vehicle(
            v.getId(),
            v.getFleetId(),
            v.getLicensePlate(),
            v.getBrand(),
            v.getModel(),
            v.getManufacturingYear(),
            null, // 'type' est null car géré dynamiquement via ID
            v.getColor(),
            mapFinancial(f),
            mapMaintenance(m),
            null // Operational
        );
    }

    protected VehicleParameters.Financial mapFinancial(FinancialParameterEntity f) {
        if (f == null) return null;
        return new VehicleParameters.Financial(
            f.getInsuranceNumber(), f.getInsuranceExpiredAt(), f.getRegisteredAt(), 
            f.getPurchasedAt(), f.getDepreciationRate(), f.getCostPerKm()
        );
    }

    protected VehicleParameters.Maintenance mapMaintenance(MaintenanceParameterEntity m) {
        if (m == null) return null;
        return new VehicleParameters.Maintenance(
            m.getLastMaintenanceAt(), m.getNextMaintenanceAt(), m.getEngineStatus(),
            tryParseInt(m.getBatteryHealth()),
            m.getMaintenanceStatus()
        );
    }

    protected Integer tryParseInt(String value) {
        try {
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}