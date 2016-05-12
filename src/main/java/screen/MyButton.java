package screen;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Font;

public class MyButton extends JButton {
	MyButton button;

	public MyButton(String text) {
		super(text);
		Dimension d = new Dimension(5, 5);
		setPreferredSize(d);
		setFont(new Font("Arial", Font.BOLD, 15));
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);

	}

}
