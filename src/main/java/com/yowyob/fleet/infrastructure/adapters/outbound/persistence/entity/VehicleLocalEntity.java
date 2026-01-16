package com.yowyob.fleet.infrastructure.adapters.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.util.UUID;

@Table(name = "vehicles", schema = "fleet")
@Data @NoArgsConstructor @AllArgsConstructor
public class VehicleLocalEntity {
    @Id
    private UUID id;

    @Column("fleet_id")
    private UUID fleetId;

    @Column("driver_id")
    private UUID driverId;

    @Column("zone_id")
    private UUID zoneId;

    @Column("license_plate")
    private String licensePlate;
    
    // Les autres champs (brand, model) sont gérés via l'API externe VehicleService
    // ou ajoutés ici si on décide de les cacher localement.
    // Pour l'instant, on garde la structure pivot minimale.
}