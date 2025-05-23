package Modele;

import java.sql.Timestamp;
import java.util.List;
/**
 * Classe représentant une commande passée par un utilisateur.
 * Contient les informations sur les articles commandés et les quantités
 */
public class Commande {
    // attributs
    private int id_article; //identifiant unique de l'article commandé
    private String nom; //Nom de l'article
    private String marque; //marque de l'article
    private int prix; //prix unitaire de l'article
    private int quantite; //quantité commandée
    private int prix_vrac; //Prix en gros (pour les lots)
    private int quantite_vrac; //quantité nécessaire pour bénéficier du prix en gros
    private int note; //note attribuée à l'article

    private int idCommande;
    private int idUtilisateur;
    private Timestamp dateCommande;
    private double montantTotal;
    private List<ArticlePanier> articles;

    //constructeur pour créer une nouvelle commande
    public Commande(int id_article,String nom, String marque, int prix, int quantite, int prix_vrac, int quantite_vrac, int note) {
    this.id_article = id_article;
    this.nom = nom;
    this.marque = marque;
    this.prix = prix;
    this.quantite = quantite;
    this.prix_vrac = prix_vrac;
    this.quantite_vrac = quantite_vrac;
    this.note = note;


    }
    public Commande(int idCommande, int idUtilisateur, Timestamp dateCommande, double montantTotal){
        this.idCommande = idCommande;
        this.idUtilisateur = idUtilisateur;
        this.dateCommande = dateCommande;
        this.montantTotal = montantTotal;
    }

    //getters
    public int getId_article() {
        return id_article;
    }
    public String getNom() {
        return nom;
    }
    public String getMarque() {
        return marque;
    }
    public int getPrix() {
        return prix;
    }
    public int getQuantite() {
        return quantite;
    }
    public int getPrix_vrac() {
        return prix_vrac;
    }
    public int getQuantite_vrac() {
        return quantite_vrac;
    }
    public int getNote() {
        return note;
    }

    public int getIdCommande() { return idCommande; }
    public int getIdUtilisateur() { return idUtilisateur; }
    public Timestamp getDateCommande() { return dateCommande; }
    public double getMontantTotal() { return montantTotal; }
    public List<ArticlePanier> getArticles() { return articles; }


    // Setter pour les articles
    public void setArticles(List<ArticlePanier> articles) { this.articles = articles; }


    public String getFormattedDate() {
        return dateCommande.toString().substring(0, 16);
    }
}
