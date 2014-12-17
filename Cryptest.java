package CryptoSigVerif;

import javax.swing.*;

public class Cryptest
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileBrowser().setVisible(true);
                
            }
        });
		/*JFrame frame = new CryptoFrame();
		frame.show(); */
	}
} 