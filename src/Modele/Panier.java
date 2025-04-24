package Modele;

import java.util.ArrayList;
import java.util.List;

public class Panier {
    private int idPanier;
    private int idUtilisateur;
    private List<ArticlePanier> articles;


    public Panier(int idPanier, int idUtilisateur) {
        this.idPanier = idPanier;
        this.idUtilisateur = idUtilisateur;
        this.articles = new ArrayList<>();
    }

    public int getIdPanier() {return idPanier;}
    public int getIdUtilisateur() {return idUtilisateur;}
    public List<ArticlePanier> getArticles() {return articles;}

    public void ajouterArticle(ArticlePanier articlePanier) {
        articles.add(articlePanier);
    }

    public double getPrixTotal() {
        double total = 0.0;
        for (ArticlePanier ap : articles) {
            total += ap.getPrixTotal();
        }
        return total;
    }
}
