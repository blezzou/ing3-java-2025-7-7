package Modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un panier d'achat associé à un utilisateur
 */
public class Panier {
    // Identifiant unique du panier
    private int idPanier;

    // Identifiant de l'utilisateur propriétaire du panier
    private int idUtilisateur;

    // Liste des articles contenus dans le panier avec leurs quantités
    private List<ArticlePanier> articles;

    /**
     * Constructeur pour créer un nouveau panier
     * @param idPanier Identifiant unique du panier
     * @param idUtilisateur Identifiant de l'utilisateur propriétaire
     */
    public Panier(int idPanier, int idUtilisateur) {
        this.idPanier = idPanier;
        this.idUtilisateur = idUtilisateur;
        this.articles = new ArrayList<>();
    }

    /**
     * @return L'identifiant unique du panier
     */
    public int getIdPanier() {
        return idPanier;
    }

    /**
     * @return L'identifiant de l'utilisateur propriétaire
     */
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    /**
     * @return La liste des articles du panier avec leurs quantités
     */
    public List<ArticlePanier> getArticles() {
        return articles;
    }

    /**
     * Ajoute un article au panier
     * @param articlePanier L'article à ajouter avec sa quantité
     */
    public void ajouterArticle(ArticlePanier articlePanier) {
        articles.add(articlePanier);
    }

    /**
     * Calcule le prix total du panier
     * @return Le montant total en additionnant tous les articles
     */
    public double getPrixTotal() {
        double total = 0.0;
        for (ArticlePanier ap : articles) {
            total += ap.getPrixTotal();
        }
        return total;
    }
}