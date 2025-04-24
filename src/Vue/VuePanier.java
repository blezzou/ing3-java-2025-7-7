package Vue;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import Modele.Article;
import Modele.Utilisateur;

public class VuePanier extends JFrame {
    private Utilisateur utilisateur;
    private Map<Integer, Article> panierArticles;
    private Map<Integer, Integer> panierQuantites;

    private JPanel articlesPanel;

    public VuePanier(Utilisateur utilisateur, Map<Integer, Article> panierArticles, Map<Integer, Integer> panierQuantites) {
        this.utilisateur = utilisateur;
        this.panierArticles = panierArticles;
        this.panierQuantites = panierQuantites;

        setTitle("Mon Panier");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel headerLabel = new JLabel("Panier");
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(articlesPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bouton retour à l'accueil
        JButton retourButton = new JButton("Retour à l'accueil");
        retourButton.addActionListener(e -> {
            new VueAccueil(utilisateur);
            dispose();
        });
        mainPanel.add(retourButton, BorderLayout.SOUTH);

        afficherArticles();

        add(mainPanel);
        setVisible(true);
    }

    private void afficherArticles() {
        articlesPanel.removeAll(); // Nettoyer avant d'ajouter

        if (panierArticles.isEmpty()) {
            articlesPanel.add(new JLabel("Votre panier est vide."));
        } else {
            for (Map.Entry<Integer, Article> entry : panierArticles.entrySet()) {
                int id = entry.getKey();
                Article article = entry.getValue();
                int quantite = panierQuantites.getOrDefault(id, 1);

                JPanel articlePanel = new JPanel(new BorderLayout());
                articlePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                articlePanel.setPreferredSize(new Dimension(750, 80));

                JLabel infoLabel = new JLabel("<html><b>" + article.getNom() + "</b> - " +
                        "Prix: " + article.getPrix() + "€ - Quantité: " + quantite + "</html>");
                articlePanel.add(infoLabel, BorderLayout.CENTER);

                JButton supprimerButton = new JButton("Supprimer");
                supprimerButton.addActionListener(e -> {
                    supprimerArticle(article);
                });
                articlePanel.add(supprimerButton, BorderLayout.EAST);

                articlesPanel.add(articlePanel);
                articlesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        articlesPanel.revalidate();
        articlesPanel.repaint();
    }

    public void ajouterArticle(Article article) {
        int id = article.getId();
        panierArticles.putIfAbsent(id, article);
        panierQuantites.put(id, panierQuantites.getOrDefault(id, 0) + 1);
    }

    private void supprimerArticle(Article article) {
        int id = article.getId();
        panierArticles.remove(id);
        panierQuantites.remove(id);
        afficherArticles();
    }
}