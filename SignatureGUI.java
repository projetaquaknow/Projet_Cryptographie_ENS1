/*
 * SignatureGUI.java
 *
 * Version mise à jour le 22 Décembre 2014
 * 
 * @author Cathie Prigent
 * @version 1.1
 */


package GUI;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * SignatureGUI
 * 
 * La classe SignatureGUI permet d'afficher
 * une fenetre qui montre le nom du fichier,
 * d'entrer le nom du signataire, le
 * prénom du signataire, le mot
 * de passe secondaire et un bouton "Signer".
 * 
 */
public class SignatureGUI {
	
	/**
	  * Affiche la fenetre décrite 
	  * précédemment
	  * 
	  * @author         Cathie Prigent
	  */
	public static void getSignatureGUI() {
		JFrame frame = new JFrame("Signature");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contentPane = frame.getContentPane();
	    contentPane.setLayout(new GridLayout(4,2));
	    
	    contentPane.add(new JLabel("File name : "));
	    contentPane.add(new JLabel("Appropriate_FileName"));
	    
	    contentPane.add(new JLabel("Nom du signataire : "));
	    contentPane.add(new JLabel("Nom"));
	    
	    contentPane.add(new JLabel("Prénom du signataire : "));
	    contentPane.add(new JLabel("Prénom"));
	    
	    contentPane.add(new JLabel("Mot de passe : "));
	    contentPane.add(new JTextField(25));
	    
	    contentPane.add(new JButton("Signer"));
	    
	    frame.pack();
	    frame.setVisible(true);
	}
	
}

