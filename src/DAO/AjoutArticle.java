package DAO;

import DAO.ArticleDAO;
import Modele.Article;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class AjoutArticle {
    public static void AjouterArticle(String nom, String marque, int prix, int prix_vrac, int quantite_vrac, int quantite, int note) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");

            Article article = new Article(
                    0,
                    nom,
                    marque,
                    prix,
                    prix_vrac,
                    quantite_vrac,
                    quantite,
                    note
            );

            ArticleDAO articleDAO = new ArticleDAO(connection);

            boolean success = articleDAO.ajouterArticle(article);

            if (success) {
                System.out.println("Article ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout de l'article.");
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la BDD : " + e.getMessage());
        }
    }
}
