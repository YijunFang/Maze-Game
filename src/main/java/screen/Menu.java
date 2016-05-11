package screen;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Menu extends JFrame {
	Color blueeee = new Color(207, 243, 255);
	CardLayout cardLayout = new CardLayout();
	JPanel emptyPanel;
	JPanel helpScreen;
	JPanel mainMenu;
	JPanel newGame;
	JPanel endScreen;
	JPanel mazeScreen;
	JPanel pauseScreen;

	boolean easy = false;
	boolean medium = false;
	boolean hard = false;

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
			JOptionPane.showMessageDialog(helpScreen, "Please usw 'up', 'down', 'left', 'right' to move the character",
					"How To Play", JOptionPane.INFORMATION_MESSAGE);
		}
	};

	ActionListener replaybutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// temp: show empty panel
			cardLayout.show(emptyPanel, "mazeScreen");
		}
	};

	ActionListener easybutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			easy = true;
			// temp: show empty panel
			cardLayout.show(emptyPanel, "mazeScreen");
		}
	};

	ActionListener mediumbutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			medium = true;
			// temp: show empty panel
			cardLayout.show(emptyPanel, "mazeScreen");
		}
	};

	ActionListener hardbutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			hard = true;
			// temp: show empty panel
			cardLayout.show(emptyPanel, "mazeScreen");
		}
	};

	ActionListener savebutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// save game
			// temp: show empty panel
			JOptionPane.showMessageDialog(mazeScreen, "GAME SAVED!", "Save Game", JOptionPane.INFORMATION_MESSAGE);
			cardLayout.show(emptyPanel, "mazeScreen");
		}
	};

	ActionListener pausebutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(emptyPanel, "pauseScreen");
		}
	};

	ActionListener restartbutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(emptyPanel, "mazeScreen");
		}
	};

	ActionListener resumebutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(emptyPanel, "mazeScreen");
		}
	};

	ActionListener endbutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			cardLayout.show(emptyPanel, "endScreen");
		}
	};

	ActionListener hintbutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// show hint: direction

		}
	};

	ActionListener quitbutton = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};

	public Menu() {
		emptyPanel = new JPanel();
		mainMenu = createMainMenu();
		newGame = createNewGame();
		helpScreen = createHelpScreen();
		mazeScreen = createMazeScreen();
		endScreen = createEndScreen();
		pauseScreen = createPauseScreen();

		this.setTitle("A-Maze-ing");

		this.setSize(700, 500);
		this.setMinimumSize(new Dimension(700, 500));

		this.setLocationRelativeTo(null);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		emptyPanel.setLayout(cardLayout);

		emptyPanel.setBackground(null);
		emptyPanel.add("mainMenu", mainMenu);
		emptyPanel.add("helpScreen", helpScreen);
		emptyPanel.add("newGame", newGame);
		emptyPanel.add("mazeScreen", mazeScreen);
		emptyPanel.add("endScreen", endScreen);
		emptyPanel.add("pauseScreen", pauseScreen);
		add(emptyPanel);
		setLocationRelativeTo(null);

		cardLayout.show(emptyPanel, "mainMenu");

		this.setVisible(true);

	}

	public JPanel createMainMenu() {

		MyButton button1 = new MyButton("New Game");
		MyButton button2 = new MyButton("Resume");
		MyButton button3 = new MyButton("How To Play");
		MyButton button4 = new MyButton("Quit");

		JPanel mainMenuPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("src/main/resources/background.jpg").getImage(), -100, -100, null);
			}
		};

		mainMenuPanel.setLayout(new GridLayout(0, 1));
		mainMenuPanel.setBorder(new EmptyBorder(300, 30, 30, 500));

		button1.addActionListener(newGamebutton);
		button2.addActionListener(resumebutton);
		button3.addActionListener(infobutton);
		button4.addActionListener(quitbutton);

		mainMenuPanel.add(button1);
		mainMenuPanel.add(button2);
		mainMenuPanel.add(button3);
		mainMenuPanel.add(button4);

		setLocationRelativeTo(null);
		return mainMenuPanel;
	}

	private JPanel createNewGame() {
		MyButton button1 = new MyButton("Easy");
		MyButton button2 = new MyButton("Medium");
		MyButton button3 = new MyButton("Hard");
		MyButton button4 = new MyButton("Main Menu");

		JPanel newGamePanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("src/main/resources/background.jpg").getImage(), -100, -100, null); 
			}
		};

		newGamePanel.setLayout(new GridLayout(0, 1, 5, 5));
		newGamePanel.setBorder(new EmptyBorder(100, 30, 100, 30));

		button1.addActionListener(easybutton);
		button2.addActionListener(mediumbutton);
		button3.addActionListener(hardbutton);
		button4.addActionListener(returnMainbutton);

		newGamePanel.add(button1);
		newGamePanel.add(button2);
		newGamePanel.add(button3);
		newGamePanel.add(button4);

		return newGamePanel;
	}

	public JPanel createHelpScreen() {

		MyButton button1 = new MyButton("How To Play");
		MyButton button2 = new MyButton("Resumes");
		MyButton button3 = new MyButton("Save");
		MyButton button4 = new MyButton("Return to Main Menu");

		JPanel helpScreenPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("/src/main/resources/background.jpg").getImage(), -100, -100, null); 
			}
		};

		helpScreenPanel.setLayout(new GridLayout(0, 1));
		helpScreenPanel.setBorder(new EmptyBorder(100, 30, 100, 30));

		button1.addActionListener(infobutton);
		button2.addActionListener(resumebutton);
		button3.addActionListener(savebutton);
		button4.addActionListener(returnMainbutton);

		helpScreenPanel.add(button1);
		helpScreenPanel.add(button2);
		helpScreenPanel.add(button3);
		helpScreenPanel.add(button4);

		return helpScreenPanel;
	}

	private JPanel createPauseScreen() {

		JPanel pauseScreen = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("src/main/resources/background.jpg").getImage(), -100, -100, null); 
			}
		};
		pauseScreen.setLayout(new GridLayout(0, 1));
		pauseScreen.setBorder(new EmptyBorder(50, 30, 100, 30));

		MyButton button1 = new MyButton("Resume");
		MyButton button2 = new MyButton("Restart");
		MyButton button3 = new MyButton("Give Up");

		button1.addActionListener(resumebutton);
		button2.addActionListener(restartbutton);
		button3.addActionListener(endbutton);

		pauseScreen.add(button1);
		pauseScreen.add(button2);
		pauseScreen.add(button3);

		return pauseScreen;
	}

	private JPanel createMazeScreen() {
		MyButton button1 = new MyButton("Help");
		MyButton button2 = new MyButton("Save");
		MyButton button3 = new MyButton("Hint");
		MyButton button4 = new MyButton("Main Menu");
		MyButton button5 = new MyButton("Pause");

		JPanel mazePanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("src/main/resources/background.jpg").getImage(), -100, -100, null); 
			}
		};

		mazePanel.setLayout(new GridLayout(0, 5, 0, 0));
		mazePanel.setBorder(new EmptyBorder(0, 0, 400, 0));

		button1.addActionListener(helpbutton);
		button2.addActionListener(savebutton);
		button3.addActionListener(hintbutton);
		button4.addActionListener(returnMainbutton);
		button5.addActionListener(pausebutton);

		mazePanel.add(button1);
		mazePanel.add(button2);
		mazePanel.add(button3);
		mazePanel.add(button4);
		mazePanel.add(button5);

		return mazePanel;

	}

	private JPanel createEndScreen() {
		MyButton button1 = new MyButton("Replay");
		MyButton button2 = new MyButton("New Game");
		MyButton button3 = new MyButton("Main Menu");

		JPanel endGamePanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("src/main/resources/background.jpg").getImage(), -100, -100, null); 
			}
		};

		endGamePanel.setLayout(new GridLayout(0, 1));
		endGamePanel.setBorder(new EmptyBorder(100, 30, 300, 30));

		button1.addActionListener(replaybutton);
		button2.addActionListener(newGamebutton);
		button3.addActionListener(returnMainbutton);

		endGamePanel.add(button1);
		endGamePanel.add(button2);
		endGamePanel.add(button3);

		return endGamePanel;
	}

}
