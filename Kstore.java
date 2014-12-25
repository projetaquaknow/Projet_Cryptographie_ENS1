package keystore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;
import java.security.Key;


/**
 * Classe permettant la manipulation
 * aisée des Keystore.
 * @author Maithili Vinayagamoorthi
 * @author Cathie Prigent
 * @author David Carmona-Moreno
 */
public class Kstore{
	
	 // Le keystore de l'instance	
     private KeyStore kstore;
   
     // Mot de passe du Keystore
     private char[] kstorepwd;
   
     /**
      * Constructeur de la classe Kstore
      * @param algoType    Le type du Keystore
      * @param passwd      Le mot de passe du Keystore
      */
     private Kstore(String algoType,char[] passwd) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
    	  
    	  // Construction d'une instance d'un keystore de type type
          kstore = KeyStore.getInstance(algoType);
          
          // Initialisation du keystore avec le contenu du fichier file
          InputStream is = new BufferedInputStream(new FileInputStream(new File("kstore.ks")));
          kstore.load(is,passwd);
          
          // Il faut garder le mot de passe du keystore pour l'utiliser par défaut
          // lorsque l'utilisateur de la classe ne précise pas de mot de passe
          // pour insérer une nouvelle entrée dans le keystore de l'instance
          // (la seule méthode concernée est importSecretKey)
          kstorepwd = passwd;
     }
     
     /**
      * Sauvegarde l'état courant du keystore manipulé dans le fichier file en le
      * protégeant avec le mot de passe passwd.
      * @param file Le fichier dans lequel sauvegarder le keystore de l'instance.
      * @param passwd Le mot de passe protégeant le fichier créé.
      */
     public void save(File file, char[] passwd)
             throws GeneralSecurityException, IOException {
    	 
         // Sérialise le contenu du keystore dans le flot attaché au fichier file
         try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
             kstore.store(os, passwd);
         }
     }
     
     /**
      * Sauvegarde l'état courant du keystore manipulé dans le fichier file en le
      * protégeant avec le mot de passe passwd.
      * @param alias L'alias de l'entrée du Keystore.
      * @param keypwd Le mot de passe protégeant de l'entrée.
      * @return key  Renvoi la clé privée associée à l'alias.
      */
     private Key getKey(String alias, char[] keypwd) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
    	 
             return kstore.getKey(alias, keypwd); 
     }
     
     /**
      * Démonstration de la classe.
      * @param args
      */
     public static void main(String[] args) throws GeneralSecurityException{
    	 
    	  try {
			Kstore mystore=new Kstore("JCEKS","azerty".toCharArray());
			 
		} catch (KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		
     }
}

