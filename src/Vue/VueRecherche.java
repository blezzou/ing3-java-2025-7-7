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
        setTitle("Résultats de recherche pour : " + texteRecherche);
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header avec barre de recherche et boutons
        headerPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Barre de recherche
        JTextField searchField = new JTextField(texteRecherche, 30);
        JButton searchButton = new JButton("Rechercher");
        JButton optionsButton = new JButton("Options");

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(optionsButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        // Retour à l'accueil
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton accueilButton = new JButton("Accueil");
        buttonsPanel.add(accueilButton);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        // Panel des filtres
        filtersPanel = createFiltersPanel();
        filtersPanel.setVisible(false);
        headerPanel.add(filtersPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel des résultats
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Chargement initial des résultats
        allResults = RechercheDAO.rechercherArticles(texteRecherche);
        displayResults(allResults);

        // Gestion des événements
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
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Filtres avancés"));
        panel.setBackground(new Color(240, 240, 240));

        // Filtre par note
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notePanel.add(new JLabel("Note minimale:"));
        noteCombo = new JComboBox<>(new Integer[]{0, 1, 2, 3, 4, 5});
        notePanel.add(noteCombo);

        // Filtre par prix
        JPanel prixPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        prixPanel.add(new JLabel("Prix entre:"));
        prixMinField = new JTextField(5);
        prixMaxField = new JTextField(5);
        prixPanel.add(prixMinField);
        prixPanel.add(new JLabel("et"));
        prixPanel.add(prixMaxField);

        // Filtre par prix vrac
        JPanel prix_vracPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        prixPanel.add(new JLabel("Prix vrac entre:"));
        prix_vracMinField = new JTextField(5);
        prix_vracMaxField = new JTextField(5);
        prixPanel.add(prix_vracMinField);
        prixPanel.add(new JLabel("et"));
        prixPanel.add(prix_vracMaxField);

        // Filtre par quantité
        JPanel quantitePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantitePanel.add(new JLabel("Quantité minimale:"));
        quantiteField = new JTextField(5);
        quantitePanel.add(quantiteField);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton applyButton = new JButton("Appliquer");
        JButton resetButton = new JButton("Réinitialiser");

        applyButton.addActionListener(e -> applyFilters());
        resetButton.addActionListener(e -> resetFilters());

        buttonPanel.add(applyButton);
        buttonPanel.add(resetButton);

        panel.add(notePanel);
        panel.add(prixPanel);
        panel.add(prix_vracPanel);
        panel.add(quantitePanel);
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
            resultPanel.add(new JLabel("Aucun résultat trouvé"));
        } else {
            for (Article a : articles) {
                resultPanel.add(createArticleCard(a));
                resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private JPanel createArticleCard(Article a) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(900, 150));

        // Panel d'informations à gauche
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoPanel.add(new JLabel("Nom: " + a.getNom()));
        infoPanel.add(new JLabel("Marque: " + a.getMarque()));
        infoPanel.add(new JLabel(String.format("Prix: %.2f€", a.getPrix())));
        infoPanel.add(new JLabel(String.format("Prix vrac: %.2f€", a.getPrix_vrac())));
        infoPanel.add(new JLabel("Stock: " + a.getQuantite() + " unités"));
        JLabel descriptionLabel = new JLabel("<html><div style='width: 250px;'>Description : "
                + a.getDescription() + "</div></html>");
        descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        infoPanel.add(descriptionLabel);
        infoPanel.add(new JLabel(String.format("Note: %.1f/5", a.getNote())));

        // Image
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 100));
        try {
            ImageIcon icon = new ImageIcon(a.getImage());
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Image non disponible");
        }

        // Panel des boutons à droite
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton voirButton = new JButton("Voir l'article");
        JButton ajouterButton = new JButton("Ajouter au panier");

        voirButton.addActionListener(e -> {
            new VueArticle(a, utilisateurConnecte);
        });

        // Ajouter article au panier
        ajouterButton.addActionListener(e -> {
            ajouterArticleAuPanier(a);
        });

        buttonPanel.add(voirButton);
        buttonPanel.add(ajouterButton);

        // Assemblage de la carte
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.add(imageLabel, BorderLayout.WEST);
        leftPanel.add(infoPanel, BorderLayout.CENTER);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private void ajouterArticleAuPanier(Article a) {
        if (utilisateurConnecte != null) {
            try {
                // Ajoute l'article dans le panier BDD
                int panierId = PanierDAO.getOrCreatePanierId(utilisateurConnecte.getIdUtilisateur());
                PanierDAO.ajouterArticleDansPanier(panierId, a.getId(), 1);

                JOptionPane.showMessageDialog(this, "Article ajouté au panier !");
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez vous connecter pour ajouter des articles au panier.");
            new VueConnexion(); // Redirige vers login
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
}
