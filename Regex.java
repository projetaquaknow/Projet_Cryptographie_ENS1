/**
 * PubKeyVerification.java
 * 
 * Version mise a jour le 9 janvier 2015
 * @author Cathie Prigent
 * version 1.0
 */


package Verification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Classe qui fournit les methodes necessaires a l'extraction du tag signature et du DN du fichier signature via des expressions regulieres 
 */
public class Regex {
       
    File file;
    FileInputStream fis;
    BufferedReader in;
    String inputLine;    
    StringBuffer sb;
    
    /**
     * Constructeur de la classe
     * @throws FileNotFoundException Si le fichier specifie est introuvable
     */
    public Regex() throws FileNotFoundException{
        file=new File("filesignature.txt");
        fis = new FileInputStream(file);
        in = new BufferedReader(new InputStreamReader(fis));
        sb = new StringBuffer();
    }
    
    /**
     * Methode de recuperation du tag de signature
     * @return Le tag de signature du fichier signature
     * @throws IOException Si la lecture du fichier signature echoue
     */ 
    public String get_tag() throws IOException{
        while((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        
        Pattern p1 = Pattern.compile("^(.*)==");
        Matcher m1 = p1.matcher(sb.toString());
        String match_tag=m1.toString()+"==";
        System.out.println(match_tag);
        return match_tag;
    }
    
    /**
     * Methode de recuperation du DN
     * @return Le DN du fichier signature
     */
    public String get_DN(){
        
        Pattern p2 = Pattern.compile("C=(.*)[A-Z]");
        Matcher m2 = p2.matcher(sb.toString());
        String match_DN="C="+m2.toString();
        System.out.println(match_DN);
        return match_DN;
    
    }
    
}
