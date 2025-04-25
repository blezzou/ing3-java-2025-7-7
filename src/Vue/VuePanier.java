package Vue;

import DAO.PanierDAO;
import Modele.Article;
import Modele.ArticlePanier;
import Modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VuePanier extends JFrame {
    private Utilisateur utilisateur;
    private List<ArticlePanier> panierArticles;
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

        JButton retourButton = new JButton("Retour à l'accueil");
        retourButton.addActionListener(e -> {
            new VueAccueil(utilisateur);
            dispose();
        });
        mainPanel.add(retourButton, BorderLayout.SOUTH);

        panierArticles = chargerArticlesDuPanier();
        afficherArticles();

        add(mainPanel);
        setVisible(true);
    }

    private List<ArticlePanier> chargerArticlesDuPanier() {
        List<ArticlePanier> articles = new ArrayList<>();
        int panierId = PanierDAO.getOrCreatePanierId(utilisateur.getIdUtilisateur());

        try (Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            String sql = "SELECT a.*, pa.quantite as quantite_panier FROM article a " +
                    "JOIN panier_article pa ON a.id_article = pa.id_article " +
                    "WHERE pa.id_panier = ?";

            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setInt(1, panierId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id_article"),
                        rs.getString("nom"),
                        rs.getString("image"),
                        rs.getString("marque"),
                        rs.getString("description"),
                        rs.getFloat("prix"),
                        rs.getFloat("prix_vrac"),
                        rs.getInt("quantite_vrac"),
                        rs.getInt("quantite"),
                        rs.getFloat("note")
                );
                articles.add(new ArticlePanier(article, rs.getInt("quantite_panier")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    private void afficherArticles() {
        articlesPanel.removeAll();

        if (panierArticles.isEmpty()) {
            articlesPanel.add(new JLabel("Votre panier est vide."));
        } else {
            for (ArticlePanier articlePanier : panierArticles) {
                JPanel articlePanel = new JPanel(new BorderLayout());
                articlePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                articlePanel.setPreferredSize(new Dimension(750, 80));

                Article article = articlePanier.getArticle();
                double prixTotal = articlePanier.getPrixTotal();

                JLabel infoLabel = new JLabel("<html><b>" + article.getNom() + "</b> - " +
                        "Prix total: " + String.format("%.2f", prixTotal) + "€ (" +
                        article.getPrix() + "€ × " + articlePanier.getQuantite() + ")</html>");
                articlePanel.add(infoLabel, BorderLayout.CENTER);

                JButton supprimerButton = new JButton("Supprimer");
                supprimerButton.addActionListener(e -> {
                    int choix = JOptionPane.showConfirmDialog(
                            this,
                            "Voulez-vous vraiment supprimer cet article ?",
                            "Confirmation de suppression",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (choix == JOptionPane.YES_OPTION) {
                        supprimerArticle(articlePanier);
                    }
                });
                articlePanel.add(supprimerButton, BorderLayout.EAST);

                articlesPanel.add(articlePanel);
                articlesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        articlesPanel.revalidate();
        articlesPanel.repaint();
    }

    private void supprimerArticle(ArticlePanier articlePanier) {
        int panierId = PanierDAO.getOrCreatePanierId(utilisateur.getIdUtilisateur());
        int articleId = articlePanier.getArticle().getId();

        try (Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            if (articlePanier.getQuantite() > 1) {
                // Mise à jour de la quantité (décrémentation)
                String sql = "UPDATE panier_article SET quantite = quantite - 1 WHERE id_panier = ? AND id_article = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);
                stmt.setInt(1, panierId);
                stmt.setInt(2, articleId);

                if (stmt.executeUpdate() > 0) {
                    articlePanier.setQuantite(articlePanier.getQuantite() - 1);
                    afficherArticles();
                    JOptionPane.showMessageDialog(this,
                            "Article supprimé du panier.",
                            "Suppression réussie",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Suppression complète si c'était le dernier exemplaire
                String sql = "DELETE FROM panier_article WHERE id_panier = ? AND id_article = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);
                stmt.setInt(1, panierId);
                stmt.setInt(2, articleId);

                if (stmt.executeUpdate() > 0) {
                    panierArticles.remove(articlePanier);
                    afficherArticles();
                    JOptionPane.showMessageDialog(this,
                            "Article supprimé du panier.",
                            "Suppression réussie",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
