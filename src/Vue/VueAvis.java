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
        setTitle("Notes - " + article.getNom());
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Section notation
        if (utilisateur != null) {
            JPanel formPanel = new JPanel();
            JLabel label = new JLabel("Donnez une note (1-5):");
            JComboBox<Integer> noteCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
            JButton submitButton = new JButton("Valider");

            submitButton.addActionListener(e -> {
                int note = (int) noteCombo.getSelectedItem();
                // Enregistrement en BDD
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
                    new AvisDAO(conn).ajouterAvis(new Avis(
                            0, article.getId(), utilisateur.getIdUtilisateur(), note, new java.util.Date()
                    ));
                    JOptionPane.showMessageDialog(this, "Merci pour votre note !");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            formPanel.add(label);
            formPanel.add(noteCombo);
            formPanel.add(submitButton);
            mainPanel.add(formPanel, BorderLayout.NORTH);
        }

        // Affichage des notes existantes
        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/shopping", "root", "")) {
            List<Avis> avisList = new AvisDAO(conn).getAvisParArticle(article.getId());

            if (avisList.isEmpty()) {
                notesPanel.add(new JLabel("Aucune note pour cet article"));
            } else {
                // Calcul moyenne
                double moyenne = avisList.stream()
                        .mapToInt(Avis::getNote)
                        .average()
                        .orElse(0);

                notesPanel.add(new JLabel(String.format("Note moyenne: %.1f/5", moyenne)));
                notesPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                for (Avis avis : avisList) {
                    JPanel noteCard = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    noteCard.add(new JLabel("★".repeat(avis.getNote())));
                    noteCard.add(new JLabel(" - " + avis.getDate()));
                    notesPanel.add(noteCard);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainPanel.add(new JScrollPane(notesPanel), BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true); // Il manquait cette ligne pour afficher la fenêtre
    }
}