package Modele;

public class Utilisateur {
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String MotDePasse;
    private int[] historique;

    public Utilisateur(int idUtilisateur, String nom, String prenom, String email, String MotDePasse, int[] historique) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.MotDePasse = MotDePasse;
        this.historique = historique;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }
    public String getnom() {
        return nom;
    }
    public String getprenom() {
        return prenom;
    }
    public String getemail() {
        return email;
    }
    public String getMotDePasse() {
        return MotDePasse;
    }
    public int[] getHistorique() {
        return historique;
    }

}
