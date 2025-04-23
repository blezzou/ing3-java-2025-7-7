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
    (1, 'Jean', 'images_Article/jean_levis_511.jpg', 'Levi\'s', 'Le Levi\'s 511 offre une silhouette moderne avec sa coupe slim fit. En denim stretch pour une liberté de mouvement, ce pantalon allie élégance et confort. Parfait pour une tenue quotidienne !', 35.00, 26.00, 30, 15, 4),
    (2, 'Pantalon de survêtement', 'images_Article/pantalon_adidas.jpg', 'Adidas', 'Pantalon de sport technique Adidas Tiro 23 en polyester stretch. Coupe tapered avec bandes contrastées, ceinture élastiquée et poches zippées. Insertions stretch aux genoux et traitement anti-odeurs. Idéal entraînement et style urbain.', 45.00, 33.00, 30, 20, 4),
    (3, 'Chaussures', 'images_Article/chaussure_nike.jpg', 'Nike', 'Chaussures Nike Air Max 270 avec amorti Air Max visible pour un confort inégalé. Upper en mesh respirant, semelle en caoutchouc et logo swoosh contrasté. Idéales pour le sport et le style urbain. Disponibles en plusieurs coloris. Poids léger (320g).', 60.00, 46.00, 25, 13, 5),
    (4, 'Chapeau de plage', 'images_Article/chapeau_tencoz.jpg', 'Tencoz', 'Chapeau de plage avec protection UV 50+, bord large 7cm en polyester respirant. Bandeau coton intérieur et lanière réglable. Léger (120g) et pliable. Tailles 54-60cm. Parfait pour plage, piscine et activités nautiques. ', 38.55, 19.99, 24, 15, 3),
    (5, 'Tee-Shirt', 'images_Article/tshirt_boss.jpg', 'Boss', 'Offrez-vous ce tee-shirt de marque BOSS, synonyme de qualité et de sophistication. Conçu dans un matériau doux et respirant (coton ou mélange selon modèle), ce t-shirt allie confort et durabilité pour un port agréable au quotidien ! ', 20.00, 12.50, 40, 25, 3),
    (6, 'Manteau', 'images_Article/manteau_tyqqu.jpg', 'Tyqqu', 'Mélange de coton et de polyester, fausse fourrure de haute qualité, doux, confortable et super chaud. Manteau d\'hiver classique et simple, couleur unie, vous rend plus belle, tendance et élégante.', 110.00, 85.00, 15, 10, 4),
    (7, 'Maillot de bain pour femmes', 'images_Article/maillotdebainF.jpg', 'Nabaiji', 'Découvrez ce maillot de bain une pièce féminin, alliant style, confort et maintien pour des moments détente ou sportifs en toute élégance. Idéal pour la plage, la piscine ou les vacances !', 9.00, 6.00, 20, 12, 3),
    (8, 'Maillot de bain pour hommes', 'images_Article/maillotdebainH.jpg', 'Haizid', 'Maillots de Bain Anti-Frottement pour Hommes HAZID - Séchage Rapide, Polyester Extensible & Spandex, Noir avec Lettrage Blanc, Taille Élastique, Lavable en Machine pour la Natation Compétitive & Récréative.', 7.00, 4.50, 30, 15, 3),
    (9, 'Pull', 'images_Article/pull_jinkang.jpg', 'Jinkang', 'Pull en Tricot Ajusté pour Homme - Chaud, Extensible en Polyester avec Motif Géométrique pour Automne/Hiver', 19.50, 12.99, 35, 22, 3),
    (10, 'Tee-Shirt', 'images_Article/tshirt_basic.jpg', 'Basic', 'Tee-shirt basique en coton 100% de coupe standard. Col rond et manches courtes. Matière douce et respirante pour un confort quotidien. Idéal pour toutes les occasions.', 12.90, 8.90, 50, 30, 4),
    (11, 'Tee-Shirt', 'images_Article/tshirt_uniqlo.jpg', 'Uniqlo', 'Tee-shirt uni noir en coton stretch. Coupe légèrement ajustée, résistant au rétrécissement. Lavage facile, parfait pour un look minimaliste.', 14.90, 9.90, 45, 25, 4),
    (12, 'Jean', 'images_Article/jean_levis_501.jpeg', 'Levi\'s', 'Jean classique 501 en denim pur coton. Coupe droite, boutons métalliques et cinq poches. Intemporel et durable pour un style décontracté.', 59.90, 45.00, 35, 20, 5),
    (13, 'Jean', 'images_Article/jean_h&m.jpg', 'H&M', 'Jean slim en denim stretch avec élasthanne. Coupe ajustée mais confortable, ceinture à boucles et fermeture à bouton.', 39.90, 19.90, 40, 25, 3),
    (14, 'Pull', 'images_Article/pull_zara.jpg', 'Zara', 'Pull unicolore en coton doux avec col rond. Coupe oversize pour un style décontracté. Manches longues et ourlet côtelé.', 29.90, 19.90, 30, 18, 4),
    (15, 'Pull', 'images_Article/pull_mango.jpg', 'Mango', 'Pull en laine mérinos avec col en rond. Coupe ajustée, idéal pour superposer ou porter seul. Doux et chaud pour l\'hiver.', 29.90, 29.90, 25, 15, 4),
    (16, 'Chemise', 'images_Article/chemise_boss.jpeg', 'Boss', 'Chemise classique en coton poplin. Col classique et coupe slim. Parfaite pour les occasions formelles.', 49.90, 35.00, 20, 12, 4),
    (17, 'Chemise', 'images_Article/chemise_tommy.jpg', 'Tommy Hilfiger', 'Chemise en coton à rayures fines. Col boutonné et poches poitrine. Style décontracté mais soigné pour un look quotidien.', 44.90, 32.00, 22, 14, 4),
    (18, 'Sweat-shirt', 'images_Article/sweat_nike.jpg', 'Nike', 'Sweat-shirt en coton avec capuche, poche kangourou et manches longues. Logo brodé sur la poitrine.', 45.00, 32.00, 28, 17, 4),
    (19, 'Sweat-shirt', 'images_Article/sweat_adidas.jpg', 'Adidas', 'Sweat-shirt uni en coton doux. Col rond et manches longues. Logo imprimé discret. Coupe confortable pour un style sportif.', 39.90, 29.90, 30, 18, 4),
    (20, 'Veste', 'images_Article/veste_tommy.jpg', 'Tommy Hilfiger', 'Veste en jean classique avec poches avant et boutons pression. Style intemporel pour toutes les saisons.', 49.90, 35.00, 18, 10, 4),
    (21, 'Veste', 'images_Article/veste_columbia.jpg', 'Columbia', 'Veste d\'hiver matelassée avec capuche intégrée. Poches et fermeture à zip. Parfaite pour le froid.', 59.90, 45.00, 15, 9, 4),
    (22, 'Chaussures', 'images_Article/chaussures_converse.jpg', 'Converse', 'Baskets montantes \'All-Star\' en toile avec semelle en caoutchouc. Style intemporel et polyvalent.', 55.00, 42.00, 25, 15, 5),
    (23, 'Chaussures', 'images_Article/chaussures_vans.jpg', 'Vans', 'Baskets noires basses en toile renforcée. Lacets et logo signature.', 65.00, 49.00, 20, 12, 4),
    (24, 'Short', 'images_Article/short_diesel.jpg', 'Diesel', 'Short bleu en denim. Taille normale. Style décontracté pour l\'été.', 39.90, 29.90, 25, 15, 3);
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
