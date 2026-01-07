-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : mer. 07 jan. 2026 à 10:55
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
-- Structure de la table `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `category`
--

INSERT INTO `category` (`id`, `name`) VALUES
(1, 'Entree'),
(2, 'Plat'),
(3, 'Dessert'),
(4, 'Boisson');

-- --------------------------------------------------------

--
-- Structure de la table `Dish`
--

CREATE TABLE `Dish` (
  `id` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` float NOT NULL,
  `category_id` int(11) NOT NULL,
  `image_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `Dish`
--

INSERT INTO `Dish` (`id`, `name`, `description`, `price`, `category_id`, `image_path`) VALUES
('B1', 'Thé Glacé', 'Maison, citron vert', 3.5, 4, 'Desserts/the.png'),
('B2', 'Bière Tsingtao', 'Bière blonde 33cl', 4.5, 4, 'Desserts/biere.png'),
('B3', 'Limonade Jap', 'Ramune à la bille', 4, 4, 'Desserts/limonade.png'),
('B4', 'Jus de Coco', 'Avec morceaux', 3.9, 4, 'Desserts/juscoco.png'),
('B5', 'Saké', 'Petit pichet', 6, 4, 'Desserts/sake.png'),
('D1', 'Perles de Coco', '2 pièces, chaud', 4.5, 3, 'Desserts/perlecoco.png'),
('D2', 'Mochi Glacé', '2 pièces, parfum au choix', 5.5, 3, 'Desserts/mochi.png'),
('D3', 'Mangue Fraîche', 'Tranches de mangue', 5.9, 3, 'Desserts/mangue.png'),
('D4', 'Banane Flambée', 'Au saké', 6.5, 3, 'Desserts/banane.png'),
('D5', 'Nougat Chinois', 'Aux graines de sésame', 3.5, 3, 'Desserts/nougat.png'),
('E1', 'Nems Poulet', '4 pièces, sauce nuoc-mâm', 6.9, 1, 'Entrees/nems.png'),
('E10', 'Dim Sum Mix', 'Panier vapeur (6 pièces)', 9.9, 1, 'Entrees/dimsum.png'),
('E2', 'Rouleaux Printemps', 'Crevettes, menthe, riz', 5.5, 1, 'Entrees/rouleaux.png'),
('E3', 'Gyozas Poulet', 'Raviolis grillés (5 pièces)', 6.5, 1, 'Entrees/gyozas.png'),
('E4', 'Samoussas Bœuf', 'Croustillants aux épices', 6, 1, 'Entrees/samoussa.png'),
('E5', 'Salade de Chou', 'Chou blanc mariné sésame', 4.5, 1, 'Entrees/salade.png'),
('E6', 'Soupe Miso', 'Tofu, algues wakame', 4, 1, 'Entrees/soupe.png'),
('E7', 'Tempura Crevettes', 'Beignets légers (4 pièces)', 8.9, 1, 'Entrees/crevettes.png'),
('E8', 'Yakitori Bœuf', 'Brochettes bœuf-fromage', 5.9, 1, 'Entrees/yakitori.png'),
('E9', 'Edamame', 'Fèves de soja, fleur de sel', 4.5, 1, 'Entrees/edamame.png'),
('P1', 'Pad Thaï', 'Nouilles riz, crevettes', 14.9, 2, 'Plats/padthai.png'),
('P10', 'Wok Végé', 'Nouilles, tofu', 11.9, 2, 'Plats/wok.png'),
('P2', 'Bo Bun Bœuf', 'Vermicelles, bœuf sauté', 13.5, 2, 'Plats/bobun.png'),
('P3', 'Curry Vert', 'Poulet, lait coco', 12.9, 2, 'Plats/curry.png'),
('P4', 'Riz Cantonais', 'Riz sauté, jambon', 10.5, 2, 'Plats/riz.png'),
('P5', 'Porc Caramel', 'Travers confits', 13.9, 2, 'Plats/porc.png'),
('P6', 'Canard Laqué', 'Avec crêpes', 16.9, 2, 'Plats/canard.png'),
('P7', 'Bibimbap', 'Riz, bœuf, légumes', 14.5, 2, 'Plats/bibimbap.png'),
('P8', 'Ramen Tonkotsu', 'Bouillon porc, nouilles', 13.9, 2, 'Plats/ramen.png'),
('P9', 'Sushi Mix 12', 'Assortiment sushis', 15.9, 2, 'Plats/sushi.png');

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

--
-- Déchargement des données de la table `orders`
--

INSERT INTO `orders` (`id`, `table_number`, `total_amount`, `order_date`) VALUES
(1, 1, 6.90, '2026-01-06 16:10:19'),
(2, 12, 19.80, '2026-01-06 16:14:29'),
(3, 1, 19.80, '2026-01-07 08:18:42'),
(4, 22, 11.00, '2026-01-07 08:19:23'),
(5, 2, 11.00, '2026-01-07 08:19:53'),
(6, 30, 16.40, '2026-01-07 09:04:19'),
(7, 15, 25.30, '2026-01-07 09:16:05'),
(8, 56, 9.90, '2026-01-07 09:43:38');

-- --------------------------------------------------------

--
-- Structure de la table `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `dish_id` varchar(10) NOT NULL,
  `quantity` int(11) DEFAULT 1,
  `piment` int(11) NOT NULL DEFAULT 0,
  `accompagnement` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `dish_id`, `quantity`, `piment`, `accompagnement`) VALUES
(1, 1, 'E1', 1, 0, 0),
(2, 2, 'E10', 1, 0, 0),
(3, 2, 'E10', 1, 0, 0),
(4, 3, 'E10', 1, 0, 0),
(5, 3, 'E10', 1, 0, 0),
(6, 4, 'E2', 1, 0, 0),
(7, 4, 'E2', 1, 0, 0),
(8, 5, 'E2', 1, 0, 0),
(9, 5, 'E2', 1, 0, 0),
(10, 6, 'E10', 1, 2, 0),
(11, 6, 'E3', 1, 0, 1),
(12, 7, 'E10', 1, 0, 0),
(13, 7, 'E10', 1, 0, 0),
(14, 7, 'E2', 1, 2, 1),
(15, 8, 'E10', 1, 2, 1);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `Dish`
--
ALTER TABLE `Dish`
  ADD PRIMARY KEY (`id`),
  ADD KEY `category_id` (`category_id`);

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
-- AUTO_INCREMENT pour la table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `Dish`
--
ALTER TABLE `Dish`
  ADD CONSTRAINT `fk_dishes_categories` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON UPDATE CASCADE;

--
-- Contraintes pour la table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `fk_order_items_dish` FOREIGN KEY (`dish_id`) REFERENCES `Dish` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
