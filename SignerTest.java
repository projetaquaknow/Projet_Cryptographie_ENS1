/**
 * SignerTest.java
 * 
 * Version mise à jour le 9 Janvier 2015
 * 
 * @author David Carmona-Moreno
 * @author Patrick Guichet
 * @version 1.0
 */

package Signature;

import Signature.Signer.PairGenerator;
import Test_Cryptographie.Extract;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.x500.X500Principal;

/**
 * Classe de démonstration de Signer
 */
public class SignerTest {
    
    // Le mot de passe de l'entrée de type clé privée
    private String user_password;
    
    // L'alias de l'entrée de type clé privée
    private String private_key_alias;
    
    // L'alias de l'entrée de type certificat
    private String certificate_alias;
    
    public SignerTest(String password,String alias,String alias_certificate) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
        
        this.user_password=password;
        this.private_key_alias=alias;
        this.certificate_alias=alias_certificate;
        
        try {
            
            // Création de l'objet signant et vérifiant une signature
            Signer signer = new Signer("SHA1withRSA");
            Extract extract =new Extract();
            
            // Générateur de clés basé sur la courbe (en caractéristique 2) "
            PairGenerator pg = new PairGenerator("RSA");
            
            // Lire le fichier qui contient le chemin du document à signer
            String path=Signer.Read_Signer("path.txt");
            System.out.println(path);
            
            // Obtenir la clé privée associée à l'entrée de type PrivateKeyEntry
            RSAPrivateKey mykey=(RSAPrivateKey)extract.extractPrivateKey(private_key_alias, user_password.toCharArray());
            System.out.println(mykey.getModulus());
            System.out.println(mykey.getPrivateExponent());
            System.out.println("\n");
            
            // Obtenir la clé privée associée à l'entrée de type Certificat
            PublicKey pubkey=extract.getCertPublicKey(certificate_alias);
            System.out.println(pubkey.toString());
            System.out.println("\n");
            
            // Obtenir le DN associé à l'entrée de type certificat
            X500Principal mydn=extract.getSubjectDN(certificate_alias);
            System.out.println(mydn.toString());
            
            // Calcul de la signature d'un fichier
            String tag = signer.signFile(path, mykey);
            System.out.printf("Tag signature : %s\n", tag);
            
            // Ecriture dans un fichier de la signature obtenue
            Signer.WriteSignature(tag);
           
            // Vérification de la signature de ce même fichier
            System.out.printf(
                    "Vérification : %B\n", signer.verifyFile(path,
                    pubkey,
                    tag));
            
            // Copier le DN dans le fichier signature
            Extract.WriteDN(mydn);
            
        } catch (IOException | GeneralSecurityException ex) {
            Logger.getLogger(SignerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}