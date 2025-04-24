package Vue;

import DAO.UtilisateurDAO;
import DAO.ArticleDAO;
import DAO.AvisDAO;
import Modele.Article;
import Modele.Utilisateur;
import Modele.Avis;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Vue pour afficher et modifier les détails d'un article
 * Respecte le pattern MVC en étant uniquement responsable de l'affichage et de la gestion des interactions utilisateur
 */

public class VueArticle extends JFrame {
    //Modèles de données
    private Article article; //L'article à afficher
    private Utilisateur utilisateur; //L'utilisateur connecté (null si non connecté)

    // Composants d'édition
    private JTextField nomField, marqueField, descriptionField;
    private JTextField prixField, prix_vraxField, quantiteField, quantite_vraxField, noteField;
    private boolean modeEdition = false; //Etat du mode édition

    /**
     * Construteur initialisant la vue avec les modèles nécessaires
     * @param article L'article à afficher
     * @param utilisateur L'utilisateur connecté (peut être null)
     */

    public VueArticle(Article article, Utilisateur utilisateur) {
        this.article = article;
        this.utilisateur = utilisateur;

        //Configuration de la fenêtre
        setTitle(article.getNom()); //Titre dynamique
        setSize(800, 600); //Taille fixe
        setLocationRelativeTo(null); //Centrage de la fenêtre
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Ferme que cette fenêtre

        buildUI(); //Construction de l'interface

        setVisible(true); //Affichage
    }

    private void buildUI() {
        //panel principal avec BorderLayout et marges
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //Zone image
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        try {
            ImageIcon icon = new ImageIcon(article.getImage());
            Image img = icon.getImage().getScaledInstance(200, 520, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Image introuvable");
            e.printStackTrace();
        }

        // Panel d'informations avec BoxLayout (affichage vertical)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        double noteMoyenne = calculerNoteMoyenne(article.getId());  // Déclaration + calcul

        //Affichage des propriétés de l'article
        infoPanel.add(new JLabel("Nom : " + article.getNom()));
        infoPanel.add(new JLabel("Marque : " + article.getMarque()));
        infoPanel.add(new JLabel("Note moyenne : " + String.format("%.1f", noteMoyenne) + "/5")); // MODIF

        JLabel descriptionLabel = new JLabel("<html><div style='width: 400px;'>Description : "
                + article.getDescription() + "</div></html>");
        descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        infoPanel.add(descriptionLabel);
        infoPanel.add(new JLabel("Prix : " + article.getPrix() + " €"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20))); //espacement

        // Ajout du bouton pour voir les avis
        JButton voirAvisButton = new JButton("Voir les avis");
        voirAvisButton.addActionListener(e -> {
            new VueAvis(article, utilisateur);
        });
        infoPanel.add(voirAvisButton);

        //section édition réservée aux admins
        if (utilisateur.getAdmin() == 1) {
            //panel d'édition avec GridLayout
            JPanel infoEditPanel = new JPanel(new GridLayout(4, 2, 10, 10));

            //Initialisation des champs avec les valeurs actuelles
            nomField = new JTextField(article.getNom());
            marqueField = new JTextField(article.getMarque());
            descriptionField = new JTextField(article.getDescription());
            prixField = new JTextField(String.valueOf(article.getPrix()));
            prix_vraxField = new JTextField(String.valueOf(article.getPrix_vrac()));
            quantite_vraxField = new JTextField(String.valueOf(article.getQuantite_vrac()));
            quantiteField = new JTextField(String.valueOf(article.getQuantite()));
            noteField = new JTextField(String.valueOf(article.getNote()));


            //Ajout des composants
            infoEditPanel.add(new JLabel("Nom:"));
            infoEditPanel.add(nomField);
            infoEditPanel.add(new JLabel("Marque:"));
            infoEditPanel.add(marqueField);
            infoEditPanel.add(new JLabel("Description:"));
            infoEditPanel.add(descriptionField);
            infoEditPanel.add(new JLabel("Prix (€):"));
            infoEditPanel.add(prixField);
            infoEditPanel.add(new JLabel("Prix vrax:"));
            infoEditPanel.add(prix_vraxField);
            infoEditPanel.add(new JLabel("Quantite vrax:"));
            infoEditPanel.add(quantite_vraxField);
            infoEditPanel.add(new JLabel("Quantite:"));
            infoEditPanel.add(quantiteField);
            infoEditPanel.add(new JLabel("Note (/5):"));
            infoEditPanel.add(noteField);


            infoEditPanel.setVisible(false); //masqué par défaut

            //Bouton pour basculer entre visualisation/édition
            JButton editButton = new JButton("Modifier les infos");
            editButton.addActionListener(e -> toggleEditMode(infoPanel, infoEditPanel, editButton));

            JPanel adminPanel = new JPanel();
            adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
            adminPanel.add(editButton);
            adminPanel.add(infoEditPanel);

            mainPanel.add(adminPanel, BorderLayout.SOUTH);
        }

        //Gestion du panier selon l'état de connexion
        if (utilisateur != null) {

            JButton ajouterPanierBtn = new JButton("Ajouter au panier");
            ajouterPanierBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            //Utilisateur connecté - ajout direct
            ajouterPanierBtn.addActionListener(e -> {
                //
                JOptionPane.showMessageDialog(this, "Article ajouté au panier !");
                // méthode DAO pour ajouter dans panier_article
            });
            infoPanel.add(ajouterPanierBtn);
        } else {
            //Utilisateur non connecté - redirection vers connexion
            JButton ajouterPanierBtn = new JButton("Ajouter au panier");
            ajouterPanierBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

            ajouterPanierBtn.addActionListener(e -> {
                new VueConnexion(); //ouvre la vue de connexion
            });
            infoPanel.add(ajouterPanierBtn);
        }

        //Bouton retour
        JButton retourBtn = new JButton("Retour");
        retourBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        retourBtn.addActionListener(e -> dispose()); //ferme la fenêtre
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(retourBtn);

        //Assemblage final
        mainPanel.add(imageLabel, BorderLayout.WEST); //Image à gauche
        mainPanel.add(infoPanel, BorderLayout.CENTER); //Infos au centre

        add(mainPanel); //Ajout du panel principal
    }

