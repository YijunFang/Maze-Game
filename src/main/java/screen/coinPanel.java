package screen;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class coinPanel extends JPanel {

	private int numOfCoin = 0;

	private JLabel label = new JLabel(format(), JLabel.CENTER);

	/**
	 * Constructor for the coin panel
	 */
	public coinPanel() {
		super();
		setupLabel();
	}

	/**
	 * Gets the coin number
	 * @return number of coins
	 */
	public int getCoinNumber() {
		// pass to save in buffer
		return numOfCoin;
	}

	/**
	 * Sets up the label of the coin panel
	 */
	private void setupLabel() {
		label.setFont(new Font("Arial", Font.BOLD, 15));
		this.setLayout(new GridLayout(1, 1));
		this.add(label);
	}

	/**
	 * Returns the format of the string
	 * @return string format for numOfCoin
	 */
	public String format() {
		return String.format( "%d", numOfCoin);
	}
	
	/**
	 * Gets the number of coins
	 * @return number of coins
	 */
	public int getNumCoins() {
	    return numOfCoin;
	}

	/**
	 * Updates the number of coins
	 * @param numOfCoin the current number of coins
	 */
	public void updateCoin(int numOfCoin) {
		this.numOfCoin = numOfCoin;
		label.setText(format());
	}
	
	/**
	 * Resets the number of coins to 0
	 */
	public void clearCoin() {
		numOfCoin = 0;
	}

}
