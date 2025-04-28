package Vue;

import DAO.AvisDAO;
import Modele.Avis;
import Modele.Article;
import Modele.Utilisateur;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class VueAvis extends JFrame {
    public VueAvis(Article article, Utilisateur utilisateur) {
        // Configuration de la fenêtre
        setTitle("Avis - " + article.getNom());
        setSize(500, 400);
        setMinimumSize(new Dimension(450, 350));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
        /* FORMULAIRE DE NOTATION */
        /* ------------------------- */
        if (utilisateur != null) {
            JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
            formPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            formPanel.setBackground(Color.WHITE);

            JLabel label = new JLabel("Donnez une note (1-5):");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JComboBox<Integer> noteCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
            noteCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JButton submitButton = createStyledButton("Valider");
            submitButton.setBackground(new Color(46, 204, 113)); // Vert

            submitButton.addActionListener(e -> {
                int note = (int) noteCombo.getSelectedItem();

                // Enregistrement de la note dans la BDD
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
                    new AvisDAO(conn).ajouterAvis(new Avis(
                            0, article.getId(), utilisateur.getIdUtilisateur(), note, new java.util.Date()
                    ));
                    JOptionPane.showMessageDialog(this,
                            "Merci pour votre note !",
                            "Succès",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new VueAvis(article, utilisateur); // Rafraîchit la fenêtre
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Erreur lors de l'enregistrement de l'avis",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            formPanel.add(label);
            formPanel.add(noteCombo);
            formPanel.add(submitButton);
            mainPanel.add(formPanel, BorderLayout.NORTH);
        }

        /* ------------------------- */
        /* AFFICHAGE DES AVIS */
        /* ------------------------- */
        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
        notesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        notesPanel.setBackground(Color.WHITE);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            List<Avis> avisList = new AvisDAO(conn).getAvisParArticle(article.getId());

            if (avisList.isEmpty()) {
                JLabel emptyLabel = new JLabel("Aucune note pour cet article");
                emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                emptyLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
                notesPanel.add(emptyLabel);
            } else {
                // Calcul la moyenne des notes
                double moyenne = avisList.stream()
                        .mapToInt(Avis::getNote)
                        .average()
                        .orElse(0);

                // Affichage de la moyenne
                JPanel moyennePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
                moyennePanel.setBackground(Color.WHITE);

                JLabel moyenneLabel = new JLabel("Note moyenne:");
                moyenneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

                JLabel noteLabel = new JLabel(String.format("%.1f/5", moyenne));
                noteLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
                noteLabel.setForeground(new Color(241, 196, 15)); // Jaune or

                JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
                starsPanel.setBackground(Color.WHITE);
                int fullStars = (int) Math.round(moyenne);
                for (int i = 1; i <= 5; i++) {
                    JLabel star = new JLabel(i <= fullStars ? "★" : "☆");
                    star.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                    star.setForeground(i <= fullStars ? new Color(241, 196, 15) : Color.LIGHT_GRAY);
                    starsPanel.add(star);
                }

                moyennePanel.add(moyenneLabel);
                moyennePanel.add(noteLabel);
                moyennePanel.add(starsPanel);

                notesPanel.add(moyennePanel);
                notesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                notesPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
                notesPanel.add(Box.createRigidArea(new Dimension(0, 15)));

                // Affichage des avis individuels
                for (Avis avis : avisList) {
                    JPanel noteCard = new JPanel(new BorderLayout(10, 5));
                    noteCard.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(220, 220, 220)),
                            BorderFactory.createEmptyBorder(10, 15, 10, 15)
                    ));
                    noteCard.setBackground(Color.WHITE);
                    noteCard.setMaximumSize(new Dimension(450, 80));

                    // Note en étoiles
                    JPanel starsPanelCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
                    starsPanelCard.setBackground(Color.WHITE);
                    for (int i = 1; i <= 5; i++) {
                        JLabel star = new JLabel(i <= avis.getNote() ? "★" : "☆");
                        star.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                        star.setForeground(i <= avis.getNote() ? new Color(241, 196, 15) : Color.LIGHT_GRAY);
                        starsPanelCard.add(star);
                    }

                    // Date
                    JLabel dateLabel = new JLabel((Icon) avis.getDate());
                    dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                    dateLabel.setForeground(new Color(120, 120, 120));

                    JPanel bottomPanel = new JPanel(new BorderLayout());
                    bottomPanel.setBackground(Color.WHITE);
                    bottomPanel.add(starsPanelCard, BorderLayout.WEST);
                    bottomPanel.add(dateLabel, BorderLayout.EAST);

                    noteCard.add(bottomPanel, BorderLayout.SOUTH);
                    notesPanel.add(noteCard);
                    notesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Erreur lors du chargement des avis");
            errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            notesPanel.add(errorLabel);
        }

        // Ajoute le panneau d'avis dans une zone scrollable
        JScrollPane scrollPane = new JScrollPane(notesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return button;
    }
}