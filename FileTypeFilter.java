/*
 * FileTypeFilter.java
 *
 * Version mise � jour le 21 D�cembre 2014
 * 
 * @author Maithili Vinayagamoorthi
 * @version 1.1
 */

package GUI;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * FileTypeFilter   
 * 
 * Filtrage des fichiers en fonction
 * de leur extension.
 */
public class FileTypeFilter extends FileFilter {

	private String extension;
	private String description;
	
	/**
	 * Constructeur de la classe FileTypeFilter
	 */
	public FileTypeFilter(String extension, String description) 
	{
	    this.extension = extension;
	    this.description = description;
	}
	
	/**
	 * Fonction qui filtre les fichiers en fonction
	 * de leur extension
	 * 
	 * @param file      Fichier
	 * @author          Maithili Vinayagamoorthi
	 * 
	 */
	@Override
	public boolean accept(File file) 
	{
	     if (file.isDirectory()) 
	     {
	         return true;
	     }
	     
	     return file.getName().toLowerCase().endsWith(extension);
	}
	
	/**
	 * Donne l'extension du fichier s�l�ctionn�
	 * 
	 * @author        Maithili Vinayagamoorthi  
	 */
	public String getDescription() 
	{
	     return description + String.format(" (*%s)", extension);
	}
}
