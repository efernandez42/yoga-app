# 🧘 Yoga App — Projet Fullstack (OpenClassrooms)

## 📌 Description
Yoga App est une application **fullstack** développée avec **Angular** (front-end), **Spring Boot** (back-end) et **MySQL** (base de données).

Fonctionnalités principales :
- Authentification (connexion / inscription).
- Gestion des utilisateurs et des sessions de yoga.
- Ajout, modification, suppression de sessions (admin).
- Participation à une session (utilisateur).

Le projet inclut une stratégie complète de **tests automatisés** : unitaires, intégration et end-to-end.

---

## ⚙️ Prérequis
- **Java 11** (OpenJDK ou équivalent)
- **Maven 3.9+**
- **Node.js 16** + **npm**
- **Angular CLI 14**
- **MySQL 8+** (port par défaut : `3306`)
- **Cypress** (installé via npm pour les tests E2E)

---

## 🗄️ Installation de la base de données
1. Créez une base MySQL nommée `yoga`.
2. Importez le script SQL :
   ```bash
   mysql -u root -p yoga < ressources/sql/script.sql
3. Vérifiez que l’utilisateur et les tables sont correctement créés.

## 🚀 Installation et lancement de l’application
Back-end (Spring Boot)
 ```bash
cd back
 ```bash
mvn clean install
 ```bash
mvn spring-boot:run


Par défaut, l’API démarre sur :
 ```bash
👉 http://localhost:8080

Front-end (Angular)
 ```bash
cd front
 ```bash
npm install
 ```bash
ng serve


Par défaut, l’application front démarre sur :
 ```bash
👉 http://localhost:4200

Identifiants de test

Admin :

login : yoga@studio.com

mot de passe : test!1234

## 🧪 Lancer les tests et générer les rapports de couverture
Front-end (unitaires + intégration)
 ```bash
cd front
 ```bash
npm run test


Rapport de couverture généré dans :

front/coverage/index.html

End-to-End (Cypress)

Mode interactif (recommandé pour naviguer dans les specs) :
 ```bash
cd front
 ```bash
npx cypress open


👉 Choisir E2E Testing → Chrome → sélectionner la spec à exécuter.

Mode headless (CI/CD) :
 ```bash
npx cypress run --browser chrome

Back-end (unitaires + intégration)
 ```bash
cd back
 ```bash
mvn clean verify


Rapport JaCoCo généré dans :

back/target/site/jacoco/index.html

## 📷 Captures d’écran des rapports

Les rapports de couverture sont disponibles dans docs/screenshots/ :

Back-end (JaCoCo)

Front-end (Jest)

End-to-End (Cypress)
