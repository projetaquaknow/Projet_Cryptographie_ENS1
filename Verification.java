/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.verification;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import keyStore.Kstore;

/**
 *
 * @author PrigentC
 */
public class Verification {
    private Kstore mystore;
    
    //Objet chargé de la vérification de la signature.
    private Signature verifier;
    
    public Verification(String algorithm) throws GeneralSecurityException {
        
        // Construction d'une instance du vérificateur de signature
        this.verifier = Signature.getInstance(algorithm);
    }
    
    /**
     * Fais récupérer la clé privée au keystore
     * @param alias Le nom sous lequel est stocké la clé privée
     * @param keypwd Le mot de passe protégeant la clé privée
     * @return La clé privée recherchée
     * @throws java.security.KeyStoreException
     * @throws java.security.NoSuchAlgorithmException
     */
    public Key retrievePrivKey(String alias, char[] keypwd) throws KeyStoreException, NoSuchAlgorithmException {
        try {
            return mystore.getKey(alias, keypwd);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(Verification.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Permet de récupérer le DN à partir du certificat joint avec le fichier signé
     * @param Cert Le certificat à partir duquel obtenir le DN
     * @return Le DN de l'émetteur
     */
    public String retrieveDN(X509Certificate Cert) {
        return Cert.getIssuerDN().getName();
    }
    
    /**
     * Permet de vérifier si le certificat est présent dans le keystore
     * (est ce que cela revient vraiment à vérifier le DN ?)
     * @param Cert Le certificat à comparer avec ceux du keystore
     * @return True si le certificat existe déjà, False sinon
     */
    public boolean verifyDN(X509Certificate Cert) {
        try {
            return (mystore.getCertificateAlias(Cert).equals("") ?  false : true);
        } catch (KeyStoreException ex) {
            Logger.getLogger(Verification.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
       /**
     * Vérification de la signature d'un fichier
     * @param file le fichier à vérifier
     * @param publicKey La clé publique initialisant la vérification
     * @param tagB64 L'encodage en Base64 de la signature à vérifier
     * @return <code>true</code> Si la signature est correcte et <code>false</code> sinon
     * @throws GeneralSecurityException  Si la vérification de la signature échoue
     * @throws IOException Si la lecture du fichier échoue
     */
    public boolean verifyFile(File file, PublicKey publicKey, String tagB64)
            throws GeneralSecurityException, IOException {
        verifier.initVerify(publicKey);
        
        // le flot entrant
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        
        // le buffer de lecture
        byte[] buffer = new byte[1024];//J'ai changé de 1024 à 2048
        
        // le nombre d'octets lus
        int nl;
        
        // boucle de lecture pour la vérification de la signature
        while((nl = in.read(buffer)) != -1)
            // remise à jour de l'objet signant avec les octets lus
            verifier.update(buffer, 0, nl);
        return verifier.verify(Base64.decodeBase64(tagB64));
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
    
    
    /*
    Travail restant :
    -Récupération de l'alias du keystore à partir de la signature
    -Récupération des certificats (ou clé publique et DN directement) à partir du keystore
    -Déchiffrage et affichage du DN
    */    
    
}
