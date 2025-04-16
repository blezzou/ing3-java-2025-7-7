package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import DAO.AjoutUtilisateur;

public class VueCreationCompte extends JFrame {
    private JTextField nomField, prenomField, emailField;
    private JPasswordField motDePasseField;

    public VueCreationCompte() {
        setTitle("Créer un compte");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        panel.add(new JLabel("Nom :"));
        nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Prénom :"));
        prenomField = new JTextField();
        panel.add(prenomField);

        panel.add(new JLabel("Email :"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Mot de passe :"));
        motDePasseField = new JPasswordField();
        panel.add(motDePasseField);

        JButton creerButton = new JButton("Créer le compte");
        panel.add(creerButton);

        add(panel);

        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String email = emailField.getText();
                String motDePasse = new String(motDePasseField.getPassword());
                AjoutUtilisateur.AjouterUtilisateur(0, nom, prenom, email, motDePasse, 0);
                //Crerr un new user
            }

        });



        setVisible(true);
    }
}
