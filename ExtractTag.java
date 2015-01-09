/**
 * ExtractTag.java
 * 
 * Version mise a jour le 9 janvier 2015
 * @author David Carmona-Moreno
 * version 1.0
 */


package Verification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe qui permet d'extraire la signature du document filesignature.txt
 */
public class ExtractTag {
    
    // Le nom du fichier contenant la signature
    private String filesignature;
    
    // Lecteur avec buffer
    BufferedReader bufferedreader;
    
    // Convertisseur d'une donnee en chaine de caracteres
    StringBuilder sb;
    
    
    /**
     * Constructeur de la classe
     * @param filesignaturetxt Le nom du fichier contenant la signature
     * @throws FileNotFoundException Si le fichier specifie est introuvable
     */
    public ExtractTag(String filesignature) throws FileNotFoundException {
        
        // Instance permettant de lire les donnees se trouvant dans le fichier filesignature
        bufferedreader=new BufferedReader(new FileReader(filesignature));
        
        // Instance permettant de convertir n'importe quelle donnee en chaine de caracteres
        sb=new StringBuilder();
        
        this.filesignature=filesignature;   
    }
    
    /**
     * Methode permettant d'extraire la signature du fichier
     * @return La signature du fichier sous forme de String
     * @throws IOException Si la lecture du fichier echoue
     */
    public String extraction() throws IOException {
        
        // Une ligne du fichier
        String line;
        
        // Parcourir toutes les lignes du fichier
        while((line=bufferedreader.readLine())!=null){
            sb.append(line);
        }
        return sb.toString();
    }
    
}
