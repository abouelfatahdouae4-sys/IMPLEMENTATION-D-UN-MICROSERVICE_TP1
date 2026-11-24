# Activité Pratique N°1 – Implémentation d'un micro-service

**Module :** Architectures Micro Services – 5ᵉ année EMSI 2026
**Encadrant :** Pr. Mohamed YOUSSFI
**Réalisé par :** Ghita JOURANE

---

## Objectif

Créer un micro-service Spring Boot qui gère des **comptes bancaires**, puis l'exposer de trois façons :
une API REST classique (avec documentation Swagger), une API générée par **Spring Data Rest** (avec projections) et une API **GraphQL**.

## Technologies

| Outil | Rôle |
|---|---|
| Spring Boot 3.5 / Java 17 | Socle du micro-service |
| Spring Web | Web service Restful |
| Spring Data JPA + H2 | Persistance (base en mémoire) |
| Lombok | Réduction du code (getters, setters, builders…) |
| Spring Data Rest | API générée automatiquement + projections |
| springdoc-openapi | Documentation Swagger |
| Spring for GraphQL | Web service GraphQL |

## Architecture du projet

```
src/main/java/ma/emsi/ebankservice
├── EbankServiceApplication.java     → démarrage + jeu de données (test DAO)
├── entities/   Compte, Client, CompteProjection (p1), CompteProjectionComplete (p2)
├── enums/      TypeCompte (COMPTE_COURANT, COMPTE_EPARGNE)
├── repositories/ CompteRepository, ClientRepository
├── dto/        CompteRequestDTO, CompteResponseDTO
├── mappers/    CompteMapper
├── services/   CompteService, CompteServiceImpl
├── exceptions/ CompteNotFoundException
└── web/        CompteRestController, CompteGraphQLController, gestion des erreurs
src/main/resources
├── application.properties
└── graphql/schema.graphqls
```

Flux d'une requête : `Client HTTP → Controller (REST / GraphQL) → Service (DTO) → Mapper → Repository → H2`

---

## Travail réalisé

### 1. Création du projet Spring Boot
Projet généré avec les dépendances **Web, Spring Data JPA, H2, Lombok** (voir `pom.xml`).
Configuration dans `application.properties` : port `8081`, base H2 `jdbc:h2:mem:ebank-db`, console H2 activée.

### 2. Entité JPA `Compte`
Attributs : `id` (String, UUID), `dateCreation`, `solde`, `devise`, `type` (enum `TypeCompte` stocké en `STRING`) et une relation `@ManyToOne` vers `Client`.

### 3. Interface `CompteRepository`
Hérite de `JpaRepository<Compte, String>` et ajoute les méthodes `findByType` et `findByDevise`.

### 4. Test de la couche DAO
- Au démarrage, un `CommandLineRunner` crée 4 clients (Ghita, Yassine, Salma, Omar) avec 3 comptes chacun, puis les affiche dans la console.
- Tests unitaires `@DataJpaTest` dans `CompteRepositoryTest` : sauvegarde/lecture, filtre par type, suppression.
- Console H2 : http://localhost:8081/h2-console (JDBC URL : `jdbc:h2:mem:ebank-db`, user `sa`).

> 📸 *Capture : console de démarrage + table COMPTE dans la console H2*

### 5. Web service Restful (`CompteRestController`)

| Méthode | URL | Action |
|---|---|---|
| GET | `/api/comptes` | Lister les comptes |
| GET | `/api/comptes/{id}` | Consulter un compte |
| POST | `/api/comptes` | Créer un compte |
| PUT | `/api/comptes/{id}` | Modifier un compte (champs non nuls) |
| DELETE | `/api/comptes/{id}` | Supprimer un compte |

Un compte inexistant renvoie `404` avec un message (`RestExceptionHandler`).

### 6. Test avec Postman
La collection `postman/ebank-service.postman_collection.json` contient toutes les requêtes (à importer dans Postman). La requête « Créer un compte » enregistre automatiquement l'id créé pour les requêtes suivantes.

