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
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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