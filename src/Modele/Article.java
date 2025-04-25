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
    private float note; //note moyenne des clients

    //constructeur
    public Article(int id, String nom, String image, String marque, String description, float prix, float prix_vrac, int quantite_vrac, int quantite, float note) {
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

    //setters
    public void setId(int id) {this.id = id;}
    public void setNom(String nom) {this.nom = nom;}
    public void setImage(String image) {this.image = image;}
    public void setMarque(String marque) {this.marque = marque;}
    public void setDescription(String description) {this.description = description;}
    public void setPrix(float prix) {this.prix = prix;}
    public void setPrix_vrac(float prix_vrac) {this.prix_vrac = prix_vrac;}
    public void setQuantite_vrac(int quantite_vrac) {this.quantite_vrac = quantite_vrac;}
    public void setQuantite(int quantite) {this.quantite = quantite;}
    public void setNote(float note) {this.note = note;}


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
    public float getNote() {return note;}
}
