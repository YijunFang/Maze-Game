package screen;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class coinPanel extends JPanel {
	
	private int numOfCoin = 0;
	
	private JLabel label = new JLabel( format(), JLabel.CENTER);
	
	public coinPanel() {
		super();
		setupLabel();
	}



	/**
	 * @param numOfCoin 
	 */
	public void saveCoinNumber(int numOfCoin) {
		//pass to save in buffer
	}
	
	private void setupLabel() {
		label.setFont(new Font("Arial", Font.BOLD, 15));
		this.setLayout(new GridLayout(1, 1));
		this.add(label);
	}
	
	public String format() {	
		return String.format("%02d", numOfCoin);
	}
	
	public void updateCoin( int numOfCoin ) {
		this.numOfCoin = numOfCoin;
		
		label.setText(format());
	}
	

}
