package Modele;

public class Utilisateur {
    private int Admin;
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String MotDePasse;
    private int[] historique;

    public Utilisateur(int idUtilisateur, int Admin, String nom, String prenom, String email, String MotDePasse, int[] historique) {
        this.idUtilisateur = idUtilisateur;
        this.Admin = Admin;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.MotDePasse = MotDePasse;
        this.historique = historique;
    }



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
    public int[] getHistorique() {
        return historique;
    }

}