package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import DAO.AjoutArticle;

public class VueAjouterArticle extends JFrame {
    private JTextField nomField, marqueField, prixField, prix_vracField, quantite_vracField, quantiteField;
    private int noteField;

    public VueAjouterArticle() {
        setTitle("Ajouter un article");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        panel.add(new JLabel("Nom :"));
        nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Marque :"));
        marqueField = new JTextField();
        panel.add(marqueField);

        panel.add(new JLabel("Prix :"));
        prixField = new JTextField();
        panel.add(prixField);

        panel.add(new JLabel("Prix en vrac :"));
        prix_vracField = new JTextField();
        panel.add(prix_vracField);

        panel.add(new JLabel("Quantite en vrac :"));
        quantite_vracField = new JTextField();
        panel.add(quantite_vracField);

        panel.add(new JLabel("Quantite :"));
        quantiteField = new JTextField();
        panel.add(quantiteField);

        noteField = 0;
        panel.add(quantite_vracField);

        JButton creerButton = new JButton("Ajouter un article");
        panel.add(creerButton);

        add(panel);

        creerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText();
                String marque = marqueField.getText();
                int prix = Integer.parseInt(prixField.getText());
                int prix_vrac = Integer.parseInt(prix_vracField.getText());
                int quantite_vrac = Integer.parseInt(quantite_vracField.getText());
                int quantite = Integer.parseInt(quantiteField.getText());
                int note = noteField;
                AjoutArticle.AjouterArticle(nom, marque, prix, prix_vrac, quantite_vrac, quantite, note);
            }
        });
        setVisible(true);
    }
}
