package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Modele.Utilisateur;
import DAO.ChercherUtilisateur;

/**
 * Vue pour la connexion des utilisateurs
 * Gère l'authentification et la redirection vers la création de compte
 */
public class VueConnexion extends JFrame {
    // Composants graphiques
    private JTextField emailField;
    private JPasswordField motDePasseField;

    /**
     * Constructeur initialisant l'interface de connexion
     */
    public VueConnexion() {
        // Configuration de la fenêtre
        setTitle("Connexion");
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme l'application si fenêtre fermée
        setLocationRelativeTo(null); // Centre la fenêtre

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

        JLabel titleLabel = new JLabel("Connexion à votre compte");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        /* ------------------------------- */
        /* 2. FORMULAIRE DE CONNEXION */
        /* ------------------------------- */
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(500, 400));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuration commune des champs
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Dimension fieldSize = new Dimension(300, 35);

        /* ------------------------------- */
        /* 2.1 CHAMP EMAIL */
        /* ------------------------------- */
        gbc.gridx = 0;
        gbc.gridy = 0;
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
        /* 2.2 CHAMP MOT DE PASSE */
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
        /* 2.3 BOUTONS */
        /* ------------------------------- */
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(Color.WHITE);

        JButton seConnecterButton = createStyledButton("Se connecter");
        seConnecterButton.setPreferredSize(new Dimension(180, 40));

        JButton creerCompteButton = createStyledButton("Créer un compte");
        creerCompteButton.setBackground(new Color(155, 89, 182)); // Violet
        creerCompteButton.setPreferredSize(new Dimension(180, 40));

        buttonsPanel.add(seConnecterButton);
        buttonsPanel.add(creerCompteButton);

        formPanel.add(buttonsPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        /* ------------------------------- */
        /* 3. GESTION DES ÉVÈNEMENTS */
        /* ------------------------------- */

        // Redirection vers la création de compte
        creerCompteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VueCreationCompte(); // Ouvre la vue de création de compte
                dispose(); // Ferme cette fenêtre
            }
        });

        // Tentative de connexion
        seConnecterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String motDePasse = new String(motDePasseField.getPassword());

                // Validation des champs
                if (email.isEmpty() || motDePasse.isEmpty()) {
                    JOptionPane.showMessageDialog(VueConnexion.this,
                            "Veuillez remplir tous les champs.",
                            "Champs manquants",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Utilisateur utilisateur = ChercherUtilisateur.ChercherUtilisateur(email, motDePasse);

                    if (utilisateur != null) {
                        // Connexion réussie
                        new VueAccueil(utilisateur);
                        dispose();
                    } else {
                        // Identifiants incorrects
                        JOptionPane.showMessageDialog(VueConnexion.this,
                                "Email ou mot de passe incorrect.",
                                "Échec de la connexion",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VueConnexion.this,
                            "Erreur lors de la connexion: " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(mainPanel);
        setVisible(true);
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