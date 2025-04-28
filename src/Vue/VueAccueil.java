package Vue;

import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.util.List;
import Modele.Article;
import Modele.Utilisateur;
import DAO.ArticleDAO;
import DAO.PanierDAO;

/**
 * Vue principale de l'application, affichant la liste des articles disponibles
 * Gère l'affichage des articles et la navigation vers d'autres vues
 */

public class VueAccueil extends JFrame {
    private JPanel headerPanel;
    private JPanel articlesPanel;
    private JScrollPane scrollPane;
    private Utilisateur utilisateurConnecte;

    /**
     * Constructeur initialisant la vue d'accueil
     * @param utilisateur L'utilisateur connecté (null si non connecté)
     */

    public VueAccueil(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;

        // Configuration de la fenêtre
        setTitle("Accueil - Shopping");
        setSize(1000, 800);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Style global
        UIManager.put("Panel.background", new Color(240, 240, 240));
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        setBackground(new Color(240, 240, 240));

        // Conteneur principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        /* ------------------------- */
        /* 1. CONFIGURATION DU HEADER */
        /* ------------------------- */
        headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        headerPanel.setBackground(new Color(52, 73, 94));

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);

        JTextField searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        JButton searchButton = new JButton("Rechercher");
        searchButton.setBackground(new Color(46, 204, 113));
        searchButton.setFocusPainted(false);

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        // Boutons de navigation
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);
        JButton panierButton = createStyledButton("Panier");

        if (utilisateurConnecte != null) {
            JButton profilButton = createStyledButton("Profil");
            buttonsPanel.add(profilButton);
            profilButton.addActionListener(e -> {
                new VueProfil(utilisateurConnecte);
                dispose();
            });
        } else {
            JButton seConnecterButton = createStyledButton("Se connecter");
            buttonsPanel.add(seConnecterButton);
            seConnecterButton.addActionListener(e -> {
                new VueConnexion();
                dispose();
            });
        }

        buttonsPanel.add(panierButton);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        /* ------------------------- */
        /* 2. ZONE DES ARTICLES (SCROLL) */
        /* ------------------------- */
        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));
        articlesPanel.setBackground(new Color(240, 240, 240));

        List<Article> articles = ArticleDAO.getAllArticles();

        if (utilisateurConnecte != null) {
            for (Article a : articles) {
                articlesPanel.add(createArticleCard(
                        a, a.getId(), a.getNom(), a.getImage(), a.getMarque(),
                        a.getDescription(), a.getPrix(), a.getPrix_vrac(),
                        a.getQuantite_vrac(), a.getQuantite(), a.getNote(),
                        utilisateurConnecte, utilisateurConnecte.getAdmin()
                ));
                articlesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        } else {
            for (Article a : articles) {
                articlesPanel.add(createArticleCard(
                        a, a.getId(), a.getNom(), a.getImage(), a.getMarque(),
                        a.getDescription(), a.getPrix(), a.getPrix_vrac(),
                        a.getQuantite_vrac(), a.getQuantite(), a.getNote(),
                        null, 0
                ));
                articlesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        // Zone défilable
        scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setUnitIncrement(16);
        vertical.setBackground(new Color(240, 240, 240));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Gestion des événements
        searchButton.addActionListener(e -> {
            String texteRecherche = searchField.getText().trim();
            if (!texteRecherche.isEmpty()) {
                new VueRecherche(texteRecherche, utilisateurConnecte);
                dispose();
            }
        });

        panierButton.addActionListener(e -> {
            new VuePanier(utilisateurConnecte);
            dispose();
        });

        // Responsive design
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                articlesPanel.setPreferredSize(
                        new Dimension(width - 50, articlesPanel.getPreferredSize().height)
                );
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Crée une carte visuelle pour un article.
     * @param a L'article à afficher
     * @param id Identifiant de l'article
     * @param nom Nom de l'article
     * @param image Chemin de l'image
     * @param marque Marque de l'article
     * @param description Description de l'article
     * @param prix Prix unitaire
     * @param prix_vrac Prix en gros
     * @param quantite Stock disponible
     * @param quantite_vrac Quantité pour prix en gros
     * @param note Note moyenne
     * @param utilisateur Utilisateur connecté (peut être null)
     * @param admin Statut admin (1 si admin, 0 sinon)
     * @return JPanel représentant la carte de l'article
     */

    private JPanel createArticleCard(Article a, int id, String nom, String image, String marque,
                                     String description, float prix, float prix_vrac, int quantite_vrac,
                                     int quantite, float note, Utilisateur utilisateur, int admin) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(900, 180));

        // Partie gauche (image + infos)
        JPanel leftPanel = new JPanel(new BorderLayout(15, 0));
        leftPanel.setBackground(Color.WHITE);

        // Image avec ombre
        JLabel imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(5, 5, 140, 140, 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            ImageIcon icon = new ImageIcon(image);
            Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Image indisponible");
        }

        // Informations produit
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        infoPanel.setBackground(Color.WHITE);

        JLabel nomLabel = new JLabel(nom);
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nomLabel.setForeground(new Color(44, 62, 80));

        JLabel marqueLabel = new JLabel(marque);
        marqueLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        JLabel prixLabel = new JLabel(String.format("%.2f€", prix));
        prixLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        prixLabel.setForeground(new Color(231, 76, 60));

        // Notation par étoiles
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        notePanel.setBackground(Color.WHITE);
        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel(i <= Math.round(note) ? "★" : "☆");
            star.setForeground(i <= Math.round(note) ? new Color(241, 196, 15) : Color.LIGHT_GRAY);
            notePanel.add(star);
        }

        infoPanel.add(nomLabel);
        infoPanel.add(marqueLabel);
        infoPanel.add(prixLabel);
        infoPanel.add(notePanel);

        leftPanel.add(infoPanel, BorderLayout.CENTER);
        leftPanel.add(imageLabel, BorderLayout.WEST);

        // Description
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descArea.setForeground(new Color(100, 100, 100));
        descArea.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        descArea.setBackground(new Color(250, 250, 250));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);

        /* --------------------------------- */
        /* GESTION DES BOUTONS (DROITE) */
        /* Différents selon le statut de l'utilisateur */
        /* --------------------------------- */
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JButton voirButton = createStyledButton("Voir l'article");
        voirButton.addActionListener(e -> new VueArticle(a, utilisateurConnecte));

        JButton ajouterButton = createStyledButton("Ajouter au panier");
        ajouterButton.addActionListener(e -> {
            if (utilisateurConnecte != null) {
                ajouterArticleAuPanier(a);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez vous connecter pour ajouter des articles au panier.");
                new VueConnexion();
            }
        });

        // Bouton admin supplémentaire
        if (admin == 1) {
            JButton supprimerButton = createStyledButton("Supprimer");
            supprimerButton.setBackground(new Color(231, 76, 60));
            supprimerButton.addActionListener(e -> {
                int choix = JOptionPane.showConfirmDialog(
                        this,
                        "Voulez-vous vraiment supprimer cet article ?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION
                );
                if (choix == JOptionPane.YES_OPTION) {
                    ArticleDAO.supprimerArticle(id);
                    dispose();
                    new VueAccueil(utilisateurConnecte);
                }
            });
            buttonsPanel.add(supprimerButton);
            buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        buttonsPanel.add(ajouterButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(voirButton);

        // Assemblage final
        card.add(leftPanel, BorderLayout.WEST);
        card.add(descArea, BorderLayout.CENTER);
        card.add(buttonsPanel, BorderLayout.EAST);

        // Effet hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                        BorderFactory.createEmptyBorder(14, 14, 14, 14)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });

        return card;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return button;
    }

    private void ajouterArticleAuPanier(Article a) {
        try {
            int panierId = PanierDAO.getOrCreatePanierId(utilisateurConnecte.getIdUtilisateur());
            PanierDAO.ajouterArticleDansPanier(panierId, a.getId(), 1);
            JOptionPane.showMessageDialog(this, "Article ajouté au panier !");
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
