package DAO;

import Modele.Avis;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisDAO {
    private Connection connection;

    public AvisDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean ajouterAvis(Avis avis) {
        String sql = "INSERT INTO avis (id_article, id_utilisateur, note, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, avis.getIdArticle());
            stmt.setInt(2, avis.getIdUtilisateur());
            stmt.setInt(3, avis.getNote());
            stmt.setDate(4, new java.sql.Date(avis.getDate().getTime()));
            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                mettreAJourNoteMoyenne(avis.getIdArticle()); // Mise à jour de la note moyenne
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void mettreAJourNoteMoyenne(int idArticle) {
        String sql = "UPDATE article SET note = (SELECT AVG(note) FROM avis WHERE id_article = ?) WHERE id_article = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idArticle);
            stmt.setInt(2, idArticle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Avis> getAvisParArticle(int idArticle) {
        List<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM avis WHERE id_article = ? ORDER BY date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idArticle);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                avisList.add(new Avis(
                        rs.getInt("id"),
                        rs.getInt("id_article"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("note"),
                        rs.getDate("date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avisList;
    }
}