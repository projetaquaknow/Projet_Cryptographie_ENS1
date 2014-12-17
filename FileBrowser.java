package CryptoSigVerif;

import java.awt.Container;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class FileBrowser extends JFrame {
	public FileBrowser() {
	    super("CryptoTest");
	     
	    setLayout(new FlowLayout());
	
	    // set up a file picker component
	    jFilePicker filePicker = new jFilePicker("Fichier", "Parcourir...");
	    filePicker.setMode(jFilePicker.MODE_SAVE);
	    filePicker.addFileTypeFilter(".txt", "Text File");
	    filePicker.addFileTypeFilter(".doc", "Document File");
	    filePicker.addFileTypeFilter(".docx", "XML Format Document File");
	    filePicker.addFileTypeFilter(".odt", "Word Processing Document");
	    filePicker.addFileTypeFilter(".pdf", "Portable Document Format");
	     
	    // access JFileChooser class directly
	    JFileChooser fileChooser = filePicker.getFileChooser();
	    fileChooser.setCurrentDirectory(new File("D:/"));
	    
	    //MDP
	    Mdp mot_de_passe = new Mdp("Mot de passe");
	     		
	    // add the component to the frame
	    add(filePicker);
	    add(mot_de_passe);
	    Container contentPane = getContentPane();
		contentPane.add(new ButtonPanel());
	     
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setSize(520, 200);
	    setLocationRelativeTo(null);    // center on screen
	}
}