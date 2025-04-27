package DAO;

import Modele.Panier;
import java.sql.*;
import java.util.Optional;


public class PanierDAO {

    // Vérifie si l'utilisateur a déjà un panier actif
    public static int getOrCreatePanierId(int utilisateurId) {

        try {Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
            PreparedStatement check = connexion.prepareStatement(
                    "SELECT id_panier FROM panier WHERE id_utilisateur = ? AND validé = FALSE");
            check.setInt(1, utilisateurId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_panier"); // ✅ Bon ID récupéré
            }

            // Sinon on le crée
            PreparedStatement insert = connexion.prepareStatement(
                    "INSERT INTO panier (id_utilisateur, prix, validé, date) VALUES (?, ?, FALSE, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            insert.setInt(1, utilisateurId);
            insert.setInt(2, 0);
            insert.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            insert.executeUpdate();


            ResultSet keys = insert.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Ajoute un article dans le panier
    public static void ajouterArticleDansPanier(int panierId, int articleId, int quantite) {
        try {Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
            connexion.setAutoCommit(false);
            // Vérifie si l'article est déjà dans le panier
            PreparedStatement check = connexion.prepareStatement(
                    "SELECT quantite FROM panier_article WHERE id_panier = ? AND id_article = ?");
            check.setInt(1, panierId);
            check.setInt(2, articleId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                // Mise à jour de la quantité
                int nouvelleQuantite = rs.getInt("quantite") + quantite;
                PreparedStatement update = connexion.prepareStatement(
                        "UPDATE panier_article SET quantite = ? WHERE id_panier = ? AND id_article = ?");
                update.setInt(1, nouvelleQuantite);
                update.setInt(2, panierId);
                update.setInt(3, articleId);
                update.executeUpdate();
            } else {
                // Insertion
                PreparedStatement insert = connexion.prepareStatement(
                        "INSERT INTO panier_article(id_panier, id_article, quantite) VALUES (?, ?, ?)");
                insert.setInt(1, panierId);
                insert.setInt(2, articleId);
                insert.setInt(3, quantite);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
