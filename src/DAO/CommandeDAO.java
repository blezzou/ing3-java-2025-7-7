package DAO;

import Modele.Commande;
import Modele.ArticlePanier;
import Modele.Article;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {
    private Connection connection;

    public CommandeDAO(Connection connection) {
        this.connection = connection;
    }

    public int creerCommande(int idUtilisateur, double montantTotal) throws SQLException {
        // Utilisation de CURRENT_TIMESTAMP directement dans la requête SQL
        String sql = "INSERT INTO commande (id_utilisateur, montant_total, date_commande) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idUtilisateur);
            stmt.setDouble(2, montantTotal);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public void ajouterArticlesCommande(int idCommande, List<ArticlePanier> articles) throws SQLException {
        String sql = "INSERT INTO commande_article (id_commande, id_article, quantite, prix_unitaire, prix_vrac) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (ArticlePanier articlePanier : articles) {
                stmt.setInt(1, idCommande);
                stmt.setInt(2, articlePanier.getArticle().getId());
                stmt.setInt(3, articlePanier.getQuantite());
                stmt.setDouble(4, articlePanier.getArticle().getPrix());
                stmt.setDouble(5, articlePanier.getArticle().getPrix_vrac());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public List<Commande> getCommandesParUtilisateur(int idUtilisateur) throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande WHERE id_utilisateur = ? ORDER BY date_commande DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUtilisateur);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Commande cmd = new Commande(
                        rs.getInt("id_commande"),
                        rs.getInt("id_utilisateur"),
                        rs.getTimestamp("date_commande"),
                        rs.getDouble("montant_total")
                );
                commandes.add(cmd);
            }
        }
        return commandes;
    }

    public List<ArticlePanier> getArticlesCommande(int idCommande) throws SQLException {
        List<ArticlePanier> articles = new ArrayList<>();
        String sql = "SELECT a.*, ca.quantite, ca.prix_unitaire, ca.prix_vrac " +
                "FROM commande_article ca " +
                "JOIN article a ON ca.id_article = a.id_article " +
                "WHERE ca.id_commande = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCommande);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id_article"),
                        rs.getString("nom"),
                        rs.getString("image"),
                        rs.getString("marque"),
                        rs.getString("description"),
                        rs.getFloat("prix_unitaire"),
                        rs.getFloat("prix_vrac"),
                        rs.getInt("quantite_vrac"),
                        rs.getInt("quantite"),
                        rs.getFloat("note")
                );
                articles.add(new ArticlePanier(article, rs.getInt("quantite")));
            }
        }
        return articles;
    }
}