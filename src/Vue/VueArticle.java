package Vue;

import DAO.PanierDAO;
import DAO.UtilisateurDAO;
import DAO.ArticleDAO;
import DAO.AvisDAO;
import Modele.Article;
import Modele.Utilisateur;
import Modele.Avis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Vue pour afficher et modifier les détails d'un article
 * Respecte le pattern MVC en étant uniquement responsable de l'affichage et de la gestion des interactions utilisateur
 */
public class VueArticle extends JFrame {
    // Modèles de données
    private Article article; // L'article à afficher
    private Utilisateur utilisateur; // L'utilisateur connecté (null si non connecté)

    // Composants d'édition
    private JTextField nomField, marqueField, descriptionField;
    private JTextField prixField, prix_vraxField, quantite_vraxField, quantiteField, noteField;
    private boolean modeEdition = false; // Etat du mode édition

    /**
     * Constructeur initialisant la vue avec les modèles nécessaires
     * @param article L'article à afficher
     * @param utilisateur L'utilisateur connecté (peut être null)
     */
    public VueArticle(Article article, Utilisateur utilisateur) {
        this.article = article;
        this.utilisateur = utilisateur;

        // Configuration de la fenêtre
        setTitle(article.getNom()); // Titre dynamique
        setSize(900, 650); // Taille ajustée
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null); // Centrage de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme que cette fenêtre

        // Style global
        UIManager.put("Panel.background", new Color(240, 240, 240));
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        setBackground(new Color(240, 240, 240));

        buildUI(); // Construction de l'interface

