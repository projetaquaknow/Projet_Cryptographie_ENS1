

package keyStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * Classe permettant la manipulation
 * ais�e des Keystore.
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
          
          // Il faut garder le mot de passe du keystore pour l'utiliser par d�faut
          // lorsque l'utilisateur de la classe ne pr�cise pas de mot de passe
          // pour ins�rer une nouvelle entr�e dans le keystore de l'instance
          // (la seule m�thode concern�e est importSecretKey)
          kstorepwd = passwd;
     }
     
     /**
      * Sauvegarde l'�tat courant du keystore manipul� dans le fichier file en le
      * prot�geant avec le mot de passe passwd.
      * @param file Le fichier dans lequel sauvegarder le keystore de l'instance.
      * @param passwd Le mot de passe prot�geant le fichier cr��.
     * @throws java.security.GeneralSecurityException
     * @throws java.io.IOException
      */
     public void save(File file, char[] passwd)
             throws GeneralSecurityException, IOException {
    	 
         // S�rialise le contenu du keystore dans le flot attach� au fichier file
         try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
             kstore.store(os, passwd);
         }
     }
     
     /**
      * Retourne la cl� associ�e � l'alias indiqu�
      * en utilisant le mot de passe passwd
      * @param alias L'alias de l'entr�e du Keystore.
      * @param keypwd Le mot de passe pour r�cup�rer la cl�
      */
     public Key getKey(String alias, char[] keypwd) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
    	 
             return kstore.getKey(alias, keypwd); 
     }
     
     /**
      * Associe une entrée du keystore a l'alias passé en paramètre
      * @param alias L'alias qui sera associé à l'entrée
      * @param entry L'entrée à insérer dans le keystore
      * @param protParam Un paramètre qui peut permettre de vérifier l'intégrité de l'entrée
      * @throws java.security.KeyStoreException 
      */
     public void setEntry (String alias, KeyStore.Entry entry, KeyStore.ProtectionParameter protParam) throws KeyStoreException {
         kstore.setEntry(alias, entry, protParam);
     }
     
     /**
      * Associe le certificat à l'alias passé en paramètre
      * @param alias L'alias qui sera associé au certificat
      * @param cert Le certificat à insérer dans le keystore
      * @throws KeyStoreException 
      */
     
     public void setCertificateEntry(String alias, Certificate cert) throws KeyStoreException {
         kstore.setCertificateEntry(alias, cert);
     }
     
     /**
      * Importe dans le keystore le certificat cert sous le nom alias.
      * @param cert Le certificat � ins�rer.
      * @param alias L'alias � associer avec le certificat ins�r�.
      * @throws java.security.GeneralSecurityException
      */
     public void importCertificate(Certificate cert, String alias)
             throws GeneralSecurityException {
         // Ins�re le certificat dans le keystore
         kstore.setCertificateEntry(alias, cert);
     }
     
     /**
      * Trouve l'alias correspondant à un certificat
      * @param cert Le certificat qui sert de base à la comparaison
      * @return L'alias correspondant au certificat si celui-ci correspond à une entrée
      * @throws KeyStoreException 
      */
     public String getCertificateAlias(Certificate cert) throws KeyStoreException {
         return kstore.getCertificateAlias(cert);
     }
     
     /**
      * D�monstration de la classe.
      * @param args
      * @throws java.security.GeneralSecurityException
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
