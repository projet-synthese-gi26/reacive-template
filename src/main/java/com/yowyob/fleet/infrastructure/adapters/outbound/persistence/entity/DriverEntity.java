package com.yowyob.fleet.infrastructure.adapters.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "drivers", schema = "fleet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverEntity {
    @Id
    @Column("user_id") // Correspond à la PK user_id dans fleet.drivers
    private UUID userId;
    
    @Column("licence_number")
    private String licenceNumber;
    
    private Boolean status;
    
    @Column("assigned_vehicle_id")
    private UUID assignedVehicleId;
}