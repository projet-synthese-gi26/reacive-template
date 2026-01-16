package com.yowyob.fleet.infrastructure.adapters.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverEntity {
    @Id
    @Column("userid") // Correspond au userId de la table
    private UUID userId;
    
    @Column("licencenumber")
    private String licenceNumber;
    
    private Boolean status;
    
    @Column("assignedvehicleid")
    private UUID assignedVehicleId;
}