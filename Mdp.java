package CryptoSigVerif;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Mdp extends JPanel{
	
	private int mode;
	private JFileChooser fileChooser;
	public static final int MODE_COR = 1;
    public static final int MODE_INCOR = 2;
	
	private JLabel label;
    private JTextField textField;
    
    public Mdp(String textFieldLabel){
    	
    	setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    	 
        // creates the GUI
        label = new JLabel(textFieldLabel);
         
        textField = new JTextField(30);
        
         
        add(label);
        add(textField);
    }
    
    @SuppressWarnings("unused")
	private void buttonActionPerformed(ActionEvent evt) {
        if (mode == MODE_COR) {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            }
        } else if (mode == MODE_INCOR) {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            }
        }
    }

}
