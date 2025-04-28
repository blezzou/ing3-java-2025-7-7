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
        setSize(900, 700); //Taille (largeur, hauteur) augmentée pour meilleure lisibilité
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Modifié pour ne pas quitter l'application
        setLocationRelativeTo(null); //centre la fenêtre sur l'écran

        // Style global
        UIManager.put("Panel.background", new Color(240, 240, 240));
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        setBackground(new Color(240, 240, 240));

        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));

        /* ------------------------------- */
        /* 1. EN-TÊTE */
        /* ------------------------------- */
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Ajouter un nouvel article");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        /* ------------------------------- */
        /* 2. FORMULAIRE PRINCIPAL */
        /* ------------------------------- */
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuration commune des champs
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Dimension fieldSize = new Dimension(250, 30);

        /* ------------------------------- */
        /* 2.1 PREMIÈRE COLONNE */
        /* ------------------------------- */
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;

        // Champ nom
        JLabel nomLabel = new JLabel("Nom :");
        nomLabel.setFont(labelFont);
        formPanel.add(nomLabel, gbc);

        gbc.gridy++;
        JLabel imageLabel = new JLabel("Chemin vers l'Image :");
        imageLabel.setFont(labelFont);
        formPanel.add(imageLabel, gbc);

        gbc.gridy++;
        JLabel marqueLabel = new JLabel("Marque :");
        marqueLabel.setFont(labelFont);
        formPanel.add(marqueLabel, gbc);

        gbc.gridy++;
        JLabel descriptionLabel = new JLabel("Description :");
        descriptionLabel.setFont(labelFont);
        formPanel.add(descriptionLabel, gbc);

        /* ------------------------------- */
        /* 2.2 DEUXIÈME COLONNE */
        /* ------------------------------- */
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;

        nomField = createStyledTextField("", 20);
        nomField.setPreferredSize(fieldSize);
        formPanel.add(nomField, gbc);

        gbc.gridy++;
        imageField = createStyledTextField("", 20);
        imageField.setPreferredSize(fieldSize);
        formPanel.add(imageField, gbc);

        gbc.gridy++;
        marqueField = createStyledTextField("", 20);
        marqueField.setPreferredSize(fieldSize);
        formPanel.add(marqueField, gbc);

        gbc.gridy++;
        descriptionField = createStyledTextField("", 20);
        descriptionField.setPreferredSize(fieldSize);
        formPanel.add(descriptionField, gbc);

        /* ------------------------------- */
        /* 2.3 TROISIÈME COLONNE */
        /* ------------------------------- */
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.3;

        JLabel prixLabel = new JLabel("Prix (€) :");
        prixLabel.setFont(labelFont);
        formPanel.add(prixLabel, gbc);

        gbc.gridy++;
        JLabel prixVracLabel = new JLabel("Prix en vrac (€) :");
        prixVracLabel.setFont(labelFont);
        formPanel.add(prixVracLabel, gbc);

        gbc.gridy++;
        JLabel quantiteVracLabel = new JLabel("Quantité min. vrac :");
        quantiteVracLabel.setFont(labelFont);
        formPanel.add(quantiteVracLabel, gbc);

        gbc.gridy++;
        JLabel quantiteLabel = new JLabel("Quantité en stock :");
        quantiteLabel.setFont(labelFont);
        formPanel.add(quantiteLabel, gbc);

        /* ------------------------------- */
        /* 2.4 QUATRIÈME COLONNE */
        /* ------------------------------- */
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.7;

        prixField = createStyledTextField("", 10);
        prixField.setPreferredSize(fieldSize);
        formPanel.add(prixField, gbc);

        gbc.gridy++;
        prix_vracField = createStyledTextField("", 10);
        prix_vracField.setPreferredSize(fieldSize);
        formPanel.add(prix_vracField, gbc);

        gbc.gridy++;
        quantite_vracField = createStyledTextField("", 10);
        quantite_vracField.setPreferredSize(fieldSize);
        formPanel.add(quantite_vracField, gbc);

        gbc.gridy++;
        quantiteField = createStyledTextField("", 10);
        quantiteField.setPreferredSize(fieldSize);
        formPanel.add(quantiteField, gbc);

        // Note initialisée à 0 (cachée car toujours 0)
        noteField = 0;

        /* ------------------------------- */
        /* 3. BOUTON DE SOUMISSION */
        /* ------------------------------- */
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);

        JButton creerButton = createStyledButton("Ajouter l'article");
        creerButton.setPreferredSize(new Dimension(200, 40));
        formPanel.add(creerButton, gbc);

        // Ajout du formulaire dans un scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        /* ------------------------------- */
        /* 4. GESTION DE LA SOUMISSION */
        /* ------------------------------- */
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Récupération des valeurs des champs
                    String nom = nomField.getText();
                    String image = imageField.getText();
                    String marque = marqueField.getText();
                    String description = descriptionField.getText();

                    // Conversion des valeurs numériques
                    int prix = Integer.parseInt(prixField.getText());
                    int prix_vrac = Integer.parseInt(prix_vracField.getText());
                    int quantite_vrac = Integer.parseInt(quantite_vracField.getText());
                    int quantite = Integer.parseInt(quantiteField.getText());
                    int note = noteField; // toujours 0

                    // Appel à la DAO
                    AjoutArticle.AjouterArticle(nom, image, marque, description, prix, prix_vrac, quantite_vrac, quantite, note);

                    // Message de succès
                    JOptionPane.showMessageDialog(VueAjouterArticle.this,
                            "Article ajouté avec succès!",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Réinitialisation du formulaire
                    resetForm();

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(VueAjouterArticle.this,
                            "Veuillez entrer des valeurs numériques valides pour les champs prix et quantités",
                            "Erreur de saisie",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VueAjouterArticle.this,
                            "Erreur lors de l'ajout de l'article: " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(mainPanel);
        setVisible(true); //affichage de la fenetre
    }

    /**
     * Réinitialise tous les champs du formulaire
     */
    private void resetForm() {
        nomField.setText("");
        imageField.setText("");
        marqueField.setText("");
        descriptionField.setText("");
        prixField.setText("");
        prix_vracField.setText("");
        quantite_vracField.setText("");
        quantiteField.setText("");
    }

    /* ------------------------- */
    /* METHODES UTILITAIRES */
    /* ------------------------- */

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return button;
    }

    private JTextField createStyledTextField(String text, int columns) {
        JTextField field = new JTextField(text, columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
}