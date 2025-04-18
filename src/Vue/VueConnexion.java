package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import DAO.ChercherUtilisateur;
import DAO.UtilisateurDAO;

public class VueConnexion extends JFrame {
    private JTextField emailField;
    private JPasswordField motDePasseField;

    public VueConnexion() {
        setTitle("Connexion");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centrer

        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Email :"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Mot de passe :"));
        motDePasseField = new JPasswordField();
        panel.add(motDePasseField);

        JButton seConnecterButton = new JButton("Se connecter");
        JButton creerCompteButton = new JButton("Créer un compte");

        /*-----------------------------------------------------------------*/
        JButton ajouterUnArticleTest = new JButton("Ajouter un article test");
        /*-----------------------------------------------------------------*/

        panel.add(seConnecterButton);
        panel.add(creerCompteButton);

        /*-----------------------------------------------------------------*/
        panel.add(ajouterUnArticleTest);
        /*-----------------------------------------------------------------*/

        add(panel);

        creerCompteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VueCreationCompte();
                dispose();
            }
        });

        /*-----------------------------------------------------------------*/
        ajouterUnArticleTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VueAjouterArticle();
                dispose();
            }
        });
        /*-----------------------------------------------------------------*/

        seConnecterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String motDePasse = new String(motDePasseField.getPassword());
                System.out.println("Tentative de connexion : " + email + " / " + motDePasse);
                ChercherUtilisateur.ChercherUtilisateur(email, motDePasse);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new VueConnexion();
    }
}
