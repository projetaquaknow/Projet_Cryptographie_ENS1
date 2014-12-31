package Signature;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.Enumeration;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * La classe ECSigner permet de signer
 * des documents avec l'algorithme ECDSA
 * (Elliptic Curve Digital Signature Algorithm)
 * @author David Carmona-Moreno
 * @author Patrick Guichet
 */
public class ECSigner {
    
    // Installation du provider BouncyCastle.
    // Il fournit toutes les méthodes et classes
    // afin de mettre en place une signature de type
    // ECDSA.
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    // Obtention d'une instance du service SecureRandom qui génère 
    // une suite de bits cryptographiquement surs.
    private static final SecureRandom RAND = new SecureRandom();
    
    /**
     * Classe interne permettant de générer des clés
     * DSA
     */
    public static class ECKeyPairGenerator{
        
        // Générateur de paires de clés
         private KeyPairGenerator kpg;
         
         /**
          * Constructeur de la classe ECKeyPairGenerator
          * @param curveName Le nom officiel de la courbe elliptique utilisée
          * @throws GeneralSecurityException On génère cette exception si le constructeur de ECKeyPairGenerator échoue
          */
         public ECKeyPairGenerator(String curveName) throws GeneralSecurityException {
            
            // Construction d'une instance d'un générateur de paires de clés.
            // L'algorithme utilisé est le ECDSA.
            this.kpg = KeyPairGenerator.getInstance("ECDSA");
            
            //On initialise le générateur de paires de clés.
            kpg.initialize(new ECGenParameterSpec(curveName));
        }
         
         /**
         * Génération de la paire de clés pour l'algorithme ECDSA
         * @return La paire de clés ECDSA générée.
         */
         public KeyPair getECKeyPair() {
             return kpg.generateKeyPair();
        }
         
         /**
         * Méthode listant les courbes implémentées par le provider
         * @return Une liste des noms officiels des courbes elliptiques implémentées par le Provider
         */
         public static String getCurvesNames() {
             
             StringBuilder sb = new StringBuilder();
             
             // Boucle de parcours de toute la liste des courbes implémentées par 
             // BouncyCastle
             for(Enumeration<String> curves = ECNamedCurveTable.getNames(); curves.hasMoreElements();){
                 
                sb.append(curves.nextElement()).append('\n');
             }
             return sb.toString();
         }
    }
    
    // L'objet chargé du calcul de la signature
    private Signature signer;
    
     /**
     * Constructeur de la classe ECSigner.
     * @param algorithm L'algorithme implémenté
     * @throws GeneralSecurityException On génère cette exception si le constructeur de ECSigner échoue
     */
    public ECSigner(String algorithm) throws GeneralSecurityException {
        
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
        byte[] buffer = new byte[1024];
        
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
        byte[] buffer = new byte[1024];
        
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
     * @param file Le nom du fichier à vérifier
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
    * Lecture du fichier contenant le chemin du fichier
    * @param myfile Le nom du fichier contenant le chemin du fichier
    */
    public static String Read_ECSigner(String myfile) throws FileNotFoundException, IOException{
       
        // Flux d'entrée ds données contenues dans un fichier
        FileInputStream fileinput=new FileInputStream(new File(myfile));
             
        // Buffer de lecture
        BufferedReader in=new BufferedReader(new FileReader(myfile));
             
        // Lecture de toute une ligne
        String readline=in.readLine();
             
        in.close();
             
        return readline;
    }
}

    

