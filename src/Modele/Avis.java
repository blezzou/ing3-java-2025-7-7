package Modele;

import java.util.Date;

/**
 * Classe représentant un avis/utilisateur sur un article
 * Contient l'évaluation (note) et la date de publication
 */
public class Avis {
    // Attributs

    /**
     * Identifiant unique de l'avis
     */
    private final int id;

    /**
     * Identifiant de l'article évalué
     */
    private final int idArticle;

    /**
     * Identifiant de l'utilisateur ayant posté l'avis
     */
    private final int idUtilisateur;

    /**
     * Note attribuée (entre 1 et 5 étoiles)
     */
    private int note;

    /**
     * Date de publication de l'avis
     */
    private final Date date;

    /**
     * Constructeur complet pour créer un avis
     * @param id Identifiant unique
     * @param idArticle Article concerné
     * @param idUtilisateur Auteur de l'avis
     * @param note Note attribuée (1-5)
     * @param date Date de publication
     * @throws IllegalArgumentException Si la note n'est pas entre 1 et 5
     */
    public Avis(int id, int idArticle, int idUtilisateur, int note, Date date) {
        if (note < 1 || note > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5");
        }

        this.id = id;
        this.idArticle = idArticle;
        this.idUtilisateur = idUtilisateur;
        this.note = note;
        this.date = new Date(date.getTime()); // Défensive copy
    }

    // Getters

    public int getId() {
        return id;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    /**
     * @return La note entre 1 et 5 étoiles
     */
    public int getNote() {
        return note;
    }

    /**
     * @return Une nouvelle instance de Date pour éviter les modifications externes
     */
    public Date getDate() {
        return new Date(date.getTime()); // Défensive copy
    }

    // Setter

    /**
     * Modifie la note de l'avis
     * @param note Nouvelle note (entre 1 et 5)
     * @throws IllegalArgumentException Si la note est invalide
     */
    public void setNote(int note) {
        if (note < 1 || note > 5) {
            throw new IllegalArgumentException("La note doit être entre 1 et 5");
        }
        this.note = note;
    }

    // Méthode utilitaire

    /**
     * Convertit la note en étoiles pour l'affichage
     * @return Chaîne représentant les étoiles (ex: "★★★☆☆")
     */
    public String toStarRating() {
        return "★".repeat(note) + "☆".repeat(5 - note);
    }
}