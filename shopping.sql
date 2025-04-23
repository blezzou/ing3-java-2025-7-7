-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3308
-- Généré le : dim. 20 avr. 2025 à 08:30
-- Version du serveur : 8.2.0
-- Version de PHP : 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `shopping`
--

-- --------------------------------------------------------

--
-- Structure de la table `article`
--

DROP TABLE IF EXISTS `article`;
CREATE TABLE IF NOT EXISTS `article` (
  `id_article` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL,
  `marque` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `prix` decimal(10,2) NOT NULL,
  `prix_vrac` decimal(10,2) NOT NULL,
  `quantite` int NOT NULL,
  `quantite_vrac` int NOT NULL,
  `note` int NOT NULL,
  PRIMARY KEY (`id_article`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `article`
--

INSERT INTO `article` (`id_article`, `nom`, `image`, `marque`, `description`, `prix`, `prix_vrac`, `quantite`, `quantite_vrac`, `note`) VALUES
  (1, 'Pantalon', 'images_Article/Levis_noir.jpg', 'Levi\'s', 'Le Levi\'s 511 offre une silhouette moderne avec sa coupe slim fit. En denim stretch pour une liberté de mouvement, ce pantalon allie élégance et confort. Parfait pour une tenue quotidienne !', 35.00, 26.00, 30, 15, 4),
  (2, 'Pantalon', 'images_Article/Adidas.jpg', 'Adidas', 'Pantalon de sport technique Adidas Tiro 23 en polyester stretch. Coupe tapered avec bandes contrastées, ceinture élastiquée et poches zippées. Insertions stretch aux genoux et traitement anti-odeurs. Idéal entraînement et style urbain.', 45.00, 33.00, 30, 20, 4),
  (3, 'Basket', 'images_Article/chaussure_Nike.jpg', 'Nike', 'Chaussures Nike Air Max 270 avec amorti Air Max visible pour un confort inégalé. Upper en mesh respirant, semelle en caoutchouc et logo swoosh contrasté. Idéales pour le sport et le style urbain. Disponibles en plusieurs coloris. Poids léger (320g).', 60.00, 46.00, 25, 13, 5),
  (4, 'Chapeau de plage', 'images_Article/tencozPlage.jpg', 'Tencoz', 'Chapeau de plage avec protection UV 50+, bord large 7cm en polyester respirant. Bandeau coton intérieur et lanière réglable. Léger (120g) et pliable. Tailles 54-60cm. Parfait pour plage, piscine et activités nautiques. ', 38.55, 19.99, 24, 15, 3),
  (5, 'Tee-Shirt', 'images_Article/tshirt_boss.jpg', 'Boss', 'Offrez-vous ce tee-shirt de marque BOSS, synonyme de qualité et de sophistication. Conçu dans un matériau doux et respirant (coton ou mélange selon modèle), ce t-shirt allie confort et durabilité pour un port agréable au quotidien ! ', 20.00, 12.50, 40, 25, 3),
  (6, 'Manteau', 'images_Article/manteauChic', 'Tyqqu', 'Mélange de coton et de polyester, fausse fourrure de haute qualité, doux, confortable et super chaud. Manteau d\'hiver classique et simple, couleur unie, vous rend plus belle, tendance et élégante.', 110.00, 85.00, 15, 10, 4),
  (7, 'Maillot de bain pour femmes', 'images_Article/maillotdebainF.jpg', 'Nabaiji', 'Découvrez ce maillot de bain une pièce féminin, alliant style, confort et maintien pour des moments détente ou sportifs en toute élégance. Idéal pour la plage, la piscine ou les vacances !', 9.00, 6.00, 20, 12, 3),
  (8, 'Maillot de bain pour hommes', 'images_Article/maillotdebainH.jpg', 'Haizid', 'Maillots de Bain Anti-Frottement pour Hommes HAZID - Séchage Rapide, Polyester Extensible & Spandex, Noir avec Lettrage Blanc, Taille Élastique, Lavable en Machine pour la Natation Compétitive & Récréative.', 7.00, 4.50, 30, 15, 3),
  (9, 'Pull-over', 'images_Article/pullover_jinkang.jpg', 'Jinkang', 'Pull en Tricot Ajusté pour Homme - Chaud, Extensible en Polyester avec Motif Géométrique pour Automne/Hiver', 19.50, 12.99, 35, 22, 3);
COMMIT;

-- --------------------------------------------------------

--
-- Structure de la table `panier`
--

DROP TABLE IF EXISTS `panier`;
CREATE TABLE IF NOT EXISTS `panier` (
  `id_panier` int NOT NULL AUTO_INCREMENT,
  `id_utilisateur` int NOT NULL,
  `articles` json NOT NULL,
  `prix` int NOT NULL,
  `validé` int NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id_panier`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id_utilisateur` int NOT NULL AUTO_INCREMENT,
  `admin` int NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `historique` int NOT NULL,
  PRIMARY KEY (`id_utilisateur`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id_utilisateur`, `admin`, `nom`, `prenom`, `email`, `mot_de_passe`, `historique`) VALUES
(1, 0, 'Nicolas', 'Pellerin', 'nicolas@gmail.com', 'azerty', 0),
(3, 0, 'aaa', 'aaa', 'nicolas@gmail.com', 'aa', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
