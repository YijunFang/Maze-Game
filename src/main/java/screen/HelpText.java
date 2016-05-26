package screen;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class HelpText extends JLabel {
	
	private String instruction;
	
	public HelpText(String instruction) {
		
		this.setFont(new Font("Courier New", Font.BOLD, 18));
		this.setForeground(Color.white);
	}

}
