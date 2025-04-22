package DAO;

import Modele.Utilisateur;
import java.sql.*;

public class UtilisateurDAO {
    private Connection connexion;

    public UtilisateurDAO(Connection connection) {
        this.connexion = connection;
    }

    public boolean ajouterUtilisateur(Utilisateur Utilisateur) {
        String sql = "INSERT INTO utilisateur (admin, nom, prenom, email, mot_de_passe, historique) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setInt(1, Utilisateur.getAdmin());
            stmt.setString(2, Utilisateur.getNom());
            stmt.setString(3, Utilisateur.getPrenom());
            stmt.setString(4, Utilisateur.getEmail());
            stmt.setString(5, Utilisateur.getMotDePasse());
            stmt.setInt(6, Utilisateur.getHistorique());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Utilisateur trouverUtilisateur(String email) {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getInt("admin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mot_de_passe"),
                        rs.getInt("historique")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean mettreAJourUtilisateur(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateur SET nom=?, prenom=?, email=?, mot_de_passe=? WHERE id_utilisateur=?";
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setInt(5, utilisateur.getIdUtilisateur());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