        setVisible(true); // Affichage
    }

    private void buildUI() {
        // Panel principal avec BorderLayout et marges
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        /* ------------------------- */
        /* ZONE IMAGE */
        /* ------------------------- */
        JLabel imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombre portée
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(5, 5, 240, 240, 15, 15);

                // Image
                try {
                    ImageIcon icon = new ImageIcon(article.getImage());
                    Image img = icon.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
                    super.setIcon(new ImageIcon(img));
                } catch (Exception e) {
                    super.setText("Image indisponible");
                }
                super.paintComponent(g);
                g2.dispose();
            }
        };
        imageLabel.setPreferredSize(new Dimension(250, 250));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        /* ------------------------- */
        /* ZONE INFORMATIONS */
        /* ------------------------- */
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        infoPanel.setBackground(Color.WHITE);

        // Titre de l'article
        JLabel titreLabel = new JLabel(article.getNom());
        titreLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titreLabel.setForeground(new Color(44, 62, 80));
        infoPanel.add(titreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Marque
        JLabel marqueLabel = new JLabel("Marque : " + article.getMarque());
        marqueLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        infoPanel.add(marqueLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Note moyenne
        double noteMoyenne = calculerNoteMoyenne(article.getId());
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        notePanel.setBackground(Color.WHITE);
        notePanel.add(new JLabel("Note moyenne : "));
        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel(i <= Math.round(noteMoyenne) ? "★" : "☆");
            star.setForeground(i <= Math.round(noteMoyenne) ? new Color(241, 196, 15) : Color.LIGHT_GRAY);
            star.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            notePanel.add(star);
        }
        notePanel.add(new JLabel(String.format(" (%.1f/5)", noteMoyenne)));
        infoPanel.add(notePanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Description avec défilement
        JTextArea descArea = new JTextArea(article.getDescription());
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descArea.setForeground(new Color(80, 80, 80));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(new Color(250, 250, 250));
        descArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(400, 100));
        descScroll.setBorder(BorderFactory.createEmptyBorder());
        infoPanel.add(descScroll);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Prix et stock
        JPanel prixPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        prixPanel.setBackground(Color.WHITE);

        JLabel prixLabel = new JLabel("Prix unitaire :");
        prixLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel prixValue = new JLabel(String.format("%.2f €", article.getPrix()));
        prixValue.setForeground(new Color(231, 76, 60));

        JLabel prixVracLabel = new JLabel("Prix en vrac (" + article.getQuantite_vrac() + " unités) :");
        prixVracLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel prixVracValue = new JLabel(String.format("%.2f €", article.getPrix_vrac()));
        prixVracValue.setForeground(new Color(231, 76, 60));

        prixPanel.add(prixLabel);
        prixPanel.add(prixValue);
        prixPanel.add(prixVracLabel);
        prixPanel.add(prixVracValue);

        infoPanel.add(prixPanel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bouton voir les avis
        JButton voirAvisButton = createStyledButton("Voir les avis");
        voirAvisButton.addActionListener(e -> new VueAvis(article, utilisateur));
        infoPanel.add(voirAvisButton);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        /* ------------------------- */
        /* SECTION NOTATION */
        /* ------------------------- */
        if (utilisateur != null && utilisateur.getAdmin() == 0) {
            JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            ratingPanel.setBackground(Color.WHITE);
            ratingPanel.add(new JLabel("Donnez une note : "));

            JComboBox<Integer> noteCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
            noteCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JButton submitButton = createStyledButton("Valider");
            submitButton.addActionListener(e -> {
                int note = (int) noteCombo.getSelectedItem();
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
                    new AvisDAO(conn).ajouterAvis(new Avis(
                            0, article.getId(), utilisateur.getIdUtilisateur(), note, new java.util.Date()
                    ));
                    JOptionPane.showMessageDialog(this, "Merci pour votre note !");
                    dispose();
                    new VueArticle(article, utilisateur); // Rafraîchit la page
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            ratingPanel.add(noteCombo);
            ratingPanel.add(submitButton);
            infoPanel.add(ratingPanel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        /* ------------------------- */
        /* SECTION ADMIN */
        /* ------------------------- */
        if (utilisateur != null && utilisateur.getAdmin() == 1) {
            // Panel d'édition avec GridLayout
            JPanel infoEditPanel = new JPanel(new GridLayout(8, 2, 10, 10));
            infoEditPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            infoEditPanel.setBackground(Color.WHITE);

            // Initialisation des champs avec les valeurs actuelles
            nomField = createStyledTextField(article.getNom());
            marqueField = createStyledTextField(article.getMarque());
            descriptionField = createStyledTextField(article.getDescription());
            prixField = createStyledTextField(String.valueOf(article.getPrix()));
            prix_vraxField = createStyledTextField(String.valueOf(article.getPrix_vrac()));
            quantite_vraxField = createStyledTextField(String.valueOf(article.getQuantite_vrac()));
            quantiteField = createStyledTextField(String.valueOf(article.getQuantite()));
            noteField = createStyledTextField(String.valueOf(article.getNote()));

            // Ajout des composants
            infoEditPanel.add(createStyledLabel("Nom:"));
            infoEditPanel.add(nomField);
            infoEditPanel.add(createStyledLabel("Marque:"));
            infoEditPanel.add(marqueField);
            infoEditPanel.add(createStyledLabel("Description:"));
            infoEditPanel.add(descriptionField);
            infoEditPanel.add(createStyledLabel("Prix (€):"));
            infoEditPanel.add(prixField);
            infoEditPanel.add(createStyledLabel("Prix vrac:"));
            infoEditPanel.add(prix_vraxField);
            infoEditPanel.add(createStyledLabel("Quantité vrac:"));
            infoEditPanel.add(quantite_vraxField);
            infoEditPanel.add(createStyledLabel("Quantité:"));
            infoEditPanel.add(quantiteField);
            infoEditPanel.add(createStyledLabel("Note (/5):"));
            infoEditPanel.add(noteField);

            infoEditPanel.setVisible(false); // Masqué par défaut

            // Bouton pour basculer entre visualisation/édition
            JButton editButton = createStyledButton("Modifier les infos");
            editButton.addActionListener(e -> toggleEditMode(infoPanel, infoEditPanel, editButton));

            JPanel adminPanel = new JPanel();
            adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
            adminPanel.add(editButton);
            adminPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            adminPanel.add(infoEditPanel);

            mainPanel.add(adminPanel, BorderLayout.SOUTH);
        }

        /* ------------------------- */
        /* BOUTONS PRINCIPAUX */
        /* ------------------------- */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Bouton retour
        JButton retourBtn = createStyledButton("Retour");
        retourBtn.setBackground(new Color(120, 120, 120));
        retourBtn.addActionListener(e -> dispose());
        buttonPanel.add(retourBtn);

        // Bouton ajouter au panier
        if (utilisateur != null) {
            JButton ajouterPanierBtn = createStyledButton("Ajouter au panier");
            ajouterPanierBtn.addActionListener(e -> {
                ajouterArticleAuPanier(article);
                JOptionPane.showMessageDialog(this, "Article ajouté au panier !");
            });
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            buttonPanel.add(ajouterPanierBtn);
        } else {
            JButton ajouterPanierBtn = createStyledButton("Ajouter au panier");
            ajouterPanierBtn.addActionListener(e -> {
                new VueConnexion();
                dispose();
            });
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            buttonPanel.add(ajouterPanierBtn);
        }

        infoPanel.add(buttonPanel);

        // Assemblage final
        mainPanel.add(imageLabel, BorderLayout.WEST);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        add(mainPanel);
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

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private double calculerNoteMoyenne(int idArticle) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            List<Avis> avisList = new AvisDAO(conn).getAvisParArticle(idArticle);
            return avisList.stream().mapToInt(Avis::getNote).average().orElse(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Bascule entre mode visualisation et mode édition
     * @param displayPanel Panel d'affichage
     * @param editPanel panel d'édition
     * @param button Bouton qui déclenche l'action
     */
    private void toggleEditMode(JPanel displayPanel, JPanel editPanel, JButton button) {
        modeEdition = !modeEdition;

        if (modeEdition) {
            // Passage en mode édition
            displayPanel.setVisible(false);
            editPanel.setVisible(true);
            button.setText("Enregistrer");
        } else {
            // Retour en mode visualisation + sauvegarder des infos
            sauvegarderModifications();
            displayPanel.setVisible(true);
            editPanel.setVisible(false);
            button.setText("Modifier les infos");

            // Mettre à jour l'affichage
            dispose();
            new VueArticle(article, utilisateur); // Rafraîchit la vue
        }
    }

    /**
     * Sauvegarde les modifications de l'article en BDD
     */
    private void sauvegarderModifications() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            ArticleDAO articleDAO = new ArticleDAO(connection);

            // Mettre à jour l'objet article
            article.setNom(nomField.getText());
            article.setMarque(marqueField.getText());
            article.setDescription(descriptionField.getText());
            article.setPrix(Float.parseFloat(prixField.getText()));
            article.setPrix_vrac(Float.parseFloat(prix_vraxField.getText()));
            article.setQuantite_vrac(Integer.parseInt(quantite_vraxField.getText()));
            article.setQuantite(Integer.parseInt(quantiteField.getText()));
            article.setNote(Float.parseFloat(noteField.getText()));

            // Mettre à jour en BDD
            boolean success = articleDAO.mettreAJourArticle(article);

            if (success) {
                JOptionPane.showMessageDialog(this, "Modifications enregistrées avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs numériques valides", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ajouterArticleAuPanier(Article a) {
        if (utilisateur != null) {
            try {
                // Ajoute l'article dans le panier BDD
                int panierId = PanierDAO.getOrCreatePanierId(utilisateur.getIdUtilisateur());
                PanierDAO.ajouterArticleDansPanier(panierId, a.getId(), 1);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez vous connecter pour ajouter des articles au panier.");
            new VueConnexion(); // Redirige vers login
        }
    }
}