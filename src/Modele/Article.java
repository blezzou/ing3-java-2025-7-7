package Modele;

public class Article {
    private int id;
    private String nom;
    private String marque;
    private int prix;
    private int prix_vrac;
    private int quantite_vrac;
    private int quantite;
    private int note;

    public Article(int id, String nom, String marque, int prix, int prix_vrac, int quantite_vrac, int quantite, int note) {
        this.id = id;
        this.nom = nom;
        this.marque = marque;
        this.prix = prix;
        this.prix_vrac = prix_vrac;
        this.quantite_vrac = quantite_vrac;
        this.quantite = quantite;
        this.note = note;
    }

    public int getId() {return id;}
    public String getNom() {return nom;}
    public String getMarque() {return marque;}
    public int getPrix() {return prix;}
    public int getPrix_vrac() {return prix_vrac;}
    public int getQuantite_vrac() {return quantite_vrac;}
    public int getQuantite() {return quantite;}
    public int getNote() {return note;}
}
