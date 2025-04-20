package DAO;

import DAO.UtilisateurDAO;
import Modele.Utilisateur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChercherUtilisateur {
    public static Utilisateur ChercherUtilisateur(String email, String motDePasse) {
        Utilisateur utilisateur = null;

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");

            String sql = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, motDePasse);

            ResultSet resultSet = statement.executeQuery();

            boolean success = resultSet.next();

            if (success) {
                utilisateur = new Utilisateur(
                        resultSet.getInt("id_utilisateur"),
                        resultSet.getInt("admin"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getString("email"),
                        resultSet.getString("mot_de_passe"),
                        resultSet.getInt("historique")
                );

                System.out.println("Connexion réussie pour : " + utilisateur.getEmail());
                System.out.println("admin" + utilisateur.getAdmin());
            }

            //System.out.println("teeeeest" + utilisateur);

            if (success) {
                System.out.println("Bienvenue");
            } else {
                System.out.println("Échec");
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion à la BDD : " + e.getMessage());
        }

        return utilisateur;
    }
}
