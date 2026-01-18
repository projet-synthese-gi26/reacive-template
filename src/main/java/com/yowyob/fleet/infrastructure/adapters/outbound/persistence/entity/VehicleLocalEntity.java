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

    @Column("current_driver_id")
    private UUID currentDriverId;

    @Column("vehicle_type_id")
    private UUID vehicleTypeId;

    @Column("license_plate")
    private String licensePlate;
    
    // Ces champs DOIVENT être présents pour que le mapper fonctionne
    private String brand;
    private String model;
    
    @Column("manufacturing_year")
    private Integer manufacturingYear;
    
    private String color;
    
    private String status; 

    @Column("photo_url")
    private String photoUrl;
}