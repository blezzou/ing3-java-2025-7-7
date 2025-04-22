package Modele;

public class Article {
    //Déclaration des attributs avec leurs types
    private int id; //Identifiant unique de l'article
    private String nom; //Nom de l'article
    private String image; //Chemin vers l'image de l'article
    private String marque; //Marque de l'article
    private String description; //Description détaillée de l'article
    private float prix; //Prix unitaire de l'article
    private float prix_vrac; //prix en gros (lot) de l'article
    private int quantite_vrac; //Quantité nécessaire pour le prix en gros
    private int quantite; //stock disponible
    private int note; //note moyenne des clients

    //constructeur
    public Article(int id, String nom, String image, String marque, String description, float prix, float prix_vrac, int quantite_vrac, int quantite, int note) {
        this.id = id;
        this.nom = nom;
        this.image = image;
        this.marque = marque;
        this.description = description;
        this.prix = prix;
        this.prix_vrac = prix_vrac;
        this.quantite_vrac = quantite_vrac;
        this.quantite = quantite;
        this.note = note;
    }

    //getters
    public int getId() {return id;}
    public String getNom() {return nom;}
    public String getImage() {return image;}
    public String getMarque() {return marque;}
    public String getDescription() {return description;}
    public float getPrix() {return prix;}
    public float getPrix_vrac() {return prix_vrac;}
    public int getQuantite_vrac() {return quantite_vrac;}
    public int getQuantite() {return quantite;}
    public int getNote() {return note;}
}
