package Vue;

import DAO.UtilisateurDAO;
import DAO.ArticleDAO;
import Modele.Article;
import Modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VueArticle extends JFrame {
    private Article article;
    private Utilisateur utilisateur;
    private JTextField nomField, marqueField, descriptionField;
    private boolean modeEdition = false;

    public VueArticle(Article article, Utilisateur utilisateur) {
        this.article = article;
        this.utilisateur = utilisateur;

        setTitle(article.getNom());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buildUI();

        setVisible(true);
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imageLabel.setText("[ Image ]"); // Tu peux charger une vraie image si dispo


        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel("Nom : " + article.getNom()));
        infoPanel.add(new JLabel("Marque : " + article.getMarque()));
        infoPanel.add(new JLabel("Description : " + article.getDescription()));
        infoPanel.add(new JLabel("Prix : " + article.getPrix() + " €"));
        infoPanel.add(new JLabel("Note : " + article.getNote() + "/5"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        if (utilisateur.getAdmin() == 1) {
            JPanel infoEditPanel = new JPanel(new GridLayout(4, 2, 10, 10));
            nomField = new JTextField(article.getNom());
            marqueField = new JTextField(article.getMarque());
            descriptionField = new JTextField(article.getDescription());
            //prixField = new J(article.getPrix());

            infoEditPanel.add(new JLabel("Nom:"));
            infoEditPanel.add(nomField);
            infoEditPanel.add(new JLabel("Marque:"));
            infoEditPanel.add(marqueField);
            infoEditPanel.add(new JLabel("Description:"));
            infoEditPanel.add(descriptionField);

            infoEditPanel.setVisible(false);

            JButton editButton = new JButton("Modifier les infos");
            editButton.addActionListener(e -> toggleEditMode(infoPanel, infoEditPanel, editButton));




        }

        if (utilisateur != null) {
            JButton ajouterPanierBtn = new JButton("Ajouter au panier");
            ajouterPanierBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            ajouterPanierBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Article ajouté au panier !");
                // méthode DAO pour ajouter dans panier_article
            });
            infoPanel.add(ajouterPanierBtn);
        } else {
            JButton ajouterPanierBtn = new JButton("Ajouter au panier");
            ajouterPanierBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            ajouterPanierBtn.addActionListener(e -> {
                new VueConnexion();
            });
            infoPanel.add(ajouterPanierBtn);
        }

        JButton retourBtn = new JButton("Retour");
        retourBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        retourBtn.addActionListener(e -> dispose());
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(retourBtn);

        mainPanel.add(imageLabel, BorderLayout.WEST);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        add(mainPanel);
    }


    private void toggleEditMode(JPanel displayPanel, JPanel editPanel, JButton button) {
        modeEdition = !modeEdition;

        if (modeEdition) {
            displayPanel.setVisible(false);
            editPanel.setVisible(true);
            button.setText("Enregistrer");
        } else {
            // Sauvegarder les modifications
            sauvegarderModifications();
            displayPanel.setVisible(true);
            editPanel.setVisible(false);
            button.setText("Modifier les infos");

            // Mettre à jour l'affichage
            ((JLabel)displayPanel.getComponent(1)).setText(nomField.getText());
            ((JLabel)displayPanel.getComponent(3)).setText(marqueField.getText());
            ((JLabel)displayPanel.getComponent(5)).setText(descriptionField.getText());
        }
    }


    private void sauvegarderModifications() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);

            // Mettre à jour l'objet utilisateur
            utilisateur.setNom(nomField.getText());
            utilisateur.setPrenom(marqueField.getText());
            utilisateur.setEmail(descriptionField.getText());
/*
            // Mettre à jour en BDD
            boolean success = ArticleDAO.mettreAJourArticle(article);

            if (success) {
                JOptionPane.showMessageDialog(this, "Modifications enregistrées avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour", "Erreur", JOptionPane.ERROR_MESSAGE);
            }*/
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


}
