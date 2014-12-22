package keystore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Certificate;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class Kstore {
	private KeyStore kstore;
	private char[] kstorepwd;
	
	//Constructeur du Keystore, pas de mdp généré pour le moment
	private Kstore(String algoType) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		kstore = KeyStore.getInstance(algoType);
		InputStream is = new BufferedInputStream(new FileInputStream(new File("kstore.ks")));
		
		kstore.load(is,kstorepwd);
	}
	
	//Renvoie le certificat (clé publique encapsulée) associée à l'alias
	private java.security.cert.Certificate getCertificate(String alias) throws KeyStoreException {
		return kstore.getCertificate(alias);
	}
	
	//Renvoie la clé privée associée à l'alias si le mdp est le bon
	private Key getKey(String alias, char[] keypwd) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
		return kstore.getKey(alias, keypwd);	//String à défaut d'autre chose pour l'instant
	}
	
	//Permet de vérifier si l'alias a déjà une clé dans le Keystore
	private boolean isKeyEntry(String alias) throws KeyStoreException {
		return kstore.isKeyEntry(alias);
	}
	
	//Remplissage du Keystore avec des valeurs bidons
	//(aucune idée de quoi mettre dans Key et Chain, Key ne viendra qu'après le DN de toute manière, non ?)
	private void populate() {
		kstore.setKeyEntry("David", key, "david", chain);
		kstore.setKeyEntry("Clément", key, "clement", chain);
		kstore.setKeyEntry("Maithili", key, "maithili", chain);
		kstore.setKeyEntry("Cathie", key, "cathie", chain);
	}
	
}
