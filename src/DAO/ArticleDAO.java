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
        String sql = "INSERT INTO article (nom, image, marque, description, prix, prix_vrac, quantite_vrac, quantite, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, Article.getNom());
            stmt.setString(2, Article.getImage());
            stmt.setString(3, Article.getMarque());
            stmt.setString(4, Article.getDescription());
            stmt.setFloat(5, Article.getPrix());
            stmt.setFloat(6, Article.getPrix_vrac());
            stmt.setInt(7, Article.getQuantite());
            stmt.setInt(8, Article.getQuantite_vrac());
            stmt.setInt(9, Article.getNote());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }}

    public static List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();

        try {
            Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
            String query = "SELECT * FROM article";
            Statement statement = connexion.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id_article");
                String nom = rs.getString("nom");
                String image = rs.getString("image");
                String marque = rs.getString("marque");
                String description = rs.getString("description");
                float prix = rs.getFloat("prix");
                float prix_vrac = rs.getFloat("prix_vrac");
                int quantite = rs.getInt("quantite");
                int quantite_vrac = rs.getInt("quantite_vrac");
                int note = rs.getInt("note");

                Article a = new Article(id, nom, image, marque, description, prix, prix_vrac, quantite, quantite_vrac, note);
                articles.add(a);
                System.out.println(a);
            }

            connexion.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des articles : " + e.getMessage());
            e.printStackTrace();
        }

        return articles;
    }

    public static boolean supprimerArticle(int id) {
        String sql = "DELETE FROM article WHERE id_article = ?";
        try (Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
             PreparedStatement pstmt = connexion.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Article supprimé avec succès.");
            } else {
                System.out.println("Aucun article trouvé avec cet ID.");
            }
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'article : " + e.getMessage());
            return false;
        }
    }
}