--liquibase formatted sql

--changeset gabriel:create-fleet-enums splitStatements:true
CREATE TYPE fleet.vehicle_type_enum AS ENUM ('CAR', 'VAN', 'TRUCK', 'BIKE');
CREATE TYPE fleet.engine_status_enum AS ENUM ('OK', 'NEED_SERVICE', 'OUT_OF_SERVICE');
CREATE TYPE fleet.maintenance_status_enum AS ENUM ('UP_TO_DATE', 'PENDING', 'OVERDUE');
CREATE TYPE fleet.event_type_enum AS ENUM ('ENTRY', 'EXIT');

--changeset gabriel:create-fleet-tables splitStatements:true
-- 1. Fleets
CREATE TABLE fleet.fleets (
  id UUID PRIMARY KEY,
  fleet_manager_id UUID NOT NULL, 
  name VARCHAR(255) NOT NULL,
  phone_number VARCHAR(50),
  created_at TIMESTAMP DEFAULT now()
);

-- 2. Geofence Zones
CREATE TABLE fleet.geofence_zones (
  id UUID PRIMARY KEY,
  surface_area NUMERIC,
  perimeter NUMERIC
);

-- 3. Vehicles (Pivot Central)
CREATE TABLE fleet.vehicles (
  id UUID PRIMARY KEY,
  fleet_id UUID REFERENCES fleet.fleets(id) ON DELETE CASCADE,
  zone_id UUID REFERENCES fleet.geofence_zones(id) ON DELETE SET NULL,
  license_plate VARCHAR(50) UNIQUE NOT NULL,
  brand VARCHAR(100),
  model VARCHAR(100),
  manufacturing_year INT,
  type fleet.vehicle_type_enum,
  color VARCHAR(50)
);

-- 4. Drivers
CREATE TABLE fleet.drivers (
  user_id UUID PRIMARY KEY,
  status BOOLEAN DEFAULT true,
  licence_number VARCHAR(100) UNIQUE NOT NULL,
  assigned_vehicle_id UUID REFERENCES fleet.vehicles(id) ON DELETE SET NULL
);

-- Mise à jour de la ref véhicule -> driver
ALTER TABLE fleet.vehicles ADD COLUMN driver_id UUID REFERENCES fleet.drivers(user_id) ON DELETE SET NULL;

-- 5. Roads & Trips
CREATE TABLE fleet.roads (
  id UUID PRIMARY KEY,
  start_point TEXT,
  end_point TEXT
);

CREATE TABLE fleet.trips (
  id UUID PRIMARY KEY,
  vehicle_id UUID REFERENCES fleet.vehicles(id) ON DELETE CASCADE,
  driver_id UUID REFERENCES fleet.drivers(user_id) ON DELETE SET NULL,
  road_id UUID REFERENCES fleet.roads(id) ON DELETE SET NULL,
  start_date DATE NOT NULL,
  end_date DATE,
  start_time TIME NOT NULL,
  end_time TIME,
  type fleet.vehicle_type_enum,
  color VARCHAR(50)
);

-- 6. Parameters
CREATE TABLE fleet.operational_parameters (
  id UUID PRIMARY KEY,
  vehicle_id UUID UNIQUE REFERENCES fleet.vehicles(id) ON DELETE CASCADE,
  trip_id UUID REFERENCES fleet.trips(id) ON DELETE SET NULL,
  statut BOOLEAN DEFAULT true,
  current_location TEXT,
  current_speed NUMERIC,
  fuel_level VARCHAR(50),
  mileage NUMERIC,
  odometer_reading NUMERIC,
  bearing NUMERIC,
  timestamp TIMESTAMP DEFAULT now()
);

CREATE TABLE fleet.financial_parameters (
  id UUID PRIMARY KEY,
  vehicle_id UUID UNIQUE REFERENCES fleet.vehicles(id) ON DELETE CASCADE,
  insurance_number VARCHAR(100),
  insurance_expired_at DATE,
  registered_at DATE,
  purchased_at DATE,
  depreciation_rate INT,
  cost_per_km NUMERIC
);

CREATE TABLE fleet.maintenance_parameters (
  id UUID PRIMARY KEY,
  vehicle_id UUID UNIQUE REFERENCES fleet.vehicles(id) ON DELETE CASCADE,
  last_maintenance_at DATE,
  next_maintenance_at DATE,
  engine_status fleet.engine_status_enum DEFAULT 'OK',
  battery_health INT,
  maintenance_status fleet.maintenance_status_enum DEFAULT 'UP_TO_DATE'
);

-- 7. Geofence Details
CREATE TABLE fleet.geofence_points (
  id UUID PRIMARY KEY,
  latitude NUMERIC NOT NULL,
  longitude NUMERIC NOT NULL
);

CREATE TABLE fleet.geofence_point_zone_linkages (
  point_id UUID REFERENCES fleet.geofence_points(id) ON DELETE CASCADE,
  zone_id UUID REFERENCES fleet.geofence_zones(id) ON DELETE CASCADE,
  vertex_order INT,
  PRIMARY KEY (point_id, zone_id)
);

CREATE TABLE fleet.geofence_events (
  id UUID PRIMARY KEY,
  vehicle_id UUID REFERENCES fleet.vehicles(id) ON DELETE CASCADE,
  zone_id UUID REFERENCES fleet.geofence_zones(id) ON DELETE SET NULL,
  type fleet.event_type_enum,
  timestamp TIMESTAMP DEFAULT now()
);