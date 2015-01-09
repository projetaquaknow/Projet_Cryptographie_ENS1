/**
 * ButtonPanel.java
 * 
 * Verson mise a jour le 9 Janvier 2015
 * 
 * @author Maithili Vinayagamoorthi
 * @author David Carmona-Moreno
 * @version 1.0
 */

package GUI;

import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import Verification.ProcVerificationTest;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * ButtonPanel
 * 
 * Dans la classe ButtonPanel on implemente 
 * les boutons "Signature" et "Verification".
 */
@SuppressWarnings("serial")
class ButtonPanel extends JPanel implements ActionListener{
	
    private final JButton BoutonSignature;
    private final JButton BoutonVerification;
	
    /**
     * Constructeur de la classe ButtonPanel
     * @author   Maithili Vinayagamoorthi
     */
    public ButtonPanel() 
    {
        BoutonSignature = new JButton("Sign");
        BoutonVerification = new JButton("Verify");
	    
        // Insertion des 2 boutons dans l'objet ButtonPanel
        add(BoutonSignature);
        add(BoutonVerification);
	    
        // Les sources d'evenements sont declarees a l'ecouteur
        BoutonSignature.addActionListener(this);
        BoutonVerification.addActionListener(this);
    }
	
    /**
    * Si on clique sur le bouton "Signature",
    * et qu'un fichier est choisi alors la fenetre 
    * de signature s'affiche.  
    * Si on clique sur le bouton "Verification",
    * et qu'un fichier est choisi alors la fenetre 
    * de verification s'affiche.
    * Si un fichier n'est pas choisi alors
    * une fenetre d'erreur s'ouvre
    * 
    * @param    evt Lecture de l'evenement souris
    * @author   Maithili Vinayagamoorthi
    * @author   David Carmona-Moreno
    * 
    */
    @Override
    public void actionPerformed(ActionEvent evt) 	
    {
	Object source = evt.getSource();
            
        if(source==BoutonSignature){
        SignatureGUI signaturegui=new SignatureGUI();
        try {
            signaturegui.getSignatureGUI();
        } catch (IOException ex) {                    
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Choose A File!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        }
            
        if(source==BoutonVerification){
               
            try {
                try {
                    ProcVerificationTest verif=new ProcVerificationTest();
                } catch (NoSuchAlgorithmException | IOException ex) {
                final JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "Choose a File!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (GeneralSecurityException ex) {
                final JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "Choose a File!", "Error", JOptionPane.ERROR_MESSAGE);
            }
                
        }
    }
}
