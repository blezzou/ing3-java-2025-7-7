package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import DAO.AjoutUtilisateur;

/**
 * Vue pour la création de nouveaux comptes utilisateurs
 * Gère l'inscription des nouveaux clients avec validation des données
 */
public class VueCreationCompte extends JFrame {
    // Composants graphiques
    private JTextField nomField, prenomField, emailField, motDePasseField;

    /**
     * Constructeur initialisant l'interface de création de compte
     */
    public VueCreationCompte() {
        // Configuration de la fenêtre principale
        setTitle("Créer un compte");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme uniquement cette fenêtre sans quitter l'application
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran

        // Création du panneau principal avec une grille 5x2
        JPanel panel = new JPanel(new GridLayout(5, 2));

        // Ajout du champ "Nom"
        panel.add(new JLabel("Nom :"));
        nomField = new JTextField();
        panel.add(nomField);

        // Ajout du champ "Prénom"
        panel.add(new JLabel("Prénom :"));
        prenomField = new JTextField();
        panel.add(prenomField);

        // Ajout du champ "Email"
        panel.add(new JLabel("Email :"));
        emailField = new JTextField();
        panel.add(emailField);

        // Ajout du champ "Mot de passe"
        panel.add(new JLabel("Mot de passe :"));
        motDePasseField = new JTextField();
        panel.add(motDePasseField);

        // Ajout du bouton de création de compte
        JButton creerButton = new JButton("Créer le compte");
        panel.add(creerButton);

        // Si le compte existe
        JButton compteExisteButton = new JButton("Vous avez un compte ? Connectez-vous !");
        compteExisteButton.addActionListener(e -> {
            new VueConnexion();
            dispose();
        });
        panel.add(compteExisteButton);

        // Ajout du panneau à la fenêtre
        add(panel);

        // Définition de l'action du bouton "Créer le compte"
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupération des données saisies
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String email = emailField.getText();
                String motDePasse = new String(motDePasseField.getText());

                // Vérifie si un champ est vide
                if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
                    JOptionPane.showMessageDialog(VueCreationCompte.this,
                            "Veuillez remplir tous les champs.",
                            "Champs manquants",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Vérifie si l'email est déjà utilisé
                if (AjoutUtilisateur.emailExiste(email)) {
                    JOptionPane.showMessageDialog(VueCreationCompte.this, "Cet email est déjà utilisé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Ajout de l'utilisateur via la couche DAO
                AjoutUtilisateur.AjouterUtilisateur(0, nom, prenom, email, motDePasse, 0);

                // Ouverture de la fenêtre de connexion
                new VueConnexion();
            }
        });

        // Affiche la fenêtre
        setVisible(true);
    }
}
