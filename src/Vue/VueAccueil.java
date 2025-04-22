package Vue;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import Modele.Article;
import Modele.Utilisateur;
import DAO.ArticleDAO;

public class VueAccueil extends JFrame {
    private JPanel headerPanel;
    private JPanel articlesPanel;
    private JScrollPane scrollPane;
    private Utilisateur utilisateurConnecte;


    public VueAccueil(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        setTitle("Accueil - Shopping");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //System.out.println(utilisateurConnecte);

        // Création du conteneur principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 1 -> Header avec recherche, panier et profil
        headerPanel = new JPanel(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String texteRecherche = searchField.getText().trim();
            if (!texteRecherche.isEmpty()) {
                new VueRecherche(texteRecherche);
                dispose();
            }
        });

        // Boutons panier et profil/connexion
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton panierButton = new JButton("Panier");
        buttonsPanel.add(panierButton);

        if (utilisateurConnecte != null) {
            // Mode connecté - bouton Profil
            JButton profilButton = new JButton("Profil");
            buttonsPanel.add(profilButton);

            profilButton.addActionListener(e -> {
                new VueProfil(utilisateurConnecte);
                dispose();
            });
        } else {
            // Mode non connecté - bouton Connexion
            JButton seConnecterButton = new JButton("Se connecter");
            buttonsPanel.add(seConnecterButton);

            seConnecterButton.addActionListener(e -> {
                new VueConnexion();
                dispose();
            });
        }

        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2 -> Zone des articles (scrollable)
        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));

        List<Article> articles = ArticleDAO.getAllArticles();
        if (utilisateurConnecte != null) {
            for (Article a : articles) {
                articlesPanel.add(createArticleCard(
                        a,
                        a.getId(),
                        a.getNom(),
                        a.getImage(),
                        a.getMarque(),
                        a.getDescription(),
                        a.getPrix(),
                        a.getPrix_vrac(),
                        a.getQuantite(),
                        a.getQuantite_vrac(),
                        a.getNote(),
                        utilisateurConnecte.getAdmin()
                ));
                articlesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
            }
        else {
        for (Article a : articles) {
            articlesPanel.add(createArticleCard(
                    a,
                    a.getId(),
                    a.getNom(),
                    a.getImage(),
                    a.getMarque(),
                    a.getDescription(),
                    a.getPrix(),
                    a.getPrix_vrac(),
                    a.getQuantite(),
                    a.getQuantite_vrac(),
                    a.getNote(),
                    0
            ));
            articlesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        }

        scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Gestion des événements
        panierButton.addActionListener(e -> {
            new VuePanier(utilisateur);
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createArticleCard(Article a, int id, String nom, String image, String marque, String description,
                                     float prix, float prix_vrac, int quantite, int quantite_vrac, int note, int admin) {
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


        if (admin == 1) {
            // Créer un panel pour empiler les boutons verticalement
            JPanel adminButtonsPanel = new JPanel();
            adminButtonsPanel.setLayout(new BoxLayout(adminButtonsPanel, BoxLayout.Y_AXIS));

            JButton voirArticle = new JButton("Voir l'article");
            voirArticle.addActionListener(e -> {
                new VueArticle(a);
            });

            JButton ajouterButton = new JButton("Ajouter au panier");
            ajouterButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Article ajouté au panier");
            });

            JButton supprimerButton = new JButton("Supprimer l'article");
            supprimerButton.addActionListener(e -> {
                ArticleDAO.supprimerArticle(id);
                dispose();
                new VueAccueil(utilisateurConnecte);
            });

            adminButtonsPanel.add(ajouterButton);
            adminButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // petit espace
            adminButtonsPanel.add(voirArticle);
            adminButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // petit espace
            adminButtonsPanel.add(supprimerButton);

            card.add(adminButtonsPanel, BorderLayout.EAST);
        } else {
            JPanel ButtonsPanel = new JPanel();
            ButtonsPanel.setLayout(new BoxLayout(ButtonsPanel, BoxLayout.Y_AXIS));
            // Si pas admin, juste le bouton Ajouter
            JButton voirArticle = new JButton("Voir l'article");
            voirArticle.addActionListener(e -> {
                new VueArticle(a);
            });

            JButton ajouterButton = new JButton("Ajouter au panier");
            ajouterButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Article ajouté au panier");
            });

            ButtonsPanel.add(ajouterButton);
            ButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // petit espace
            ButtonsPanel.add(voirArticle);

            card.add(ButtonsPanel, BorderLayout.EAST);
        }


        card.add(infoPanel, BorderLayout.WEST);
        card.add(descArea, BorderLayout.CENTER);


        return card;
    }

    public static void main(String[] args) {
        // Pour tester sans utilisateur connecté
        SwingUtilities.invokeLater(() -> new VueAccueil(null));
    }
}