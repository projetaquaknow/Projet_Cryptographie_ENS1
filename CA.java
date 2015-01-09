/**
 * CA.java
 * 
 * Version mise à jour le 9 Janvier 2015
 * 
 * @author Lepagnot Julien
 * @author Cathie Prigent
 * @author Maithili Vinayagamoorthi
 * @author David Carmona-Moreno
 * @version 1.0
 */

package Test_Cryptographie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.asn1.x500.X500Name;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;


/**
 * Une classe implémentant une autorité de certification
 */
public class CA {
     
    // Le DN du CA
    private static final String DN = "CN=RootCA, OU=M2, O=miage, C=FR";
    //private static final String DN = "CN=David Carmona, OU=ENSISA, O=UHA, C=FR";
    // Le DN du sujet
    private static final String SDN= "CN=RootCA, OU=M2, O=miage, C=FR";
      
    // L'alias permettant la récupération du certificat autosigné du CA
    private static final String ALIAS = "miageCA";
    
    // Le chemin du fichier contenant le keystore du CA
    private static final String CA_KS_FILE = "ksca.ks";
    
    // L'OID de l'algorithme SHA-1
    private static final String SHA1_OID = "1.3.14.3.2.26";
    
    // L'OID de l'algorithme SHA1withRSA
    private static final String SHA1_WITH_RSA_OID = "1.2.840.113549.1.1.5";
    
    // L'OID de l'extension Basic Constraint
    private static final String BASIC_CONSTRAINT_OID = "2.5.29.19";
    
    // L'OID de l'extension SubjectKeyIdentifier
    private static final String SUBJECT_KEY_IDENTIFIER_OID = "2.5.29.14";
    
    // L'OID de l'extension keyUsage
    private static final String KEY_USAGE_OID = "2.5.29.15";
    
    // L'OID de l'extension extKeyUsage
    private static final String EXT_KEY_USAGE_OID = "2.5.29.37";
    
    // L'OID de l'extension altName
    private static final String SUBJECT_ALT_NAME_OID = "2.5.29.17";
    
    // La valeur de l'extension keyUsage pour une autorité racine
    private static final int CA_KEY_USAGE_VALUE =
        KeyUsage.digitalSignature | KeyUsage.nonRepudiation | KeyUsage.cRLSign | KeyUsage.keyCertSign;
    
    // La valeur de l'extension keyUsage pour un certificat de serveur
    private static final int SV_KEY_USAGE_VALUE =
        KeyUsage.keyAgreement | KeyUsage.keyEncipherment | KeyUsage.digitalSignature;
    
    // Délimiteur début certificat
    private static final String CERT_BEGIN = "-----BEGIN CERTIFICATE-----\n";
    
    // Délimiteur fin certificat
    private static final String CERT_END = "\n-----END CERTIFICATE-----";

    // Le certificat du CA
    private Certificate caCert;
    
    // La clé privée du CA
    private PrivateKey caPk;
   
