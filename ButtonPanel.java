/*
 * ButtonPanel.java
 * 
 * Verson mise à jour le 21 Décembre 2014
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
 * Dans la classe ButtonPanel on implémente 
 * les boutons "Signature" et "Vérification".
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
	    
	    // Les sources d'événements sont déclarées à l'écouteur
	    BoutonSignature.addActionListener(this);
	    BoutonVerification.addActionListener(this);
	}
	
	/**
	 * Si on clique sur le bouton "Signature",
	 * la couleur de fond du panneau contenant
	 * les boutons "Signature" et "Vérification"
	 * devient bleu.
	 * Si on clique sur le bouton "Vérification",
	 * la couleur de fond du panneau contenant
	 * les boutons "Signature" et "Vérification" 
	 * devient rouge.
	 * 
	 * @param evt     Lecture de l'événement souris
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
	}

}
