package Modele;

public class Article {
    private int id;
    private String nom;
    private String marque;
    private String description;
    private float prix;
    private float prix_vrac;
    private int quantite_vrac;
    private int quantite;
    private int note;

    public Article(int id, String nom, String marque, String description, float prix, float prix_vrac, int quantite_vrac, int quantite, int note) {
        this.id = id;
        this.nom = nom;
        this.marque = marque;
        this.description = description;
        this.prix = prix;
        this.prix_vrac = prix_vrac;
        this.quantite_vrac = quantite_vrac;
        this.quantite = quantite;
        this.note = note;
    }

    public int getId() {return id;}
    public String getNom() {return nom;}
    public String getMarque() {return marque;}
    public String getDescription() {return description;}
    public float getPrix() {return prix;}
    public float getPrix_vrac() {return prix_vrac;}
    public int getQuantite_vrac() {return quantite_vrac;}
    public int getQuantite() {return quantite;}
    public int getNote() {return note;}
}
