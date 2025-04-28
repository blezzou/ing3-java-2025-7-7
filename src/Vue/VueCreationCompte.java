package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import DAO.AjoutUtilisateur;

/**
 * Vue pour la création de nouveaux comptes utilisateurs
 * Gère l'inscription des nouveaux clients avec validation des données
 */
public class VueCreationCompte extends JFrame {
    // Composants graphiques
    private JTextField nomField, prenomField, emailField;
    private JPasswordField motDePasseField;

    /**
     * Constructeur initialisant l'interface de création de compte
     */
    public VueCreationCompte() {
        // Configuration de la fenêtre principale
        setTitle("Créer un compte");
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme uniquement cette fenêtre sans quitter l'application
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran

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
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Création de compte");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        /* ------------------------------- */
        /* 2. FORMULAIRE DE CRÉATION */
        /* ------------------------------- */
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuration commune des champs
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Dimension fieldSize = new Dimension(300, 35);

        /* ------------------------------- */
        /* 2.1 CHAMP NOM */
        /* ------------------------------- */
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;

        JLabel nomLabel = new JLabel("Nom :");
        nomLabel.setFont(labelFont);
        formPanel.add(nomLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        nomField = createStyledTextField("", 20);
        nomField.setPreferredSize(fieldSize);
        formPanel.add(nomField, gbc);

        /* ------------------------------- */
        /* 2.2 CHAMP PRÉNOM */
        /* ------------------------------- */
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;

        JLabel prenomLabel = new JLabel("Prénom :");
        prenomLabel.setFont(labelFont);
        formPanel.add(prenomLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        prenomField = createStyledTextField("", 20);
        prenomField.setPreferredSize(fieldSize);
        formPanel.add(prenomField, gbc);

        /* ------------------------------- */
        /* 2.3 CHAMP EMAIL */
        /* ------------------------------- */
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;

        JLabel emailLabel = new JLabel("Email :");
        emailLabel.setFont(labelFont);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        emailField = createStyledTextField("", 20);
        emailField.setPreferredSize(fieldSize);
        formPanel.add(emailField, gbc);

        /* ------------------------------- */
        /* 2.4 CHAMP MOT DE PASSE */
        /* ------------------------------- */
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.3;

        JLabel mdpLabel = new JLabel("Mot de passe :");
        mdpLabel.setFont(labelFont);
        formPanel.add(mdpLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        motDePasseField = new JPasswordField();
        motDePasseField.setFont(fieldFont);
        motDePasseField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        motDePasseField.setPreferredSize(fieldSize);
        formPanel.add(motDePasseField, gbc);

        /* ------------------------------- */
        /* 2.5 BOUTON DE CRÉATION */
        /* ------------------------------- */
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);

        // Si le compte existe
        JButton compteExisteButton = new JButton("Vous avez un compte ? Connectez-vous !");
        compteExisteButton.addActionListener(e -> {
            new VueConnexion();
            dispose();
        });
        formPanel.add(compteExisteButton);

        JButton creerButton = createStyledButton("Créer le compte");
        creerButton.setPreferredSize(new Dimension(200, 40));
        formPanel.add(creerButton, gbc);

        // Ajout du formulaire dans un scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        /* ------------------------------- */
        /* 3. GESTION DE LA CRÉATION */
        /* ------------------------------- */
        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupération des données saisies
                String nom = nomField.getText().trim();
                String prenom = prenomField.getText().trim();
                String email = emailField.getText().trim();
                String motDePasse = new String(motDePasseField.getPassword());

                // Vérifie si un champ est vide
                if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
                    JOptionPane.showMessageDialog(VueCreationCompte.this,
                            "Veuillez remplir tous les champs.",
                            "Champs manquants",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validation de l'email
                if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(VueCreationCompte.this,
                            "Veuillez entrer une adresse email valide.",
                            "Email invalide",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Vérifie si l'email est déjà utilisé
                if (AjoutUtilisateur.emailExiste(email)) {
                    JOptionPane.showMessageDialog(VueCreationCompte.this,
                            "Cet email est déjà utilisé.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validation du mot de passe
                if (motDePasse.length() < 6) {
                    JOptionPane.showMessageDialog(VueCreationCompte.this,
                            "Le mot de passe doit contenir au moins 6 caractères.",
                            "Mot de passe trop court",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    // Ajout de l'utilisateur via la couche DAO
                    AjoutUtilisateur.AjouterUtilisateur(0, nom, prenom, email, motDePasse, 0);

                    // Message de succès
                    JOptionPane.showMessageDialog(VueCreationCompte.this,
                            "Compte créé avec succès!\nVous pouvez maintenant vous connecter.",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Fermeture de la fenêtre
                    dispose();

                    // Ouverture de la fenêtre de connexion
                    new VueConnexion();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VueCreationCompte.this,
                            "Erreur lors de la création du compte: " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(mainPanel);
        setVisible(true); // Affiche la fenêtre
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