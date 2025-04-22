package DAO;

import Modele.Article;
import Modele.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour gérer les opérations CRUD sur les articles
 */

public class ArticleDAO {
    private Connection connexion; //Connexion à la base de données

    /**
     * Constructeur initialisant la connexion à la BDD
     * @param connection Object Connection déjà établi
     */

    public ArticleDAO(Connection connection) {
        this.connexion = connection;
    }

    /**
     * Ajoute un nouvel article dans la base de données
     * @param Article L'objet Article à ajouter
     * @return true si l'ajout a réussi sinon false
     */

    public boolean ajouterArticle(Article Article) {
        //Requete SQL paramétrée poru éciter les injections
        String sql = "INSERT INTO article (nom, image, marque, description, prix, prix_vrac, quantite_vrac, quantite, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            //remplissage des paramètres
            stmt.setString(1, Article.getNom());
            stmt.setString(2, Article.getImage());
            stmt.setString(3, Article.getMarque());
            stmt.setString(4, Article.getDescription());
            stmt.setFloat(5, Article.getPrix());
            stmt.setFloat(6, Article.getPrix_vrac());
            stmt.setInt(7, Article.getQuantite());
            stmt.setInt(8, Article.getQuantite_vrac());
            stmt.setInt(9, Article.getNote());

            //exécution de la requete
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; //retourne true si au moins une ligue a été insérée
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * récupère tous les articles de la base de données
     * @return Liste des articles trouvés
     */

    public static List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();

        try {
            //établissement de la connexion
            Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
            String query = "SELECT * FROM article";

            //Exécution de la requete
            Statement statement = connexion.createStatement();
            ResultSet rs = statement.executeQuery(query);

            //parcours des résultats
            while (rs.next()) {
                //Création d'un objet Article pour chaque ligne
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
                System.out.println(a); //Debug : affichage de l'article
            }

            connexion.close(); //Fermeture de la connexion
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

    public boolean mettreAJourArticle(Article article) {
        String sql = "UPDATE article SET nom=?, marque=?, description=? WHERE id_article=?";
        try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
            stmt.setString(1, article.getNom());
            stmt.setString(2, article.getMarque());
            stmt.setString(3, article.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}