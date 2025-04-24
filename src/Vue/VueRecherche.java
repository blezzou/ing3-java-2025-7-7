package Vue;

import DAO.RechercheDAO;
import Modele.Article;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import Modele.Utilisateur;

public class VueRecherche extends JFrame {
    private JPanel resultPanel;
    private JPanel headerPanel;
    private Utilisateur utilisateurConnecte;

    public VueRecherche(String texteRecherche, Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        //Définition de la fenêtre
        setTitle("Résultats de recherche pour : " + texteRecherche);
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme juste la fenêtre
        setLocationRelativeTo(null);// centre la fenêtre à l'écran

        //panel principal avec un layout en BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Panel qui contiendra les résultats sous forme de liste verticale
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        //Header contenant la barre de recherche et potentiellement d'autres boutons
        headerPanel = new JPanel(new BorderLayout());

        // Barre de recherche (champ texte + bouton)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        //Action déclenchée lorsqu'on clique sur le bouton "Rechercher"
        searchButton.addActionListener(e -> {
            String nouveauTexteRecherche = searchField.getText().trim();
            if (!nouveauTexteRecherche.isEmpty()) {
                new VueRecherche(nouveauTexteRecherche, utilisateur); //relance une recherche avec le nouveau mot-clé
                dispose(); //Ferme la fenêtre actuelle
            }
        });

        //Placeholder pour d'autres boutons éventuels à droite (ex. accueil, panier...)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        JButton accueilButton = new JButton("Accueil");
        accueilButton.addActionListener(e -> {
            new VueAccueil(utilisateurConnecte);
            dispose();
        });
        buttonsPanel.add(accueilButton);

        //On ajoute le header à la partie haute du panel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        //Recherche les articles correspondant au texte saisi
        List<Article> resultats = RechercheDAO.rechercherArticlesParNom(texteRecherche);

        //Si aucun article trouvé
        if (resultats.isEmpty()) {
            resultPanel.add(new JLabel("Aucun article trouvé pour : " + texteRecherche));
        } else {
            //Pour chaque article trouvé, on crée une "carte" affichage
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
                //Espacement entre les cartes
                resultPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        //Ajout de la zone de résultats dans une zone scrollable
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //Ajout de la zone scrollable au centre de la fenêtre
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        //Ajout du panel principal à la fenêtre
        add(mainPanel);
        setVisible(true); //affichage de la fenêtre
    }

    /**
     * Méthode qui crée une "carte" pour un article (non, image, description, prix...)
     */

    private JPanel createArticleCard(String nom, String image, String marque, String description, float prix, float prix_vrac, int quantite, int quantite_vrac, int note) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY)); //bordure pour délimiter l'article
        card.setPreferredSize(new Dimension(900, 150)); //taille fixe de la carte

        //Panel contenant les infos de base à gauche
        JPanel infoPanel = new JPanel(new GridLayout(6, 0));
        infoPanel.add(new JLabel("Nom: " + nom));
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 100)); // Taille réduite pour s'adapter à la carte
        try {
            ImageIcon icon = new ImageIcon(image);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("Image introuvable");
            e.printStackTrace();
        }
        infoPanel.add(imageLabel);
        infoPanel.add(new JLabel("Marque: " + marque));
        JLabel descriptionLabel = new JLabel("<html><div style='width: 655px;'>Description : "
                + description + "</div></html>");
        descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        infoPanel.add(descriptionLabel);
        infoPanel.add(new JLabel("Prix: " + prix + "€"));
        infoPanel.add(new JLabel("Note: " + note + "/5"));


        //Zone texte centrale pour afficher la description complète
        JTextArea descArea = new JTextArea(description);
        descArea.setEditable(false);
        descArea.setLineWrap(true); //retour automatique à la ligne

        //Bouton pour ajouter l'article au panier
        JButton ajouterButton = new JButton("Ajouter au panier");
        ajouterButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Article ajouté au panier");
        });

        //Ajout des composants à la carte
        card.add(infoPanel, BorderLayout.WEST);
        card.add(descArea, BorderLayout.CENTER);
        card.add(ajouterButton, BorderLayout.EAST);

        return card;
    }
}
