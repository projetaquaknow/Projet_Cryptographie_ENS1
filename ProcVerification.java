/**
 * ProcVerification.java
 * 
 * Version mise a jour le 9 janvier 2015
 * @author David Carmona-Moreno
 * version 1.0
 */


package Verification;

import Signature.Signer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import org.apache.commons.codec.binary.Base64;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Classe qui permet de verifier qu'un document a ete signe
 * par un utilisateur se trouvant bien dans le Keystore
 */
public class ProcVerification {
    
    // L'objet charge du calcul de la signature
    private Signature signer;
    
    // Objet de recuperation du tag et du DN
    private Regex rex;
    
    /**
     * Constructeur de la classe
     * @throws java.security.NoSuchAlgorithmException si l'algorithme specifie 
     * est introuvable dans l'environnement du projet
     * @throws GeneralSecurityException si la creation echoue
     * @throws FileNotFoundException si le fichier specifie est introuvable
     */
    public ProcVerification() throws NoSuchAlgorithmException,GeneralSecurityException, FileNotFoundException{
        
        // Construction d'une instance du signataire de fichiers.
        this.signer=Signature.getInstance("SHA1withRSA");
        rex=new Regex();
    }
    
    /**
     * Verification de la signature d'un fichier
     * @param file File le fichier à verifier
     * @param publicKey La cle publique initialisant la verification
     * @param tagB64 L'encodage en Base64 de la signature a verifier
     * @return <code>true</code> Si la signature est correcte et <code>false</code> sinon
     * @throws GeneralSecurityException  Si la verification de la signature echoue
     * @throws IOException Si la lecture du fichier echoue
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
        
        // boucle de lecture pour la verification de la signature
        while((nl = in.read(buffer)) != -1)
            // remise a jour de l'objet signant avec les octets lus
            signer.update(buffer, 0, nl);
        return signer.verify(Base64.decodeBase64(tagB64));
    }

    /**
     * Verification de la signature d'un fichier
     * @param file Le nom du fichier a verifier
     * @param publicKey La clée publique initialisant la verification
     * @param tagB64 L'encodage en Base64 de la signature a verifier
     * @return <code>true</code> Si la signature est correcte et <code>false</code> sinon
     * @throws GeneralSecurityException  Si la verification de la signature echoue
     * @throws IOException Si la lecture du fichier echoue
     */
    public boolean verifyFile(String fileName, PublicKey publicKey, String tagB64)
            throws GeneralSecurityException, IOException {
        return verifyFile(new File(fileName), publicKey, tagB64);
    }
    
    /**
     * Extraction du DN se trouvant dans le fichier filesignature_DN
     * @param filename Le nom du fichier contenant le DN
     * @throws IOException Si la lecture du fichier echoue
     * @return Le DN se trouvant dans le fichier filesignature_DN
     */
    public String extractDNfromFile(String filename) throws IOException{
        
        // Le DN de l'utilisateur
        String dn;
        
        // Flux d'entree ds donnees contenues dans un fichier
        FileInputStream fileinput=new FileInputStream(new File(filename));
             
        // Buffer de lecture
        BufferedReader in=new BufferedReader(new FileReader(filename));
             
        // Lecture de toute une ligne
        dn=in.readLine();
             
        in.close();
        
        return dn;
        
    }
    
    
}
