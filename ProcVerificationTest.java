/**
 * ProcVerificationTest.java
 * 
 * Version mise a jour le 9 janvier 2015
 * @author David Carmona-Moreno
 * version 1.0
 */


package Verification;


import Signature.Signer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Classe testant le bon fonctionnement de la procedure de verification 
 */
public class ProcVerificationTest {
    
    // Procedure de verification
    ProcVerification verif;
    
    public ProcVerificationTest() throws GeneralSecurityException, NoSuchAlgorithmException, FileNotFoundException, IOException{
           
        // Instance de la procedure de verification
        verif=new ProcVerification();
        
        // Obtenir le DN se trouvant dans le fichier filesignature_DN
        String DN=verif.extractDNfromFile("filesignature_DN.txt");
        System.out.println(DN);
        
        // Fichier contenant le chemin absolu du fichier a verifier
        String path_Verification=Signer.Read_Signer("path.txt");
        
        // Classe qui permet d'obtenir la tag signature
        ExtractTag tag=new ExtractTag("filesignature.txt");
        String mytag=tag.extraction();
        System.out.println(mytag);
        
        // Instance de la classe qui permet d'extraire la cle publique correspondant au DN figurant dans le fichier filesignature_DN
        PubKeyVerification publickey_verification=new PubKeyVerification(DN);
        
        // Avoir la cle publique associee au DN
        PublicKey publicKey= publickey_verification.getPublicKey();
        System.out.println(publicKey.toString());
        boolean b=verif.verifyFile(path_Verification,publicKey,mytag);
        
        if(b==true){
            GUI.Verification verif_GUI=new GUI.Verification(DN,path_Verification);
        }else{
            
            // Fenetre d'erreur a afficher si la procedure de verification n'a pas marche
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Verification failed", "Error", JOptionPane.ERROR_MESSAGE);
        }     
    }  
}
