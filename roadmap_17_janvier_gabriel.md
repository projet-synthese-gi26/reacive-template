# 📅 Roadmap - Jalon 1 & Validation Initiale (17 Janvier 2026)

## Objectif Principal
Assurer un environnement de développement local stable et complet (Docker), valider la migration Liquibase, et préparer l'intégration Kafka/Redis avant de continuer sur les fonctionnalités métier.

---

### 🎯 Jalon 1 - Consolidation & Validation

#### ⚙️ Sous-Jalon 1.1 : Infrastructure Docker Locale Complète
*Ce jalon vise à avoir un environnement local fonctionnel incluant la base de données, Kafka et Redis.*

- [x] **Tâche 1.1.1 :** Mettre à jour `docker-compose.yml` pour inclure Kafka et Redis.
    - [x] Ajouter un service `kafka` (ex: `bitnami/kafka` ou un broker léger).
    - [x] Ajouter un service `zookeeper` (si nécessaire pour la version de Kafka choisie).
    - [x] Ajouter un service `redis` (ex: `redis:latest`).
    - [x] Configurer les réseaux Docker pour la communication inter-conteneurs.
    - [x] Adapter `application.yml` pour utiliser les bonnes adresses (`localhost:9092` pour Kafka, `localhost:6379` pour Redis).
- [x] **Tâche 1.1.2 :** Lancer l'environnement Docker complet (`docker-compose up -d`).
- [x] **Tâche 1.1.3 :** Vérifier la connectivité de l'application aux nouveaux services (Kafka/Redis) via les logs Spring Boot.
- [x] **Tâche 1.1.4 :** Vérifier l'absence des logs d'erreur Kafka (`Bootstrap broker localhost:9092 disconnected`) après configuration correcte.

#### ⚙️ Sous-Jalon 1.2 : Validation de Non-Régression Post-Liquibase
*Tester les fonctionnalités CRUD existantes via Swagger pour s'assurer que la migration de base de données n'a rien cassé.*

- [ ] **Tâche 1.2.1 :** Lancer l'application avec le profil `local` (`./run_local.sh`).
- [ ] **Tâche 1.2.2 :** Ouvrir Swagger UI ([http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)).
- [ ] **Tâche 1.2.3 :** Tester le CRUD des **Fleets** :
    - [ ] Créer une flotte (vérifier insertion dans `fleet.fleets`).
    - [ ] Récupérer la flotte par ID.
    - [ ] Lister toutes les flottes.
    - [ ] Mettre à jour la flotte.
    - [ ] Supprimer la flotte.
- [ ] **Tâche 1.2.4 :** Tester la création d'un **Driver** (incluant l'appel au service d'Auth).
    - [ ] Vérifier la création dans `public.users`, `public.business_actors`, et `fleet.drivers`.
- [ ] **Tâche 1.2.5 :** Tester l'ajout d'un **Véhicule** à une flotte.
    - [ ] Vérifier l'insertion dans `fleet.vehicles` et la présence de `fleet_id`.
- [ ] **Tâche 1.2.6 :** Tester la mise à jour des **Paramètres Véhicules** (Financier/Maintenance).
    - [ ] Vérifier les modifications dans `fleet.financial_parameters` et `fleet.maintenance_parameters`.

---

### 🎯 Prochaines Étapes (Après Validation)

Une fois ces validations effectuées, nous pourrons enchaîner sur le Jalon 5 : Gestion des Trajets.