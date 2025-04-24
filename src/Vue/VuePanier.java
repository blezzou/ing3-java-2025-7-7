package Vue;

import Modele.Article;
import Modele.Utilisateur;
import DAO.ArticleDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VuePanier extends JFrame {
    //Composants de la fenêtre principale
    private JPanel mainPanel;
    private JList<String> articleList;
    private DefaultListModel<String> listModel;
    private JLabel totalLabel;
    private JLabel economieLabel;
    private JButton validerButton;
    private JButton supprimerButton;
    private JButton retourButton;
    private JButton augmenterButton, diminuerButton;

    //Structures pour stocker les articles et quantités associées
    private Map<Integer, Article> articlesMap;
    private Map<Integer, Integer> quantitesMap;
    private Utilisateur utilisateur;
    private float total = 0;
    private float economie = 0;

    public VuePanier(Utilisateur utilisateur, Map<Integer, Article> articlesMap, Map<Integer, Integer> quantitesMap) {
        this.utilisateur = utilisateur;
        this.articlesMap = articlesMap != null ? articlesMap : new HashMap<>();
        this.quantitesMap = quantitesMap != null ? quantitesMap : new HashMap<>();

        setTitle("Panier d'achats");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ferme toute l'app si on clique sur la croix
        setLocationRelativeTo(null); //centre la fenêtre

        //panel principal de la vue panier
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1 -> Header (identique aux autres vues)
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2 -> Création de la liste qui affichera les articles du panier
        listModel = new DefaultListModel<>();
        articleList = new JList<>(listModel);
        articleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articleList.setCellRenderer(new ArticleListRenderer());

        JScrollPane scrollPane = new JScrollPane(articleList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 3 -> Panel en bas avec le total et les boutons
        JPanel bottomPanel = new JPanel(new BorderLayout());

        //Affichage du total et de l'économie
        JPanel totalPanel = new JPanel(new GridLayout(2, 1));
        totalLabel = new JLabel("TOTAL : 0.00€");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        economieLabel = new JLabel("Économie : 0.00€");
        economieLabel.setForeground(Color.RED); //couleur rouge pour l'économie
        totalPanel.add(totalLabel);
        totalPanel.add(economieLabel);
        bottomPanel.add(totalPanel, BorderLayout.CENTER);

        //Panel pour les boutons pour les actions
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        supprimerButton = new JButton("Supprimer");
        validerButton = new JButton("Payer");
        retourButton = new JButton("Retour à l'accueil");
        augmenterButton = new JButton("+");
        diminuerButton = new JButton("-");

        buttonsPanel.add(diminuerButton);
        buttonsPanel.add(augmenterButton);
        buttonsPanel.add(supprimerButton);
        buttonsPanel.add(validerButton);
        buttonsPanel.add(retourButton);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        //Ajoutez les listeners
        augmenterButton.addActionListener(e -> modifierQuantite(1));
        diminuerButton.addActionListener(e -> modifierQuantite(-1));

        //listeners pour les boutons
        supprimerButton.addActionListener(e -> supprimerArticle());
        validerButton.addActionListener(e -> validerPaiement());
        retourButton.addActionListener(e -> {
            new VueAccueil(utilisateur); //retourne à l'accueil
            dispose(); //ferme la fenêtre actuelle
        });
        add(mainPanel);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());

        //Bouton d'accueil en haut à gauche
        JButton accueilButton = new JButton("Accueil");
        headerPanel.add(accueilButton, BorderLayout.WEST);

        //barre de recherche au centre
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        //Boutons Panier et Profil en haut à droite
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton panierButton = new JButton("Panier");
        JButton profilButton = new JButton("Profil");
        buttonsPanel.add(panierButton);
        buttonsPanel.add(profilButton);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        //évènements associés aux boutons
        accueilButton.addActionListener(e -> {
            new VueAccueil(utilisateur);
            dispose();
        });
        searchButton.addActionListener(e -> {
            String texteRecherche = searchField.getText().trim();
            if(!texteRecherche.isEmpty()) {
                new VueRecherche(texteRecherche, utilisateur);
                dispose();
            }
        });

        panierButton.addActionListener(e -> {
            new VuePanier(utilisateur, articlesMap, quantitesMap);
            dispose();
        });

        profilButton.addActionListener(e -> {
            new VueProfil(utilisateur);
            dispose();
        });
        return headerPanel;
    }

    private void modifierQuantite(int delta) {
        int selectedIndex = articleList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedValue = articleList.getSelectedValue();
            int id = Integer.parseInt(selectedValue.split("-")[0]);
            Article article = articlesMap.get(id);
            int nouvelleQuantite = quantitesMap.get(id) + delta;

            if (nouvelleQuantite > 0 && nouvelleQuantite <= article.getQuantite()) {
                quantitesMap.put(id, nouvelleQuantite);
                mettreAJourAffichage();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Quantite invalide (min : 1, max " + article.getQuantite() + ")",
                        "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un article",
                    "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void ajouterArticle(Article article) {
        int id = article.getId();
        if (articlesMap.containsKey(id)) {
            //fait en sorte que si l'article est déjà dans le panier, on incrémente la quantité de l'article
            int quantite = quantitesMap.get(id);
            if (quantite < article.getQuantite()) {
                quantitesMap.put(id, quantite + 1);
            }else {
                JOptionPane.showMessageDialog(this, "Quantité maximale atteinte pour cet article", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        }else {
            //sinon on l'ajoute avec une quantité de 1
            articlesMap.put(id, article);
            quantitesMap.put(id, 1);
        }
        mettreAJourAffichage(); //on refresh la vue
    }

    private void supprimerArticle() {
        int selectedIndex = articleList.getSelectedIndex();
        if (selectedIndex != -1) {
            int id = (int) articleList.getSelectedValue().charAt(0); //on stocke l'id dans le premier caractère
            articlesMap.remove(id);
            quantitesMap.remove(id);
            mettreAJourAffichage();
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un article à supprimer", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void validerPaiement() {
        if (articlesMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide","Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //fenetre de paiement (à completer plus tard)
        JOptionPane.showMessageDialog(this, "Code à faire - Interface de paiement", "Paiement", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mettreAJourAffichage() {
        listModel.clear(); //reset liste affichée
        total = 0;
        economie = 0;

        for (Map.Entry<Integer, Article> entry : articlesMap.entrySet()) {
            int id = entry.getKey();
            Article article = entry.getValue();
            int quantite = quantitesMap.get(id);

            //calcul du prix avec remise en vrac si applicable
            float prixArticle = calculerPrixArticle(article, quantite);
            total += prixArticle;

            //calcul de l'économie potentielle
            float prixNormal = article.getPrix() * quantite;
            economie += (prixNormal - prixArticle);

            // Ajout à la liste
            String affichage = String.format("%d - %s (%s) - %d x %.2f€ = %.2f€",
                    id, article.getNom(), article.getMarque(),
                    quantite, article.getPrix(), prixArticle);
            listModel.addElement(affichage);
        }
        totalLabel.setText("TOTAL : " + String.format("%2f€", total));
        economieLabel.setText("Economie : " + String.format("%2f€", economie));
    }

    private float calculerPrixArticle(Article article, int quantite) {
        if (article.getQuantite_vrac() > 0 && quantite >= article.getQuantite_vrac()) {
            int nbVrac = quantite / article.getQuantite_vrac();
            int reste = quantite % article.getQuantite_vrac();
            return (nbVrac * article.getPrix_vrac()) + (reste * article.getPrix());
        } else {
            return quantite * article.getPrix();
        }
    }

    //Renderer personnalisé pour afficher les articles dans la JList
    private class ArticleListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            //ici on peut personnalise l'affichage de chaque partie avec une icone, couleurs etc..
            //
            //
            //
            //

            //
            //
            return this;
        }
    }
}
