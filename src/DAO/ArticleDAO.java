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
        String sql = "INSERT INTO article (nom, marque, prix, prix_vrac, quantite_vrac, quantite, note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, Article.getNom());
            stmt.setString(2, Article.getMarque());
            stmt.setFloat(3, Article.getPrix());
            stmt.setFloat(4, Article.getPrix_vrac());
            stmt.setInt(5, Article.getQuantite());
            stmt.setInt(6, Article.getQuantite_vrac());
            stmt.setInt(7, Article.getNote());
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
                String nom = rs.getString("nom");
                String marque = rs.getString("marque");
                double prix = rs.getDouble("prix");
                String description = rs.getString("description");

                Article a = new Article(nom, marque, prix, description);
                articles.add(a);
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des articles : " + e.getMessage());
        }

        return articles;
    }



}
