/*
 * SignatureGUI.java
 *
 * Version mise à jour le 22 Décembre 2014
 * 
 * @author Cathie Prigent
 * @version 1.1
 */

package GUI;

import Signature.Searcher;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
	
    // Bouton signer
    private JButton mybutton;
    
    // Case que l'utilisateur doit remplir avec son nom
    private JTextField surname;
    
    // Case que l'utilisateur doit remplir avec son prénom
    private JTextField name;
    
    // Case que l'utilisateur doit remplir avec son mot de passe
    private JTextField passwd;
       
	/**
	  * Affiche la fenetre décrite 
	  * pr�écédemment
	  * 
	  * @author         Cathie Prigent
          * @throws java.io.IOException
	  */
     public void getSignatureGUI() throws IOException {
    	 
         JFrame frame = new JFrame("Sign Chosen Document");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
         Container contentPane = frame.getContentPane();
         
         contentPane.setLayout(new GridLayout(4,2));
         
         // Permet de récupérer le chemin du fichier choisi par l'utilisateur
         String m=this.Read_SignatureGUI("path.txt");
         
         // Instance du bouton signer
         mybutton= new JButton("Sign");
         
         // Instance de la case Prénom et Nom
         name= new JTextField();
         
         // Instance de la case Mot de Passe
         passwd= new JTextField();
         
         
         contentPane.add(new JLabel("File name : "));
         contentPane.add(new JLabel(m));
         
         //this.getSurname(surname);
         
         contentPane.add(new JLabel("Username : "));
         contentPane.add(name);
         
         //this.getName(name);
         
         contentPane.add(new JLabel("Password : "));
         contentPane.add(passwd);
         
         //this.getPassword(passwd);
         
         contentPane.add(mybutton);
         
         frame.pack();
         frame.setVisible(true);
         
         //Ajouter un écouteur d'événements au bouton
         mybutton.addActionListener(new ActionListener() {
	    	 
	         @Override
	         public void actionPerformed(ActionEvent evt) {
                     try {
                         buttonActionPerformed(evt);
                     } catch (KeyStoreException ex) {
                         Logger.getLogger(SignatureGUI.class.getName()).log(Level.SEVERE, null, ex);
                     } catch (NoSuchAlgorithmException ex) {
                         Logger.getLogger(SignatureGUI.class.getName()).log(Level.SEVERE, null, ex);
                     } catch (CertificateException ex) {
                         Logger.getLogger(SignatureGUI.class.getName()).log(Level.SEVERE, null, ex);
                     } catch (IOException ex) {
                         Logger.getLogger(SignatureGUI.class.getName()).log(Level.SEVERE, null, ex);
                     } catch (UnrecoverableKeyException ex) {
                         Logger.getLogger(SignatureGUI.class.getName()).log(Level.SEVERE, null, ex);
                     }
	         }
	     
          });
     
     }
     
    /**
     * Méthode permettant de lire le contenu du fichier contenant le chemin du fichier choisi par l'utilisateur
     * @param file Le nom du fichier contenant le chemin du fichier choisi par l'utilisateur
     * @return  Le chemin du fichier
     * @throws java.io.FileNotFoundException
     */
    public String Read_SignatureGUI(String file) throws FileNotFoundException, IOException{
        // Flux d'entrée ds données contenues dans un fichier
             FileInputStream fileinput=new FileInputStream(new File("path.txt"));
             
             String readline;
        // Lecture de toute une ligne
        try ( // Buffer de lecture
                BufferedReader in = new BufferedReader(new FileReader("path.txt"))) {
            // Lecture de toute une ligne
            readline = in.readLine();
        }
             
             return readline;
    }
    
    /**
     * On lance la procédure de signature de fichier lorsq'on clique sur le bouton "Signer"
     * @param evt Evénement souris
     * @throws java.security.KeyStoreException
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.cert.CertificateException
     * @throws java.io.IOException
     * @throws java.security.UnrecoverableKeyException
     */
    public void buttonActionPerformed(ActionEvent evt) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException{
       
        // Instance de la classe qui sert à vérifier si les données introduites
        // existent dans le Keystore
        Searcher search=new Searcher();
        
        // Si le nom,le prénom et le mot de passe introduits apparaissent bien
        // dans le Keystore alors on peut effectuer la signature du document
        if(search.verify(name.getText(),passwd.getText())==true){
            
            // Récupérer l'alias associé au nom introduit par l'utilisateur
            String alias=search.getAlias();
            
            // Récupérer l'alias associé au certificat utilisateur
            String alias_certificate=search.getCertificateAlias();
            
            // Instance de la classe qui réalise la procédure de signature
            Signature.SignerTest ecsignertest=new Signature.SignerTest(passwd.getText(),alias,alias_certificate);
            
            // Affiche un message pop-up pour confirmer la signature du fichier
            JFrame parent = new JFrame();
            JOptionPane.showMessageDialog(parent, "File Signed");
            
        }else {
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Data Error Entry!", "Error", JOptionPane.ERROR_MESSAGE);
       }
    }
}
