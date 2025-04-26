package Controleur;

import Vue.VueAccueil;

import javax.swing.*;

public class Controleur {

    /**
     * Méthode main pour tester la vue indépendamment
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VueAccueil(null));
    }
}
