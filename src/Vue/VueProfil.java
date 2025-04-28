package Vue;

import javax.swing.*;
import java.awt.*;
import Modele.Utilisateur;
import DAO.UtilisateurDAO;
import DAO.CommandeDAO;
import Modele.Commande;
import Modele.Article;
import Modele.ArticlePanier;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class VueProfil extends JFrame {

    private Utilisateur utilisateur;
    private JTextField nomField, prenomField, emailField;
    private JPasswordField motDePasseField;
    private boolean modeEdition = false;
    private JLabel nomLabel, prenomLabel, emailLabel;

    public VueProfil(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        // Debug: vérifier que l'utilisateur est bien reçu
        System.out.println("Utilisateur dans VueProfil:");
        System.out.println("Nom: " + utilisateur.getNom());
        System.out.println("Prénom: " + utilisateur.getPrenom());
        System.out.println("Email: " + utilisateur.getEmail());

        // Configuration de la fenêtre
        setTitle("Profil Utilisateur");
        setSize(1000, 800);
        setMinimumSize(new Dimension(900, 700));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Style global
        UIManager.put("Panel.background", new Color(240, 240, 240));
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        setBackground(new Color(240, 240, 240));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));

        /* ------------------------- */
        /* EN-TÊTE */
        /* ------------------------- */
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);

        JTextField searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        JButton searchButton = createStyledButton("Rechercher");
        searchButton.setBackground(new Color(46, 204, 113)); // Vert

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        // Boutons de navigation
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        JButton panierButton = createStyledButton("Panier");
        JButton accueilButton = createStyledButton("Accueil");
        JButton logOutButton = createStyledButton("Se déconnecter");
        logOutButton.setBackground(new Color(231, 76, 60)); // Rouge

        buttonsPanel.add(panierButton);
        buttonsPanel.add(accueilButton);
        buttonsPanel.add(logOutButton);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        /* ------------------------- */
        /* CONTENU PRINCIPAL */
        /* ------------------------- */
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Partie infos personnelles
        JPanel infoPanel = createInfoPanel();
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bouton admin si nécessaire
        if (utilisateur.getAdmin() == 1) {
            JButton ajouterUnArticle = createStyledButton("Ajouter un article");
            ajouterUnArticle.setBackground(new Color(155, 89, 182)); // Violet
            ajouterUnArticle.addActionListener(e -> {
                new VueAjouterArticle();
                dispose();
            });

            JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            adminPanel.setBackground(Color.WHITE);
            adminPanel.add(ajouterUnArticle);
            contentPanel.add(adminPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Historique des commandes
        JPanel historiquePanel = createHistoriquePanel();
        contentPanel.add(historiquePanel);

        // ScrollPane pour le contenu
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        /* ------------------------- */
        /* GESTION DES ÉVÈNEMENTS */
        /* ------------------------- */
        accueilButton.addActionListener(e -> {
            new VueAccueil(utilisateur);
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
        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Informations personnelles",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(70, 130, 180))
        );
        panel.setBackground(Color.WHITE);

        // Panel avec CardLayout pour basculer entre affichage/édition
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(Color.WHITE);

        /* ------------------------- */
        /* PANEL D'AFFICHAGE */
        /* ------------------------- */
        JPanel infoDisplayPanel = new JPanel(new GridLayout(4, 2, 15, 10));
        infoDisplayPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        infoDisplayPanel.setBackground(Color.WHITE);

        JLabel nomTitre = createInfoLabel("Nom:");
        nomLabel = createInfoValue(utilisateur.getNom());
        JLabel prenomTitre = createInfoLabel("Prénom:");
        prenomLabel = createInfoValue(utilisateur.getPrenom());
        JLabel emailTitre = createInfoLabel("Email:");
        emailLabel = createInfoValue(utilisateur.getEmail());
        JLabel mdpTitre = createInfoLabel("Mot de passe:");
        JLabel mdpValue = createInfoValue("********");

        infoDisplayPanel.add(nomTitre);
        infoDisplayPanel.add(nomLabel);
        infoDisplayPanel.add(prenomTitre);
        infoDisplayPanel.add(prenomLabel);
        infoDisplayPanel.add(emailTitre);
        infoDisplayPanel.add(emailLabel);
        infoDisplayPanel.add(mdpTitre);
        infoDisplayPanel.add(mdpValue);

        /* ------------------------- */
        /* PANEL D'ÉDITION */
        /* ------------------------- */
        JPanel infoEditPanel = new JPanel(new GridLayout(4, 2, 15, 10));
        infoEditPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        infoEditPanel.setBackground(Color.WHITE);

        nomField = createStyledTextField(utilisateur.getNom());
        prenomField = createStyledTextField(utilisateur.getPrenom());
        emailField = createStyledTextField(utilisateur.getEmail());
        motDePasseField = new JPasswordField();
        motDePasseField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        motDePasseField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        infoEditPanel.add(createInfoLabel("Nom:"));
        infoEditPanel.add(nomField);
        infoEditPanel.add(createInfoLabel("Prénom:"));
        infoEditPanel.add(prenomField);
        infoEditPanel.add(createInfoLabel("Email:"));
        infoEditPanel.add(emailField);
        infoEditPanel.add(createInfoLabel("Nouveau mot de passe:"));
        infoEditPanel.add(motDePasseField);

        // Ajout des deux panels
        contentPanel.add(infoDisplayPanel, "DISPLAY");
        contentPanel.add(infoEditPanel, "EDIT");
        panel.add(contentPanel, BorderLayout.CENTER);

        /* ------------------------- */
        /* BOUTON D'ÉDITION */
        /* ------------------------- */
        JButton editButton = createStyledButton("Modifier les infos");
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
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(editButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHistoriquePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Historique des commandes",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(70, 130, 180))
        );
        panel.setBackground(Color.WHITE);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        try (Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            CommandeDAO commandeDAO = new CommandeDAO(connexion);
            List<Commande> commandes = commandeDAO.getCommandesParUtilisateur(utilisateur.getIdUtilisateur());

            if (commandes.isEmpty()) {
                JLabel emptyLabel = new JLabel("Aucune commande passée pour le moment.", SwingConstants.CENTER);
                emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                emptyLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
                contentPanel.add(emptyLabel);
            } else {
                for (Commande commande : commandes) {
                    JPanel cmdPanel = new JPanel(new BorderLayout());
                    cmdPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(220, 220, 220)),
                            BorderFactory.createEmptyBorder(15, 15, 15, 15)
                    ));
                    cmdPanel.setBackground(Color.WHITE);
                    cmdPanel.setMaximumSize(new Dimension(800, 150));

                    // Récupérer les articles de la commande
                    List<ArticlePanier> articles = commandeDAO.getArticlesCommande(commande.getIdCommande());
                    commande.setArticles(articles);

                    // Créer le récapitulatif
                    StringBuilder recap = new StringBuilder("<html>");
                    recap.append("<div style='font-size:14px; margin-bottom:8px;'><b>Commande #").append(commande.getIdCommande())
                            .append("</b> - ").append(commande.getFormattedDate())
                            .append("</div><div style='color:#2ecc71; font-weight:bold; margin-bottom:10px;'>")
                            .append("Montant total: ").append(String.format("%.2f", commande.getMontantTotal())).append("€</div>");

                    for (ArticlePanier articlePanier : articles) {
                        Article article = articlePanier.getArticle();
                        int quantiteVrac = articlePanier.getQuantite() / 3;
                        int quantiteUnitaire = articlePanier.getQuantite() % 3;
                        double prixTotal = quantiteVrac * article.getPrix_vrac() +
                                quantiteUnitaire * article.getPrix();

                        recap.append("<div style='margin-bottom:3px;'>• ").append(article.getNom())
                             .append(" <span style='color:#777;'>(x").append(articlePanier.getQuantite()).append(")</span> : ")
                             .append(String.format("%.2f", prixTotal)).append("€</div>");
                    }
                    recap.append("</html>");

                    JLabel recapLabel = new JLabel(recap.toString());
                    contentPanel.add(cmdPanel);
                    contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                    cmdPanel.add(recapLabel, BorderLayout.CENTER);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Erreur lors du chargement de l'historique", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            contentPanel.add(errorLabel);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

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
                JOptionPane.showMessageDialog(this,
                        "Modifications enregistrées avec succès !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la mise à jour",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur de connexion à la base de données",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ------------------------- */
    /* METHODES UTILITAIRES */
    /* ------------------------- */

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return button;
    }

    private JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private JLabel createInfoValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }
}