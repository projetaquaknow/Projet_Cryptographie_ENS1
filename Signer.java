/**
 * SignerTest.java
 * 
 * Version mise à jour le 9 Janvier 2015
 * 
 * @author David Carmona-Moreno
 * @author Maithili Vinayagaoorthi
 * @author Patrick Guichet
 * @version 1.0
 */

package Signature;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import org.apache.commons.codec.binary.Base64;

/**
 * La classe Signer permet de signer
 * des documents avec l'algorithme RSA
 */
public class Signer {
    
    // Obtention d'une instance du service SecureRandom qui génère 
    // une suite de bits cryptographiquement surs.
    private static final SecureRandom RAND = new SecureRandom();
    
    /**
     * Classe interne permettant de générer des clés
     * DSA
     */
    public static class PairGenerator{
        
        // Générateur de paires de clés
         private KeyPairGenerator kpg;
         
         /**
          * Constructeur de la classe PairGenerator
          * @param algorithm Le nom officiel de l'algorithme utilisé
          * @throws GeneralSecurityException On génère cette exception si le constructeur de Generator échoue
          */
         public PairGenerator(String algorithm) throws GeneralSecurityException {
            
            // Construction d'une instance d'un générateur de paires de clés.
            // L'algorithme utilisé est le RSA.
            this.kpg = KeyPairGenerator.getInstance(algorithm);
            
            //On initialise le générateur de paires de clés à 2048 bits
            kpg.initialize(2048);
        }
         
         /**
         * Génération de la paire de clés pour l'algorithme RSA
         * @return La paire de clés RSA générée.
         */
         public KeyPair getPair() {
             return kpg.generateKeyPair();
        }
         
         
    }
    
    // L'objet chargé du calcul de la signature
    private final Signature signer;
    
     /**
     * Constructeur de la classe Signer.
     * @param algorithm L'algorithme implémenté
     * @throws GeneralSecurityException On génère cette exception si le constructeur de Signer échoue
     */
    public Signer(String algorithm) throws GeneralSecurityException {
        
        // Construction d'une instance du signataire de fichiers.
        this.signer = Signature.getInstance(algorithm);
    }
    
     /**
     * Calcul de la signature d'un fichier
     * @param file Le fichier à signer
     * @param privateKey La clé privée pour initialiser la signature
     * @return La signature sous forme encodée en base64
     * @throws GeneralSecurityException Si le calcul de la signature échoue
     * @throws IOException Si la lecture du fichier échoue
     */
    public String signFile(File file, PrivateKey privateKey)
            throws GeneralSecurityException, IOException {
        signer.initSign(privateKey);
        
        // le flot entrant
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        
        // le buffer de lecture
        byte[] buffer = new byte[1024];//J'ai changé de 1024 à 2048 bits
        
        // le nombre d'octets lus
        int nl;
        
        // boucle de lecture pour le calcul de la signature
        while((nl = in.read(buffer)) != -1)
            // remise à jour de l'objet signant avec les octets lus
            signer.update(buffer, 0, nl);
        return Base64.encodeBase64String(signer.sign());
    }
    
    /**
     * Calcul de la signature d'un fichier
     * @param fileName Le nom du fichier à signer
     * @param privateKey La clé privée pour initialiser la signature
     * @return La signature sous forme encodée en base64
     * @throws GeneralSecurityException Si le calcul de la signature échoue
     * @throws IOException Si la lecture du fichier échoue
     */
    public String signFile(String fileName, PrivateKey privateKey)
            throws GeneralSecurityException, IOException {
        
        // Variable qui permet de récupérer la signature du fichier
        String sgfile=signFile(new File(fileName), privateKey);
        
        // Appel à la méthode permettant d'écrire dans un fichier la signature obtenue
        Signer.WriteSignature(sgfile);
        
        return signFile(new File(fileName), privateKey);
    }
    
        /**
     * Vérification de la signature d'un fichier
     * @param file File le fichier à vérifier
     * @param publicKey La clé publique initialisant la vérification
     * @param tagB64 L'encodage en Base64 de la signature à vérifier
     * @return <code>true</code> Si la signature est correcte et <code>false</code> sinon
     * @throws GeneralSecurityException  Si la vérification de la signature échoue
     * @throws IOException Si la lecture du fichier échoue
     */
    public boolean verifyFile(File file, PublicKey publicKey, String tagB64)
            throws GeneralSecurityException, IOException {
        signer.initVerify(publicKey);
        
        // le flot entrant
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        
        // le buffer de lecture
        byte[] buffer = new byte[1024];//J'ai changé de 1024 à 2048
        
        // le nombre d'octets lus
        int nl;
        
        // boucle de lecture pour la vérification de la signature
        while((nl = in.read(buffer)) != -1)
            // remise à jour de l'objet signant avec les octets lus
            signer.update(buffer, 0, nl);
        return signer.verify(Base64.decodeBase64(tagB64));
    }

    /**
     * Vérification de la signature d'un fichier
     * @param fileName Le nom du fichier à vérifier
     * @param publicKey La clé publique initialisant la vérification
     * @param tagB64 L'encodage en Base64 de la signature à vérifier
     * @return <code>true</code> Si la signature est correcte et <code>false</code> sinon
     * @throws GeneralSecurityException  Si la vérification de la signature échoue
     * @throws IOException Si la lecture du fichier échoue
     */
    public boolean verifyFile(String fileName, PublicKey publicKey, String tagB64)
            throws GeneralSecurityException, IOException {
        return verifyFile(new File(fileName), publicKey, tagB64);
    }
    
    /**
    * Lecture du fichier contenant le chemin du fichier que l'on souhaite signer
    * @param myfile Le nom du fichier contenant le chemin du fichier cible
    * @return Le chemin vers le fichier à signer
    * @throws java.io.FileNotFoundException Si le fichier n'existe pas
    */
    public static String Read_Signer(String myfile) throws FileNotFoundException, IOException{
        
        // Buffer de lecture
        BufferedReader in=new BufferedReader(new FileReader(myfile));
             
        // Lecture de toute une ligne
        String readline=in.readLine();
             
        in.close();
             
        return readline;
    }
    
    /**
     * On génère un fichier texte(.txt) contenant la signature 
     * du fichier choisi par l'utilisateur
     * @param sig La signature du fichier
     * @throws java.io.FileNotFoundException Si le fichier n'existe pas
     * @throws java.io.UnsupportedEncodingException Si l'encodage des caractères n'est pas supporté
     */
    public static void WriteSignature(String sig) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        
        // Flux de sortie récupéré dans un fichier
        FileOutputStream fileoutput=new FileOutputStream(new File("filesignature.txt"));
             
        // Conversion du paramètre path_buffer en un tableau de byte
        byte[] b=sig.getBytes("UTF-8");
             
        // Ecriture du tableau de byte dans le fichier
        fileoutput.write(b);
                
        // Fermeture du flux
        fileoutput.close();
        
    } 
}

    

