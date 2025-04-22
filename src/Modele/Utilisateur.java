package Modele;

/**
 * Classe représentant un utilisateur du système
 * Peut être soit un client soit un admin
 */
public class Utilisateur {
    // attributs
    private int Admin; //1 si admin, 0 si client normal
    private int idUtilisateur; //identifiant unique
    private String nom; //nom de famille
    private String prenom; //prénom
    private String email; //email (est utilisé comme identifiant de connexion)
    private String MotDePasse; //mot de passe
    private int historique; //historique des commandes

    //constructeur pour créer un nouvel utilisateur
    public Utilisateur(int idUtilisateur, int Admin, String nom, String prenom, String email, String MotDePasse, int historique) {
        this.idUtilisateur = idUtilisateur;
        this.Admin = Admin;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.MotDePasse = MotDePasse;
        this.historique = historique;
    }

    //setters
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.MotDePasse = motDePasse;
    }

    //getters
    public int getIdUtilisateur() {return idUtilisateur;}
    public int getAdmin() {return Admin;}
    public String getNom() {
        return nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public String getEmail() {
        return email;
    }
    public String getMotDePasse() {
        return MotDePasse;
    }
    public int getHistorique() {return historique;}

}