Exemple de corps pour un POST :
```json
{ "solde": 15000, "devise": "MAD", "type": "COMPTE_COURANT" }
```

> 📸 *Captures : GET, POST, PUT, DELETE dans Postman*

### 7. Documentation Swagger
- Interface : http://localhost:8081/swagger-ui.html
- Description OpenAPI (JSON) : http://localhost:8081/v3/api-docs

Les endpoints sont décrits avec `@Tag` et `@Operation`, et peuvent être testés directement depuis Swagger UI (« Try it out »).

> 📸 *Capture : Swagger UI*

### 8. Spring Data Rest + projections
`@RepositoryRestResource` expose automatiquement le repository sous `/v1` (configuré par `spring.data.rest.base-path=/v1` pour ne pas entrer en conflit avec `/api`).

| URL | Résultat |
|---|---|
| `/v1/comptes` | Tous les comptes (format HAL, paginé) |
| `/v1/comptes?projection=p1` | Seulement `id`, `solde`, `type` |
| `/v1/comptes?projection=p2` | Toutes les infos + nom du client |
| `/v1/comptes/search/byType?t=COMPTE_EPARGNE` | Recherche par type |
| `/v1/comptes/search/byDevise?d=MAD` | Recherche par devise |
| `/v1/clients` | Liste des clients |

> 📸 *Capture : résultat avec projection p1*

### 9. DTOs et Mappers
- `CompteRequestDTO` : données envoyées par le client (`solde`, `devise`, `type`).
- `CompteResponseDTO` : données renvoyées (`id`, `dateCreation`, `solde`, `devise`, `type`).
- `CompteMapper` : conversion entité ↔ DTO avec `BeanUtils.copyProperties`.

Intérêt : l'entité JPA n'est jamais exposée directement, ce qui découple l'API du modèle de persistance.

### 10. Couche service (métier)
`CompteService` / `CompteServiceImpl` (annoté `@Service` et `@Transactional`) centralise la logique : génération de l'UUID et de la date de création, mise à jour partielle, levée de `CompteNotFoundException`. Le contrôleur REST et le contrôleur GraphQL passent tous les deux par cette couche.

### 11. Web service GraphQL
Schéma : `src/main/resources/graphql/schema.graphqls` – Contrôleur : `CompteGraphQLController` (`@QueryMapping`, `@MutationMapping`, `@SchemaMapping`).
Interface de test GraphiQL : http://localhost:8081/graphiql

Une entité `Client` (relation un-à-plusieurs avec `Compte`) a été ajoutée pour illustrer les requêtes imbriquées.

**Requêtes :**
```graphql
query {
  comptesList { id solde devise type dateCreation client { nom } }
}

query {
  compteById(id: "ID_DU_COMPTE") { id solde type }
}

query {
  clientsList { id nom comptes { id solde type } }
}
```

**Mutations :**
```graphql
mutation {
  addCompte(compte: { solde: 9000, devise: "EUR", type: COMPTE_EPARGNE }) { id solde type }
}

mutation {
  updateCompte(id: "ID_DU_COMPTE", compte: { solde: 12000 }) { id solde }
}

mutation {
  deleteCompte(id: "ID_DU_COMPTE")
}
```

Les erreurs (ex. compte introuvable) sont renvoyées avec un message clair grâce à `GraphQLExceptionHandler`.

> 📸 *Captures : requêtes et mutations dans GraphiQL*

---

## Lancer le projet

```bash
mvn spring-boot:run      # démarrer le micro-service sur http://localhost:8081
mvn test                 # exécuter les tests de la couche DAO
```

## Conclusion

Ce TP a permis de construire un micro-service complet en couches (entités, repository, DTO/mapper, service, web) et de l'exposer de plusieurs manières : REST classique documenté avec Swagger, API générée par Spring Data Rest avec projections, et GraphQL qui permet au client de choisir exactement les champs qu'il veut récupérer.
