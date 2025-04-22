package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import DAO.AjoutArticle;

/**
 * Vue permettant d'ajouter un nouvel article dans le système
 * Réservée aux administrateurs
 */

public class VueAjouterArticle extends JFrame {
    //Champs de formulaire pour les propriétés de l'article (différent champs pour chaque propriété de l'article)
    private JTextField nomField, imageField, marqueField, descriptionField, prixField, prix_vracField, quantite_vracField, quantiteField;
    private int noteField;

    /**
     * Constructeur initialisant le formulaire d'ajout d'article
     */

    public VueAjouterArticle() {
        // Configuration de la fenêtre
        setTitle("Ajouter un article"); //Titre de la fenêtre
        setSize(750, 500); //Taille (largeur, hauteur)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Comportement à la fermeture
        setLocationRelativeTo(null); //centre la fenêtre sur l'écran

        //Création du panel principal avec une grille de 5 lignes x 2 colonnes
        JPanel panel = new JPanel(new GridLayout(5, 2));

        /* ------------------------------- */
        /* 1. AJOUT DES CHAMPS DU FORMULAIRE */
        /* ------------------------------- */

        //Champ nom
        panel.add(new JLabel("Nom :")); //Libellé
        nomField = new JTextField(); //Zone de saisie
        panel.add(nomField); //Ajout au panel

        //Champ Image
        panel.add(new JLabel("Chemin vers l'Image :"));
        imageField = new JTextField();
        panel.add(imageField);

        //Champ marque
        panel.add(new JLabel("Marque :"));
        marqueField = new JTextField();
        panel.add(marqueField);

        //Champ description
        panel.add(new JLabel("Description :"));
        descriptionField = new JTextField();
        panel.add(descriptionField);

        //Champ prix unitaire
        panel.add(new JLabel("Prix :"));
        prixField = new JTextField();
        panel.add(prixField);

        //Champ prix en vrac
        panel.add(new JLabel("Prix en vrac :"));
        prix_vracField = new JTextField();
        panel.add(prix_vracField);

        //Champ Quantité minimale pour prix en gros
        panel.add(new JLabel("Quantite en vrac :"));
        quantite_vracField = new JTextField();
        panel.add(quantite_vracField);

        // Champ quantité en stock
        panel.add(new JLabel("Quantite :"));
        quantiteField = new JTextField();
        panel.add(quantiteField);

        //Note initialisée à 0
        noteField = 0;
        panel.add(quantite_vracField);

        /* ------------------------------- */
        /* 2. BOUTON DE SOUMISSION */
        /* ------------------------------- */

        JButton creerButton = new JButton("Ajouter un article");
        panel.add(creerButton);

        //Ajout du panel à la fenêtre
        add(panel);

        /* ------------------------------- */
        /* 3. GESTION DE LA SOUMISSION */
        /* ------------------------------- */

        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Récupération des valeurs des champs
                String nom = nomField.getText();
                String image = imageField.getText();
                String marque = marqueField.getText();
                String description = descriptionField.getText();

                //Conversion des valeurs numériques
                int prix = Integer.parseInt(prixField.getText());
                int prix_vrac = Integer.parseInt(prix_vracField.getText());
                int quantite_vrac = Integer.parseInt(quantite_vracField.getText());
                int quantite = Integer.parseInt(quantiteField.getText());
                int note = noteField; //toujours 0
                AjoutArticle.AjouterArticle(nom, image, marque, description, prix, prix_vrac, quantite_vrac, quantite, note);
            }
        });
        setVisible(true); //affichage de la fenetre
    }
}
