package screen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class Amazing extends JFrame {

	private MainPanel parentPanel;

	public Amazing() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setTitle("A-Maze-ing");

		setupBorder(this);

		setMinimumSize(new Dimension(1000, 800));
		
		setResizable(false);

		pack();

		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		parentPanel = new MainPanel(){
			public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(new ImageIcon("background.jpg").getImage(), -200, -50, null);
		}
	};

		// parentPanel.setBounds(0, 0, getWidth(), getHeight());

		add(parentPanel);

		// parentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		setLocationRelativeTo(null);

		this.setVisible(true);

	}

	private void setupBorder(JFrame container) {
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		container.setContentPane(contentPane);
	}

	public static void main(String[] args) {

		new Amazing();

	}

}


















