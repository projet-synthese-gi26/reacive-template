# Fleet Management & Geofencing API 🚚🛰️

Service backend réactif pour la gestion de flottes de véhicules et le géorepérage en temps réel.

## 🤝 Workflow de Collaboration IA

Ce projet est développé en mode **Pair Programming** avec une IA (Gemini/ChatGPT). Pour maintenir la cohérence :

1. **Roadmap** : Consultez le fichier `todo.md` pour voir la tâche en cours.
2. **Contextualisation** : L'IA a besoin du code source complet. Utilisez le script de synchronisation :
   ```bash
   chmod +x import_context.sh
   ./import_context.sh
   ```
   Cela génère/met à jour le fichier `project_context.txt`.
3. **Initialisation de l'IA** : Pour commencer une session, copiez-collez le contenu de `docs/prompts/master_pair_programmer.md` suivi du contenu de `project_context.txt`.

## 🛠️ Installation & Tests

### Prérequis
- Java 21
- PostgreSQL (avec accès aux serveurs distants configurés dans le `.yml`)


### Comment lancer le projet ?

**Sur Linux / Mac :**
1. Oouvrez un terminal à la racine.
2. Lancez : `./run_local.sh`

**Sur Windows :**
1. Double-cliquez sur `run_local.bat` (ou lancez-le depuis un terminal).

*Cela va automatiquement monter la base de données Docker et lancer l'application avec la configuration locale.*

### En cas de problème de base de données
Si vous avez des erreurs Liquibase ou de schéma, réinitialisez la base avec :
- Linux/Mac : `./reset_db.sh`
- Windows : `reset_db.bat`

### Valider les changements
- **Swagger UI** : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Santé de la DB** : Utilisez les endpoints définis dans chaque jalon (voir `todo.md`).
```
