/*
 * JFilePicker.java
 *
 * Version mise à jour le 21 Décembre 2014
 * 
 * @author Maithili Vinayagamoorthi
 * @version 1.1
 */

package GUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;


/**
 * JFilePicker
 * 
 * Dans la classe JFilePicker on implémente 
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
		 
	     fileChooser = new JFileChooser();
	     setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
	     
	     // creates the GUI
	     label = new JLabel(textFieldLabel);
	     textField = new JTextField(30);
	     button = new JButton(buttonLabel);
	     button.addActionListener(new ActionListener() {
	    	 
	         @Override
	         public void actionPerformed(ActionEvent evt) {
	             buttonActionPerformed(evt);
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
	  * @param evt      Evénement souris
	  * @author         Maithili Vinayagamoorthi
	  */
	 private void buttonActionPerformed(ActionEvent evt) {
		 
	    if (mode == MODE_OPEN) {
	    	
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	        	
	            textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
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
	  * 
	  * @return textField.getText  Nom du fichier
	  * @author                    Maithili Vinayagamoorthi
	  */
	 public String getSelectedFilePath() {
	     return textField.getText();
	 }
	 
	 /**
	  * 
	  * @return this.filechooser   Explorateur de fichiers
	  * @author                    Maithili Vinayagamoorthi
	  */
	 public JFileChooser getFileChooser() {
	     return this.fileChooser;
	 }

}
