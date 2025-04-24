-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3308
-- Généré le : jeu. 24 avr. 2025 à 17:26
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
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `article`
--

INSERT INTO `article` (`id_article`, `nom`, `image`, `marque`, `description`, `prix`, `prix_vrac`, `quantite`, `quantite_vrac`, `note`) VALUES
(1, 'article 1', '', 'marque 1', 'description 1', 10.00, 5.00, 10, 10, 0),
(2, 'article 3', '', 'marque 2', 'description 2', 100.00, 50.00, 100, 100, 0),
(5, 'test', 'test', 'test', 'test', 1.00, 1.00, 1, 1, 0);

-- --------------------------------------------------------

--
-- Structure de la table `panier`
--

DROP TABLE IF EXISTS `panier`;
CREATE TABLE IF NOT EXISTS `panier` (
  `id_panier` int NOT NULL AUTO_INCREMENT,
  `id_utilisateur` int NOT NULL,
  `prix` int NOT NULL,
  `validé` int NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id_panier`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `panier`
--

INSERT INTO `panier` (`id_panier`, `id_utilisateur`, `prix`, `validé`, `date`) VALUES
(7, 1, 0, 0, '2025-04-24');

-- --------------------------------------------------------

--
-- Structure de la table `panier_article`
--

DROP TABLE IF EXISTS `panier_article`;
CREATE TABLE IF NOT EXISTS `panier_article` (
  `id_panier` int NOT NULL,
  `id_article` int NOT NULL,
  `quantite` int DEFAULT '1',
  PRIMARY KEY (`id_panier`,`id_article`),
  KEY `id_article` (`id_article`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `panier_article`
--

INSERT INTO `panier_article` (`id_panier`, `id_article`, `quantite`) VALUES
(-1, 2, 1),
(7, 1, 1),
(-1, 5, 1);

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
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id_utilisateur`, `admin`, `nom`, `prenom`, `email`, `mot_de_passe`, `historique`) VALUES
(1, 0, 'Nicolas', 'Pellerin', 'nicolas@gmail.com', 'azerty', 0),
(3, 0, 'aaa', 'aaa', 'nicolas@gmail.com', 'aa', 0),
(6, 1, 'admin', 'admin', 'admin', 'admin', 0),
(7, 0, 'jean', 'jean', 'jean', 'jean', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
