package Signature;

import GUI.jFilePicker;
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
    
    public ECSignerTest(){
        try {
            
            // Création de l'objet signant et vérifiant une signature
            ECSigner ecs = new ECSigner("SHA256withECDSA");
            
            // Générateur de clés basé sur la courbe (en caractéristique 2) "c2pnb208w1"
            ECKeyPairGenerator eckp = new ECKeyPairGenerator("c2pnb208w1");
            KeyPair kp = eckp.getECKeyPair();
            
            // Lire le fichier contenant le chemin du fichier choisi par l'utilisateur
            String path=ECSigner.Read_ECSigner("path.txt");
            System.out.println(path);
            
            // Affichage des clés
            System.out.printf("Clée privée :\n\t%s\n", kp.getPrivate().toString());
            System.out.printf("Clée publique :\n\t%s\n", kp.getPublic().toString());
            
            // Calcul de la signature d'un fichier
            String tag = ecs.signFile(path, kp.getPrivate());
            System.out.printf("Tag signature : %s\n", tag);
           
            // Vérification de la signature de ce même fichier
            System.out.printf(
                    "Vérification : %B\n", ecs.verifyFile(path,
                    kp.getPublic(),
                    tag));
        } catch (IOException ex) {
            Logger.getLogger(ECSignerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(ECSignerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
