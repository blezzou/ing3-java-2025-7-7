package Modele;

import java.util.Date;

public class Avis {
    private int id;
    private int idArticle;
    private int idUtilisateur;
    private int note; // 1-5 étoiles
    private Date date;

    public Avis(int id, int idArticle, int idUtilisateur, int note, Date date) {
        this.id = id;
        this.idArticle = idArticle;
        this.idUtilisateur = idUtilisateur;
        this.note = note;
        this.date = date;
    }

    // Getters
    public int getId() { return id; }
    public int getIdArticle() { return idArticle; }
    public int getIdUtilisateur() { return idUtilisateur; }
    public int getNote() { return note; }
    public Date getDate() { return date; }
}