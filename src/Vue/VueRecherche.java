package Vue;

import DAO.RechercheDAO;
import DAO.PanierDAO;
import Modele.Article;
import Modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class VueRecherche extends JFrame {
    private JPanel resultPanel;
    private JPanel headerPanel;
    private JPanel filtersPanel;
    private boolean filtersVisible = false;
    private List<Article> allResults;
    private Utilisateur utilisateurConnecte;

    // Composants de filtre
    private JComboBox<Integer> noteCombo;
    private JTextField prixMinField;
    private JTextField prixMaxField;
    private JTextField prix_vracMinField;
    private JTextField prix_vracMaxField;
    private JTextField quantiteField;

    public VueRecherche(String texteRecherche, Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;

        // Configuration de la fenêtre
        setTitle("Résultats pour : " + texteRecherche);
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

        /* ------------------------- */
        /* EN-TÊTE */
        /* ------------------------- */
        headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);

        JTextField searchField = new JTextField(texteRecherche, 30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        JButton searchButton = createStyledButton("Rechercher");
        JButton optionsButton = createStyledButton("Filtres");
        optionsButton.setBackground(new Color(155, 89, 182)); // Violet

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(optionsButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        // Bouton retour
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        JButton accueilButton = createStyledButton("Accueil");
        buttonsPanel.add(accueilButton);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        // Panel des filtres
        filtersPanel = createFiltersPanel();
        filtersPanel.setVisible(false);
        headerPanel.add(filtersPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        /* ------------------------- */
        /* RÉSULTATS */
        /* ------------------------- */
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Chargement initial des résultats
        allResults = RechercheDAO.rechercherArticles(texteRecherche);
        displayResults(allResults);

        /* ------------------------- */
        /* GESTION DES ÉVÈNEMENTS */
        /* ------------------------- */
        searchButton.addActionListener(e -> {
            String newSearch = searchField.getText().trim();
            if (!newSearch.isEmpty()) {
                new VueRecherche(newSearch, utilisateurConnecte);
                dispose();
            }
        });

        optionsButton.addActionListener(e -> toggleFilters());
        accueilButton.addActionListener(e -> {
            new VueAccueil(utilisateurConnecte);
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createFiltersPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 15, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Filtres avancés",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(70, 130, 180))
        );
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Filtre par note
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        notePanel.setBackground(Color.WHITE);
        notePanel.add(new JLabel("Note minimale:"));
        noteCombo = new JComboBox<>(new Integer[]{0, 1, 2, 3, 4, 5});
        noteCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notePanel.add(noteCombo);

        // Filtre par prix
        JPanel prixPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        prixPanel.setBackground(Color.WHITE);
        prixPanel.add(new JLabel("Prix entre:"));
        prixMinField = createStyledTextField("", 5);
        prixPanel.add(prixMinField);
        prixPanel.add(new JLabel("et"));
        prixMaxField = createStyledTextField("", 5);
        prixPanel.add(prixMaxField);

        // Filtre par prix vrac
        JPanel prix_vracPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        prix_vracPanel.setBackground(Color.WHITE);
        prix_vracPanel.add(new JLabel("Prix vrac entre:"));
        prix_vracMinField = createStyledTextField("", 5);
        prix_vracPanel.add(prix_vracMinField);
        prix_vracPanel.add(new JLabel("et"));
        prix_vracMaxField = createStyledTextField("", 5);
        prix_vracPanel.add(prix_vracMaxField);

        // Filtre par quantité
        JPanel quantitePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        quantitePanel.setBackground(Color.WHITE);
        quantitePanel.add(new JLabel("Quantité minimale:"));
        quantiteField = createStyledTextField("", 5);
        quantitePanel.add(quantiteField);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton applyButton = createStyledButton("Appliquer");
        JButton resetButton = createStyledButton("Réinitialiser");
        resetButton.setBackground(new Color(120, 120, 120)); // Gris

        applyButton.addActionListener(e -> applyFilters());
        resetButton.addActionListener(e -> resetFilters());

        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);

        panel.add(notePanel);
        panel.add(prixPanel);
        panel.add(prix_vracPanel);
        panel.add(quantitePanel);
        panel.add(new JLabel()); // Espace vide
        panel.add(buttonPanel);

        return panel;
    }

    private void toggleFilters() {
        filtersVisible = !filtersVisible;
        filtersPanel.setVisible(filtersVisible);
        pack();
    }

    private void applyFilters() {
        int minNote = (int) noteCombo.getSelectedItem();
        float minPrix = parseFloat(prixMinField.getText());
        float maxPrix = parseFloat(prixMaxField.getText());
        float minPrixVrac = parseFloat(prix_vracMinField.getText());
        float maxPrixVrac = parseFloat(prix_vracMaxField.getText());
        int minQuantite = parseInt(quantiteField.getText());

        List<Article> filtered = allResults.stream()
                .filter(a -> a.getNote() >= minNote)
                .filter(a -> minPrix == 0 || a.getPrix() >= minPrix)
                .filter(a -> maxPrix == 0 || a.getPrix() <= maxPrix)
                .filter(a -> minPrixVrac == 0 || a.getPrix_vrac() >= minPrixVrac)
                .filter(a -> maxPrixVrac == 0 || a.getPrix_vrac() <= maxPrixVrac)
                .filter(a -> minQuantite == 0 || a.getQuantite() >= minQuantite)
                .collect(Collectors.toList());

        displayResults(filtered);
    }

    private void resetFilters() {
        noteCombo.setSelectedIndex(0);
        prixMinField.setText("");
        prixMaxField.setText("");
        prix_vracMinField.setText("");
        prix_vracMaxField.setText("");
        quantiteField.setText("");
        displayResults(allResults);
    }

    private void displayResults(List<Article> articles) {
        resultPanel.removeAll();

        if (articles.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun résultat trouvé", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
            resultPanel.add(emptyLabel);
        } else {
            for (Article a : articles) {
                resultPanel.add(createArticleCard(a));
                resultPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private JPanel createArticleCard(Article a) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(900, 350));

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
            ImageIcon icon = new ImageIcon(a.getImage());
            Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Image indisponible");
        }

        // Panel d'informations
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        infoPanel.setBackground(Color.WHITE);

        JLabel nomLabel = new JLabel("Nom: " + a.getNom());
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel marqueLabel = new JLabel("Marque: " + a.getMarque());
        marqueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel prixLabel = new JLabel(String.format("Prix: %.2f€", a.getPrix()));
        prixLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel prixVracLabel = new JLabel(String.format("Prix vrac: %.2f€", a.getPrix_vrac()));
        prixVracLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel stockLabel = new JLabel("Stock: " + a.getQuantite() + " unités");
        stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel noteLabel = new JLabel(String.format("Note: %.1f/5", a.getNote()));
        noteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Description avec HTML pour le retour à la ligne
        JLabel descriptionLabel = new JLabel("<html><div style='width:200px;'>" + a.getDescription() + "</div></html>");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        infoPanel.add(nomLabel);
        infoPanel.add(marqueLabel);
        infoPanel.add(prixLabel);
        infoPanel.add(prixVracLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(descriptionLabel);
        infoPanel.add(noteLabel);

        // Panel des boutons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton voirButton = createStyledButton("Voir l'article");
        JButton ajouterButton = createStyledButton("Ajouter au panier");

        voirButton.addActionListener(e -> new VueArticle(a, utilisateurConnecte));
        ajouterButton.addActionListener(e -> ajouterArticleAuPanier(a));

        buttonPanel.add(voirButton);
        buttonPanel.add(ajouterButton);

        // Assemblage final
        JPanel leftPanel = new JPanel(new BorderLayout(15, 0));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(imageLabel, BorderLayout.WEST);
        leftPanel.add(infoPanel, BorderLayout.CENTER);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private void ajouterArticleAuPanier(Article a) {
        if (utilisateurConnecte != null) {
            try {
                int panierId = PanierDAO.getOrCreatePanierId(utilisateurConnecte.getIdUtilisateur());
                PanierDAO.ajouterArticleDansPanier(panierId, a.getId(), 1);

                JOptionPane.showMessageDialog(this,
                        "Article ajouté au panier !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez vous connecter pour ajouter des articles au panier.",
                    "Connexion requise",
                    JOptionPane.WARNING_MESSAGE);
            new VueConnexion();
        }
    }

    private float parseFloat(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
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

    private JTextField createStyledTextField(String text, int columns) {
        JTextField field = new JTextField(text, columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
}