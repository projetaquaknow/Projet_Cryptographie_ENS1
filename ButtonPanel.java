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
import java.awt.*;
import javax.swing.*;


/**
 * ButtonPanel
 * 
 * Dans la classe ButtonPanel on impl�mente 
 * les boutons "Signature" et "V�rification".
 */
@SuppressWarnings("serial")
class ButtonPanel extends JPanel implements ActionListener{
	
	private JButton BoutonSignature;
	private JButton BoutonVerification;
	
	/**
	 * Constructeur de la classe ButtonPanel
	 */
	public ButtonPanel() 
	{
	    BoutonSignature = new JButton("Signer");
	    BoutonVerification = new JButton("Verifier");
	    
	    // Insertion des 2 boutons dans l'objet ButtonPanel
	    add(BoutonSignature);
	    add(BoutonVerification);
	    
	    // Les sources d'�v�nements sont d�clar�es � l'�couteur
	    BoutonSignature.addActionListener(this);
	    BoutonVerification.addActionListener(this);
	}
	
	/**
	 * Si on clique sur le bouton "Signature",
	 * la couleur de fond du panneau contenant
	 * les boutons "Signature" et "V�rification"
	 * devient bleu.
	 * Si on clique sur le bouton "V�rification",
	 * la couleur de fond du panneau contenant
	 * les boutons "Signature" et "V�rification" 
	 * devient rouge.
	 * 
	 * @param evt     Lecture de l'�v�nement souris
	 * @author        Maithili Vinayagamoorthi
	 * @version       1.1
	 * 
	 */
	public void actionPerformed(ActionEvent evt) 	
	{
	    Object source = evt.getSource();
	    Color color = getBackground();
	    if (source == BoutonSignature) color = Color.blue;
	    else if (source == BoutonVerification) color = Color.red;
	    setBackground(color);
	    repaint();
            
            if(source==BoutonSignature){
               SignatureGUI signaturegui=new SignatureGUI();
	       signaturegui.getSignatureGUI();
            }
            
            if(source==BoutonVerification){
               Verification verif = new Verification();
            }
	}

}
