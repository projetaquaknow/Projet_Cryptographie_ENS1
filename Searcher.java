/**
 * Searcher.java
 * 
 * Version mise à jour le 9 Janvier 2015
 * 
 * @author David Carmona-Moreno
 * @author Cathie Prigent
 * @version 1.0
 */

package Signature;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

/**
 * Classe qui permet de vérifier si le données introduites par l'utilisateur
 * existent dans le Keystore
 */
public class Searcher {
    
    // L'alias associé à l'entrée de type clé privée
    private String alias_member;
    
    // L'alias associé à l'entrée de type certificate
    private String alias_member_certificate;
    
    // Le Keystore
    Test_Cryptographie.CA.KStore kstore;
    
    /**
     * Constructeur de la classe Searcher
     * @throws java.security.KeyStoreException si aucun provider ne peut d'implémentation ou qu'il n'a pas été initialisé
     * @throws java.security.NoSuchAlgorithmException si l'algorithme indiqué n'est pas reconnu
     * @throws java.security.cert.CertificateException si le certificat n'a pas pu être chargé
     * @throws java.io.IOException si une erreur d'entrée-sortie se produit, par exemple sérialisation du keystore corrompue
     */
    public Searcher() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
        
        // Instance du Keystore
        kstore=new Test_Cryptographie.CA.KStore();
    }
    
    /**
     * Méthode qui permet de vérifier que les données introduites par 
     * l'utilisateur existent dans le Keystore
     * @param name Le nom de l'utilisation (champ CN du DN)
     * @param password Le mot de passe qui protège la clé privée
     * @return True si les informations concordent avec celle du keystore, sinon False
     * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au niveau du constructeur du Keystore
     * @throws java.security.NoSuchAlgorithmException si l'algorithme indiqué n'est pas reconnu
     * @throws java.security.cert.CertificateException si le certificat n'a pas pu être chargé
     * @throws java.io.IOException 
     * @throws java.security.UnrecoverableKeyException Si la clé ne peut être récupérée, si le mot de passe fourni est erronné, par exemple
     */
    public boolean verify(String name,String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException{
        
        if((checkUserName(name)&&checkPassword(name,password))==true){
            return true;
        }else{
            return false;
        }
        
    }
    
    /**
     * Méthode qui permet de vérifier que le mot de passe de l'utilisateur
     * est correct.
     * @param username Le nom de l'utilisation auquel est associé le mot de passe
     * @param pass Mot de passe de l'entrée
     * @return True si le mot de passe saisi est correct, sinon False
     * @throws java.security.KeyStoreException 
     * @throws java.security.cert.CertificateEncodingException
     */
    public boolean checkPassword(String username,String pass) throws KeyStoreException, CertificateEncodingException  {
        
        // On récupèrela liste des alias identifiant les entrées de type clé privée
        List<String> alias_list=this.loopAliasKey();
        
        // La taille de la liste des alias
        int alias_list_size=alias_list.size();
        
        // On va parcourir la liste des alias
        int i=0;
        
        // Parcourir la liste des alias
        while(i!=alias_list_size){
            String alias_buffer=alias_list.get(i);
            try {
                Key myKey=kstore.getPrivateKey(alias_buffer, pass.toCharArray());
            } catch (NoSuchAlgorithmException | UnrecoverableKeyException ex) {
                
                i++;
                
                // Le continue sert à sortir du bloc catch et à effectuer une
                // nouvelle itération.
                continue;
            }
            setAlias(alias_buffer);
            return true;
        }
        return false;
    }
    
    /**
     * Fixe l'alias associé au certificat en tant que donnée membre
     * @param myalias L'alias associé au certificat
     * @return La donnée membre
     */
    public String setAlias(String myalias){
        return this.alias_member=myalias;
    }
    
    /**
     * Renvoie la donnée membre alias de la classe
     * @return L'alias 
     */
    public String getAlias(){
        return alias_member;
    }
    
    /**
     * Méthode qui permet de vérifier que l'username inséré par l'utilisateur
     * existe bien dans le Keystore
     * @param username Le nom et prénom de d'utilisateur
     * @return True si l'useername existe dans le keystore, False sinon
     * @throws java.security.KeyStoreException si aucun provider ne peut d'implémentation ou qu'il n'a pas été initialisé
     * @throws java.security.NoSuchAlgorithmException si l'algorithme indiqué n'est pas reconnu
     * @throws java.security.cert.CertificateException si le certificat n'a pas pu être chargé
     * @throws java.io.IOException si une erreur d'entrée-sortie se produit, par exemple sérialisation du keystore corrompue
     */
    public boolean checkUserName(String username) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException{
        
        // Avoir la liste des certificats du Keystore
        List<X509Certificate> certificatelist=this.loop();
        
        // Avoir la liste des alias associés à chaque certificat
        List<String> myalias=this.loopAliasCertificate();
        
        // Taille de la liste des certificats
        int list_cert_size=certificatelist.size();
        
        // Taille de la liste des alias
        int list_alias_size=myalias.size();
        
        // On va parcourir la liste des certificats et des alias
        int i=0;
        int k=0;
  
        // Parcourir la liste des certificats
        while(i!=list_cert_size){
            X509Certificate cert_buffer=certificatelist.get(i);
            //System.out.println(cert_buffer.toString());
            
            // Parcourir la liste des alias
            while(k!=list_alias_size){
                String alias_buffer=myalias.get(k);
                
                // Si le CN entré par l'utilisateur est le meme que celui du certificat
                if(username == null ? ExtractCNCert(alias_buffer,cert_buffer) == null : username.equals(ExtractCNCert(alias_buffer,cert_buffer))){
                    
                    setCertificateAlias(alias_buffer);
                    return true;
                }
                k++;
            }
            i++;
        }
        return false;
    }
    
    /**
     * Méthode qui permet d'associer l'alias à l'entrée de type certificat
     * @param certificateAlias Le nom à associer au certificat
     * @return L'alias de l'entrée de type certificat
     */
    public String setCertificateAlias(String certificateAlias){
        return this.alias_member_certificate=certificateAlias;
    }
    
    /**
     * Méthode qui permet de récupérer l'alias de l'entrée de type certificat
     * @return L'alias associé à l'entrée de type certificat
     */
    public String getCertificateAlias(){
        return alias_member_certificate;
    }
    /**
     * Méthode qui permet de récupérer le Common Name du DN du sujet
     * @param cert Le certificat dont il faut extraire le CN
     * @param alias L'identificateur de l'entrée du Keystore dont on veut récupérer le CN
     * @return Le common name asscocié à l'entrée de type certificat
     * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au nievau du constructeur du Keystore
     * @throws java.security.cert.CertificateEncodingException Si l'encodage du certificat échoue
     */
    public String ExtractCNCert(String alias,X509Certificate cert) throws KeyStoreException, CertificateEncodingException {
        
        // Instance du certificat
        cert=(X509Certificate) kstore.getCert(alias);
        
        // Le DN
        X500Name x500name = new JcaX509CertificateHolder(cert).getSubject();
        
        // Extraction su CN
        RDN cn = x500name.getRDNs(BCStyle.CN)[0];
        
        // Converion de cn en une variable de type String
        String commonname=IETFUtils.valueToString(cn.getFirst().getValue());
        
        return commonname;
    }
    
    /**
     * Méthode qui permet de parcourir toutes les entrées d'un Keystore  de type
     * Certificat et les insérer dans une liste.
     * @return La Liste des alias
     * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au nievau du constructeur du Keystore
     */
    public List<X509Certificate> loop() throws KeyStoreException{
        
        // Liste de tous les certificats d'un Keystore
        List<X509Certificate> certificates = new LinkedList<X509Certificate>();
        
        // Liste des alias
        List<String> myalias= new LinkedList<String>();
        
        // La liste statique des alias du Keystore
        Enumeration<String> aliases = kstore.aliasList();
        
        // Boucle qui sert à parcourir la liste des alias
	while(aliases.hasMoreElements()) {
               
		String alias = aliases.nextElement();
                
		// tester si l'entrée nommée par l'alias courant est un certificat
		if(kstore.isCertEntry(alias)){
                    
			//Si c'est le cas la récupérer
			certificates.add(kstore.getCert(alias));
                        
                        //Récupérer l'alias qui lui est associé
                        myalias.add(alias);
		}
        }
        return certificates;
    }
    
    /**
     * Méthode qui sert à récupérer les alias
     * @return La liste des alias
     * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au nievau du constructeur du Keystore
     */
    public List<String> loopAliasCertificate() throws KeyStoreException{
        
        // Liste de tous les certificats d'un Keystore
        List<X509Certificate> certificates = new LinkedList<X509Certificate>();
        
        // Liste des alias
        List<String> myalias= new LinkedList<String>();
        
        // La liste statique des alias du Keystore
        Enumeration<String> aliases = kstore.aliasList();
        
        // Boucle qui sert à parcourir la liste des alias
	while(aliases.hasMoreElements()) {
               
		String alias = aliases.nextElement();
                
		// tester si l'entrée nommée par l'alias courant est un certificat
		if(kstore.isCertEntry(alias)){
                    
			//Si c'est le cas la récupérer
			certificates.add(kstore.getCert(alias));
                        
                        //Récupérer l'alias qui lui est associé
                        myalias.add(alias);
		}
        }
        return myalias;
    }
    
    /**
     * Méthode qui permet de parcourir tous les alias du Keystore
     * et de les sauvegarder dans une liste
     * @return La liste des alias dont l'entrée est une clé de type privée
     * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au nievau du constructeur du Keystore
     */
    public List<String> loopAliasKey() throws KeyStoreException{
        
        // Liste de tous les certificats d'un Keystore
        List<String> alias_list_Key = new LinkedList<String>();
        
        // La liste statique des alias du Keystore
        Enumeration<String> aliases = kstore.aliasList();
        
        // Boucle qui sert à parcourir la liste statique des alias
	while(aliases.hasMoreElements()) {
               
		String alias = aliases.nextElement();
                
		// tester si l'entrée nommée par l'alias courant est une clée privée
		if(kstore.isPrivateKeyEntry(alias)){
                    
	            //Si c'est le cas récupérer l'alias
		    alias_list_Key.add(alias);
		}
        }
        return alias_list_Key;                   
    }    
}
