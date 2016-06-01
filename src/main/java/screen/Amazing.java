package screen;

import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Amazing extends JFrame {

	private MainPanel parentPanel;

	/**
	 * Constructor
	 */
	public Amazing() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setTitle("Mazecraft");

		setMinimumSize(new Dimension(1000, 800));

		setResizable(false);

		pack();

		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		parentPanel = new MainPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("background.jpg").getImage(), -200, -50, null);
			}
		};

		add(parentPanel);

		setLocationRelativeTo(null);

		this.setVisible(true);

	}

	public static void main(String[] args) {

		new Amazing();

	}

}
