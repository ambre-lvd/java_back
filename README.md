# 🍜 NetWok - Backend Server API

Ce projet constitue la partie **Serveur (Back-end)** de la borne de commande **NetWok**.
Il s'agit d'une **API REST** légère développée en Java avec le framework **Javalin**, connectée à une base de données **MySQL**.

## 🛠 Technologies utilisées

* **Langage :** Java 21
* **Framework API :** [Javalin](https://javalin.io/) (Serveur Web léger)
* **Base de données :** MySQL
* **Build Tool :** Maven
* **Documentation :** OpenAPI / Swagger

---

## ⚙️ Prérequis

Avant de lancer le serveur, assurez-vous d'avoir installé :
1.  **Java JDK 21** ou supérieur.
2.  **Maven** (installé ou via votre IDE).
3.  **MySQL Server** (via WAMP, XAMPP, MAMP ou Docker).

---

## 🚀 Installation et Configuration

### 1. Base de Données
Le serveur nécessite une base de données MySQL pour fonctionner.

1.  Lancez votre serveur MySQL (Port 3306).
2.  Ouvrez votre gestionnaire SQL (phpMyAdmin, DBeaver, Workbench).
3.  Exécutez le script SQL fourni pour créer la structure et les données :
    * 📁 Emplacement : `src/main/resources/restaurant_db.sql`
    * *Note : Le script crée la base `restaurant_db` et la table `Dish`.*

### 2. Configuration de la connexion
Vérifiez que les identifiants de connexion correspondent à votre configuration locale dans le fichier :
`src/main/java/fr/isen/demo/service/DishServiceImpl.java` (ou `Main.java` selon votre implémentation).

Par défaut :
* **URL :** `jdbc:mysql://localhost:3306/restaurant_db`
* **User :** `root`
* **Password :** `""` (vide) ou `"root"` (pour MAMP).

---

## ▶️ Comment lancer le serveur

### Via un IDE (IntelliJ IDEA, Eclipse)
1.  Ouvrez le projet `java_back` (ou le dossier contenant le `pom.xml`) comme projet Maven.
2.  Laissez Maven télécharger les dépendances.
3.  Cherchez la classe principale : **`Main.java`**.
4.  Faites un clic droit -> **Run 'Main'**.
