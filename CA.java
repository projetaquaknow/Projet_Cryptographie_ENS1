package Test_Cryptographie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.asn1.x500.X500Name;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
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
 * @author David Carmona-Moreno
 * @author Patrick Guichet
 * @author Julien Lepagnot
 */
public class CA {
     
    // Le DN du CA
    private static final String DN = "CN=RootCA OU=M2 O=miage C=FR";
    
    // Le DN du sujet
    private static final String SDN= "CN=Cathie Prigent OU=ENSISA O=UHA C=FR";
    
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
         * @throws IOException si une erreur d'entrée-sortie se produit,
         * par exemple sérialisation du keystore corrompue
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
         * @throws IOExceptionsi une erreur d'entrée-sortie se produit, 
         * par exemple sérialisation du keystore corrompue
     */
    private void installCA(KeyStore ks, char[] passwd, File caDir)
        throws GeneralSecurityException, IOException, OperatorCreationException {
        
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        kpg.initialize(2048);
        KeyPair caKp = kpg.generateKeyPair();
        this.caPk = caKp.getPrivate();
        PublicKey publicKey=caKp.getPublic();
        
        // le numéro de série de ce certificat
        // certGen.setSerialNumber(BigInteger.ONE);
        // SecureRandom random = SecureRandom.getInstance("SHA1_WITH_RSA_OID");
        // BigInteger serialNumber = BigInteger.valueOf(Math.abs(random.nextInt()));
        
        // le nom de l'émetteur 
        X500Name caDn = new X500Name(DN);
        
        // Le nom du sujet
        X500Name subjdn=new X500Name(SDN);
        
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
     * @author David Carmona-Moreno
     * @author Cathie Prigent
     * @author Maithili Vinayagamoorthi
     */
    public static class KStore {
        
        // Le keystore de l'instance	
        private KeyStore kstore;
   
        // Mot de passe du Keystore
        private char[] kstorepwd;
     
        private static final Map<String, String> OID_MAP = new HashMap<>();
        static {
            OID_MAP.put("1.2.840.113549.1.9.1", "emailAddress");
            OID_MAP.put("1.2.840.113549.1.9.2", "unstructuredName");
            OID_MAP.put("1.2.840.113549.1.9.8", "unstructuredAddress");
            OID_MAP.put("1.2.840.113549.1.9.16", "S/MIME Object Identifier Registry");
        }
     
        /**
         * Constructeur de la classe Kstore
         * @param algoType    Le type du Keystore
         * @param passwd      Le mot de passe du Keystore
         */
        private KStore(String algoType,char[] passwd) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
    	  
    	    // Construction d'une instance d'un keystore de type type
            kstore = KeyStore.getInstance(algoType);
          
            // Initialisation du keystore avec le contenu du fichier file
            InputStream is = new BufferedInputStream(new FileInputStream(new File("kstore.ks")));
            kstore.load(is,passwd);
          
            // Il faut garder le mot de passe du keystore pour l'utiliser par défaut
            // lorsque l'utilisateur de la classe ne précise pas de mot de passe
            // pour insérer une nouvelle entrée dans le keystore de l'instance
            // (la seule méthode concernée est importSecretKey)
            kstorepwd = passwd;
        }
     
        /**
         * Sauvegarde l'état courant du keystore manipulé dans le fichier file en le
         * protégeant avec le mot de passe passwd.
         * @param file Le fichier dans lequel sauvegarder le keystore de l'instance.
         * @param passwd Le mot de passe protégeant le fichier créé.
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
         * @arg alias L'alias 
         * @arg entry Le type de l'entrée qu'on veut insérer
         * @arg protParam le paramètre de protection de l'entrée
         */
        public void enterpk(String alias,KeyStore.Entry entry,KeyStore.ProtectionParameter protParam)
                throws KeyStoreException
        {
            kstore.setEntry(alias, entry, protParam);
        }
        
        /**
         * Méthode qui permet d'ajouter une entrée de type CertificateEntry dans le Keystore généré
         * @param alias L'alias 
         * @param cert  Le certificat 
         * @throws KeyStoreException 
         */
        public void entercert(String alias,Certificate cert)throws KeyStoreException{
            kstore.setCertificateEntry(alias, cert);
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
    X509Certificate generateServerCertificate(String dn, String altName, PublicKey pk)
                throws GeneralSecurityException, IOException, OperatorCreationException {
                   
        // Génération d'un numéro de série aléatoire
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        BigInteger serialNumber = BigInteger.valueOf(Math.abs(random.nextInt()));
        
        // le nom de l'émetteur 
        X500Name caDnI = new X500Name(DN);
        
        // Le nom du sujet
        X500Name caDnS=new X500Name(SDN);
        
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
        
        // Extension définissant le nom alternatif du serveur
        certGen.addExtension(
            Extension.subjectAlternativeName, true, new GeneralNames(new GeneralName(GeneralName.dNSName,altName)));
        
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
    * @param le certificat à exporter
    * @throws GeneralSecurityException si l'encodage DER échoue
    * @throws IOException si l'exportation échoue
    */
    public static void exportCertificate(String fileName, Certificate cert) 
        throws GeneralSecurityException, IOException {
                exportCertificate(new File(fileName), cert);
    }
         
    
    public static void main(String[] args) throws KeyStoreException{
      
        try {
            // Pour pouvoir utiliser l'API BouncyCastle au travers du mécanisme standard du JCE
            Security.addProvider(new BouncyCastleProvider());
            
            // Instanciation d'une CA depuis un fichier keystore, s'il existe
            CA ca = new CA("x4TRDf4JHY578pth".toCharArray());
            //CA ca = new CA("azerty".toCharArray());
            
            // Instanciation du Keystore ave tous les DN des utilisateurs
            KStore mystore=new KStore("JCEKS","azerty".toCharArray());
            
            // Génération d'une paire de clés pour un certificat serveur
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair caKp = kpg.generateKeyPair();
            
            // Récupération de la clé privée
            PrivateKey priv= caKp.getPrivate();
            
            // Récupération de la clé publique
            PublicKey pub=caKp.getPublic();
            
            X509Certificate srvCert = ca.generateServerCertificate(
                    "CN=Cathie Prigent, OU=ENSISA, O=UHA, L=BRUNSTATT, ST=68350, C=FR",
                    "Cathie Prigent",
                    pub);
            
            // Exportation du certificat du serveur
            CA.exportCertificate("srv.cer", srvCert);
            
            // Exportation du certificat du CA
            CA.exportCertificate("ca.cer", ca.caCert);
            
            // Création d'un chemin de certification pour srvCert
            CertificateFactory factory = CertificateFactory.getInstance("X509");
            List list = new ArrayList();
            list.add(srvCert);
            CertPath cp = factory.generateCertPath(list);
            byte[] encoded = cp.getEncoded("PKCS7");
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream("srv.p7b"))) {
                out.write(encoded);
            }
            
            // Générer une instance de PrivateKeyEntry contenant la clé privée
            PrivateKeyEntry priventry=new PrivateKeyEntry(priv,new Certificate[]{srvCert});
            
            // La clé privée est associée à un alias et est protégée par un mot de passe
	    mystore.enterpk("key1", priventry, new KeyStore.PasswordProtection("qwerty".toCharArray()));
            
            // On associe un certificat à l'alias
            mystore.entercert("key2", srvCert);
            
            //Sauver le Keystore
            mystore.save("kstore.ks","azerty".toCharArray());
            
            /* Vérification de ce chemin de certification en utilisant caCert
            PKIXValidator pkiV = new PKIXValidator(new String[]{"ca.cer"});
            pk = pkiV.validate("srv.p7b", "PKCS7");*/
            
            // Affichage de la clé publique du serveur
            System.out.println(pub);
        } catch (GeneralSecurityException | IOException | OperatorCreationException ex) {
            Logger.getLogger(CA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
