/*
 * Gui.java
 * 
 * Verson mise � jour le 21 D�cembre 2014
 * 
 * @author Maithili Vinayagamoorthi
 * @author Clément Chambat
 * @author Cathie Prigent
 * @author David Carmona-Moreno
 * @version 1.1
 */

package GUI;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * Gui
 * 
 * Dans la classe Gui on intancie tous 
 * les objets n�cessaires � l'affichage
 * d'une interface graphique. 
 * Une fenetre, un explorateur de fichiers,
 * les boutons "Signature" et "V�rification", 
 * une fenetre affichant le DN et
 * le nom du fichier et une autre fenetre 
 * pour la signature du fichier.
 */
public class Gui {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
	         @Override
	         public void run() {
                     try {
                         new FileBrowser().setVisible(true);
                     } catch (IOException ex) {
                         Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
                     }
	         }
                 
	         
	         });
	  }

}
