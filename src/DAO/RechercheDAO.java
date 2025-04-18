package DAO;

import Modele.Article;
import DAO.ArticleDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RechercheDAO {

    public static List<Article> rechercherArticlesParNom(String termeRecherche) {
        List<Article> articlesTrouves = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
            String query = "SELECT * FROM article WHERE nom LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + termeRecherche + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Article a = new Article(
                        rs.getInt("id_article"),
                        rs.getString("nom"),
                        rs.getString("image"),
                        rs.getString("marque"),
                        rs.getString("description"),
                        rs.getFloat("prix"),
                        rs.getFloat("prix_vrac"),
                        rs.getInt("quantite"),
                        rs.getInt("quantite_vrac"),
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
