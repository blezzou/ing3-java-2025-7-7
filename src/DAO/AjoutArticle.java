package DAO;


import Modele.Article;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utiliser pour ajouter des articles à la base de données
 */
public class AjoutArticle {
    //ajoute un nouvel article dans la base de données
    public static void AjouterArticle(String nom, String image,String marque, String description, int prix, int prix_vrac, int quantite_vrac, int quantite, int note) {

        try {
            //établissement de la connexion à la BDD
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");

            //création de l'objet article
            Article article = new Article(
                    0,
                    nom,
                    image,
                    marque,
                    description,
                    prix,
                    prix_vrac,
                    quantite_vrac,
                    quantite,
                    note
            );

            //utilisation du DAO pour l'insertion
            ArticleDAO articleDAO = new ArticleDAO(connection);

            boolean success = articleDAO.ajouterArticle(article);

            //Feedback utilisateur
            if (success) {
                System.out.println("Article ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout de l'article.");
            }

            //fermeture de la connexion
            connection.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la BDD : " + e.getMessage());
        }
    }
}
