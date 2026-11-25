# MS etudiant-backend

Backend qui gère les APIs des utilisateurs de la bibliothèque et les CRUD des étudiants.

## Configuration du backend

    - name: etudiant-backend
    - port: 8080

## Pré-requis pour le bon fonctionnement du service :

    -> JDK 21
    -> Docker
    -> Docker Compose
    -> Maven 3.9.3 (https://archive.apache.org/dist/maven/maven-3/3.9.3/binaries/) ou plus
       (ou le wrapper fourni : ./mvnw, qui évite d'installer Maven)

## Variables d'environnement (fichier `.env`)

| Variable | Rôle |
| --- | --- |
| `DB_USER`, `DB_PASSWORD`, `DB_HOST`, `DB_PORT`, `DB_NAME` | connexion à la base MySQL |
| `JWT_SECRET` | clé secrète qui signe les tokens JWT (au moins 32 caractères) |
| `JWT_EXPIRATION` | durée de validité d'un token, en millisecondes |

## Démarrage du backend
Pour démarrer le projet backend, il faut : 
- avoir démarré Docker-Desktop sur votre poste de travail local.
- dans une console, se placer à la racine du projet et exécuter la commande Maven suivante :
```
mvn spring-boot:run
```

Cette commande va : 
 - initialiser le container Docker qui contient la base de données 
 - lancer le serveur du backend et le connecter à la base de données précédemment créée

Les traces logs devraient ressemblées à ceci : 
```
.   ____          _            __ _ _
/\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
'  |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/

:: Spring Boot ::                (v3.5.5)

[etudiant-backend] [           main] c.o.etudiant.EtudiantBackendApplication  : Starting EtudiantBackendApplication using Java 21.0.3 with PID 6964
[etudiant-backend] [           main] c.o.etudiant.EtudiantBackendApplication  : No active profile set, falling back to 1 default profile: "default"
[etudiant-backend] [           main] .s.b.d.c.l.DockerComposeLifecycleManager : Using Docker Compose file ******etudiant-backend\compose.yaml*****
[etudiant-backend] [utReader-stderr] o.s.boot.docker.compose.core.DockerCli   :  Container etudiant-backend-mysql-1  Created
[etudiant-backend] [utReader-stderr] o.s.boot.docker.compose.core.DockerCli   :  Container etudiant-backend-mysql-1  Starting
[etudiant-backend] [utReader-stderr] o.s.boot.docker.compose.core.DockerCli   :  Container etudiant-backend-mysql-1  Started
[etudiant-backend] [utReader-stderr] o.s.boot.docker.compose.core.DockerCli   :  Container etudiant-backend-mysql-1  Waiting
[etudiant-backend] [utReader-stderr] o.s.boot.docker.compose.core.DockerCli   :  Container etudiant-backend-mysql-1  Healthy
[etudiant-backend] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
[etudiant-backend] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 39 ms. Found 1 JPA repository interface.
[etudiant-backend] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
[etudiant-backend] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
[etudiant-backend] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.44]
[etudiant-backend] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
[etudiant-backend] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1354 ms
[etudiant-backend] [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
[etudiant-backend] [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.6.26.Final
[etudiant-backend] [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
[etudiant-backend] [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
[etudiant-backend] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
[etudiant-backend] [           main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection com.mysql.cj.jdbc.ConnectionImpl@4db16677
[etudiant-backend] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
[etudiant-backend] [           main] org.hibernate.orm.connections.pooling    : HHH10001005: Database info:
[etudiant-backend] [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
[etudiant-backend] [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
[etudiant-backend] [           main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
[etudiant-backend] [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
[etudiant-backend] [           main] eAuthenticationProviderManagerConfigurer : Global AuthenticationManager configured with AuthenticationProvider bean with name authenticationProvider
[etudiant-backend] [           main] r$InitializeUserDetailsManagerConfigurer : Global AuthenticationManager configured with an AuthenticationProvider bean. UserDetailsService beans w
ill not be used by Spring Security for automatically configuring username/password login. Consider removing the AuthenticationProvider bean. Alternatively, consider using the UserDetailsService in a manually instantiated DaoAuth
enticationProvider. If the current configuration is intentional, to turn off this warning, increase the logging level of 'org.springframework.security.config.annotation.authentication.configuration.InitializeUserDetailsBeanManagerConfigurer' to ERROR
[etudiant-backend] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
[etudiant-backend] [           main] c.o.etudiant.EtudiantBackendApplication  : Started EtudiantBackendApplication in 10.27 seconds (process running for 10.642)
```

Sur Docker-Desktop, vous devriez voir apparaître un container MySQL qui correspond au projet.

![1-docker-desktop](pictures/1-docker-desktop.png)

Vous pouvez vous connecter à la base de données et vérifier que la table ```user``` a été créée automatiquement.
Pour cela, cliquez sur le lien `mysql-1` ce qui vous amènera sur la vue complète de la base de données. 
Dans l'onglet ```Exec```, il faut : 

1. se connecter à la base de données. Tapez la commande ci-dessous

    ```
    mysql -u etudiant_db -p
    ```
   L'invite de commande demandera le mot de passe. Il est identique au nom d'utilisateur, c'est-à-dire ```etudiant_db```.


2. Se connecter au schéma de base de données `etudiant_db`. Dans l'invite de commande, tapez la commande ci-dessous :

    ```
    use etudiant_db;
    ```
  
3. Vérifier que la table `user` existe (elle est néanmoins vide pour le moment).

    ```
    select * from user;
    ```
    Le résultat devrait être : `Empty set (0.00 sec)`

La capture d'écran ci-dessous résume les étapes précédentes : 

![2-docker-desktop-bdd](pictures/2-docker-desktop-bdd.png)


## Fonctionnalités portées

- API de création d'un utilisateur (agent de la bibliothèque)
- API d'authentification d'un utilisateur, qui renvoie un token JWT
- APIs CRUD des étudiants de la bibliothèque, protégées par le token JWT

| Méthode | Route | Token | Réponse |
| --- | --- | --- | --- |
| `POST` | `/api/register` | non | 201 : agent créé |
| `POST` | `/api/login` | non | 200 : `{ "token": "..." }` (401 si identifiants invalides) |
| `GET` | `/api/students` | oui | 200 : liste des étudiants |
| `GET` | `/api/students/{id}` | oui | 200 : un étudiant |
| `POST` | `/api/students` | oui | 201 : étudiant créé |
| `PUT` | `/api/students/{id}` | oui | 200 : étudiant modifié |
| `DELETE` | `/api/students/{id}` | oui | 204 : étudiant supprimé |

Les routes `/api/students` attendent le header `Authorization: Bearer <token>`, avec le token reçu à la connexion.

## Écrans concernés (front-end)

Le front Angular est dans le dépôt [etudiant-frontend](https://github.com/JuS1302/etudiant-frontend) :

- Écran d'inscription (`/register`)
- Écran de connexion (`/login`)
- Liste des étudiants (`/students`)
- Détail d'un étudiant (`/students/{id}`)
- Ajout et modification d'un étudiant (`/students/new`, `/students/{id}/edit`)

## Exécution des tests

Pour exécuter les tests JUnit, il faut :
- avoir démarré Docker-Desktop sur votre poste de travail local. Cette étape est nécessaire car les tests d'intégration ont besoin de Docker pour créer une base MySQL temporaire de test (Testcontainers).
- arrêter le backend s'il tourne.
- dans une console, se placer à la racine du projet et exécuter la commande Maven suivante :

```
./mvnw clean test
```

Résultat attendu à la fin du log : `Tests run: 20, Failures: 0, Errors: 0` et `BUILD SUCCESS`.

| Fichier | Type | Tests | Ce qu'il teste |
| --- | --- | --- | --- |
| `StudentServiceTest` | unitaire (Mockito) | 5 | `findAll`, `findById`, `create`, `update`, `delete` |
| `UserServiceTest` | unitaire (Mockito) | 4 | inscription et connexion |
| `JwtServiceTest` | unitaire | 2 | fabrication et lecture du token |
| `StudentControllerTest` | intégration (MockMvc + MySQL) | 5 | les 5 routes `/api/students`, avec le token |
| `UserControllerTest` | intégration (MockMvc + MySQL) | 4 | `/api/register` et `/api/login` |

## Couverture de code (JaCoCo)

Le rapport est généré automatiquement à chaque `./mvnw clean test` :

```
open target/site/jacoco/index.html
```

Une copie du dernier rapport est rangée dans le dossier [`rapports/`](rapports/) :

- `rapports/couverture-jacoco/index.html` : le rapport de couverture
- `rapports/resultats-tests.txt` : le résultat des tests et le résumé de la couverture

| Mesure | Couverture |
| --- | --- |
| Instructions | 82,3 % |
| Lignes | 84,7 % |

Le code généré par Lombok est exclu de la mesure grâce au fichier `lombok.config`.

