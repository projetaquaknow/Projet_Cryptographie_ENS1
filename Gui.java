/**
 * Gui.java
 * 
 * Verson mise a jour le 9 Janvier 2015
 * 
 * @author Maithili Vinayagamoorthi
 * @author Clément Chambat
 * @author Cathie Prigent
 * @author David Carmona-Moreno
 * @version 1.0
 */

package GUI;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 * Gui
 * 
 * Dans la classe Gui on instancie tous 
 * les objets necessaires à l'affichage
 * d'une interface graphique. 
 * Une fenetre : un explorateur de fichiers,
 * les boutons "Signature" et "Verification".
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