    /**
    * Construction d'une instance de la classe
    * @param passwd Le mot de passe protégeant le keystore du CA
    * @throws GeneralSecurityException si la fabrication/récupération du certificat du CA échoue
    * @throws IOException si une erreur d'entrée-sortie se produit, par exemple sérialisation du keystore corrompue
    * @throws org.bouncycastle.operator.OperatorCreationException 
     */
    public CA(char[] passwd) throws GeneralSecurityException, IOException, OperatorCreationException {
        
            KeyStore ksCa = KeyStore.getInstance("JCEKS");
            File caDir = new File(CA_KS_FILE);
            if (caDir.exists()) {
                
                 // Le keystore existe déjà il suffit de le charger
                 ksCa.load(new BufferedInputStream(new FileInputStream(caDir)), passwd);
            
                 // puis de récupérer le certificat du CA et la clé privée associée
                 this.caCert = ksCa.getCertificate(ALIAS);
                 this.caPk = (PrivateKey) ksCa.getKey(ALIAS, passwd);
            } else {
                 // le keystore n'existe pas il faut construire la paire de clés publique, privée
                 // et empaqueter la clé publique dans un certificat X509 autosigné
                 installCA(ksCa, passwd, caDir);
             }
    }
    /**
         * Méthode d'aide pour la fabrication d'une CA qui n'existe pas encore 
         * @param ks le keystore du CA
         * @param passwd le mot de passe qui protège le keystore
         * @param caDir le fichier où sera sérialisé le keystore
         * @throws GeneralSecurityException si la fabrication/récupération du certificat du CA échoue
         * @throws IOException si une erreur d'entrée-sortie se produit, par exemple sérialisation du keystore corrompue
     */
    private void installCA(KeyStore ks, char[] passwd, File caDir)
        throws GeneralSecurityException, IOException, OperatorCreationException {
        
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair caKp = kpg.generateKeyPair();
        this.caPk = caKp.getPrivate();
        PublicKey publicKey=caKp.getPublic();
        
        // le nom de l'émetteur 
        X500Name caDn = new X500Name(DN);
        
        // Le nom du sujet
        X500Name subjdn=new X500Name(DN);
        
        // Instance d'un calendrier
        Calendar calendar = Calendar.getInstance();
        
        // le début de la période de validité
        Date notBefore = calendar.getTime();
        calendar.set(1970, 11, 31);
      
        // Une instance d'une SubjectPublicKeyInfo
        SubjectPublicKeyInfo subjPubKeyInfo = 
                             new SubjectPublicKeyInfo(ASN1Sequence.getInstance(publicKey.getEncoded()));
        
        // Instance du certificat que l'on veut générer
        X509v3CertificateBuilder certGen=new X509v3CertificateBuilder(caDn,BigInteger.ONE,notBefore,calendar.getTime(),subjdn,subjPubKeyInfo);
        
        // Algorithme de l'identificateur de l'algorithme de signature.
        DigestCalculator digCalc = new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));
        X509ExtensionUtils x509ExtensionUtils = new X509ExtensionUtils(digCalc);

        // Extension définissant l'usage de la clé
        certGen.addExtension(
            Extension.keyUsage, false, new KeyUsage(CA_KEY_USAGE_VALUE));
        
        // Extension BasicConstraint
        certGen.addExtension(
            Extension.basicConstraints, true, new BasicConstraints(Integer.MAX_VALUE));
        
        // extension subjectKeyIdentifier
        certGen.addExtension(
            Extension.subjectKeyIdentifier, 
            false, 
             x509ExtensionUtils.createSubjectKeyIdentifier(subjPubKeyInfo));
        
        // Algorithme de signature du contenu du certificat.
        ContentSigner sigGen = new JcaContentSignerBuilder("SHA1WithRSAEncryption").setProvider("BC").build(this.caPk);
        this.caCert= new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(sigGen));
        ks.load(null, passwd);
        
        // Insérer le certificat dans le keystore
        ks.setCertificateEntry(ALIAS, caCert);
        
        // Insérer la clé privée associée dans le keystore
        KeyStore.PrivateKeyEntry pke = 
            new KeyStore.PrivateKeyEntry(caPk, new Certificate[]{this.caCert});
        
        ks.setEntry(ALIAS, pke, new KeyStore.PasswordProtection(passwd));
        
        // Sauvegarder le keystore nouvellement créé
        OutputStream out = new BufferedOutputStream(new FileOutputStream(caDir));
        ks.store(out, passwd);
          
    }
    
    /**
     * Classe interne qui permet de générer le Keystore
     * @author Cathie Prigent
     * @author Maithili Vinayagamoorthi
     * @author David Carmona-Moreno
     */
    public static class KStore {
        
        // Le keystore de l'instance	
        public KeyStore kstore;
   
        // Mot de passe du Keystore
        private char[] kstorepwd;
     
        /**
         * Constructeur de la classe Kstore
         * @throws java.security.KeyStoreException si aucun provider ne peut d'implémentation ou qu'il n'a pas été initialisé
         * @throws java.security.NoSuchAlgorithmException si l'algorithme indiqué n'est pas reconnu
         * @throws java.security.cert.CertificateException si le certificat n'a pas pu être chargé
         * @throws java.io.IOException si une erreur d'entrée-sortie se produit, par exemple sérialisation du keystore corrompue
         */
        public KStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
    	  
    	    // Construction d'une instance d'un keystore de type type
            kstore = KeyStore.getInstance("JCEKS");
          
            // Initialisation du keystore avec le contenu du fichier file
            InputStream is = new BufferedInputStream(new FileInputStream(new File("kstore.ks")));
            kstore.load(is,"azerty".toCharArray());
          
            // Il faut garder le mot de passe du keystore pour l'utiliser par défaut
            // lorsque l'utilisateur de la classe ne précise pas de mot de passe
            // pour insérer une nouvelle entrée dans le keystore de l'instance
            // (la seule méthode concernée est importSecretKey)
            kstorepwd = "azerty".toCharArray();
        }
     
        /**
         * Sauvegarde l'état courant du keystore manipulé dans le fichier file en le
         * protégeant avec le mot de passe passwd.
         * @param file Le fichier dans lequel sauvegarder le keystore de l'instance.
         * @param passwd Le mot de passe protégeant le fichier créé.
         * @throws java.security.GeneralSecurityException si le keystore est corrompu
         * @throws java.io.IOException si une erreur d'entrée-sortie se produit, par exemple sérialisation du keystore corrompue
         */
        public void save(String file, char[] passwd)
            throws GeneralSecurityException, IOException {
    	 
            // Sérialise le contenu du keystore dans le flot attaché au fichier file
            try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
            kstore.store(os, passwd);
            }
        }
        
        /**
         * Méthode qui permet d'ajouter une entrée au Keystore généré
         * @param alias Alias qui sera associé à l'entrée dans le Keystore
         * @param entry L'objet (clé privée, certificat...) que l'on souhaite insérée
         * @param protParam Détermine avec quelle type de protection est protégée l'entrée
         * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au nievau du constructeur du Keystore
         */
        public void enterpk(String alias,KeyStore.Entry entry,KeyStore.ProtectionParameter protParam)
                throws KeyStoreException
        {
            kstore.setEntry(alias, entry, protParam);
        }
        
        /**
         * Méthode qui permet d'ajouter une entrée de type CertificateEntry dans le Keystore généré
         * @param alias L'alias associé au certificat
         * @param cert  Le certificat 
         * @throws KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au nievau du constructeur du Keystore
         */
        public void entercert(String alias,Certificate cert)throws KeyStoreException{
            kstore.setCertificateEntry(alias, cert);
        }
        
        /**
         * Méthode qui permet de savoir si l'entrée identifiée par l'alias est de type clé privée
         * @param alias L'identificateur de l'entrée
         * @return Si l'entrée est de type clé privée alors la méthode renvoie true sinon false
         * @throws KeyStoreException  On lance cette exception lorsqu'il y a eu une erreur au nievau du constructeur du Keystore
         */
        public final boolean isPrivateKeyEntry(String alias) throws KeyStoreException {
            return kstore.isKeyEntry(alias);
        }
        
        /**
         * Méthode qui permet de récupérer la clé privée d'une entrée de type PrivateKey
         * @param alias L'identificateur de l'entrée
         * @param entrypasswd Le mot de passe de l'entrée
         * @return La clé privée associée à l'alias et au mot de passe
         * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au niveau du constructeur du Keystore
         * @throws java.security.NoSuchAlgorithmException Si l'algorithme indiqué n'est pas reconnu
         * @throws java.security.UnrecoverableKeyException Si la clé ne peut être récupérée, si le mot de passe fourni est erronné, par exemple
         */
        public final Key getPrivateKey(String alias,char[] entrypasswd) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException{
            return kstore.getKey(alias, entrypasswd);
        }
        
        /**
         * Méthode qui permet de savoir si l'entrée identifiée par l'alias est une entrée de type certificat
         * @param alias L'identificateur de l'entrée
         * @return Le certificat associé à l'alias
         * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au niveau du constructeur du Keystore
         */
        public final boolean isCertEntry(String alias) throws KeyStoreException{
            return kstore.isCertificateEntry(alias);
        }
        
        /**
         * Méthode qui permet d'obtenir le certificat associé à l'alias de l'entrée de type Certificat
         * @param alias L'identificateur de l'entrée
         * @return Le Certificat que l'on souhaite récupérer
         * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au niveau du constructeur du Keystore
         */
        public final X509Certificate getCert(String alias) throws KeyStoreException{
            return (X509Certificate) kstore.getCertificate(alias);
        }
        
        /**
         * Méthode qui permet de charger le Keystore
         * @throws java.io.FileNotFoundException
         * @throws java.security.NoSuchAlgorithmException si l'algorithme indiqué n'est pas reconnu
         * @throws java.security.cert.CertificateException si le certificat n'a pas pu être chargé
         */
        public void loadKeystore () throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException {
            
            InputStream is = new BufferedInputStream(new FileInputStream(new File("kstore.ks")));
            kstore.load(is,"azerty".toCharArray());
            
        }
        
        /**
         * Méthode qui permet de récupérer une liste de tous les alias d'un Keystore
         * @return La liste des alias contenus dans le Keystore
         * @throws java.security.KeyStoreException On lance cette exception lorsqu'il y a eu une erreur au niveau du constructeur du Keystore
         */
        public final Enumeration<String> aliasList() throws KeyStoreException{
            return kstore.aliases();
        }
    }
    
    /**
    * Génération d'un certificat pour l'identification d'un serveur
    * @param dn le nom distingué du serveur
    * @param altName le nom alternatif du serveur
    * @param pk la clé publique devant être enrobée dans le certificat
    * @return un certificat (norme X509 v3)empaquetant la clé publique pk
    * @throws GeneralSecurityException si la fabrication du certificat échoue
    * @throws IOException si la fabrication du numéro de série échoue 
    */
    X509Certificate generateUserCertificate(String dn,PublicKey pk)
                throws GeneralSecurityException, IOException, OperatorCreationException {
                   
        // Génération d'un numéro de série aléatoire
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        BigInteger serialNumber = BigInteger.valueOf(Math.abs(random.nextInt()));
        
        // le nom de l'émetteur 
        X500Name caDnI = new X500Name(DN);
        
        // Le nom du sujet
        X500Name caDnS=new X500Name(dn);
        
        // Instance d'un calendrier
        Calendar calendar = Calendar.getInstance();
        
        // le début de la période de validité
        Date notBefore = calendar.getTime();
        calendar.add(Calendar.YEAR,2);
      
        // Instance d'un SubjectPublicKeyInfo
        SubjectPublicKeyInfo subjPubKeyInfos = 
                             new SubjectPublicKeyInfo(ASN1Sequence.getInstance(pk.getEncoded()));
           
        //Instance du certificat que l'on veut générer
        X509v3CertificateBuilder certGen=new X509v3CertificateBuilder(caDnI,serialNumber,notBefore,calendar.getTime(),caDnS,subjPubKeyInfos);
        
        DigestCalculator digCalc = new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1));
        X509ExtensionUtils x509ExtensionUtils = new X509ExtensionUtils(digCalc);

        // Extension définissant l'usage de la clé
        certGen.addExtension(
            Extension.keyUsage, false, new KeyUsage(SV_KEY_USAGE_VALUE));
        
        // extension subjectKeyIdentifier
        certGen.addExtension(
            Extension.subjectKeyIdentifier, 
            false, 
             x509ExtensionUtils.createSubjectKeyIdentifier(subjPubKeyInfos));
        
        // Algorthme de signature du contenu du certificat
        ContentSigner sigGen = new JcaContentSignerBuilder("SHA1WithRSAEncryption").setProvider("BC").build(this.caPk);
        
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(sigGen));
        
    }
    
    /**
    * Exportation du certificat du CA en DER encodé Base64
    * @param file le fichier où exporter le certificat
    * @param cert Le certificat à exporter
    * @throws GeneralSecurityException si l'encodage DER échoue
    * @throws IOException si l'exportation échoue
    */
    public static void exportCertificate(File file, Certificate cert)
        throws GeneralSecurityException, IOException {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            out.write(CERT_BEGIN.getBytes("UTF-8"));
            out.write(Base64.encodeBase64Chunked(cert.getEncoded()));
            out.write(CERT_END.getBytes("UTF-8"));
        }
    }
    
    /**
    * Exportation du certificat du CA en DER encodé base64
    * @param fileName le nom du fichier où exporter le certificat
    * @param cert le certificat à exporter
    * @throws GeneralSecurityException si l'encodage DER échoue
    * @throws IOException si l'exportation échoue
    */
    public static void exportCertificate(String fileName, Certificate cert) 
        throws GeneralSecurityException, IOException {
                exportCertificate(new File(fileName), cert);
    }
         
    
    public static void main(String[] args) throws KeyStoreException, GeneralSecurityException, IOException, OperatorCreationException{
      
        try {
            // Pour pouvoir utiliser l'API BouncyCastle au travers du mécanisme standard du JCE
            Security.addProvider(new BouncyCastleProvider());
            
            // Instanciation d'une CA depuis un fichier keystore, s'il existe
            CA ca = new CA("x4TRDf4JHY578pth".toCharArray());
            //CA ca = new CA("azerty".toCharArray());
            
            // Instanciation du Keystore ave tous les DN des utilisateurs
            KStore mystore=new KStore();
            
            // Génération d'une paire de clés pour un certificat serveur
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair caKp = kpg.generateKeyPair();
            
            // Récupération de la clé privée
            PrivateKey priv= caKp.getPrivate();
            
            // Récupération de la clé publique
            PublicKey pub=caKp.getPublic();
            
            X509Certificate usrCert = ca.generateUserCertificate(
                    "CN=Julien Lepagnot, OU=FST, O=UHA, L=Mulhouse, ST=68093, C=FR",
                    pub);
            
            // Exportation du certificat du serveur
            CA.exportCertificate("usrCert.cer", usrCert);
            
            // Exportation du certificat du CA
            CA.exportCertificate("ca.cer", ca.caCert);
          
            // Générer une instance de PrivateKeyEntry contenant la clé privée
            PrivateKeyEntry priventry=new PrivateKeyEntry(priv,new Certificate[]{usrCert});
            
            // La clé privée est associée à un alias et est protégée par un mot de passe
	    mystore.enterpk("key3", priventry, new KeyStore.PasswordProtection("julien".toCharArray()));
            
            // On associe un certificat à l'alias
            mystore.entercert("key4", usrCert);
            
            //Sauver le Keystore
            mystore.save("kstore.ks","azerty".toCharArray());

            // Affichage de la clé publique du serveur
            System.out.println(pub);
        } catch (GeneralSecurityException | IOException | OperatorCreationException ex) {
            Logger.getLogger(CA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}