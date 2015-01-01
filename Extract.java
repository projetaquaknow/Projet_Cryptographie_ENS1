/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test_Cryptographie;
import java.io.FileNotFoundException;
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
import java.io.FileWriter;

/**
 * Classe qui permet d'extraire les données cryptogaphiques du Keystore
 * @author David Carmona-Moreno
 */
public class Extract {
    
    // Le Keystore dont on veut extraire les données cryptographiques
    Test_Cryptographie.CA.KStore kstore;
    
    public Extract() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
        
        // Instance du Keystore dont on veut extraire les données cryptographiques
        kstore=new Test_Cryptographie.CA.KStore();
    }
    
    /**
     * Extraire la clé privée associée à un alias
     * @param alias L'identificateur de l'entrée
     * @passwd passwd Le mot de passe de l'entrée
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
     */
    public PublicKey getCertPublicKey(String alias) throws KeyStoreException{
        
        //Variable qui permet de savoir si l'netrée associée à l'alias est une entrée de type Certificat
        boolean b=kstore.isCertEntry(alias);
        
        // La clé publique
        PublicKey publickey;
        
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
     * @return 
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
     * @param mydn Le Distinguished de l'utilisateur
     */
    public static void WriteDN(X500Principal mydn) throws FileNotFoundException, IOException{
       
        // Le nom du fichier dans lequel il faut écrire
        String filename="filesignature.txt";
        
        // Le fichier dans lequel on va écrire le DN
        FileWriter signaturefile=new FileWriter("filesignature.txt",true);
        
        // Convertir le DN en une chaine de caractères
        String dn_string=mydn.toString();
        
        // On copie dans le fichier le DN du signataire
        signaturefile.write(dn_string);
        
        // Fermeture du flux
        signaturefile.close();
    }
    
    
}
