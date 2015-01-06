/*
 * FileBrowser.java
 *
 * Version mise à jour le 21 Décembre 2014
 * 
 * @author Maithili Vinayagamoorthi
 * @version 1.1
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
 * Dans la classe FileBrowser on créé une fenetre
 * et on y ajoute l'explorateur de fichiers et 
 * les deux boutons "Signature" et "V�rification".
 */
@SuppressWarnings("serial")
public final class FileBrowser extends JFrame {
	
	/**
	 * Constructeur de la classe FileBrowser
     * @throws java.io.IOException
	 */
	public FileBrowser() throws IOException {
                super("Cryptography Project");
		setLayout(new FlowLayout());
		
		// Instance de l'explorateur de fichiers
		JFilePicker filePicker = new JFilePicker("File", "Find...");
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
                
                //Récupérer le chemin du fichier
                //String path_main=filePicker.getPath();
                //System.out.println(path_main);
                		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.delete_path_file();
		setSize(520, 200);
		setLocationRelativeTo(null); // center on screen    
        }
        
        /**
         * Supprime le fichier "path.txt"
         */
        public void delete_path_file(){
            File myfile=new File("path.txt");
            myfile.delete();        
        }
}
