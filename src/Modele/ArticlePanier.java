package Modele;

/**
 * Classe représentant un article dans le panier d'achat.
 * Associe un article à une quantité et calcule le prix total.
 */
public class ArticlePanier {
    // Attributs

    /**
     * L'article associé au panier
     */
    private Article article;

    /**
     * Quantité de cet article dans le panier
     */
    private int quantite;

    /**
     * Constructeur pour créer un nouvel article dans le panier
     * @param article L'article à ajouter au panier
     * @param quantite La quantité souhaitée de cet article
     */
    public ArticlePanier(Article article, int quantite) {
        this.article = article;
        this.quantite = quantite;
    }

    // Getters

    /**
     * Retourne l'article associé
     * @return L'article dans le panier
     */
    public Article getArticle() {
        return article;
    }

    /**
     * Retourne la quantité de l'article dans le panier
     * @return La quantité actuelle
     */
    public int getQuantite() {
        return quantite;
    }

    // Setter

    /**
     * Modifie la quantité de l'article dans le panier
     * @param quantite La nouvelle quantité (doit être positive)
     */
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    // Méthode de calcul

    /**
     * Calcule le prix total pour cet article dans le panier
     * (prix unitaire * quantité)
     * @return Le prix total pour cette ligne du panier
     */
    public double getPrixTotal() {
        return article.getPrix() * quantite;
    }
}