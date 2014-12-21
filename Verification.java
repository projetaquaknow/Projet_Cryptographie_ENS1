/*
 * Verification.java
 *
 * Version mise à jour le 21 Décembre 2014
 * 
 * @author Clément Chambat
 * @version 1.1
 */

package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Verification
 * 
 * La classe Verification permet d'afficher
 * une fenetre qui montre le Distinguished Name
 * du signataire et le nom du fichier vérifié.
 */
public class Verification extends JFrame 
{
	
	private JLabel label1 = new JLabel("DN: ");
    private JLabel label2 = new JLabel("Distinguished_Name");
    private JLabel label3 = new JLabel("Nom Fichier: ");
    private JLabel label4 = new JLabel("NOM_FICHIER");
   
    /**
     * Constucteur de la classe Verification
     */
    public Verification()
    {
    	
       this.setTitle("Vérification");
       this.setSize(400, 100);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.setLocationRelativeTo(null);
       
       JPanel container = new JPanel();
       container.setLayout(new GridLayout(2,2));
       container.add(label1);
       container.add(label2);
       container.add(label3);
       container.add(label4);
    
       this.setContentPane(container);
       this.setVisible(true);
       
    }
    
}


