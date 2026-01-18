--liquibase formatted sql

--changeset gabriel:create-fleet-enums splitStatements:true
-- 1. ENUMS STATIQUES (Logique métier figée)
CREATE TYPE fleet.engine_status_enum AS ENUM ('OK', 'NEEDS_SERVICE', 'OUT_OF_SERVICE');
CREATE TYPE fleet.maintenance_status_enum AS ENUM ('UP_TO_DATE', 'PENDING', 'OVERDUE');
CREATE TYPE fleet.event_type_enum AS ENUM ('ENTRY', 'EXIT');
CREATE TYPE fleet.trip_status_enum AS ENUM ('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED');
CREATE TYPE fleet.driver_status_enum AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE fleet.vehicle_status_enum AS ENUM ('AVAILABLE', 'ON_TRIP', 'MAINTENANCE');

--changeset gabriel:create-fleet-tables splitStatements:true

-- 2. DONNÉES DE RÉFÉRENCE DYNAMIQUES
CREATE TABLE fleet.vehicle_types (
    id UUID PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL, -- Ex: CAR, TRUCK
    label VARCHAR(100) NOT NULL,      -- Ex: Voiture de Tourisme
    description TEXT
);

-- 3. ACTEURS (Extensions de public.users)
CREATE TABLE fleet.fleet_managers (
    user_id UUID PRIMARY KEY, -- FK vers public.users
    company_name VARCHAR(100)
);

-- 4. ORGANISATION
CREATE TABLE fleet.fleets (
  id UUID PRIMARY KEY,
  manager_id UUID NOT NULL REFERENCES fleet.fleet_managers(user_id),
  name VARCHAR(255) NOT NULL,
  phone_number VARCHAR(50),
  created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE fleet.drivers (
  user_id UUID PRIMARY KEY, -- FK vers public.users
  fleet_id UUID REFERENCES fleet.fleets(id) ON DELETE SET NULL,
  licence_number VARCHAR(100) UNIQUE NOT NULL,
  status fleet.driver_status_enum DEFAULT 'ACTIVE',
  photo_url VARCHAR(255)
);

-- 5. GEOFENCING (Zones)
CREATE TABLE fleet.geofence_zones (
  id UUID PRIMARY KEY,
  fleet_id UUID NOT NULL REFERENCES fleet.fleets(id) ON DELETE CASCADE,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  surface_area NUMERIC,
  perimeter NUMERIC
);

-- 6. VÉHICULES
CREATE TABLE fleet.vehicles (
  id UUID PRIMARY KEY,
  fleet_id UUID NOT NULL REFERENCES fleet.fleets(id) ON DELETE CASCADE,
  
  -- Assignation dynamique
  current_driver_id UUID REFERENCES fleet.drivers(user_id) ON DELETE SET NULL,
  
  -- Typage dynamique (FK vers table)
  vehicle_type_id UUID REFERENCES fleet.vehicle_types(id), 
  
  license_plate VARCHAR(50) UNIQUE NOT NULL,
  brand VARCHAR(100),
  model VARCHAR(100),
  manufacturing_year INT,
  color VARCHAR(50),
  status fleet.vehicle_status_enum DEFAULT 'AVAILABLE',
  photo_url VARCHAR(255)
);

-- 7. PARAMÈTRES VÉHICULES (1-1)
CREATE TABLE fleet.operational_parameters (
  id UUID PRIMARY KEY,
  vehicle_id UUID UNIQUE REFERENCES fleet.vehicles(id) ON DELETE CASCADE,
  current_location_point_id UUID, -- FK ajoutée plus bas
  status BOOLEAN DEFAULT true,
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

-- 8. TRAJETS & ROUTES
CREATE TABLE fleet.trips (
  id UUID PRIMARY KEY,
  vehicle_id UUID REFERENCES fleet.vehicles(id) ON DELETE CASCADE,
  driver_id UUID REFERENCES fleet.drivers(user_id) ON DELETE SET NULL,
  start_date DATE NOT NULL,
  end_date DATE,
  start_time TIME NOT NULL,
  end_time TIME,
  status fleet.trip_status_enum DEFAULT 'SCHEDULED',
  
  -- Historisation du type au moment du trajet
  vehicle_type_id UUID REFERENCES fleet.vehicle_types(id), 
  
  distance_km NUMERIC,
  duration_minutes INT
);

-- 9. GÉOMÉTRIE (Legacy / Compatibilité Objet)
CREATE TABLE fleet.geofence_points (
  id UUID PRIMARY KEY,
  latitude NUMERIC NOT NULL,
  longitude NUMERIC NOT NULL
);

ALTER TABLE fleet.operational_parameters 
ADD CONSTRAINT fk_operational_point 
FOREIGN KEY (current_location_point_id) REFERENCES fleet.geofence_points(id) ON DELETE SET NULL;

CREATE TABLE fleet.routes (
  id UUID PRIMARY KEY,
  trip_id UUID REFERENCES fleet.trips(id) ON DELETE CASCADE,
  start_point_id UUID REFERENCES fleet.geofence_points(id),
  end_point_id UUID REFERENCES fleet.geofence_points(id)
);

CREATE TABLE fleet.geofence_point_zone_linkages (
  point_id UUID REFERENCES fleet.geofence_points(id) ON DELETE CASCADE,
  zone_id UUID REFERENCES fleet.geofence_zones(id) ON DELETE CASCADE,
  vertex_order INT,
  PRIMARY KEY (point_id, zone_id)
);

-- 10. ÉVÉNEMENTS
CREATE TABLE fleet.geofence_events (
  id UUID PRIMARY KEY,
  vehicle_id UUID REFERENCES fleet.vehicles(id) ON DELETE CASCADE,
  zone_id UUID REFERENCES fleet.geofence_zones(id) ON DELETE SET NULL,
  type fleet.event_type_enum,
  timestamp TIMESTAMP DEFAULT now()
);