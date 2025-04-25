package Vue;

import DAO.PanierDAO;
import Modele.Article;
import Modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VuePanier extends JFrame {
    private Utilisateur utilisateur;
    private List<Article> panierArticles;
    private JPanel articlesPanel;

    public VuePanier(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

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

        // Charger les articles du panier à partir de la base de données
        panierArticles = chargerArticlesDuPanier();

        afficherArticles();

        add(mainPanel);
        setVisible(true);
    }

    private List<Article> chargerArticlesDuPanier() {
        List<Article> articles = new ArrayList<>();

        // On récupère l'ID du panier de l'utilisateur
        int panierId = PanierDAO.getOrCreatePanierId(utilisateur.getIdUtilisateur());

        try (Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            String sql = "SELECT a.id_article, a.nom, a.image, a.marque, a.description, a.prix, a.prix_vrac, a.quantite_vrac, a.quantite, a.note, pa.quantite " +
                    "FROM article a " +
                    "JOIN panier_article pa ON a.id_article = pa.id_article " +
                    "WHERE pa.id_panier = ?";

            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setInt(1, panierId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idArticle = rs.getInt("id_article");
                String nom = rs.getString("nom");
                String image = rs.getString("image");
                String marque = rs.getString("marque");
                String description = rs.getString("description");
                float prix = rs.getFloat("prix");
                float prix_vrac = rs.getFloat("prix_vrac");
                int quantite_vrac = rs.getInt("quantite_vrac");
                int quantite = rs.getInt("quantite");
                int note = rs.getInt("note");

                // Création de l'objet Article
                Article article = new Article(
                        idArticle,
                        nom,
                        image,
                        marque,
                        description,
                        prix,
                        prix_vrac,
                        quantite_vrac,
                        quantite,
                        note);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return articles;
    }


    private void afficherArticles() {
        articlesPanel.removeAll(); // Nettoyer avant d'ajouter

        if (panierArticles.isEmpty()) {
            articlesPanel.add(new JLabel("Votre panier est vide."));
        } else {
            for (Article article : panierArticles) {
                JPanel articlePanel = new JPanel(new BorderLayout());
                articlePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                articlePanel.setPreferredSize(new Dimension(750, 80));

                JLabel infoLabel = new JLabel("<html><b>" + article.getNom() + "</b> - " +
                        "Prix: " + article.getPrix() + "€ - Quantité: " + article.getQuantite() + "</html>");
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

    private void supprimerArticle(Article article) {
        // Logique de suppression de l'article du panier (à ajouter)
        // Par exemple : supprimer de la BDD et rafraîchir l'affichage
    }
}
