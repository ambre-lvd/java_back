-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : lun. 05 jan. 2026 à 22:22
-- Version du serveur : 10.4.28-MariaDB
-- Version de PHP : 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `restaurant_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `dishes`
--

CREATE TABLE `dishes` (
  `id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `category` varchar(50) NOT NULL,
  `image_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `dishes`
--

INSERT INTO `dishes` (`id`, `name`, `description`, `price`, `category`, `image_path`) VALUES
('B1', 'Thé Glacé', 'Maison, citron vert', 3.50, 'Boisson', 'Desserts/the glace.png'),
('B2', 'Bière Tsingtao', 'Bière blonde 33cl', 4.50, 'Boisson', 'Desserts/tsingtao.png'),
('B3', 'Limonade Jap', 'Ramune à la bille', 4.00, 'Boisson', 'Desserts/ramune.png'),
('B4', 'Jus de Coco', 'Avec morceaux', 3.90, 'Boisson', 'Desserts/jus coco.png'),
('B5', 'Saké', 'Petit pichet', 6.00, 'Boisson', 'Desserts/sake.png'),
('D1', 'Perles de Coco', '2 pièces, chaud', 4.50, 'Dessert', 'Desserts/coco.png'),
('D2', 'Mochi Glacé', '2 pièces, parfum au choix', 5.50, 'Dessert', 'Desserts/mochi.png'),
('D3', 'Mangue Fraîche', 'Tranches de mangue', 5.90, 'Dessert', 'Desserts/mangue.png'),
('D4', 'Banane Flambée', 'Au saké', 6.50, 'Dessert', 'Desserts/banane.png'),
('D5', 'Nougat Chinois', 'Aux graines de sésame', 3.50, 'Dessert', 'Desserts/nougat.png'),
('E1', 'Nems Poulet', '4 pièces, sauce nuoc-mâm', 6.90, 'Entrée', 'Entrees/nems.png'),
('E10', 'Dim Sum Mix', 'Panier vapeur (6 pièces)', 9.90, 'Entrée', 'Entrees/dim sum mix.png'),
('E2', 'Rouleaux Printemps', 'Crevettes, menthe, riz', 5.50, 'Entrée', 'Entrees/rouleaux printemps.png'),
('E3', 'Gyozas Poulet', 'Raviolis grillés (5 pièces)', 6.50, 'Entrée', 'Entrees/gyozas.png'),
('E4', 'Samoussas Bœuf', 'Croustillants aux épices', 6.00, 'Entrée', 'Entrees/samoussa.png'),
('E5', 'Salade de Chou', 'Chou blanc mariné sésame', 4.50, 'Entrée', 'Entrees/salade de chou.png'),
('E6', 'Soupe Miso', 'Tofu, algues wakame', 4.00, 'Entrée', 'Entrees/Soupe miso.png'),
('E7', 'Tempura Crevettes', 'Beignets légers (4 pièces)', 8.90, 'Entrée', 'Entrees/crevettes.png'),
('E8', 'Yakitori Bœuf', 'Brochettes bœuf-fromage', 5.90, 'Entrée', 'Entrees/yakitori boeuf.png'),
('E9', 'Edamame', 'Fèves de soja, fleur de sel', 4.50, 'Entrée', 'Entrees/Edamame.png'),
('P1', 'Pad Thaï', 'Nouilles riz, crevettes', 14.90, 'Plat', 'Plats/pad thai.png'),
('P10', 'Wok Végé', 'Nouilles, tofu', 11.90, 'Plat', 'Plats/wok vege.png'),
('P2', 'Bo Bun Bœuf', 'Vermicelles, bœuf sauté', 13.50, 'Plat', 'Plats/bobun.png'),
('P3', 'Curry Vert', 'Poulet, lait coco', 12.90, 'Plat', 'Plats/curry.png'),
('P4', 'Riz Cantonais', 'Riz sauté, jambon', 10.50, 'Plat', 'Plats/riz cantonais.png'),
('P5', 'Porc Caramel', 'Travers confits', 13.90, 'Plat', 'Plats/porc caramel.png'),
('P6', 'Canard Laqué', 'Avec crêpes', 16.90, 'Plat', 'Plats/canard.png'),
('P7', 'Bibimbap', 'Riz, bœuf, légumes', 14.50, 'Plat', 'Plats/bibimbap.png'),
('P8', 'Ramen Tonkotsu', 'Bouillon porc, nouilles', 13.90, 'Plat', 'Plats/ramen.png'),
('P9', 'Sushi Mix 12', 'Assortiment sushis', 15.90, 'Plat', 'Plats/sushi.png');

-- --------------------------------------------------------

--
-- Structure de la table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `table_number` int(11) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `order_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `dish_id` varchar(10) NOT NULL,
  `quantity` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `dishes`
--
ALTER TABLE `dishes`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `dish_id` (`dish_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
