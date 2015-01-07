/*
 * FileTypeFilter.java
 *
 * Version mise à jour le 21 Décembre 2014
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

	private final String extension;
	private final String description;
	
	/**
	 * Constructeur de la classe FileTypeFilter
         * @param extension
         * @param description
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
         * @return 
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
	 * @author   Maithili Vinayagamoorthi  
         * @return   String: Description du fichier
	 */
        @Override
	public String getDescription() 
	{
	     return description + String.format(" (*%s)", extension);
	}
}
