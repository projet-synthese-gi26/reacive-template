package com.yowyob.fleet.infrastructure.adapters.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import java.time.Instant;
import java.util.UUID;

@Table(name = "fleets", schema = "fleet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FleetEntity {
    @Id
    private UUID id;
    
    @Column("fleet_manager_id")
    private UUID fleetManagerId; // Ref to public.business_actors
    
    private String name;
    
    @Column("phone_number")
    private String phoneNumber;
    
    @Column("created_at")
    private Instant createdAt;
}