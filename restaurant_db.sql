-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : jeu. 08 jan. 2026 à 14:29
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
  `image_path` varchar(255) DEFAULT NULL,
  `disponibilite` int(11) DEFAULT 0,
  `allergens` varchar(255) DEFAULT NULL,
  `calories` int(11) DEFAULT 0,
  `prep_time` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `Dish`
--

INSERT INTO `Dish` (`id`, `name`, `description`, `price`, `category_id`, `image_path`, `disponibilite`, `allergens`, `calories`, `prep_time`) VALUES
('B1', 'Thé Glacé', 'Thé frais fait maison avec citron vert et glaçons, idéal pour se rafraîchir', 3.5, 4, 'Desserts/the.png', 0, 'Aucun', 5, '5-10 min'),
('B2', 'Bière Tsingtao', 'Bière blonde premium chinoise très rafraîchissante 33cl', 4.5, 4, 'Desserts/biere.png', 0, 'Gluten, orge', 150, 'immédiat'),
('B3', 'Limonade Jap', 'Boisson pétillante japonaise authentique avec bille en verre', 4, 4, 'Desserts/limonade.png', 0, 'Aucun', 120, 'immédiat'),
('B4', 'Jus de Coco', 'Jus de coco frais avec morceaux, saveur tropicale naturelle', 3.9, 4, 'Desserts/juscoco.png', 0, 'Noix de coco', 180, 'immédiat'),
('B5', 'Saké', 'Saké traditionnel japonais servi dans un petit pichet raffiné', 6, 4, 'Desserts/sake.png', 0, 'Riz, sulfites', 140, 'immédiat'),
('D1', 'Perles de Coco', 'Boulettes moelleuses de coco 2 pièces servies chaudes et enrobées', 4.5, 3, 'Desserts/perlecoco.png', 0, 'Noix de coco, gluten', 280, '8-10 min'),
('D2', 'Mochi Glacé', 'Gâteaux de riz farcis à la glace, saveurs vanille et matcha 2 pièces', 5.5, 3, 'Desserts/mochi.png', 0, 'Riz, lait, œuf', 320, '5 min'),
('D3', 'Mangue Fraîche', 'Mangue juteuse et sucrée coupée en tranches fines, gourmandise tropicale', 5.9, 3, 'Desserts/mangue.png', 0, 'Aucun', 210, '5 min'),
('D4', 'Banane Flambée', 'Banane caramélisée et flambée au saké pour un dessert spectaculaire', 6.5, 3, 'Desserts/banane.png', 0, 'Riz, sulfites', 350, '12-15 min'),
('D5', 'Nougat Chinois', 'Friandise croustillante aux graines de sésame, spécialité asiatique', 3.5, 3, 'Desserts/nougat.png', 0, 'Sésame, cacahuète', 420, '10 min'),
('E1', 'Nems Poulet', '4 rouleaux croustillants de poulet servis avec sauce nuoc-mâm savoureuse', 6.9, 1, 'Entrees/nems.png', 0, 'Blé, poisson', 380, '10-12 min'),
('E10', 'Dim Sum Mix', 'Panier vapeur de 6 petits plats assortis de spécialités chinoises', 9.9, 1, 'Entrees/dimsum.png', 0, 'Gluten, crustacés, œuf', 520, '15-18 min'),
('E2', 'Rouleaux Printemps', 'Rouleaux frais garnis de crevettes, menthe et vermicelles de riz', 5.5, 1, 'Entrees/rouleaux.png', 0, 'Crustacés, poisson', 280, '8-10 min'),
('E3', 'Gyozas Poulet', 'Raviolis de poulet grillés à la plancha 5 pièces, croustillants dehors moelleux dedans', 6.5, 1, 'Entrees/gyozas.png', 0, 'Gluten, œuf, soja', 320, '10-12 min'),
('E4', 'Samoussas Bœuf', 'Beignets triangulaires croustillants au bœuf avec un mélange d\'épices orientales', 6, 1, 'Entrees/samoussa.png', 0, 'Blé, sésame', 360, '12-15 min'),
('E5', 'Salade de Chou', 'Chou blanc frais mariné à la vinaigrette de sésame, salade croquante et légère', 4.5, 1, 'Entrees/salade.png', 0, 'Sésame', 120, '5 min'),
('E6', 'Soupe Miso', 'Bouillon de soja délicat garni de tofu soyeux et algues wakame', 4, 1, 'Entrees/soupe.png', 0, 'Soja, algues', 150, '8-10 min'),
('E7', 'Tempura Crevettes', 'Crevettes enrobées d\'une pâte à tempura légère et dorée 4 pièces', 8.9, 1, 'Entrees/crevettes.png', 0, 'Crustacés, gluten, œuf', 420, '12-15 min'),
('E8', 'Yakitori Bœuf', 'Brochettes de bœuf mariné et fromage grillées à la sauce sucrée-salée', 5.9, 1, 'Entrees/yakitori.png', 0, 'Œuf, lait, soja', 380, '10-12 min'),
('E9', 'Edamame', 'Jeunes fèves de soja fraîches bouillies et assaisonnées de fleur de sel', 4.5, 1, 'Entrees/edamame.png', 0, 'Soja', 180, '5-8 min'),
('P1', 'Pad Thaï', 'Nouilles de riz sautées aux crevettes, œuf et cacahuètes, classique thaïlandais', 14.9, 2, 'Plats/padthai.png', 0, 'Crustacés, cacahuète, soja', 680, '15-18 min'),
('P10', 'Wok Végé', 'Nouilles et légumes frais sautés au wok avec tofu mariné', 11.9, 2, 'Plats/wok.png', 0, 'Soja', 520, '12-15 min'),
('P2', 'Bo Bun Bœuf', 'Vermicelles de riz frais avec bœuf sauté, légumes et sauce piquante', 13.5, 2, 'Plats/bobun.png', 0, 'Poisson, cacahuète', 640, '12-15 min'),
('P3', 'Curry Vert', 'Poulet tendre dans une sauce curry verte riche au lait de coco', 12.9, 2, 'Plats/curry.png', 0, 'Noix de coco, lait', 720, '18-20 min'),
('P4', 'Riz Cantonais', 'Riz sauté aux œufs, jambon et légumes mélangés, savoureux et réconfortant', 10.5, 2, 'Plats/riz.png', 0, 'Œuf, soja, gluten', 580, '12-15 min'),
('P5', 'Porc Caramel', 'Travers de porc fondants confits dans une sauce caramel dorée et savoureuse', 13.9, 2, 'Plats/porc.png', 0, 'Soja, sucre', 750, '20-25 min'),
('P6', 'Canard Laqué', 'Canard rôti à la peau croustillante servie avec crêpes et sauce hoisin', 16.9, 2, 'Plats/canard.png', 0, 'Gluten, soja, œuf', 820, '25-30 min'),
('P7', 'Bibimbap', 'Riz coréen garni de bœuf, légumes cuits et crus mélangés au gochujang', 14.5, 2, 'Plats/bibimbap.png', 0, 'Soja, sésame, œuf', 700, '15-18 min'),
('P8', 'Ramen Tonkotsu', 'Nouilles ondulées dans un bouillon riche de porc blanc au goût intense', 13.9, 2, 'Plats/ramen.png', 0, 'Gluten, soja, œuf', 780, '20-25 min'),
('P9', 'Sushi Mix 12', 'Assortiment varié de 12 pièces de sushi frais avec poisson cru de qualité', 15.9, 2, 'Plats/sushi.png', 0, 'Poisson, crustacés, œuf, soja', 620, '10-12 min');

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
(8, 56, 9.90, '2026-01-07 09:43:38'),
(9, 65, 16.80, '2026-01-07 13:31:49'),
(10, 13, 31.70, '2026-01-07 13:33:46'),
(11, 2, 13.90, '2026-01-07 13:56:49'),
(12, 66, 7.94, '2026-01-07 14:19:29'),
(13, 54, 19.32, '2026-01-07 14:21:08'),
(14, 20, 6.90, '2026-01-08 09:26:48'),
(15, 15, 5.50, '2026-01-08 09:43:12'),
(16, 16, 9.90, '2026-01-08 10:01:54'),
(17, 20, 6.90, '2026-01-08 10:04:42'),
(18, 30, 6.90, '2026-01-08 10:09:49'),
(19, 20, 15.40, '2026-01-08 10:23:20'),
(20, 25, 39.20, '2026-01-08 10:41:14');

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
(15, 8, 'E10', 1, 2, 1),
(16, 12, 'E1', 1, 0, 0),
(17, 13, 'E1', 1, 0, 0),
(18, 13, 'E10', 1, 0, 0),
(19, 14, 'E1', 1, 1, 1),
(20, 15, 'E2', 1, 2, 1),
(21, 16, 'E10', 1, 1, 1),
(22, 17, 'E1', 1, 2, 1),
(23, 18, 'E1', 1, 1, 1),
(24, 19, 'E10', 1, 2, 1),
(25, 19, 'D2', 1, 0, 0),
(26, 20, 'E10', 1, 0, 0),
(27, 20, 'E10', 1, 0, 0),
(28, 20, 'E2', 1, 2, 1),
(29, 20, 'P5', 1, 0, 0);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT pour la table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

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
