package Modele;

import java.util.Date;

/**
 * Classe représentant un avis/utilisateur sur un article du système
 */
public class Avis {
    // Identifiant unique de l'avis en base de données
    private int id;

    // Identifiant de l'article concerné par l'avis
    private int idArticle;

    // Identifiant de l'utilisateur ayant posté l'avis
    private int idUtilisateur;

    // Note attribuée à l'article (échelle de 1 à 5 étoiles)
    private int note;

    // Date de publication de l'avis
    private Date date;

    /**
     * Constructeur pour créer un objet Avis
     * @param id Identifiant unique de l'avis
     * @param idArticle Identifiant de l'article évalué
     * @param idUtilisateur Identifiant de l'auteur de l'avis
     * @param note Note attribuée (entre 1 et 5)
     * @param date Date de création de l'avis
     */
    public Avis(int id, int idArticle, int idUtilisateur, int note, Date date) {
        this.id = id;
        this.idArticle = idArticle;
        this.idUtilisateur = idUtilisateur;
        this.note = note;
        this.date = date;
    }

    // GETTERS

    /**
     * @return L'identifiant unique de l'avis
     */
    public int getId() { return id; }

    /**
     * @return L'identifiant de l'article évalué
     */
    public int getIdArticle() { return idArticle; }

    /**
     * @return L'identifiant de l'utilisateur auteur
     */
    public int getIdUtilisateur() { return idUtilisateur; }

    /**
     * @return La note attribuée (1-5)
     */
    public int getNote() { return note; }

    /**
     * @return La date de publication de l'avis
     */
    public Date getDate() { return date; }
}