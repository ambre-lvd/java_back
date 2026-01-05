# java_back

CREATE DATABASE IF NOT EXISTS restaurant_db;
USE restaurant_db;

-- Table des plats (Le Menu)
CREATE TABLE dishes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    category VARCHAR(50) NOT NULL
);

-- Table des commandes (Pour l'historique)
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_price DOUBLE NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Données initiales pour le menu
INSERT INTO dishes (name, price, category) VALUES 
('Burger Gourmet', 14.50, 'Plat'),
('Salade César', 11.00, 'Entrée'),
('Profiteroles', 8.00, 'Dessert'),
('Vin Rouge (verre)', 5.50, 'Boisson');
