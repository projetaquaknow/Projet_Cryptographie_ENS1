package CryptoSigVerif;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
class ButtonPanel extends JPanel implements ActionListener    // interface écouteur d'événements
{ 
	private JButton BoutonSignature;
	private JButton BoutonVerification;

   public ButtonPanel() // constructeur de la classe ButtonPanel
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
   

   public void actionPerformed(ActionEvent evt) // Permet de traiter l'événement en fonction de l'objet source
   {
	   Object source = evt.getSource();
	   Color color = getBackground();
	   
	   if (source == BoutonSignature) color = Color.blue;
	   else if (source == BoutonVerification) color = Color.red;
	   setBackground(color);
	   repaint();
   }

}