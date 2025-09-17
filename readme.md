# 🧘 Yoga App — Application Fullstack

## 🎯 Présentation
Yoga App est une application web fullstack (Angular + Spring Boot + MySQL), pensée pour gérer des sessions de yoga avec des rôles utilisateurs / administrateurs.  
Elle permet :  
- S'inscrire / se connecter  
- Pour un administrateur : créer, modifier, supprimer des sessions  
- Pour un utilisateur : consulter les sessions, participer à une session

---

## ⚙️ Prérequis
- Java 11  
- Maven 3.9+  
- Node.js 16 + npm  
- Angular CLI 14  
- MySQL 8+ (port 3306)  

---

## 🗄️ Installation de la base de données
```bash
mysql -u root -p
CREATE DATABASE yoga CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit
mysql -u root -p yoga < ressources/sql/script.sql
```

---

## 🚀 Installation & lancement

### Back-end
```bash
cd back
mvn clean install
mvn spring-boot:run
```
API accessible sur : `http://localhost:8080`

### Front-end
```bash
cd front
npm install
ng serve
```
Front accessible sur : `http://localhost:4200`

---

## 🔐 Identifiants de test
- **Admin** :  
  - email : `yoga@studio.com`  
  - mot de passe : `test!1234`

---

## 🧪 Tests & couverture

### Tests unitaires / intégration backend
```bash
cd back
mvn clean verify
```
Rapport : `back/target/site/jacoco/index.html`

### Tests front-end et Jest
```bash
cd front
npm run test
```
Rapport : `front/coverage/index.html`

### Tests end-to-end (Cypress)
```bash
cd front
npx cypress open          # pour naviguer dans les specs via interface
npx cypress run --browser chrome
```

---

## 📷 Captures des rapports de couverture
Les captures sont dans `docs/screenshots/` :  
- Back-end (JaCoCo) → `docs/screenshots/coverage-back.png`  
- Front‑end (Jest) → `docs/screenshots/coverage-front.png`  
- End‑to‑End (Cypress) → `docs/screenshots/coverage-e2e.png`




