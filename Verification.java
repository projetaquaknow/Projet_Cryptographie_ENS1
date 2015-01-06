/*
 * Verification.java
 *
 * Version mise à jour le 21 D�cembre 2014
 * 
 * @author Clément Chambat
 * @version 1.1
 */

package GUI;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Verification
 * 
 * La classe Verification permet d'afficher
 * une fenetre qui montre le Distinguished Name
 * du signataire et le nom du fichier verifie.
 */
public class Verification extends JFrame 
{
	
	private JLabel label1 = new JLabel("DN : ");
    private JLabel label2 = new JLabel("Distinguished_Name");
    private JLabel label3 = new JLabel("File Name :");
    private JLabel label4 = new JLabel("NOM_FICHIER");
   
    /**
     * Constucteur de la classe Verification
     */
    public Verification()
    {
    	
       this.setTitle("Verification");
       this.setSize(400, 100);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.setLocationRelativeTo(null);
       
       JPanel container = new JPanel();
       container.setLayout(new GridLayout(2,2));
       container.add(label1);
       container.add(label2);
       container.add(label3);
       container.add(label4);
    
       this.setContentPane(container);
       this.setVisible(true);       
    }
    
}


