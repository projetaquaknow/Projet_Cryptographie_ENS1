package gui;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SignatureGUI {
	
	public static void getSignatureGUI() {
		JFrame frame = new JFrame("Signature");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contentPane = frame.getContentPane();
	    contentPane.setLayout(new GridLayout(4,2));
	    
	    contentPane.add(new JLabel("File name : "));
	    contentPane.add(new JLabel("Appropriate_FileName"));
	    
	    contentPane.add(new JLabel("Nom du signataire : "));
	    contentPane.add(new JLabel("Nom"));
	    
	    contentPane.add(new JLabel("Prénom du signataire : "));
	    contentPane.add(new JLabel("Prénom"));
	    
	    contentPane.add(new JLabel("Mot de passe : "));
	    contentPane.add(new JTextField(25));
	    
	    contentPane.add(new JButton("Signer"));
	    
	    frame.pack();
	    frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		getSignatureGUI();
	}
	
}

