package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Modele.Utilisateur;
import DAO.ChercherUtilisateur;

/**
 * Vue pour la connexion des utilisateurs
 * Gère l'authentification et la redirection vers la création de compte
 */

public class VueConnexion extends JFrame {
    //Composants graphiques
    private JTextField emailField;
    private JPasswordField motDePasseField;

    /**
     * Constructeur initialisant l'interface de connexion
     */

    public VueConnexion() {
        //Configuration de la fenêtre
        setTitle("Connexion");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Ferme l'application si fenêtre fermée
        setLocationRelativeTo(null); // centre la fenêtre

        //Création du panel principal avec GridLayout
        JPanel panel = new JPanel(new GridLayout(3, 2));

        //Champ email
        panel.add(new JLabel("Email :"));
        emailField = new JTextField();
        panel.add(emailField);

        //Champ de mot de passe
        panel.add(new JLabel("Mot de passe :"));
        motDePasseField = new JPasswordField();
        panel.add(motDePasseField);

        //Boutons
        JButton seConnecterButton = new JButton("Se connecter");
        JButton creerCompteButton = new JButton("Créer un compte");


        panel.add(seConnecterButton);
        panel.add(creerCompteButton);


        add(panel);

        //gestion des évènements

        //redirection vers la création de compte
        creerCompteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VueCreationCompte(); //ouvre la vue de création de compte
                dispose(); //ferme cette fenêtre
            }
        });

        //tentative de connexion
        seConnecterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String motDePasse = new String(motDePasseField.getPassword());
                System.out.println("Tentative de connexion : " + email + " / " + motDePasse);
                Utilisateur utilisateur = ChercherUtilisateur.ChercherUtilisateur(email, motDePasse);
                new VueAccueil(utilisateur);
                dispose();
            }
        });
        setVisible(true);
    }
}
