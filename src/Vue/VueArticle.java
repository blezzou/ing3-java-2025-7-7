package Vue;

import Modele.Article;
import Modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class VueArticle extends JFrame {
    private Article article;
    private Utilisateur utilisateur;

    public VueArticle(Article article) {
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

        // Image de l'article
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imageLabel.setText("[ Image ]"); // Tu peux charger une vraie image si dispo

        // Infos de l'article
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel("Nom : " + article.getNom()));
        infoPanel.add(new JLabel("Marque : " + article.getMarque()));
        infoPanel.add(new JLabel("Description : " + article.getDescription()));
        infoPanel.add(new JLabel("Prix : " + article.getPrix() + " €"));
        infoPanel.add(new JLabel("Note : " + article.getNote() + "/5"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bouton "Ajouter au panier"
        JButton ajouterPanierBtn = new JButton("Ajouter au panier");
        ajouterPanierBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        ajouterPanierBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Article ajouté au panier !");
            // TODO : méthode DAO pour ajouter dans panier_article
        });
        infoPanel.add(ajouterPanierBtn);

        // Retour
        JButton retourBtn = new JButton("Retour");
        retourBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        retourBtn.addActionListener(e -> dispose());
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(retourBtn);

        mainPanel.add(imageLabel, BorderLayout.WEST);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        add(mainPanel);
    }
}
