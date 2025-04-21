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
  (1, 'Pantalon', 'images_Article/Levis_noir.jpg', 'Levi\'s', 'Le Levi''s 511 offre une silhouette moderne avec sa coupe slim fit. En denim stretch pour une liberté de mouvement, ce pantalon allie élégance et confort. Poches avant et arrière fonctionnelles, fermeture à zip et bouton. Parfait pour une tenue quotidienne ou sortie habillée décontractée. ', 35.00, 26.00, 30, 15, 8),
  (2, 'Pantalon', 'images_Article/Adidas.jpg', 'Adidas', 'Pantalon de sport technique Adidas Tiro 23 en polyester stretch. Coupe tapered avec bandes contrastées, ceinture élastiquée et poches zippées. Insertions stretch aux genoux et traitement anti-odeurs. Idéal entraînement et style urbain. Lavage 30°C. Tailles S-XXL.', 45.00, 33.00, 30, 20, 9),
  (3, 'Chaussures', 'images_Article/chaussure_Nike.jpg', 'Nike', 'Chaussures Nike Air Max 270 avec amorti Air Max visible pour un confort inégalé. Upper en mesh respirant, semelle en caoutchouc et logo swoosh contrasté. Idéales pour le sport et le style urbain. Disponibles en plusieurs coloris. Poids léger (320g).', 60.00, 46.00, 25, 13, 8),
  (4, 'Chapeau de plage', 'images_Article/tencozPlage.jpg', 'Tencoz', 'Chapeau de plage avec protection UV 50+, bord large 7cm en polyester respirant. Bandeau coton intérieur et lanière réglable. Léger (120g) et pliable. Tailles 54-60cm. Parfait pour plage, piscine et activités nautiques.', 38.55, 19.99, 24, 15, 7);
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
