package DAO;

import Modele.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * classe DAO pour les opérations de recherche d'articles
 */
public class RechercheDAO {

    /**
     * Recherche des articles par nom (recherche partielle)
     * @param termeRecherche Chaine à rechercher dans les noms d'articles
     * @return Liste des articles correspondants
     */

    public static List<Article> rechercherArticles(String termeRecherche) {
        List<Article> articlesTrouves = new ArrayList<>();

        try {
            //Connexion à la BDD
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
            //Requete avec LIKE pour recherche partielle
            String query = "SELECT * FROM article WHERE " +
                    "nom LIKE ? OR " +
                    "marque LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + termeRecherche + "%"); //% pour recherche partielle, nom
            statement.setString(2, "%" + termeRecherche + "%"); // marque

            //exécution de la requete
            ResultSet rs = statement.executeQuery();

            //construction des résultats
            while (rs.next()) {
                Article a = new Article(
                        rs.getInt("id_article"),
                        rs.getString("nom"),
                        rs.getString("image"),
                        rs.getString("marque"),
                        rs.getString("description"),
                        rs.getFloat("prix"),
                        rs.getFloat("prix_vrac"),
                        rs.getInt("quantite_vrac"),
                        rs.getInt("quantite"),
                        rs.getInt("note")
                );
                articlesTrouves.add(a);
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche : " + e.getMessage());
        }

        return articlesTrouves;
    }
}
