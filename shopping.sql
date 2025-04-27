-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3308
-- Généré le : jeu. 27 avr. 2025 à 16:47
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
    `note` float NOT NULL,
    PRIMARY KEY (`id_article`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `article`
--

INSERT INTO `article` (`id_article`, `nom`, `image`, `marque`, `description`, `prix`, `prix_vrac`, `quantite`, `quantite_vrac`, `note`) VALUES
    (1, 'Jean', 'images_Article/jean_levis_511.jpg', 'Levi\'s', 'Le Levi\'s 511 offre une silhouette moderne avec sa coupe slim fit. En denim stretch pour une liberté de mouvement, ce pantalon allie élégance et confort. Parfait pour une tenue quotidienne !', 35.00, 95.00, 30, 3, 0),
    (2, 'Pantalon de survêtement', 'images_Article/pantalon_adidas.jpg', 'Adidas', 'Pantalon de sport technique Adidas Tiro 23 en polyester stretch. Coupe tapered avec bandes contrastées, ceinture élastiquée et poches zippées. Idéal pour l\'entraînement et pour un style urbain.', 45.00, 120.00, 30, 3, 0),
    (3, 'Chaussures', 'images_Article/chaussure_nike.jpg', 'Nike', 'Chaussures Nike Air Max 270 avec amorti Air Max visible pour un confort inégalé. Semelle en caoutchouc et logo swoosh contrasté. Idéales pour le sport et pour le style urbain.', 60.00, 160.00, 25, 3, 0),
    (4, 'Chapeau de plage', 'images_Article/chapeau_tencoz.jpg', 'Tencoz', 'Chapeau de plage avec protection UV 50+, bord large 7cm en polyester respirant. Bandeau coton intérieur et lanière réglable. Tailles 54-60cm. Parfait pour la plage ou la piscine.', 38.55, 101.50, 24, 3, 0),
    (5, 'Tee-Shirt', 'images_Article/tshirt_boss.jpg', 'Boss', 'Offrez-vous ce tee-shirt de marque BOSS, synonyme de qualité. Conçu dans un matériau doux et respirant, ce t-shirt allie confort et durabilité pour un port agréable au quotidien !', 20.00, 52.50, 40, 3, 0),
    (6, 'Manteau', 'images_Article/manteau_tyqqu.jpg', 'Tyqqu', 'Mélange de coton et de polyester, fourrure de haute qualité, doux, confortable et super chaud. Manteau d\'hiver classique et simple, couleur unie.', 110.00, 308.00, 15, 3, 0),
    (7, 'Maillot de bain pour femmes', 'images_Article/maillotdebainF.jpg', 'Nabaiji', 'Découvrez ce maillot de bain une pièce féminin, alliant style, confort et maintien pour des moments détente ou sportifs en toute élégance.', 9.00, 23.90, 20, 3, 0),
    (8, 'Maillot de bain pour hommes', 'images_Article/maillotdebainH.jpg', 'Haizid', 'Maillot de bain anti-frottement pour hommes. Noir avec lettrage blanc, taille élastique, lavable en machine et séchage rapide pour la natation compétitive et récréative.', 7.00, 17.50, 30, 3, 0),
    (9, 'Pull', 'images_Article/pull_jinkang.jpg', 'Jinkang', 'Pull en tricot ajusté pour hommes - Chaud, extensible en polyester avec motif géométrique pour automne/hiver', 19.50, 49.99, 35, 3, 0),
    (10, 'Tee-Shirt', 'images_Article/tshirt_basic.jpg', 'Basic', 'Tee-shirt basique en coton 100% de coupe standard. Col rond et manches courtes. Matière douce et respirante pour un confort quotidien. Idéal pour toutes les occasions.', 12.90, 32.50, 50, 3, 0),
    (11, 'Tee-Shirt', 'images_Article/tshirt_uniqlo.jpg', 'Uniqlo', 'Tee-shirt uni noir en coton stretch. Coupe légèrement ajustée, résistant au rétrécissement. Lavage facile, parfait pour un look minimaliste.', 14.90, 40.90, 45, 3, 0),
    (12, 'Jean', 'images_Article/jean_levis_501.jpeg', 'Levi\'s', 'Jean classique 501 en denim pur coton. Coupe droite, boutons métalliques et cinq poches. Intemporel et durable pour un style décontracté.', 59.90, 170.00, 35, 3, 0),
    (13, 'Jean', 'images_Article/jean_h&m.jpg', 'H&M', 'Jean slim en denim stretch avec élasthanne. Coupe ajustée mais confortable, ceinture à boucles et fermeture à bouton.', 39.90, 98.50, 40, 3, 0),
    (14, 'Pull', 'images_Article/pull_zara.jpg', 'Zara', 'Pull unicolore en coton doux avec col rond. Coupe oversize pour un style décontracté. Manches longues et ourlet côtelé.', 29.90, 82.00, 30, 3, 0),
    (15, 'Pull', 'images_Article/pull_mango.jpg', 'Mango', 'Pull en laine mérinos avec col en rond. Coupe ajustée, idéal pour superposer ou porter seul. Doux et chaud pour l\'hiver.', 29.90, 81.50, 25, 3, 0),
    (16, 'Chemise', 'images_Article/chemise_boss.jpeg', 'Boss', 'Chemise classique en coton poplin. Col classique et coupe slim. Parfaite pour les occasions formelles.', 49.90, 133.00, 20, 3, 0),
    (17, 'Chemise', 'images_Article/chemise_tommy.jpg', 'Tommy Hilfiger', 'Chemise en coton à rayures fines. Col boutonné et poches poitrine. Style décontracté mais soigné pour un look quotidien.', 44.90, 109.50, 22, 3, 0),
    (18, 'Sweat-shirt', 'images_Article/sweat_nike.jpg', 'Nike', 'Sweat-shirt en coton avec capuche, poche kangourou et manches longues. Logo brodé sur la poitrine.', 45.00, 120.50, 28, 3, 0),
    (19, 'Sweat-shirt', 'images_Article/sweat_adidas.jpg', 'Adidas', 'Sweat-shirt uni en coton doux. Col rond et manches longues. Logo imprimé discret. Coupe confortable pour un style sportif.', 39.90, 99.90, 30, 3, 0),
    (20, 'Veste', 'images_Article/veste_tommy.jpg', 'Tommy Hilfiger', 'Veste en jean classique avec poches avant et boutons pression. Style intemporel pour toutes les saisons.', 49.90, 125.00, 18, 3, 0),
    (21, 'Veste', 'images_Article/veste_columbia.jpg', 'Columbia', 'Veste d\'hiver matelassée avec capuche intégrée. Poches et fermeture à zip. Parfaite pour le froid.', 59.90, 147.00, 15, 3, 0),
    (22, 'Chaussures', 'images_Article/chaussures_converse.jpg', 'Converse', 'Baskets montantes \'All-Star\' en toile avec semelle en caoutchouc. Style intemporel et polyvalent.', 55.00, 148.00, 25, 3, 0),
    (23, 'Chaussures', 'images_Article/chaussures_vans.jpg', 'Vans', 'Baskets noires basses en toile renforcée. Lacets et logo signature.', 65.00, 181.00, 20, 3, 0),
    (24, 'Short', 'images_Article/short_diesel.jpg', 'Diesel', 'Short bleu en denim. Taille normale. Style décontracté pour l\'été.', 39.90, 109.50, 25, 3, 0),
    (25, 'Pantalon', 'images_Article/pantalon_celio.jpg', 'Celio', 'Conçu dans un tissu résistant et confortable, ce pantalon assure une coupe moderne et une liberté de mouvement au quotidien.', 53.80, 156.00, 38, 3, 0),
    (26, 'Bonnet', 'images_Article/bonnet_stetson.jpg', 'Stetson', 'Fabriqué avec des matériaux premium, ce couvre-chef allie durabilité, confort et un style unique.', 6.90, 16.50, 45, 3, 0),
    (27, 'Casquette', 'images_Article/casquette_sweetPants.jpg', 'Sweet-Pants', 'Parfaite pour compléter vos tenues décontractées ou sportives, cette casquette allie look tendance et qualité pour un style affirmé au quotidien.', 10.00, 28.50, 28, 3, 0),
    (28, 'Sandalettes', 'images_Article/sandalette_gabor.jpg', 'Gabor', 'Idéales pour les beaux jours, ces sandales féminines offrent une morphologie étudiée et des matières haut de gamme pour un look chic et décontracté, en ville comme en vacances.', 65.00, 189.90, 30, 3, 0),
    (29, 'Lunettes de soleil', 'images_Article/lunettes_level.jpg', 'Level', 'Parfaites pour compléter vos tenues streetwear ou vos outfits décontractés, ces lunettes offrent une protection UV optimale tout en restant légères et résistantes.', 20.00, 56.50, 40, 3, 0),
    (30, 'Echarpe', 'images_Article/echarpe_pimkie.JPG', 'Pimkie', 'Craquez pour cette écharpe Pimkie, un accessoire mode incontournable pour allier chaleur et élégance pendant la saison froide.', 8.99, 21.50, 15, 3, 0);
COMMIT;



-- --------------------------------------------------------

--
-- Structure de la table `avis`
--

DROP TABLE IF EXISTS `avis`;
CREATE TABLE IF NOT EXISTS `avis` (
    `id` int NOT NULL AUTO_INCREMENT,
    `id_article` int NOT NULL,
    `id_utilisateur` int NOT NULL,
    `note` int NOT NULL,
    `date` date NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_rating` (`id_article`,`id_utilisateur`),
    KEY `id_utilisateur` (`id_utilisateur`)
) ;

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
