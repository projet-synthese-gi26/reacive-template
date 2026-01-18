--liquibase formatted sql

--changeset gabriel:mock-public-schema context:local
-- 1. Structure PUBLIC (Simulation Auth Service)
CREATE TABLE IF NOT EXISTS public.users (
    id UUID PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);

CREATE TABLE IF NOT EXISTS public.roles (
    name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS public.user_has_roles (
    user_id UUID REFERENCES public.users(id),
    role_name VARCHAR(50) REFERENCES public.roles(name),
    PRIMARY KEY (user_id, role_name)
);

-- 2. Insertion des Rôles (Standards TransEns)
INSERT INTO public.roles (name) VALUES 
('FLEET_ADMIN'), 
('FLEET_MANAGER'), 
('FLEET_DRIVER')
ON CONFLICT DO NOTHING;

-- 3. Insertion des Types de Véhicules (Obligatoire pour créer des véhicules)
-- UUIDs codés en dur pour faciliter les tests
INSERT INTO fleet.vehicle_types (id, code, label, description) VALUES 
('11111111-1111-1111-1111-111111111111', 'CAR', 'Voiture', 'Véhicule léger de tourisme'),
('22222222-2222-2222-2222-222222222222', 'TRUCK', 'Camion', 'Poids lourd pour logistique'),
('33333333-3333-3333-3333-333333333333', 'MOTO', 'Moto', 'Deux roues pour livraison rapide'),
('44444444-4444-4444-4444-444444444444', 'VAN', 'Fourgon', 'Transport de matériel')
ON CONFLICT DO NOTHING;

-- 4. SCÉNARIO DE TEST COMPLET

-- A. Création du Fleet Manager (Auth + Profil)
INSERT INTO public.users (id, name, email, password) VALUES 
('aaaa1111-bbbb-2222-cccc-333333333333', 'Jean Manager', 'manager@test.com', 'password')
ON CONFLICT DO NOTHING;

INSERT INTO public.user_has_roles VALUES ('aaaa1111-bbbb-2222-cccc-333333333333', 'FLEET_MANAGER')
ON CONFLICT DO NOTHING;

INSERT INTO fleet.fleet_managers (user_id, company_name) VALUES 
('aaaa1111-bbbb-2222-cccc-333333333333', 'TransCam S.A.')
ON CONFLICT DO NOTHING;

-- B. Création de la Flotte
INSERT INTO fleet.fleets (id, manager_id, name, phone_number) VALUES 
('ffffffff-aaaa-bbbb-cccc-dddddddddddd', 'aaaa1111-bbbb-2222-cccc-333333333333', 'Flotte Douala', '+237699000000')
ON CONFLICT DO NOTHING;

-- C. Création du Chauffeur (Auth + Profil + Lien Flotte)
INSERT INTO public.users (id, name, email, password) VALUES 
('dddd2222-eeee-3333-ffff-444444444444', 'Moussa Driver', 'driver@test.com', 'password')
ON CONFLICT DO NOTHING;

INSERT INTO public.user_has_roles VALUES ('dddd2222-eeee-3333-ffff-444444444444', 'FLEET_DRIVER')
ON CONFLICT DO NOTHING;

INSERT INTO fleet.drivers (user_id, fleet_id, licence_number, status) VALUES 
('dddd2222-eeee-3333-ffff-444444444444', 'ffffffff-aaaa-bbbb-cccc-dddddddddddd', 'LT-12345-X', 'ACTIVE')
ON CONFLICT DO NOTHING;

-- D. Création du Véhicule (Lien Flotte + Type Voiture + Chauffeur)
INSERT INTO fleet.vehicles (id, fleet_id, vehicle_type_id, current_driver_id, license_plate, brand, model, status) VALUES 
('vvvvvvvv-1111-2222-3333-444444444444', 
 'ffffffff-aaaa-bbbb-cccc-dddddddddddd', 
 '11111111-1111-1111-1111-111111111111', -- Type CAR
 'dddd2222-eeee-3333-ffff-444444444444', -- Assigné à Moussa
 'LT 007 JB', 'Toyota', 'Yaris', 'AVAILABLE')
ON CONFLICT DO NOTHING;