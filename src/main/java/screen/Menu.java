package screen;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Menu extends JFrame {
	CardLayout cardLayout = new CardLayout();
	JPanel emptyPanel;
	JPanel helpScreen;
	JPanel mainMenu;
	JPanel newGame;
	JPanel endScreen;
	
	boolean easy = false;
	boolean medium = false;
	boolean hard = false;
	//test
	ActionListener newGamebutton = new ActionListener() {		
		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(emptyPanel, "newGame");
		}
	};
	ActionListener returnMainbutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(emptyPanel, "mainMenu");
			
		}
	};
	
	ActionListener helpbutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(emptyPanel, "helpScreen");
			
		}
	};
	ActionListener infobutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(helpScreen, "Please usw 'up', 'down', 'left', 'right' to move the character", "How To Play", JOptionPane.INFORMATION_MESSAGE);					
		}
	};
	ActionListener easybutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(emptyPanel, "easyGame");
			
		}
	};
	
	public Menu() {
		endScreen = createEndScreen();
		newGame = createNewGame();
		helpScreen = createHelpScreen();
		mainMenu = createMainMenu();
		emptyPanel = new JPanel();
//		easyGame

		
		this.setTitle("A-Maze-ing");
		
		this.setSize(400, 400);
		
		this.setLocationRelativeTo(null);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		emptyPanel.setLayout(cardLayout);
		
		emptyPanel.add("mainMenu",mainMenu);
		emptyPanel.add("helpScreen",helpScreen);
		emptyPanel.add("newGame",newGame);
//		emptyPanel.add("easyGame",easyGame);
		
		add(emptyPanel);
		setLocationRelativeTo(null);
		
		cardLayout.show(emptyPanel, "mainMenu");
		this.setVisible(true);
		
	}
	
	
	private JPanel createEndScreen() {
		// TODO Auto-generated method stub
		return null;
	}


	private JPanel createNewGame() {
		JButton button1, button2, button3;
		button1 = new JButton("Easy");
		button2 = new JButton("Medium");	
		button3 = new JButton("Hard");	
		
		JPanel newGamePanel = new JPanel();
		
		newGamePanel.setLayout(new GridLayout(0,1));
		
		button1.addActionListener(easybutton);
//		button2.addActionListener(mediumbutton);
//		button3.addActionListener(hardbutton);
		
		newGamePanel.add(button1);
		newGamePanel.add(button2);
		newGamePanel.add(button3);
		
		return newGamePanel;
	}


	public JPanel createHelpScreen() {
		JButton button1, button2, button3;
		JButton button4, button5;

		
		button1 = new JButton("How To Play");
		button2 = new JButton("Continue");	
		button3 = new JButton("Resumes");	
		button4 = new JButton("Save");
		button5 = new JButton("Return to Main Menu");
		JPanel helpScreenPanel = new JPanel();
		
		helpScreenPanel.setLayout(new GridLayout(0,1));

		
		button1.addActionListener(infobutton);
//		button2.addActionListener(lButton2);
//		button3.addActionListener(lButton3);
//		button4.addActionListener(lButton4);
		button5.addActionListener(returnMainbutton);
	
		helpScreenPanel.add(button1);
		helpScreenPanel.add(button2);
		helpScreenPanel.add(button3);
		helpScreenPanel.add(button4);
		helpScreenPanel.add(button5);

//		this.add(helpScreenPanel);
	
		return helpScreenPanel;
	}
	
	
	
	public JPanel createMainMenu() {
		JButton button1, button2, button3, button4;
//		ListenForButton lButton1,lButton2,lButton3;
		
		button1 = new JButton("New Game");
		button2 = new JButton("Continue");	
		button3 = new JButton("Quit");	
		button4 = new JButton("Help");
		
		JPanel mainMenuPanel = new JPanel();
		
		mainMenuPanel.setLayout(new GridLayout(0,1));

		
		button1.addActionListener(newGamebutton);
//		button2.addActionListener(lButton2);
//		button3.addActionListener(lButton3);
		button4.addActionListener(infobutton);

	
		mainMenuPanel.add(button1);
		mainMenuPanel.add(button2);
		mainMenuPanel.add(button3);
		mainMenuPanel.add(button4);
		
		
		return mainMenuPanel;
	}
	
}
