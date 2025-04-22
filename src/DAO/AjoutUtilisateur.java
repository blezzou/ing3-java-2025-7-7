package DAO;

import Modele.Utilisateur;
import java.sql.*;

/**
 * Classe utilitaire pour gérrer l'ajout et la vérification des utilisateurs
 */


public class AjoutUtilisateur {
    //ajoute un nouvel utilisateur dans la base de données
    public static void AjouterUtilisateur(int admin, String nom, String prenom, String email, String motDePasse, int historique) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");

            //creation de l'objet utilisateur
            Utilisateur utilisateur = new Utilisateur(
                    0, //ID auto-incrémenté
                    admin,
                    nom,
                    prenom,
                    email,
                    motDePasse,
                    historique
            );

            // utilisation de UtilisateurDAO
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);

            boolean success = utilisateurDAO.ajouterUtilisateur(utilisateur);

            if (success) {
                System.out.println("Utilisateur ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout de l'utilisateur.");
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la BDD : " + e.getMessage());
        }
    }

    /**
     * Vérifie si un email existe déjà
     * @param email Email à vérifier
     * @return true si l'email existe déjà
     */


    public static boolean emailExiste(String email) {
        try ( Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM utilisateur WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
