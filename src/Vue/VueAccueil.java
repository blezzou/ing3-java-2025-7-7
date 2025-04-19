package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import Modele.Article;
import Modele.Utilisateur;
import DAO.ArticleDAO;

public class VueAccueil extends JFrame {
    private JPanel headerPanel;
    private JPanel articlesPanel;
    private JScrollPane scrollPane;
    private Utilisateur utilisateurConnecte;

    public VueAccueil(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        setTitle("Accueil - Shopping");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création du conteneur principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 1 -> Header avec recherche, panier et profil
        headerPanel = new JPanel(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String texteRecherche = searchField.getText().trim();
            if (!texteRecherche.isEmpty()) {
                new VueRecherche(texteRecherche);
                dispose();
            }
        });

        // Boutons panier et profil/connexion
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton panierButton = new JButton("Panier");
        buttonsPanel.add(panierButton);

        if (utilisateurConnecte != null) {
            // Mode connecté - bouton Profil
            JButton profilButton = new JButton("Profil");
            buttonsPanel.add(profilButton);

            profilButton.addActionListener(e -> {
                new VueProfil(utilisateurConnecte);
                dispose();
            });
        } else {
            // Mode non connecté - bouton Connexion
            JButton seConnecterButton = new JButton("Se connecter");
            buttonsPanel.add(seConnecterButton);

            seConnecterButton.addActionListener(e -> {
                new VueConnexion();
                dispose();
            });
        }

        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2 -> Zone des articles (scrollable)
        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));

        List<Article> articles = ArticleDAO.getAllArticles();
        for (Article a : articles) {
            articlesPanel.add(createArticleCard(
                    a.getNom(),
                    a.getImage(),
                    a.getMarque(),
                    a.getDescription(),
                    a.getPrix(),
                    a.getPrix_vrac(),
                    a.getQuantite(),
                    a.getQuantite_vrac(),
                    a.getNote()
            ));
            articlesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Gestion des événements
        panierButton.addActionListener(e -> {
            new VuePanier();
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createArticleCard(String nom, String image, String marque, String description,
                                     float prix, float prix_vrac, int quantite, int quantite_vrac, int note) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setPreferredSize(new Dimension(900, 150));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Nom: " + nom));
        infoPanel.add(new JLabel("Marque: " + marque));
        infoPanel.add(new JLabel("Prix: " + prix + "€"));

        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setLineWrap(true);

        JButton ajouterButton = new JButton("Ajouter au panier");
        ajouterButton.addActionListener(e -> {
            // À implémenter
            JOptionPane.showMessageDialog(this, "Article ajouté au panier");
        });

        card.add(infoPanel, BorderLayout.WEST);
        card.add(descArea, BorderLayout.CENTER);
        card.add(ajouterButton, BorderLayout.EAST);

        return card;
    }

    public static void main(String[] args) {
        // Pour tester sans utilisateur connecté
        SwingUtilities.invokeLater(() -> new VueAccueil(null));


    }
}