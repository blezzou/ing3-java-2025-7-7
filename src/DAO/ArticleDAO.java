package DAO;

import Modele.Article;
import java.sql.*;

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
}
