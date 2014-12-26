package Signature;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.logging.Level;
import java.util.logging.Logger;
import static Signature.ECSigner.ECKeyPairGenerator;

/**
 * Classe de démonstration de ECSigner
 * @author David Carmona-Moreno
 * @author Patrick Guichet
 */
public class ECSignerTest {

    public static void main(String[] args) {
        try {
            
            // Création de l'objet signant et vérifiant une signature
            ECSigner ecs = new ECSigner("SHA256withECDSA");
            
            // Générateur de clés basé sur la courbe (en caractéristique 2) "c2pnb208w1"
            ECKeyPairGenerator eckp = new ECKeyPairGenerator("c2pnb208w1");
            KeyPair kp = eckp.getECKeyPair();
            
            // Affichage des clés
            System.out.printf("Clée privée :\n\t%s\n", kp.getPrivate().toString());
            System.out.printf("Clée publique :\n\t%s\n", kp.getPublic().toString());
            
            // Calcul de la signature d'un fichier
            String tag = ecs.signFile("C:\\Users\\David\\Documents\\NetBeansProjects\\JavaApplication1\\src\\Signature\\Clustering.ppt", kp.getPrivate());
            System.out.printf("Tag signature : %s\n", tag);
            
            // Vérification de la signature de ce même fichier
            System.out.printf(
                    "Vérification : %B\n", ecs.verifyFile("C:\\Users\\David\\Documents\\NetBeansProjects\\JavaApplication1\\src\\Signature\\Clustering.ppt",
                    kp.getPublic(),
                    tag));
        } catch (IOException ex) {
            Logger.getLogger(ECSignerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(ECSignerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}