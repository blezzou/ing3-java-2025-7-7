package Modele;

public class Commande {
    private int id_article;
    private String nom;
    private String marque;
    private int prix;
    private int quantite;
    private int prix_vrac;
    private int quantite_vrac;
    private int note;

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
}
