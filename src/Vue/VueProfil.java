package Vue;

import javax.swing.*;
import java.awt.*;

import Modele.Article;
import Modele.Utilisateur;
import DAO.UtilisateurDAO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class VueProfil extends JFrame {

    private Map<Integer, Article> panierArticles = new HashMap<>();
    private Map<Integer, Integer> panierQuantites = new HashMap<>();

    //L'utilisateur actuellement connecté
    private Utilisateur utilisateur;

    //Champs pour l'édition des infos utilisateur
    private JTextField nomField, prenomField, emailField;
    private JPasswordField motDePasseField;

    //variable pour savoir si on est en mode édition ou pas
    private boolean modeEdition = false;

    //Composants pour l'affichage des infos
    private JLabel nomLabel, prenomLabel, emailLabel;

    public VueProfil(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        // Debug: vérifier que l'utilisateur est bien reçu
        System.out.println("Utilisateur dans VueProfil:");
        System.out.println("Nom: " + utilisateur.getNom());
        System.out.println("Prénom: " + utilisateur.getPrenom());
        System.out.println("Email: " + utilisateur.getEmail());

        setTitle("Profil Utilisateur");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //centre la fenêtre sur l'écran

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ---- HEADER ---- //
        // Comme dans les autres vues, avec recherche et boutons en haut
        JPanel headerPanel = new JPanel(new BorderLayout());

        // Barre de recherche avec champ texte + bouton
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        // Les boutons en haut à droite : Panier, Accueil et Se Déconnecter
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton panierButton = new JButton("Panier");
        JButton accueilButton = new JButton("Accueil");
        JButton logOutButton = new JButton("Se déconnecter");

        buttonsPanel.add(panierButton);
        buttonsPanel.add(accueilButton);
        buttonsPanel.add(logOutButton);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ---- CONTENU ---- //
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Partie infos personnelles (avec bouton modifier)
        JPanel infoPanel = createInfoPanel();
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20))); //petit espace

        //si l'utilisateur est admin, on ajoute le bouton pour gérer les articles
        if (utilisateur.getAdmin() == 1) {
            JButton ajouterUnArticle = new JButton("Ajouter un article");
            // Tu peux ajouter ici un ActionListener si tu veux ouvrir une nouvelle fenêtre
            ajouterUnArticle.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new VueAjouterArticle(); //ouverture de la vue d'ajout
                    dispose(); //on ferme celle-ci
                }
            });
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10))); // petit espace visuel
            contentPanel.add(ajouterUnArticle);
        }

        // Historique des commandes (pas encore codé mais on prépare le terrain)
        JPanel historiquePanel = createHistoriquePanel();
        contentPanel.add(historiquePanel);

        //scroll si le contenu est trop grand
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ---- Évènements des boutons du header ---- //
        accueilButton.addActionListener(e -> {
            new VueAccueil(utilisateur);  // Passer l'utilisateur à VueAccueil
            dispose();
        });

        panierButton.addActionListener(e -> {
            new VuePanier(utilisateur);
            dispose();
        });

        logOutButton.addActionListener(e -> {
            new VueConnexion();
            dispose();
        });

        searchButton.addActionListener(e -> {
            String texteRecherche = searchField.getText().trim();
            if (!texteRecherche.isEmpty()) {
                new VueRecherche(texteRecherche, utilisateur);
                dispose();
            }
        });


        add(mainPanel);
        setVisible(true);
    }

    private JPanel createInfoPanel() {
        // Création du bloc contenant les infos de l'utilisateur
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Informations personnelles"));

        //Panel principal qui contiendra les deux modes
        JPanel contentPanel = new JPanel(new CardLayout());

        // Panel d'affichage (toujours visible)
        JPanel infoDisplayPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        infoDisplayPanel.add(new JLabel("Nom:"));
        nomLabel = new JLabel(utilisateur.getNom());
        infoDisplayPanel.add(nomLabel);

        infoDisplayPanel.add(new JLabel("Prénom:"));
        prenomLabel = new JLabel(utilisateur.getPrenom());
        infoDisplayPanel.add(prenomLabel);

        infoDisplayPanel.add(new JLabel("Email:"));
        emailLabel = new JLabel(utilisateur.getEmail());
        infoDisplayPanel.add(emailLabel);

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

        //Ajout des deux panels avec CardLayout
        contentPanel.add(infoDisplayPanel, "DISPLAY");
        contentPanel.add(infoEditPanel, "EDIT");
        panel.add(contentPanel, BorderLayout.CENTER);

        // Bouton de modification
        JButton editButton = new JButton("Modifier les infos");
        editButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            if (modeEdition) {
                // Sauvegarde des modifications
                utilisateur.setNom(nomField.getText());
                utilisateur.setPrenom(prenomField.getText());
                utilisateur.setEmail(emailField.getText());

                // Mise à jour de l'affichage
                nomLabel.setText(utilisateur.getNom());
                prenomLabel.setText(utilisateur.getPrenom());
                emailLabel.setText(utilisateur.getEmail());

                // Sauvegarde en BDD
                sauvegarderModifications();

                cl.show(contentPanel, "DISPLAY");
                editButton.setText("Modifier les infos");
            } else {
                cl.show(contentPanel, "EDIT");
                editButton.setText("Enregistrer");
            }

            // Bascule entre les modes
            modeEdition = !modeEdition;
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void sauvegarderModifications() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);

            // Mettre à jour l'objet utilisateur
            utilisateur.setNom(nomField.getText());
            utilisateur.setPrenom(prenomField.getText());
            utilisateur.setEmail(emailField.getText());

            String nouveauMdp = new String(motDePasseField.getPassword());
            if (!nouveauMdp.isEmpty()) {
                utilisateur.setMotDePasse(nouveauMdp);
            }

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

        JLabel placeholder = new JLabel("Historique des commandes sera affiché ici", SwingConstants.CENTER);
        placeholder.setForeground(Color.GRAY);
        panel.add(placeholder, BorderLayout.CENTER);

        return panel;
    }
}
