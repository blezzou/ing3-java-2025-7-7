package Vue;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Modele.Article;
import Modele.Utilisateur;
import DAO.ArticleDAO;
import DAO.PanierDAO;

/**
 * Vue principale de l'application, affichant la liste des articles disponibles
 * Gère l'affichage des articles et la navigation vers d'autres vues
 */

public class VueAccueil extends JFrame {
    private JPanel headerPanel; //Panel pour l'en-tête avec barre de recherche
    private JPanel articlesPanel; //Panel pour la liste des articles
    private JScrollPane scrollPane; //panel avec barre de défilement
    private Utilisateur utilisateurConnecte; //Référence à l'utilisateur connecté
    private Map<Integer, Article> panierArticles = new HashMap<>();
    private Map<Integer, Integer> panierQuantites = new HashMap<>();

    /**
     * Constructeur initialisant la vue d'accueil
     * @param utilisateur L'utilisateur connecté (null si non connecté)
     */

    public VueAccueil(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;

        //Configuration de la fenêtre principale
        setTitle("Accueil - Shopping");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //Centre la fenêtre

        //System.out.println(utilisateurConnecte);

        // Création du conteneur principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        /* ------------------------- */
        /* 1. CONFIGURATION DU HEADER */
        /* ------------------------- */
        headerPanel = new JPanel(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        //Gestion de la recherche
        searchButton.addActionListener(e -> {
            String texteRecherche = searchField.getText().trim();
            if (!texteRecherche.isEmpty()) {
                new VueRecherche(texteRecherche, utilisateurConnecte); //Ouvre la vue de recherche
                dispose(); //Ferme la vue actuelle
            }
        });

        // Boutons de navigation (panier et profil/connexion)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton panierButton = new JButton("Panier");
        buttonsPanel.add(panierButton);

        // Affichage différent selon si l'utilisateur est connecté
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

        /* ----------------------------- */
        /* 2. ZONE DES ARTICLES (SCROLL) */
        /* ----------------------------- */
        articlesPanel = new JPanel();
        articlesPanel.setLayout(new BoxLayout(articlesPanel, BoxLayout.Y_AXIS));

        //Récupération de tous les articles depuis la BDD
        List<Article> articles = ArticleDAO.getAllArticles();

        //Création des cartes d'articles (affichage différent selon le statut admin)
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
                        utilisateurConnecte,
                        utilisateurConnecte.getAdmin()
                ));
                articlesPanel.add(Box.createRigidArea(new Dimension(0, 10))); //Espace entre les articles
            }
        } else {
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
                        utilisateurConnecte,
                        0
                ));
                articlesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        //Ajout du scrolling
        scrollPane = new JScrollPane(articlesPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Gestion du clic sur le bouton Panier
        panierButton.addActionListener(e -> {
            new VuePanier(utilisateurConnecte, panierArticles, panierQuantites);
            dispose();
        });


        //Finalisation de l'interface
        add(mainPanel);
        setVisible(true);
    }

    /**
     * Crée une carte visuelle pour un article.
     * @param a L'article à afficher
     * @param id Identifiant de l'article
     * @param nom Nom de l'article
     * @param image Chemin de l'image
     * @param marque Marque de l'article
     * @param description Description de l'article
     * @param prix Prix unitaire
     * @param prix_vrac Prix en gros
     * @param quantite Stock disponible
     * @param quantite_vrac Quantité pour prix en gros
     * @param note Note moyenne
     * @param utilisateur Utilisateur connecté (peut être null)
     * @param admin Statut admin (1 si admin, 0 sinon)
     * @return JPanel représentant la carte de l'article
     */

    private JPanel createArticleCard(Article a, int id, String nom, String image, String marque, String description,
                                     float prix, float prix_vrac, int quantite, int quantite_vrac, float note,
                                     Utilisateur utilisateur, int admin) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setPreferredSize(new Dimension(900, 150));

        //Panel d'informations (gauche)
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(new JLabel("Nom: " + nom));
        infoPanel.add(new JLabel("Marque: " + marque));
        infoPanel.add(new JLabel("Prix: " + prix + "€"));
        infoPanel.add(new JLabel("Note: " + note + "/5"));

        //Zone de description (centre)
        JTextArea descArea = new JTextArea(description);
        descArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);

        /* --------------------------------- */
        /* GESTION DES BOUTONS (DROITE) */
        /* Différents selon le statut de l'utilisateur */
        /* --------------------------------- */

        if(utilisateurConnecte != null) {
            if (admin == 1) {
                // Interface ADMIN - plus de boutons
                JPanel adminButtonsPanel = new JPanel();
                adminButtonsPanel.setLayout(new BoxLayout(adminButtonsPanel, BoxLayout.Y_AXIS));

                // Bouton Voir l'article
                JButton voirArticle = new JButton("Voir l'article");
                voirArticle.addActionListener(e -> {
                    new VueArticle(a, utilisateurConnecte);
                });

                //Bouton Ajouter au panier
                JButton ajouterButton = new JButton("Ajouter au panier");
                ajouterButton.addActionListener(e -> {
                    ajouterArticleAuPanier(a);
                });

                //Bouton Supprimer l'article (spécifique aux admins)
                JButton supprimerButton = new JButton("Supprimer l'article");
                supprimerButton.addActionListener(e -> {
                    ArticleDAO.supprimerArticle(id);
                    dispose();
                    new VueAccueil(utilisateurConnecte); //Rafraîchit la vue
                });

                //Organisation des boutons
                adminButtonsPanel.add(ajouterButton);
                adminButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // petit espace
                adminButtonsPanel.add(voirArticle);
                adminButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // petit espace
                adminButtonsPanel.add(supprimerButton);

                card.add(adminButtonsPanel, BorderLayout.EAST);
            } else {
                //Interface UTILISATEUR STANDARD
                JPanel ButtonsPanel = new JPanel();
                ButtonsPanel.setLayout(new BoxLayout(ButtonsPanel, BoxLayout.Y_AXIS));
                // Si pas admin, juste le bouton Ajouter
                JButton voirArticle = new JButton("Voir l'article");
                voirArticle.addActionListener(e -> {
                    new VueArticle(a, utilisateurConnecte);
                });

                JButton ajouterButton = new JButton("Ajouter au panier");
                ajouterButton.addActionListener(e -> {
                    ajouterArticleAuPanier(a);
                });


                ButtonsPanel.add(ajouterButton);
                ButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5))); // petit espace
                ButtonsPanel.add(voirArticle);

                card.add(ButtonsPanel, BorderLayout.EAST);
            }
        }else {
            // INTERFACE VISITEUR NON CONNECTÉ
            JPanel ButtonsPanel = new JPanel();
            ButtonsPanel.setLayout(new BoxLayout(ButtonsPanel, BoxLayout.Y_AXIS));
            // Si pas admin, juste le bouton Ajouter
            JButton voirArticle = new JButton("Voir l'article");
            voirArticle.addActionListener(e -> {
                new VueArticle(a, utilisateurConnecte);
            });

            JButton ajouterButton = new JButton("Ajouter au panier");
            ajouterButton.addActionListener(e -> {
                new VueConnexion(); //redirige vers la connexion
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

    private void ajouterArticleAuPanier(Article a) {
        if (utilisateurConnecte != null) {
            int panierId = PanierDAO.getOrCreatePanierId(utilisateurConnecte.getIdUtilisateur());
            PanierDAO.ajouterArticleDansPanier(panierId, a.getId(), 1);

            JOptionPane.showMessageDialog(this, "Article ajouté au panier !");
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez vous connecter pour ajouter des articles au panier.");
            new VueConnexion();
        }
    }

    /**
     * Méthode main pour tester la vue indépendamment
     */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VueAccueil(null));
    }
}
