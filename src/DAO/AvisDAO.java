package DAO;

import Modele.Avis;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour gérer les opérations CRUD sur les avis
 * Communique avec la table 'avis' dans la base de données
 */

public class AvisDAO {
    private Connection connection; //Connexion à la base de données

    /**
     * Constructeur initialisant la connexion à la BDD
     * @param connection Objet Connection déjà établi
     */

    public AvisDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Ajoute un nouvel avis dans la base de données
     * @param avis Objet Avis à ajouter
     * @return true si l'ajout a réussi, false sinon
     */

    public boolean ajouterAvis(Avis avis) {
        // Requête SQL paramétrée pour éviter les injections SQL
        String sql = "INSERT INTO avis (id_article, id_utilisateur, note, date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            //Remplissage des paramètres
            stmt.setInt(1, avis.getIdArticle());
            stmt.setInt(2, avis.getIdUtilisateur());
            stmt.setInt(3, avis.getNote());
            stmt.setDate(4, new java.sql.Date(avis.getDate().getTime()));

            // Exécution de la requête
            boolean success = stmt.executeUpdate() > 0;

            // Si l'ajout a réussi, on met à jour la note moyenne de l'article
            if (success) {
                mettreAJourNoteMoyenne(avis.getIdArticle()); // Mise à jour de la note moyenne
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour la note moyenne d'un article dans la table 'article'
     * @param idArticle ID de l'article à mettre à jour
     */

    public void mettreAJourNoteMoyenne(int idArticle) {
        // Requête qui calcule la moyenne des notes et met à jour l'article
        String sql = "UPDATE article SET note = (SELECT AVG(note) FROM avis WHERE id_article = ?) WHERE id_article = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idArticle);
            stmt.setInt(2, idArticle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère tous les avis pour un article donné, triés par date (du plus récent au plus ancien
     * @param idArticle idArticle ID de l'article
     * @return Liste des avis correspondants
     */

    public List<Avis> getAvisParArticle(int idArticle) {
        List<Avis> avisList = new ArrayList<>();

        //Requête SQL pour récupérer les avis triés par date décroissante
        String sql = "SELECT * FROM avis WHERE id_article = ? ORDER BY date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idArticle);
            ResultSet rs = stmt.executeQuery();

            // Parcours des résultats et création des objets Avis
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