/**
 * PubKeyVerification.java
 * 
 * Version mise a jour le 9 janvier 2015
 * @author David Carmona-Moreno
 * @author Clement Chambat
 * version 1.0
 */


package Verification;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x500.X500Name;

/**
 * Classe qui permet d'extraire d'obtenir la cle publique a partir d'un DN
 */
public class PubKeyVerification {
    
    // Le DN qui figure dans le fichier cree dans la signature
    private String DN;
    
    // Le Keystore du projet
    private Test_Cryptographie.CA.KStore kstore;
    
    // Le DN present dans le keystore
    X500Principal dn_X;
    
    /**
     * Methode de verification de la cle publique
     * @param DN le DN du fichier cree dans la signature
     * @throws KeyStoreException si l'initialisation du keystore echoue
     * @throws java.security.NoSuchAlgorithmException si l'algorithme specifie 
     * est introuvable dans l'environnement du projet
     * @throws CertificateException si le type de certificat dans lequel se trouve 
     * le DN stocke dans le keystore est introuvable
     * @throws IOException si la lecture du DN du fichier echoue
     */
    
    public PubKeyVerification(String DN) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        
        this.DN=DN;
        dn_X=new X500Principal(DN);
        this.dn_X=dn_X;
       
       // Instance d'un Keystore
       kstore=new Test_Cryptographie.CA.KStore();
    }
    
    /**
     * Methode qui permet d'obtenir la cle publique a partir du DN
     * @return la cle publique associee
     */
    public PublicKey getPublicKey() throws KeyStoreException {
        
        // La liste des certificats
        List<X509Certificate> certificateList=this.getCert();
        
        // la cle publique qu'il faut renvoyer e travers la methode
        PublicKey pubKey;
        
        // Taille de la liste des certificats
        int size_cert=certificateList.size();
        System.out.println(size_cert);
        
        // On va parcourir la liste des certificats
        int i=0;
        
        while(i!=size_cert){
            
            // Le certificat de la liste
            X509Certificate mycertificate=certificateList.get(i);
            
            // Comparaison entre le DN du proprietaire du certificat et celui du fichier
            if(mycertificate.getSubjectDN().toString().equals(dn_X.toString())){
                System.out.println("Youpi");
                return mycertificate.getPublicKey();
            }else{
                System.out.println("Salut");
            }
            i=i+1;
        }
        return null;
    }

    /**
     * Methode qui parcourt les alias et le insère les certificats correspondants dans une liste
     * @return la liste des certificats
     */
    public List<X509Certificate> getCert() throws KeyStoreException{
        
        // Liste de tous les certificats d'un Keystore
        List<X509Certificate> certificates = new LinkedList<X509Certificate>();
        
        // La liste statique des alias du Keystore
        Enumeration<String> aliases = kstore.aliasList();
        
        // Boucle qui sert à parcourir la liste des alias
	while(aliases.hasMoreElements()) {
               
                // Aller au prochain element de type Alias
		String alias = aliases.nextElement();
                
		// Tester si l'entree nommee par l'alias courant est un certificat
		if(kstore.isCertEntry(alias)){
                    
			//Si c'est le cas recuperer le certificat
			certificates.add(kstore.getCert(alias));
		}
        }
        return certificates;
    }
}
