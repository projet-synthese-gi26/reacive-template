--liquibase formatted sql

--changeset gabriel:mock-public-schema context:local
-- Création des tables public minimales pour éviter les erreurs de FK si elles existent
-- Note: Dans un environnement réel, ces tables sont gérées par le service Core

CREATE TABLE IF NOT EXISTS public.users (
    id UUID PRIMARY KEY,
    name TEXT,
    email_address TEXT
);

CREATE TABLE IF NOT EXISTS public.business_actors (
    id UUID PRIMARY KEY REFERENCES public.users(id)
);

-- Données de test pour le développement local
INSERT INTO public.users (id, name, email_address) VALUES 
('7b2e3f4a-1c9d-4b8a-8e6f-2d3c4b5a6d7e', 'Gabriel Manager', 'manager@yowyob.com')
ON CONFLICT DO NOTHING;

INSERT INTO public.business_actors (id) VALUES 
('7b2e3f4a-1c9d-4b8a-8e6f-2d3c4b5a6d7e')
ON CONFLICT DO NOTHING;