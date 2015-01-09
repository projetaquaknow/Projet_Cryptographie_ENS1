/**
 * Extract.java
 * 
 * Version mise à jour le 9 Janvier 2015
 * 
 * @author David Carmona-Moreno
 * @version 1.0
 */

package Test_Cryptographie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import javax.security.auth.x500.X500Principal;

/**
 * Classe qui permet d'extraire les données cryptogaphiques du Keystore
 */
public class Extract {
    
    // Le Keystore dont on veut extraire les données cryptographiques
    Test_Cryptographie.CA.KStore kstore;
    
    /**
     * Constructeur de l'extracteur de données
     * @throws java.security.KeyStoreException si aucun provider ne peut d'implémentation ou qu'il n'a pas été initialisé
         * @throws java.security.NoSuchAlgorithmException si l'algorithme indiqué n'est pas reconnu
         * @throws java.security.cert.CertificateException si le certificat n'a pas pu être chargé
         * @throws java.io.IOException si une erreur d'entrée-sortie se produit, par exemple sérialisation du keystore corrompue
     */
    public Extract() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
        
        // Instance du Keystore dont on veut extraire les données cryptographiques
        kstore=new Test_Cryptographie.CA.KStore();
    }
    
    /**
     * Extraire la clé privée associée à un alias
     * @param alias L'identificateur de l'entrée
     * @param passwd Le mot de passe qui protège la clé privée
     * @return La clé privée RSA associée à l'alias et protégée par le mot de passe
     * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au niveau du constructeur du Keystore
     * @throws java.security.NoSuchAlgorithmException Si l'algorithme indiqué n'est pas reconnu
     * @throws java.security.UnrecoverableKeyException Si la clé ne peut être récupérée, si le mot de passe fourni est erronné, par exemple
     */
    public RSAPrivateKey extractPrivateKey(String alias,char[] passwd) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException{
        
        // Variable qui permet de savoir si l'entrée associée à l'alias est de type private key
        boolean b=kstore.isPrivateKeyEntry(alias);
        
        //Clé privée associée à l'entrée de type PrivateKey identifiée par un alias
        Key privatekey;
        
        // Si l'entrée est bien de type clé privée
        if(b==true){
            privatekey=kstore.getPrivateKey(alias, passwd);
            return (RSAPrivateKey) privatekey;
        }else{
            System.out.println("Problème \n");
            return null;    
        }
    }
    
    /**
     * Méthode qui permet d'extraire la clé publique du certificat
     * @param alias Identificateur associé à l'entrée de type certificat
     * @return La clé publique du certificat associé à l'alias
     * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au niveau du constructeur du Keystore
     */
    public PublicKey getCertPublicKey(String alias) throws KeyStoreException{
        
        //Variable qui permet de savoir si l'entrée associée à l'alias est une entrée de type Certificat
        boolean b=kstore.isCertEntry(alias);

        // Le certificat
        X509Certificate mycert;
        
        // Si l'entrée est de type certificat
        if(b==true){
            
            mycert=(X509Certificate) kstore.getCert(alias);
            return mycert.getPublicKey();
            
        }else{
            System.out.println("Erreur");
            return null;
        }
        
    }
    
    /**
     * Méthode qui permet de renvoyer le DN du propriétaire du certificat
     * @param alias L'identificateur de l'entrée
     * @return Le DN se trouvant dans le certificat associé à l'alias
     * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au niveau du constructeur du Keystore
     */
    public X500Principal getSubjectDN(String alias) throws KeyStoreException{
        
        //Variable qui permet de savoir si l'netrée associée à l'alias est une entrée de type Certificat
        boolean b=kstore.isCertEntry(alias);
        
        // Le certificat
        X509Certificate mycert;
        
        // Le Distinguished Name (DN)
        X500Principal dn;
        
        // Si l'entrée est de type certificat
        if(b==true){
            
            mycert=(X509Certificate) kstore.getCert(alias);
            dn=mycert.getSubjectX500Principal();
            
            return dn;
            
        }else{
            System.out.println("Erreur avec le Distinguished Name");
            return null;
        }
    }
    
    /**
     * Méthode qui permet d'écrire le DN du propriétaire dans un fichier texte 
     * @param mydn Le Distinguished Name de l'utilisateur
     * @throws java.io.FileNotFoundException Dans le cas où le fichier n'existerait pas
     */
    public static void WriteDN(X500Principal mydn) throws FileNotFoundException, IOException{
       
        // Flux de sortie récupéré dans un fichier
        FileOutputStream fileoutput=new FileOutputStream(new File("filesignature_DN.txt"));
        
        //
        String g=mydn.toString();
        // Conversion du paramètre path_buffer en un tableau de byte
        byte[] b=g.getBytes("UTF-8");
             
        // Ecriture du tableau de byte dans le fichier
        fileoutput.write(b);
                
        // Fermeture du flux
        fileoutput.close();
    }
}
