/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto.verification;

import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import keyStore.Kstore;

/**
 *
 * @author PrigentC
 */
public class Verification {
    Kstore mystore;
    
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
    
    
    /*
    Travail restant :
    -Récupération de l'alias du keystore à partir de la signature
    -Récupération des certificats (ou clé publique et DN directement) à partir du keystore
    -Déchiffrage et affichage du DN
    */    
    
}
