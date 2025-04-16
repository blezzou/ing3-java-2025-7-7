package DAO;

import DAO.UtilisateurDAO;
import Modele.Utilisateur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class AjoutUtilisateur {
    public static void AjouterUtilisateur(int admin, String nom, String prenom, String email, String motDePasse, int historique) {
        /*
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();
        System.out.print("Admin (1 = oui, 0 = non) : ");
        int admin = Integer.parseInt(scanner.nextLine());
        System.out.print("Historique : ");
        int historique = Integer.parseInt(scanner.nextLine());
        */
        // Connexion a la BDD

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");

            Utilisateur utilisateur = new Utilisateur(
                    0,
                    admin,
                    nom,
                    prenom,
                    email,
                    motDePasse,
                    historique
            );

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
}