    private double calculerNoteMoyenne(int idArticle) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            List<Avis> avisList = new AvisDAO(conn).getAvisParArticle(idArticle);
            return avisList.stream().mapToInt(Avis::getNote).average().orElse(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Bascule entre mode visualisation et mode édition
     * @param displayPanel Panel d'affichage
     * @param editPanel panel d'édition
     * @param button Bouton qui déclenche laction
     */


    private void toggleEditMode(JPanel displayPanel, JPanel editPanel, JButton button) {
        modeEdition = !modeEdition;

        if (modeEdition) {
            //passage en mode édition
            displayPanel.setVisible(false);
            editPanel.setVisible(true);
            button.setText("Enregistrer");
        } else {
            // Retour en mode visualisation + sauvegarder des infos
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

    /**
     * Sauvegarde les modifications de l'article en BDD
     */

    private void sauvegarderModifications() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            ArticleDAO articleDAO = new ArticleDAO(connection);

            // Mettre à jour l'objet article
            article.setNom(nomField.getText());
            article.setMarque(marqueField.getText());
            article.setDescription(descriptionField.getText());
            article.setPrix(Float.parseFloat(prixField.getText()));
            article.setPrix_vrac(Float.parseFloat(prix_vraxField.getText()));
            article.setQuantite_vrac(Integer.parseInt(quantiteField.getText()));
            article.setQuantite(Integer.parseInt(quantiteField.getText()));
            article.setNote(Integer.parseInt(noteField.getText()));


            // Mettre à jour en BDD
            boolean success = articleDAO.mettreAJourArticle(article);

            if (success) {
                JOptionPane.showMessageDialog(this, "Modifications enregistrées avec succès !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
