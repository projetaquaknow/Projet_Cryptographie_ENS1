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

import javax.swing.JFileChooser;
import javax.swing.JFrame;


/**
 * FileBrowser
 * 
 * Dans la classe FileBrowser on créé une fenetre
 * et on y ajoute l'explorateur de fichiers et 
 * les deux boutons "Signature" et "V�rification".
 */
@SuppressWarnings("serial")
public class FileBrowser extends JFrame {
	
	/**
	 * Constructeur de la classe FileBrowser
	 */
	public FileBrowser() throws IOException {
            
		setLayout(new FlowLayout());
		
		// Instance de l'explorateur de fichiers
		jFilePicker filePicker = new jFilePicker("Fichier", "Parcourir...");
		filePicker.setMode(jFilePicker.MODE_OPEN);
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
		setSize(520, 200);
		setLocationRelativeTo(null); // center on screen    
        }
}
