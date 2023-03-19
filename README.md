# Workshop : Des micro services avec Spring Boot et Eureka discovery

Dans cet atelier, vous apprendrez à lancer un système dont l'architecture est en microservices.
Vous découvrirez le rôle du discovery et comment se déroule la communication entre microservices.

## Présentations des services 

Ce projet se constitue de 4 sous projets :
* `discovery-server` : ce microservice sert d'annuaire. Tous les autres services s'enregistrent auprès de lui, et peuvent contacter les autres services par cet annuaire (juste par leur nom)
* `movie-info-service` : ce microservice fournit une information sur un film par son ID. L'implémentation récupère actuellement un titre de film au hasard sur internet
* `ratings-data-service` : ce microservice fournit les ratings des films, pour un film ou un utilisateur donné.  
* `movie-catalog-service` : ce microservice va chercher un rating pour un user, puis va chercher les infos du film associé à ce ratin. Ce service est le plus intéressant car il montre comment appeler d'autres services.

## Lancement du projet

Ce projet est constitué de plusieurs services, il faut donc les lancer un par un, dans un ordre bien spécifique, car le projet ne bénéficie pas d'un outil d'ordonnancement de lancement / déploiement.

Il serait intéressant pour faciliter le lancement en local ou le déploiement sur un serveur de créer un groupe de services avec docker-compose, mais cela dépasse le périmètre de ce petit atelier.

Il faut commencer par lancer le serveur de discovery (l'annuaire permettant le référencement des services) : 

```
cd discovery-server
./mvnw spring-boot:run
```

Une fois que ce service est démarré, dans 3 terminaux différents, lancer le reste des services.
Note : si vous ouvrez le workspace `spring-boot-microservices.code-workspace` dans VSCode, la tâche de debug `StartAllServices` lance les 3 services (il vous faut donc au préalable juste lancer le discovery server)

Vérifiez que vos 4 services sont lancés, avant de passer à la suite (pas d'erreur au démarrage).

## Test du endpoint

Il faut attendre quelques secondes que les services aient bien démarré et que l'enregistrement auprès du `discovery-server` se soit fait avec succès. Une fois que toutes les pièces du puzzle sont bien assemblée, le endpoint de démo devrait marcher.

Il ne vous reste plus qu'à accéder à `http://localhost:8081/catalog/1` qui va récupérer pour un hypothétique user d'ID 1, deux ratings aléatoires, puis récupérer des films associés à ce rating (aléatoires également pour cette démo).

## Exercice : ajouter un endpoint accédant à des sous services