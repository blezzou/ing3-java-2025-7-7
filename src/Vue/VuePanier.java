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
        
        /*-------------------- Options en bas de page ------------------------*/
        
        // Bouton paiement
        JButton payerButton = new JButton("Payer");
        payerButton.addActionListener(e -> afficherRecapitulatif());

        // Bouton retour
        JButton retourButton = new JButton("Retour à l'accueil");
        retourButton.addActionListener(e -> {
            new VueAccueil(utilisateur);
            dispose();
        });

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.add(payerButton);
        bottomPanel.add(retourButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

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
    
    /*---------------- Affichage des articles -----------------*/

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
                    /* met les boutons en français */
                    UIManager.put("OptionPane.yesButtonText", "Oui");
                    UIManager.put("OptionPane.noButtonText", "Non");

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

    /*----------------------- Supprimer l'article -------------------*/ 
    private void supprimerArticle(ArticlePanier articlePanier) {
        int panierId = PanierDAO.getOrCreatePanierId(utilisateur.getIdUtilisateur());
        int articleId = articlePanier.getArticle().getId();
        Connection connexion = null;

        try {
            connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping", "root", "");
            // Désactiver l'auto-commit pour gérer la transaction
            connexion.setAutoCommit(false);

            if (articlePanier.getQuantite() > 1) {
                // Mise à jour de la quantité (décrémentation)
                String sql = "UPDATE panier_article SET quantite = quantite - 1 WHERE id_panier = ? AND id_article = ?";
                PreparedStatement stmt = connexion.prepareStatement(sql);
                stmt.setInt(1, panierId);
                stmt.setInt(2, articleId);

                if (stmt.executeUpdate() > 0) {
                    // Réincrémenter le stock NORMAL
                    PreparedStatement updateStock = connexion.prepareStatement(
                            "UPDATE article SET quantite = quantite + 1 WHERE id_article = ?");
                    updateStock.setInt(1, articleId);
                    updateStock.executeUpdate();

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
                    // Réincrémenter le stock NORMAL avec la quantité totale qui était dans le panier
                    PreparedStatement updateStock = connexion.prepareStatement(
                            "UPDATE article SET quantite = quantite + ? WHERE id_article = ?");
                    updateStock.setInt(1, articlePanier.getQuantite());
                    updateStock.setInt(2, articleId);
                    updateStock.executeUpdate();

                    panierArticles.remove(articlePanier);
                    afficherArticles();
                    JOptionPane.showMessageDialog(this,
                            "Article supprimé du panier.",
                            "Suppression réussie",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
            // Valider la transaction
            connexion.commit();

        } catch (SQLException e) {
            // Annuler la transaction en cas d'erreur
            if (connexion != null) {
                try {
                    connexion.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (connexion != null) {
                try {
                    connexion.setAutoCommit(true); // Rétablir l'auto-commit
                    connexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*------------------- Récapitulatif du paiement ----------------*/
    
    private void afficherRecapitulatif() {
        if (panierArticles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Votre panier est vide.",
                    "Panier vide",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Création du message de récapitulatif
        StringBuilder recap = new StringBuilder("<html><h2>Récapitulatif de votre commande</h2><ul>");

        double totalGeneral = 0;

        for (ArticlePanier articlePanier : panierArticles) {
            Article article = articlePanier.getArticle();
            double prixTotal = articlePanier.getPrixTotal();
            totalGeneral += prixTotal;

            recap.append("<li>")
                    .append(article.getNom())
                    .append(" - Quantité: ").append(articlePanier.getQuantite())
                    .append(" - Prix unitaire: ").append(String.format("%.2f", article.getPrix())).append("€")
                    .append(" - Total: ").append(String.format("%.2f", prixTotal)).append("€")
                    .append("</li>");
        }

        recap.append("</ul><b>Total général: ").append(String.format("%.2f", totalGeneral)).append("€</b><br/><br/>");

        // Ajout des boutons de confirmation
        Object[] options = {"Payer", "Annuler"};
        int choix = JOptionPane.showOptionDialog(
                this,
                recap.toString(),
                "Confirmation de paiement",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choix == JOptionPane.YES_OPTION) {
            effectuerPaiement();
        }
    }

    /*--------------- Effectuation du paiement -------------*/
    private void effectuerPaiement() {

        int panierId = PanierDAO.getOrCreatePanierId(utilisateur.getIdUtilisateur());

        try (Connection connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            // Vider complètement le panier
            String sql = "DELETE FROM panier_article WHERE id_panier = ?";
            PreparedStatement stmt = connexion.prepareStatement(sql);
            stmt.setInt(1, panierId);
            stmt.executeUpdate();

            // Mettre à jour l'affichage
            panierArticles.clear();
            afficherArticles();

            JOptionPane.showMessageDialog(this,
                    "Paiement effectué !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du paiement: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
