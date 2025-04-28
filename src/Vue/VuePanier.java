package Vue;

import DAO.PanierDAO;
import DAO.CommandeDAO;
import Modele.Article;
import Modele.ArticlePanier;
import Modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VuePanier extends JFrame {
    private Utilisateur utilisateur;
    private List<ArticlePanier> panierArticles;
    private JPanel articlesPanel;

    public VuePanier(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        // Configuration de la fenêtre
        setTitle("Mon Panier");
        setSize(900, 700);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Style global
        UIManager.put("Panel.background", new Color(240, 240, 240));
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        setBackground(new Color(240, 240, 240));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));

        /* ------------------------- */
        /* EN-TÊTE */
        /* ------------------------- */
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel headerLabel = new JLabel("Votre Panier");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        /* ------------------------- */
        /* LISTE DES ARTICLES */
        /* ------------------------- */
        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));
        articlesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        articlesPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        /* ------------------------- */
        /* PANEL BAS */
        /* ------------------------- */
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        bottomPanel.setBackground(new Color(240, 240, 240));

        // Bouton paiement
        JButton payerButton = createStyledButton("Procéder au paiement");
        payerButton.setBackground(new Color(46, 204, 113)); // Vert
        payerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        payerButton.addActionListener(e -> afficherRecapitulatif());

        // Bouton retour
        JButton retourButton = createStyledButton("Retour à l'accueil");
        retourButton.setBackground(new Color(120, 120, 120)); // Gris
        retourButton.addActionListener(e -> {
            new VueAccueil(utilisateur);
            dispose();
        });

        bottomPanel.add(retourButton);
        bottomPanel.add(payerButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Chargement des articles
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
            JLabel emptyLabel = new JLabel("Votre panier est vide.");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
            articlesPanel.add(emptyLabel);
        } else {
            for (ArticlePanier articlePanier : panierArticles) {
                JPanel articlePanel = new JPanel(new BorderLayout(15, 0));
                articlePanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                articlePanel.setBackground(Color.WHITE);
                articlePanel.setMaximumSize(new Dimension(800, 100));

                Article article = articlePanier.getArticle();
                double prixTotal = (articlePanier.getQuantite()/3) * article.getPrix_vrac() + (articlePanier.getQuantite()%3) * article.getPrix();

                // Panel d'informations
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                infoPanel.setBackground(Color.WHITE);

                // Nom et marque
                JLabel nomLabel = new JLabel(article.getNom());
                nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                nomLabel.setForeground(new Color(44, 62, 80));

                JLabel marqueLabel = new JLabel(article.getMarque());
                marqueLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));

                // Affichage des articles s'il n'y a pas de vrac
                JLabel prixLabel;
                if(articlePanier.getQuantite() < 3) {
                    prixLabel = new JLabel(String.format(
                            "Prix total: %.2f€ (%d × %.2f€)",
                            prixTotal, articlePanier.getQuantite(), article.getPrix()
                    ));
                }
                // Affichage des articles s'il y a achat en vrac
                else {
                    if(articlePanier.getQuantite()%3 == 0) {
                        prixLabel = new JLabel(String.format(
                                "Prix total: %.2f€ (%d lots de 3 × %.2f€)",
                                prixTotal, articlePanier.getQuantite()/3, article.getPrix_vrac()
                        ));
                    } else {
                        prixLabel = new JLabel(String.format(
                                "Prix total: %.2f€ (%d lots de 3 × %.2f€ + %d × %.2f€)",
                                prixTotal, articlePanier.getQuantite()/3, article.getPrix_vrac(),
                                articlePanier.getQuantite()%3, article.getPrix()
                        ));
                    }
                }
                prixLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                prixLabel.setForeground(new Color(231, 76, 60));

                infoPanel.add(nomLabel);
                infoPanel.add(marqueLabel);
                infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                infoPanel.add(prixLabel);

                // Bouton supprimer
                JButton supprimerButton = createStyledButton("Supprimer");
                supprimerButton.setBackground(new Color(231, 76, 60)); // Rouge
                supprimerButton.addActionListener(e -> {
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

                articlePanel.add(infoPanel, BorderLayout.CENTER);
                articlePanel.add(supprimerButton, BorderLayout.EAST);

                articlesPanel.add(articlePanel);
                articlesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
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
            connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
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
                    // Réincrémenter le stock avec la quantité totale qui était dans le panier
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

        // Calcul du total
        double totalGeneral = 0;
        for (ArticlePanier articlePanier : panierArticles) {
            Article article = articlePanier.getArticle();
            totalGeneral += (articlePanier.getQuantite()/3) * article.getPrix_vrac() +
                    (articlePanier.getQuantite()%3) * article.getPrix();
        }

        // Création du panel de récapitulatif
        JPanel recapPanel = new JPanel(new BorderLayout(10, 10));
        recapPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Récapitulatif de votre commande");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        recapPanel.add(titleLabel, BorderLayout.NORTH);

        // Liste des articles
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        for (ArticlePanier articlePanier : panierArticles) {
            Article article = articlePanier.getArticle();
            int qteVrac = articlePanier.getQuantite() / 3;
            int qteUnitaire = articlePanier.getQuantite() % 3;
            double prixTotal = qteVrac * article.getPrix_vrac() + qteUnitaire * article.getPrix();

            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
            itemPanel.setBackground(Color.WHITE);

            JLabel nameLabel = new JLabel(article.getNom() + " (" + article.getMarque() + ")");
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JLabel detailsLabel = new JLabel();
            detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            if (qteVrac > 0) {
                detailsLabel.setText(String.format(
                        "%d lot(s) de 3 à %.2f€", qteVrac, article.getPrix_vrac()
                ));
                if (qteUnitaire > 0) {
                    detailsLabel.setText(detailsLabel.getText() + String.format(
                            " + %d unité(s) à %.2f€", qteUnitaire, article.getPrix()
                    ));
                }
            } else {
                detailsLabel.setText(String.format(
                        "%d unité(s) à %.2f€", qteUnitaire, article.getPrix()
                ));
            }

            JLabel priceLabel = new JLabel(String.format("%.2f€", prixTotal));
            priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            priceLabel.setForeground(new Color(231, 76, 60));

            JPanel leftPanel = new JPanel(new GridLayout(2, 1));
            leftPanel.setBackground(Color.WHITE);
            leftPanel.add(nameLabel);
            leftPanel.add(detailsLabel);

            itemPanel.add(leftPanel, BorderLayout.WEST);
            itemPanel.add(priceLabel, BorderLayout.EAST);
            itemsPanel.add(itemPanel);
            itemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        recapPanel.add(scrollPane, BorderLayout.CENTER);

        // Total général
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        totalPanel.setBackground(Color.WHITE);

        JLabel totalLabel = new JLabel("Total général:");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel totalValue = new JLabel(String.format("%.2f€", totalGeneral));
        totalValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalValue.setForeground(new Color(46, 204, 113)); // Vert

        totalPanel.add(totalLabel, BorderLayout.WEST);
        totalPanel.add(totalValue, BorderLayout.EAST);
        recapPanel.add(totalPanel, BorderLayout.SOUTH);

        // Options de paiement
        Object[] options = {"Payer", "Annuler"};
        int choix = JOptionPane.showOptionDialog(
                this,
                recapPanel,
                "Confirmation de paiement",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choix == JOptionPane.YES_OPTION) {
            effectuerPaiement();
        }
    }

    /*--------------- Paiement -------------*/
    private void effectuerPaiement() {
        int panierId = PanierDAO.getOrCreatePanierId(utilisateur.getIdUtilisateur());
        Connection connexion = null;

        try {
            connexion = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "");
            connexion.setAutoCommit(false); // Démarrer une transaction

            // 1. Calculer le montant total
            double totalGeneral = 0;
            for (ArticlePanier articlePanier : panierArticles) {
                Article article = articlePanier.getArticle();
                totalGeneral += (articlePanier.getQuantite()/3) * article.getPrix_vrac() +
                        (articlePanier.getQuantite()%3) * article.getPrix();
            }

            // 2. Créer la commande
            CommandeDAO commandeDAO = new CommandeDAO(connexion);
            int idCommande = commandeDAO.creerCommande(utilisateur.getIdUtilisateur(), totalGeneral);

            if (idCommande == -1) {
                throw new SQLException("Échec de création de la commande");
            }

            // 3. Ajouter les articles à la commande
            commandeDAO.ajouterArticlesCommande(idCommande, panierArticles);

            // 4. Vider le panier
            String sql = "DELETE FROM panier_article WHERE id_panier = ?";
            try (PreparedStatement stmt = connexion.prepareStatement(sql)) {
                stmt.setInt(1, panierId);
                stmt.executeUpdate();
            }

            connexion.commit(); // Valider la transaction

            // Mettre à jour l'affichage
            panierArticles.clear();
            afficherArticles();

            JOptionPane.showMessageDialog(this,
                    "Paiement effectué ! La commande a été enregistrée.",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            try {
                if (connexion != null) {
                    connexion.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du paiement: " + e.getMessage(),
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
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return button;
    }
}