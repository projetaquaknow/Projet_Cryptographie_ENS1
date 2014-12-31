/*
 * SignatureGUI.java
 *
 * Version mise � jour le 22 D�cembre 2014
 * 
 * @author Cathie Prigent
 * @version 1.1
 */

package GUI;

import Test_Cryptographie.CA;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
 * pr�nom du signataire, le mot
 * de passe secondaire et un bouton "Signer".
 * 
 */
public class SignatureGUI {
	
	/**
	  * Affiche la fenetre d�crite 
	  * pr�c�demment
	  * 
	  * @author         Cathie Prigent
	  */
     public void getSignatureGUI() throws IOException {
    	 
         JFrame frame = new JFrame("Signature");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
         Container contentPane = frame.getContentPane();
         
         contentPane.setLayout(new GridLayout(4,2));
         
         // Permet de récupérer le chemin du fichier choisi par l'utilisateur
         String m=this.Read_SignatureGUI("path.txt");
         
         contentPane.add(new JLabel("File name : "));
         contentPane.add(new JLabel(m));
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
     
    /**
     * Méthode permettant de lire le contenu du fichier contenant le chemin du fichier choisi par l'utilisateur
     * @param file Le nom du fichier contenant le chemin du fichier choisi par l'utilisateur
     * @return  Le chemin du fichier
     */
    public String Read_SignatureGUI(String file) throws FileNotFoundException, IOException{
        // Flux d'entrée ds données contenues dans un fichier
             FileInputStream fileinput=new FileInputStream(new File("path.txt"));
             
             // Buffer de lecture
             BufferedReader in=new BufferedReader(new FileReader("path.txt"));
             
             // Lecture de toute une ligne
             String readline=in.readLine();
             
             in.close();
             
             return readline;
    }

}

