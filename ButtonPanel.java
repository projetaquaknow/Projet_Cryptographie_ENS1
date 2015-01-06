/*
 * ButtonPanel.java
 * 
 * Verson mise � jour le 21 D�cembre 2014
 * 
 * @author Maithili Vinayagamoorthi
 * @version 1.1
 */

package GUI;

import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;


/**
 * ButtonPanel
 * 
 * Dans la classe ButtonPanel on impl�mente 
 * les boutons "Signature" et "V�rification".
 */
@SuppressWarnings("serial")
class ButtonPanel extends JPanel implements ActionListener{
	
	private final JButton BoutonSignature;
	private final JButton BoutonVerification;
	
	/**
	 * Constructeur de la classe ButtonPanel
	 */
	public ButtonPanel() 
	{
	    BoutonSignature = new JButton("Sign");
	    BoutonVerification = new JButton("Verify");
	    
	    // Insertion des 2 boutons dans l'objet ButtonPanel
	    add(BoutonSignature);
	    add(BoutonVerification);
	    
	    // Les sources d'�v�nements sont d�clar�es � l'�couteur
	    BoutonSignature.addActionListener(this);
	    BoutonVerification.addActionListener(this);
	}
	
	/**
	 * Si on clique sur le bouton "Signature",
	 * et qu'un fichier est choisi alors la fenêtre 
         * de signature s'affiche.  
	 * Si on clique sur le bouton "Vérification",
         * et qu'un fichier est choisi alors la fenêtre 
         * de vérification s'affiche.
         * Si un fichier n'est pas choisi alors
         * une fenêtre d'erreur s'ouvre
	 * 
	 * @param evt     Lecture de l'�v�nement souris
	 * @author        Maithili Vinayagamoorthi
	 * @version       1.1
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
                    /**
                     * A modifier s'il n'y a pas de fichier à signer
                     */
                    //Logger.getLogger(ButtonPanel.class.getName()).log(Level.SEVERE, null, ex);
                    final JPanel panel = new JPanel();
                    JOptionPane.showMessageDialog(panel, "Choose A File!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            if(source==BoutonVerification){
               Verification verif = new Verification();
            }
	}
}
