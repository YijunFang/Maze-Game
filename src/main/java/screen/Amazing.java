package screen;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
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

		pack();

		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		parentPanel = new MainPanel(getWidth(), getHeight());

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


















