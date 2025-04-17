package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import Modele.Article;
import DAO.ArticleDAO;

public class VueAccueil extends JFrame {
    private JPanel headerPanel;
    private JPanel articlesPanel;
    private JScrollPane scrollPane;

    public VueAccueil() {
        setTitle("Accueil - Shopping");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //création du conteneur principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 1 -> Header avec recherche, panier et profil
        headerPanel = new JPanel(new BorderLayout());

        //Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Rechercher : ");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        // Boutons panier et profil
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton panierButton = new JButton("Panier");
        JButton SeConnecterButton = new JButton("Se connecter");
        buttonsPanel.add(panierButton);
        buttonsPanel.add(SeConnecterButton);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        SeConnecterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VueConnexion();
                dispose();
            }
        });

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2 -> Zone des articles (scrollable)
        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));

        List<Article> articles = ArticleDAO.getAllArticles();
        for (Article a : articles) {
            articlesPanel.add(createArticleCard(
                    a.getNom(),
                    a.getMarque(),
                    a.getPrix(),
                    //a.getDescription()
            ));
            articlesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        //Gestion des évènements
        panierButton.addActionListener(e -> {
            new VuePanier(); //à implémenter
            dispose();
        });

        searchButton.addActionListener(e -> {
            new VueProfil(); //à implémenter
            dispose();
        });

        searchButton.addActionListener(e -> {
            // À implémenter
            JOptionPane.showMessageDialog(this, "Fonctionnalité recherche à venir");
        });

        add(mainPanel);
        setVisible(true);
    }

    //méthode pour créer une carte d'article
    private JPanel createArticleCard(String nom, String marque, double prix, String description) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setPreferredSize(new Dimension(900, 150));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Nom: " + nom));
        infoPanel.add(new JLabel("Marque: " + marque));
        infoPanel.add(new JLabel("Prix: " + prix + "€"));

        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setLineWrap(true);

        JButton ajouterButton = new JButton("Ajouter au panier");
        ajouterButton.addActionListener(e -> {
            // À implémenter
            JOptionPane.showMessageDialog(this, "Article ajouté au panier");
        });

        card.add(infoPanel, BorderLayout.WEST);
        card.add(descArea, BorderLayout.CENTER);
        card.add(ajouterButton, BorderLayout.EAST);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VueAccueil());
    }
}
