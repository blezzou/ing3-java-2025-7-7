package Vue;

import javax.swing.*;
import java.awt.*;
import Modele.Utilisateur;
import DAO.UtilisateurDAO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VueProfil extends JFrame {
    private Utilisateur utilisateur;
    private JTextField nomField, prenomField, emailField;
    private JPasswordField motDePasseField;
    private boolean modeEdition = false;

    public VueProfil(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        setTitle("Profil Utilisateur");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // 1 -> Header (identique à VueAccueil mais avec bouton Accueil)
        JPanel headerPanel = new JPanel(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        // Boutons panier et accueil
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton panierButton = new JButton("Panier");
        JButton accueilButton = new JButton("Accueil");
        buttonsPanel.add(panierButton);
        buttonsPanel.add(accueilButton);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2 -> Contenu du profil (scrollable)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Section Infos Utilisateur
        JPanel infoPanel = createInfoPanel();
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        if (utilisateur.getAdmin() == 1) {
            JButton ajouterUnArticle = new JButton("Ajouter un article");
            // Tu peux ajouter ici un ActionListener si tu veux ouvrir une nouvelle fenêtre
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // petit espace visuel
            contentPanel.add(ajouterUnArticle);

            ajouterUnArticle.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new VueAjouterArticle();
                    dispose();
                }
            });
        }

        // Section Historique (A FAIRE)
        JPanel historiquePanel = createHistoriquePanel();
        contentPanel.add(historiquePanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Gestion des événements
        accueilButton.addActionListener(e -> {
            new VueAccueil(utilisateur);  // Passer l'utilisateur à VueAccueil
            dispose();
        });

        panierButton.addActionListener(e -> {
            new VuePanier(utilisateur);
            dispose();
        });

        searchButton.addActionListener(e -> {
            String texteRecherche = searchField.getText().trim();
            if (!texteRecherche.isEmpty()) {
                new VueRecherche(texteRecherche);
                dispose();
            }
        });



        add(mainPanel);
        setVisible(true);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Informations personnelles"));

        // Panel des informations (non éditable par défaut)
        JPanel infoDisplayPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoDisplayPanel.add(new JLabel("Nom:"));
        infoDisplayPanel.add(new JLabel(utilisateur.getNom()));
        infoDisplayPanel.add(new JLabel("Prénom:"));
        infoDisplayPanel.add(new JLabel(utilisateur.getPrenom()));
        infoDisplayPanel.add(new JLabel("Email:"));
        infoDisplayPanel.add(new JLabel(utilisateur.getEmail()));
        infoDisplayPanel.add(new JLabel("Mot de passe:"));
        infoDisplayPanel.add(new JLabel("********"));

        // Panel d'édition (caché par défaut)
        JPanel infoEditPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        nomField = new JTextField(utilisateur.getNom());
        prenomField = new JTextField(utilisateur.getPrenom());
        emailField = new JTextField(utilisateur.getEmail());
        motDePasseField = new JPasswordField();

        infoEditPanel.add(new JLabel("Nom:"));
        infoEditPanel.add(nomField);
        infoEditPanel.add(new JLabel("Prénom:"));
        infoEditPanel.add(prenomField);
        infoEditPanel.add(new JLabel("Email:"));
        infoEditPanel.add(emailField);
        infoEditPanel.add(new JLabel("Nouveau mot de passe:"));
        infoEditPanel.add(motDePasseField);
        infoEditPanel.setVisible(false);

        // Bouton Modifier/Enregistrer
        JButton editButton = new JButton("Modifier les infos");
        editButton.addActionListener(e -> toggleEditMode(infoDisplayPanel, infoEditPanel, editButton));

        panel.add(infoDisplayPanel, BorderLayout.CENTER);
        panel.add(infoEditPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);  // Correction: "editButtton" -> "editButton"
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void toggleEditMode(JPanel displayPanel, JPanel editPanel, JButton button) {
        modeEdition = !modeEdition;

        if (modeEdition) {
            displayPanel.setVisible(false);
            editPanel.setVisible(true);
            button.setText("Enregistrer");
        } else {
            // Sauvegarder les modifications
            sauvegarderModifications();
            displayPanel.setVisible(true);
            editPanel.setVisible(false);
            button.setText("Modifier les infos");

            // Mettre à jour l'affichage
            ((JLabel)displayPanel.getComponent(1)).setText(nomField.getText());
            ((JLabel)displayPanel.getComponent(3)).setText(prenomField.getText());
            ((JLabel)displayPanel.getComponent(5)).setText(emailField.getText());
        }
    }

    private void sauvegarderModifications() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);

            // Mettre à jour l'objet utilisateur
            utilisateur.setNom(nomField.getText());
            utilisateur.setPrenom(prenomField.getText());
            utilisateur.setEmail(emailField.getText());

            // Ne mettre à jour le mot de passe que s'il a été modifié
            String nouveauMdp = new String(motDePasseField.getPassword());
            if (!nouveauMdp.isEmpty()) {
                utilisateur.setMotDePasse(nouveauMdp);
            }

            // Mettre à jour en BDD
            boolean success = utilisateurDAO.mettreAJourUtilisateur(utilisateur);

            if (success) {
                JOptionPane.showMessageDialog(this, "Modifications enregistrées avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createHistoriquePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Historique des commandes"));

        // Placeholder pour l'historique (à implémenter)
        JLabel placeholder = new JLabel("Historique des commandes sera affiché ici", SwingConstants.CENTER);
        placeholder.setForeground(Color.GRAY);
        panel.add(placeholder, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        // Pour tester
        Utilisateur testUser = new Utilisateur(1, 0, "Doe", "John", "john@example.com", "mdp123", 0);
        SwingUtilities.invokeLater(() -> new VueProfil(testUser));
    }
}


