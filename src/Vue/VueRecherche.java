package Vue;

import DAO.RechercheDAO;
import Modele.Article;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VueRecherche extends JFrame {
    private JPanel resultPanel;

    public VueRecherche(String texteRecherche) {
        setTitle("Résultats de recherche pour : " + texteRecherche);
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme juste la fenêtre
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        List<Article> resultats = RechercheDAO.rechercherArticlesParNom(texteRecherche);

        if (resultats.isEmpty()) {
            resultPanel.add(new JLabel("Aucun article trouvé pour : " + texteRecherche));
        } else {
            for (Article a : resultats) {
                resultPanel.add(createArticleCard(
                        a.getNom(),
                        a.getImage(),
                        a.getMarque(),
                        a.getDescription(),
                        a.getPrix(),
                        a.getPrix_vrac(),
                        a.getQuantite(),
                        a.getQuantite_vrac(),
                        a.getNote()
                ));
                resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    private JPanel createArticleCard(String nom, String image, String marque, String description, float prix, float prix_vrac, int quantite, int quantite_vrac, int note) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setPreferredSize(new Dimension(900, 150));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Nom: " + nom));
        infoPanel.add(new JLabel("Image: " + image));
        infoPanel.add(new JLabel("Marque: " + marque));
        infoPanel.add(new JLabel("Description: " + description));
        infoPanel.add(new JLabel("Prix: " + prix + "€"));

        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setLineWrap(true);

        JButton ajouterButton = new JButton("Ajouter au panier");
        ajouterButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Article ajouté au panier");
        });

        card.add(infoPanel, BorderLayout.WEST);
        card.add(descArea, BorderLayout.CENTER);
        card.add(ajouterButton, BorderLayout.EAST);

        return card;
    }
}
