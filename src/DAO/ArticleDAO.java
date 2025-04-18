package DAO;

import Modele.Article;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ArticleDAO {
    private Connection connection;

    public ArticleDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean ajouterArticle(Article Article) {
        String sql = "INSERT INTO article (nom, marque, description, prix, prix_vrac, quantite_vrac, quantite, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, Article.getNom());
            stmt.setString(2, Article.getMarque());
            stmt.setString(3, Article.getDescription());
            stmt.setFloat(4, Article.getPrix());
            stmt.setFloat(5, Article.getPrix_vrac());
            stmt.setInt(6, Article.getQuantite());
            stmt.setInt(7, Article.getQuantite_vrac());
            stmt.setInt(8, Article.getNote());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }}

    public static List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
            String query = "SELECT nom, marque, prix, description FROM article";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String marque = rs.getString("marque");
                String description = rs.getString("description");
                float prix = rs.getFloat("prix");
                float prix_vrac = rs.getFloat("prix_vrac");
                int quantite = rs.getInt("quantite");
                int quantite_vrac = rs.getInt("quantite_vrac");
                int note = rs.getInt("note");

                Article a = new Article(id, nom, marque, description, prix, prix_vrac, quantite, quantite_vrac, note);
                articles.add(a);
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des articles : " + e.getMessage());
        }

        return articles;
    }



}
