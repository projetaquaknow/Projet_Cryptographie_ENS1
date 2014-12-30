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
     */
    public Key retrievePrivKey(String alias, char[] keypwd) {
        try {
            return mystore.getKey(alias, keypwd);
        } catch (UnrecoverableKeyException ex) {
            Logger.getLogger(Verification.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (KeyStoreException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Verification.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /*
    Travail restant :
    -Récupération de l'alias du keystore à partir de la signature
    -Récupération des certificats (ou clé publique et DN directement) à partir du keystore
    -Déchiffrage et affichage du DN
    */    
    
}
