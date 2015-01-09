/**
 * FileBrowser.java
 *
 * Version mise a jour le 9 Janvier 2015
 * 
 * @author  Maithili Vinayagamoorthi
 * @author David Carmona-Moreno
 * @version 1.0
 */

package GUI;

import java.awt.Container;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;


/**
 * FileBrowser
 * 
 * Dans la classe FileBrowser on cree une fenetre
 * et on y ajoute l'explorateur de fichiers et 
 * les deux boutons "Signature" et "Verification".
 */
@SuppressWarnings("serial")
public final class FileBrowser extends JFrame {
	
    /**
     * Constructeur de la classe FileBrowser
     * @author  Maithili Vinayagamoorthi
     * @throws java.io.IOException
    */
    public FileBrowser() throws IOException 
    {
        super("Cryptography Project");
        setLayout(new FlowLayout());
		
	// Instance de l'explorateur de fichiers
	JFilePicker filePicker = new JFilePicker();
	filePicker.setMode(JFilePicker.MODE_OPEN);
	filePicker.addFileTypeFilter(".txt", "Text File");
	filePicker.addFileTypeFilter(".doc", "Document File");
	filePicker.addFileTypeFilter(".docx", "XML Format Document File");
	filePicker.addFileTypeFilter(".odt", "Word Processing Document");
	filePicker.addFileTypeFilter(".pdf", "Portable Document Format");
		
	// Ajouter le composant explorateur de fichiers dans la fenetre
	add(filePicker);
	Container contentPane = getContentPane();
	contentPane.add(new ButtonPanel());
              		
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.delete_path_file();
	setSize(520, 200);
	setLocationRelativeTo(null); // center on screen    
    }
        
    /**
     * Supprime le fichier "path.txt"
     * @author  Maithili Vinayagamoorthi
     * @author David Carmona-Moreno
     */
    public void delete_path_file()
    {
        File myfile=new File("path.txt");
        myfile.delete();        
    }
}
