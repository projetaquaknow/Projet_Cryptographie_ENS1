/*
 * JFilePicker.java
 *
 * Version mise � jour le 21 D�cembre 2014
 * 
 * @author Maithili Vinayagamoorthi
 * @version 1.1
 */

package GUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;


/**
 * JFilePicker
 * 
 * Dans la classe JFilePicker on impl�mente 
 * l'explorateur de fichiers.
 */
@SuppressWarnings("serial")
public class jFilePicker extends JPanel {

	 private JLabel label;
	 private JTextField textField;
	 private JButton button;
	 
	 private JFileChooser fileChooser;
	 private int mode;
         
	 public static final int MODE_OPEN = 1;
	 public static final int MODE_SAVE = 2;
	 
	 /**
	  * Constructeur de la classe jFilePicker
	  */
	 public jFilePicker(String textFieldLabel, String buttonLabel) {
             
             // Explorateur de fichiers
	     fileChooser = new JFileChooser();
	     setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	     
	     // creates the GUI
	     label = new JLabel(textFieldLabel);
	     textField = new JTextField(30);
	     button = new JButton(buttonLabel);
	     button.addActionListener(new ActionListener() {
	    	 
	         @Override
	         public void actionPerformed(ActionEvent evt) {
                     try {
                         buttonActionPerformed(evt);
                     } catch (FileNotFoundException ex) {
                         Logger.getLogger(jFilePicker.class.getName()).log(Level.SEVERE, null, ex);
                     } catch (IOException ex) {
                         Logger.getLogger(jFilePicker.class.getName()).log(Level.SEVERE, null, ex);
                     }
	         }
	     
	      });
	     
	    add(label);
	    add(textField);
	    add(button);
	 }
	 
	 /**
	  * Si on clique sur le bouton "Choisir
	  * un fichier"
	  * 
	  * @param evt      Ev�nement souris
	  * @author         Maithili Vinayagamoorthi
	  */
	 public void buttonActionPerformed(ActionEvent evt) throws FileNotFoundException, IOException {
            
	    if (mode == MODE_OPEN) {
	    	
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	
	            textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                   // System.out.printf("%s", fileChooser.getSelectedFile().toString());
                    String path_button;
                    path_button=fileChooser.getSelectedFile().toString();
                    
                    // On copie le chemin du fichier dans un fichier
                    this.Write(path_button);
                  
	        }
	        
            } 
	    
	     else if (mode == MODE_SAVE) {
	    	 
	           if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	   
	               textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                              
	           }
	    }
	}
	 
	 /**
	  * 
	  * @param extension      Extension du fichier
	  * @param description    Description du fichier
	  * @author               Maithili Vinayagamoorthi
	  */
	 public void addFileTypeFilter(String extension, String description) {
		 
	     FileFilter filter = new FileTypeFilter(extension, description);
	     fileChooser.addChoosableFileFilter(filter);
	 }
	 
	 /**
	  * 
	  * @param mode
	  */
	 public void setMode(int mode) {
	     this.mode = mode;
         }
        
         /**
          * Méthode qui fixe automatiquement le chemin du fichier choisi
          * @param path1 Le chemin du fichier tel qu'il est défini dans actionButtonListener
          */
         public String getPath(){
               return "Salut";
         }
         
         /**
          * Ecriture dans un fichier
          * @param path_buffer Le chemin du fichier
          */
         public void Write(String path_buffer) throws FileNotFoundException, UnsupportedEncodingException, IOException{
             
             // Flux de sortie récupéré dans un fichier
             FileOutputStream fileoutput=new FileOutputStream(new File("path.txt"));
             
             // Conversion du paramètre path_buffer en un tableau de byte
             byte[] b=path_buffer.getBytes("UTF-8");
             
             // Ecriture du tableau de byte dans le fichier
             fileoutput.write(b);
             
             // Fermeture du flux
             fileoutput.close();
         }
         
         /**
          * Lecture du fichier contenant le chemin du fichier
          * @param myfile Le nom du fichier contenant le chemin du fichier
          */
         public String Read(String myfile) throws FileNotFoundException, IOException{
             
             // Flux d'entrée ds données contenues dans un fichier
             FileInputStream fileinput=new FileInputStream(new File("path.txt"));
             
             // Buffer de lecture
             BufferedReader in=new BufferedReader(new FileReader("path.txt"));
             
             // Lecture de toute une ligne
             String readline=in.readLine();
             
             in.close();
             
             return readline;
         }
                 
         
         
}
