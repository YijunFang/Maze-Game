package screen;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Common.Difficulty;
import GameRep.Game;
import screen.TimerPanel;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	private CardLayout cardLayout = new CardLayout();
	private JPanel mainMenu;
	private JPanel newGame;
	private JPanel mazeScreen;
	private JPanel pauseScreen;
	// private JPanel helpScreen;
	private JPanel winEndScreen;
	private JPanel quitEndScreen;

	private boolean checkGameWon = false;
	private boolean gameRunning = false;
	private boolean saveFlag = false;
	private JPanel maze;
	private CardLayout mazeLayout;

	private Game currGame;
	private JFrame noticBox;
	private TimerPanel timerPanel;
	private JPanel coinPanel;
	private int coinNumber;
	private int difficulty;

	private boolean globalPaint = false;
	// LOAD IMAGE

	public MainPanel() {
		setMaximumSize(new Dimension(1000, 800));

		setLayout(cardLayout);

		createMainMenu();
		add(mainMenu, "mainMenu");
		cardLayout.show(this, "mainMenu");
//		System.out.println("+");

		createNewGame();
		add("newGame", newGame);
//		System.out.println("+");

		mazeScreen = createMazeScreen();
		add("mazeScreen", mazeScreen);
//		System.out.println("+");

		createPauseScreen();
		add("pauseScreen", pauseScreen);
//		System.out.println("+");

	}

	private KeyEventDispatcher formKeyEventDispatcher() {
		KeyEventDispatcher ked = new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent ke) {
				synchronized (Game.class) {
					switch (ke.getID()) {
					case KeyEvent.KEY_PRESSED:
						switch (ke.getKeyCode()) {
						case KeyEvent.VK_W:
							currGame.keyPressedUp();
							checkState();
							break;
						case KeyEvent.VK_A:
							currGame.keyPressedLeft();
							checkState();
							break;
						case KeyEvent.VK_S:
							currGame.keyPressedDown();
							checkState();
							break;
						case KeyEvent.VK_D:
							currGame.keyPressedRight();
							checkState();
							break;
						}
						break;
					}
					return false;
				}
			}
		};
		return ked;
	}

	private void checkState() {
		// check game won
//		System.out.println(currGame.isGameWon());
		if (currGame.isGameWon() == true) {
//			System.out.println("enter here?");
			checkGameWon = true;
			String resultTime = timerPanel.totalTime();
			timerPanel.clearTimer();
			String resultScore = ((screen.coinPanel) coinPanel).format();
			((screen.coinPanel) coinPanel).clearCoin();

			

//			if(this.getComponentCount() >4){
//				this.remove(5);
//			}
			winEndScreen = createWinEndScreen(resultTime, resultScore);
			add("winEndScreen", winEndScreen);
			cardLayout.show(this, "winEndScreen");
			deleteGame();
			currGame.stop();
			debug();

		} else {
//			System.out.println(currGame.getNumCoins());
			((screen.coinPanel) coinPanel).updateCoin(currGame.getNumCoins());
		}
	}

	public void createMainMenu() {
//		System.out.println(true);
		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 0, 0, null);
			}
		};
		titlePanel.setOpaque(false);

		mainMenu = new JPanel();
		mainMenu.setOpaque(false);
		mainMenu.setFocusable(false);
		mainMenu.setBorder(new EmptyBorder(100, 200, 100, 200));
		mainMenu.setMinimumSize(new Dimension(1000, 800));

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1, 15, 15));
		component.add(new Button("New Game", this, "newgame.png", 595, 75));
		component.add(new Button("Continue", this, "continue.png", 595, 75));
		component.add(new Button("How To Play", this, "howtoplay.png", 595, 75));
		component.add(new Button("Quit", this, "quitgame.png", 595, 75));

		GroupLayout gl_mainMenu = new GroupLayout(mainMenu);
		gl_mainMenu.setHorizontalGroup(gl_mainMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainMenu.createSequentialGroup().addContainerGap()
						.addGroup(gl_mainMenu.createParallelGroup(Alignment.LEADING)
								.addComponent(titlePanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 595,
										Short.MAX_VALUE)
								.addComponent(component, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 595,
										Short.MAX_VALUE))
						.addGap(0)));
		gl_mainMenu.setVerticalGroup(gl_mainMenu.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainMenu.createSequentialGroup().addContainerGap()
						.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE).addGap(0)
						.addComponent(component, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE).addGap(0)));

		gl_mainMenu.setAutoCreateContainerGaps(true);
		mainMenu.setLayout(gl_mainMenu);

	}
	
	private void createNewGame() {

		newGame = new JPanel();
		newGame.setOpaque(false);
		newGame.setFocusable(false);
		newGame.setBorder(new EmptyBorder(100, 200, 100, 200));
		newGame.setMinimumSize(new Dimension(1000, 800));

		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 0, 0, null);
			}
		};
		titlePanel.setOpaque(false);

//		JLabel title = new JLabel("Select Level", JLabel.CENTER);
//		title.setFont(new Font("Courier New", Font.BOLD, 50));
//		title.setSize(new Dimension(150, 60));

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1, 15, 15));

		component.add(new Button("Easy", this, "easy.png", 595, 75));
		component.add(new Button("Medium", this, "medium.png", 595, 75));
		component.add(new Button("Hard", this, "hard.png", 595, 75));
		component.add(new Button("Main Menu", this, "mainmenu.png", 595, 75));

		GroupLayout gl_newGame = new GroupLayout(newGame);
		gl_newGame.setHorizontalGroup(gl_newGame.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_newGame.createSequentialGroup().addContainerGap()
						.addGroup(gl_newGame.createParallelGroup(Alignment.LEADING)
								.addComponent(titlePanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 595,
										Short.MAX_VALUE)
								.addComponent(component, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 595,
										Short.MAX_VALUE))
						.addGap(0)));
		gl_newGame.setVerticalGroup(gl_newGame.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_newGame.createSequentialGroup().addContainerGap()
						.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE).addGap(0)
						.addComponent(component, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE).addGap(0)));

		gl_newGame.setAutoCreateContainerGaps(true);
		
		newGame.setLayout(gl_newGame);
	}

//	private void createNewGame() {
//
//		newGame = new JPanel();
//		newGame.setOpaque(false);
//		newGame.setFocusable(false);
//		newGame.setBorder(new EmptyBorder(100, 200, 100, 200));
//		newGame.setMinimumSize(new Dimension(1000, 800));
//
//		JPanel titlePanel = new JPanel() {
//			@Override
//			public void paintComponent(Graphics g) {
//				super.paintComponent(g);
//				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 0, 0, null);
//			}
//		};
//		titlePanel.setOpaque(false);
//
//		JLabel title = new JLabel("Select Level", JLabel.CENTER);
//		title.setFont(new Font("Courier New", Font.BOLD, 50));
//		title.setSize(new Dimension(150, 60));
//
//		JPanel component = new JPanel();
//		component.setOpaque(false);
//		component.setLayout(new GridLayout(0, 1, 10, 10));
//
//		component.add(new Button("Easy", this, "easy.png", 595, 75));
//		component.add(new Button("Medium", this, "medium.png", 595, 75));
//		component.add(new Button("Hard", this, "hard.png", 595, 75));
//		component.add(new Button("Main Menu", this, "mainmenu.png", 595, 75));
//
//		GroupLayout gl_newGame = new GroupLayout(newGame);
//		gl_newGame
//				.setHorizontalGroup(
//						gl_newGame.createParallelGroup(Alignment.TRAILING)
//								.addGroup(gl_newGame.createSequentialGroup()
//										.addGroup(gl_newGame.createParallelGroup(Alignment.LEADING)
//												.addComponent(component, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
//												.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 595,
//														Short.MAX_VALUE)
//												.addComponent(title, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE))
//										.addContainerGap()));
//		gl_newGame.setVerticalGroup(gl_newGame.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_newGame.createSequentialGroup()
//						.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE).addGap(0)
//						.addComponent(title).addComponent(component, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
//						.addGap(0)));
//		newGame.setLayout(gl_newGame);
//	}

	private JFrame askHelp() {

		JFrame help = new JFrame("How to Play");
		help.setUndecorated(true);
		help.pack();

		help.setMinimumSize(new Dimension(800, 800));
		help.setResizable(true);
		help.setLocationRelativeTo(null);
		help.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		help.setVisible(true);

		JPanel mainTextArea = new JPanel(); //{
//			public void paintComponent(Graphics g) {
//				super.paintComponent(g);
//				g.drawImage(new ImageIcon("background.png").getImage(), -200, -50, null);
//			}
//		};
		
		

		mainTextArea.setLayout(new BoxLayout(mainTextArea, BoxLayout.Y_AXIS));
		mainTextArea.setBackground(Color.GRAY);
		mainTextArea.setBorder(new EmptyBorder(50, 30, 30, 30));

		JPanel grid = new JPanel();

		grid.setLayout(new GridLayout(0, 2));
		grid.setOpaque(false);

		JLabel intro = new JLabel("<html><p>You're a zombie and it has been a week since you have had some delicious villager brain</p></html>");
		intro.setFont(new Font("Courier New", Font.BOLD, 18));
		intro.setForeground(Color.white);
		grid.add(intro);
		
		ImageIcon zombie = new ImageIcon(getClass().getResource("zombiebrain.png"));
		grid.add(new JLabel(zombie));
		
		JLabel goal= new JLabel("<html><p>Trouble is, the villager's hiding somewhere in the maze and you have got to use your puzzle solving skills to get to him</p></html>");
		goal.setFont(new Font("Courier New", Font.BOLD, 18));
		goal.setForeground(Color.white);
		grid.add(goal);
		
		ImageIcon villager = new ImageIcon(getClass().getResource("villagerhelp.png"));
		grid.add(new JLabel(villager));
		
		JLabel controlText = new JLabel(
				"<html><p>Use the W key to move up, the A key to move left, the S key to move down and and D key to move right</p></html>");
		controlText.setFont(new Font("Courier New", Font.BOLD, 18));
		controlText.setForeground(Color.white);
		grid.add(controlText);
		
		ImageIcon controls = new ImageIcon(getClass().getResource("controls.png"));
		grid.add(new JLabel(controls));
		
		JLabel enderText = new JLabel("<html><p>If you come across an eye of ender, you can use it to show some portion of the correct path</p></html>");
		enderText.setFont(new Font("Courier New", Font.BOLD, 18));
		enderText.setForeground(Color.white);
		grid.add(enderText);
		
		ImageIcon eye = new ImageIcon(getClass().getResource("eyeofenderhelp.png"));
		grid.add(new JLabel(eye));

		mainTextArea.setVisible(true);
		mainTextArea.add(grid);
		mainTextArea.setForeground(Color.WHITE);

		JPanel box = new JPanel();

		box.setLayout(new BoxLayout(box, BoxLayout.X_AXIS));

		box.add(new Button("OK", this, null, 300, 15));
		box.setOpaque(false);

		mainTextArea.add(box);

		help.getContentPane().add(mainTextArea);

		return help;
	}

	private void createPauseScreen() {

		pauseScreen = new JPanel();
		pauseScreen.setOpaque(false);
		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 0, 0, null);
			}
		};
		titlePanel.setOpaque(false);

		pauseScreen.setLayout(new GridLayout(0, 1));
		pauseScreen.setBorder(new EmptyBorder(80, 200, 60, 200));

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1, 7, 7));

		component.add(new Button("How To Play", this, "howtoplay.png", 595, 70));
		component.add(new Button("Resume", this, "continue.png", 595, 70));
		component.add(new Button("Save", this, null, 595, 70));
		component.add(new Button("Give Up", this, "giveup.png", 595, 70));
		component.add(new Button("Return to Main Menu", this, "mainmenu.png", 595, 70));

		GroupLayout gl_pauseScreen = new GroupLayout(pauseScreen);
		gl_pauseScreen.setHorizontalGroup(gl_pauseScreen.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pauseScreen.createSequentialGroup().addContainerGap()
						.addGroup(gl_pauseScreen.createParallelGroup(Alignment.LEADING)
								.addComponent(titlePanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 44,
										Short.MAX_VALUE)
								.addComponent(component, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 44,
										Short.MAX_VALUE))
						.addGap(0)));
		gl_pauseScreen.setVerticalGroup(gl_pauseScreen.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pauseScreen.createSequentialGroup().addContainerGap()
						.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE).addGap(0)
						.addComponent(component, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE).addGap(0)));
		gl_pauseScreen.setAutoCreateContainerGaps(true);
		pauseScreen.setLayout(gl_pauseScreen);

	}

	private JPanel createWinEndScreen(String resultTime, String resultCoin) {
		saveFlag = false;
		gameRunning = false;
		
//		here
		System.out.println(difficulty);
		
		JPanel endScreen = new JPanel();
		
		endScreen = new JPanel();
		endScreen.setOpaque(false);
		endScreen.setFocusable(false);
		endScreen.setBorder(new EmptyBorder(100, 150, 100, 150));
		endScreen.setMinimumSize(new Dimension(1000, 800));

		

		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image oldImage = null;
				try {
					oldImage = getImage("wintitle.png");
				} catch (IOException e) {
					e.printStackTrace();
				}
				Image newNewgame = oldImage.getScaledInstance(700, 120,java.awt.Image.SCALE_SMOOTH);
				g.drawImage(newNewgame, 0, 0, null);
			}
		};
		titlePanel.setOpaque(false);

//		JLabel title = new JLabel("YOU WIN", JLabel.CENTER);
//		title.setFont(new Font("Courier New", Font.BOLD, 30));
//		title.setSize(new Dimension(150, 60));

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1, 10, 10));
		component.setBorder(new EmptyBorder(0, 50, 0, 50));

		component.add(new Button("New Game", this, "newgame.png", 595, 75));
		component.add(new Button("Main Menu", this, "mainmenu.png", 595, 75));

		JPanel ScorePanel = new JPanel();
		ScorePanel.setOpaque(false);
		//ScorePanel.setLayout(new GridLayout(1, 2));
		ScorePanel.setLayout(new GridBagLayout());
		GridBagConstraints endScreenConstraints = new GridBagConstraints();

		JLabel showCoin = new JLabel("COINS: " + resultCoin, JLabel.CENTER);
		showCoin.setFont(new Font("Courier New", Font.BOLD, 25));
		endScreenConstraints.weightx = 0.5;
		endScreenConstraints.fill = GridBagConstraints.HORIZONTAL;
		endScreenConstraints.gridx = 0;
		endScreenConstraints.gridy = 0;
		ScorePanel.add(showCoin, endScreenConstraints);
		
		JLabel showTime = new JLabel(resultTime, JLabel.CENTER);
		showTime.setFont(new Font("Courier New", Font.BOLD, 25));
		endScreenConstraints.weightx = 0.5;
		endScreenConstraints.fill = GridBagConstraints.HORIZONTAL;
		endScreenConstraints.gridx = 1;
		endScreenConstraints.gridy = 0;
		ScorePanel.add(showTime, endScreenConstraints);
		
		String[] timeSplit = resultTime.split(":");
		int hours = Integer.parseInt(timeSplit[0]);
		int minutes = Integer.parseInt(timeSplit[1]);
		int seconds = Integer.parseInt(timeSplit[2]);
		
		int coinsInt = Integer.parseInt(resultCoin);
		
		int timeInt = (hours*60) + (minutes) + (seconds/60);
		System.out.println("time output:" + timeInt);
		//long scoreCalc = Math.round(((Math.exp(-timeInt))+coinsInt)*10)*10;
		
		int initial = determineDifficulty();
		
		int scoreCalc = initial - (timeInt)*10 + coinsInt;
		System.out.println("score output:" + scoreCalc);
		
		JLabel showScore = new JLabel(("\n\nYour Score is:" + scoreCalc + "!"), JLabel.CENTER);
		showScore.setFont(new Font("Courier New", Font.BOLD, 32));
		endScreenConstraints.weightx = 0.5;
		endScreenConstraints.fill = GridBagConstraints.HORIZONTAL;
		endScreenConstraints.gridwidth = 2;
		endScreenConstraints.gridx = 0;
		endScreenConstraints.gridy = 1;
		ScorePanel.add(showScore, endScreenConstraints);
		
		GroupLayout gl_endScreen = new GroupLayout(endScreen);
		gl_endScreen
				.setHorizontalGroup(
						gl_endScreen.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_endScreen.createSequentialGroup()
										.addGroup(gl_endScreen.createParallelGroup(Alignment.LEADING)
												.addComponent(ScorePanel, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
												.addComponent(component, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
												.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 595,Short.MAX_VALUE))
										.addContainerGap()));
		gl_endScreen.setVerticalGroup(gl_endScreen.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_endScreen.createSequentialGroup()
						.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
						.addGap(0)
						.addComponent(ScorePanel, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
						.addGap(0)
						.addComponent(component, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
						.addGap(0)));
		endScreen.setLayout(gl_endScreen);
		
		
		return endScreen;
	}
	
	private int determineDifficulty() {
		if (difficulty == 1) {
			return 500;
		} else if (difficulty == 2) {
			return 1000;
		} else {
			return 2000;
		}
	}

	private JPanel createQuitEndScreen(String resultTime, String resultScore) {
		saveFlag = false;
		gameRunning = false;
		
		JPanel endScreen = new JPanel();
		
		endScreen = new JPanel();
		endScreen.setOpaque(false);
		endScreen.setFocusable(false);
		endScreen.setBorder(new EmptyBorder(100, 200, 100, 200));
		endScreen.setMinimumSize(new Dimension(1000, 800));

		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("losetitle.png").getImage(), 100, 0, null);
			}
		};
		titlePanel.setOpaque(false);

//		JLabel title = new JLabel("YOU LOOSE", JLabel.CENTER);
//		title.setFont(new Font("Courier New", Font.BOLD, 30));
//		title.setSize(new Dimension(150, 60));

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1, 10, 10));

		component.add(new Button("New Game", this, "newgame.png", 595, 75));
		component.add(new Button("Main Menu", this, "mainmenu.png", 595, 75));

		JPanel ScorePanel = new JPanel();
		ScorePanel.setOpaque(false);
		ScorePanel.setLayout(new GridLayout(1, 2));

		JLabel showCoin = new JLabel(resultScore, JLabel.CENTER);
		showCoin.setFont(new Font("Courier New", Font.BOLD, 25));
		ScorePanel.add(showCoin);
		JLabel showTime = new JLabel(resultTime, JLabel.CENTER);
		showTime.setFont(new Font("Courier New", Font.BOLD, 25));
		ScorePanel.add(showTime);
		
		GroupLayout gl_endScreen = new GroupLayout(endScreen);
		gl_endScreen
				.setHorizontalGroup(
						gl_endScreen.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_endScreen.createSequentialGroup()
										.addGroup(gl_endScreen.createParallelGroup(Alignment.LEADING)
												.addComponent(ScorePanel, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
												.addComponent(component, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
												.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 595,Short.MAX_VALUE))
										.addContainerGap()));
		gl_endScreen.setVerticalGroup(gl_endScreen.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_endScreen.createSequentialGroup()
						.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
						.addGap(0)
						.addComponent(ScorePanel, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
						.addGap(0)
						.addComponent(component, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
						.addGap(0)));
		endScreen.setLayout(gl_endScreen);
		
		endScreen.add(component,JPanel.BOTTOM_ALIGNMENT);

		return endScreen;
	}

	private JPanel createMazeScreen() {
		coinNumber = 0;

		JPanel mazePanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("background.jpg").getImage(), -200, -50, null);
			}
		};

		// mazePanel.setBorder(new EmptyBorder(1, 2, 0, 0));

		JPanel component = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("sidebackground.jpg").getImage(), -10, 0, null);
			}
		};
		component.setOpaque(false);
		component.setLayout(new GridLayout(6, 1, 10, 10));
		component.setSize(new Dimension(200, 800));
		// component.add(new Button("Help", this,null, 595, 75));
		component.add(new Button("Save", this, null, 200, 120));
		component.add(new Button("Hint", this, null, 200, 120));
		component.add(new Button("Pause", this, null, 200, 120));
		component.add(new Button("Main Menu", this, null, 200, 120));

		// timer here!!
		timerPanel = new TimerPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image oldImg = new ImageIcon("labelbackground.png").getImage();
				Image newNewgame = oldImg.getScaledInstance(300, 120, java.awt.Image.SCALE_SMOOTH);
				while ((g.drawImage(newNewgame, 0, 0, null)) != true)
					;
			}
		};
		timerPanel.setOpaque(false);
		component.add(timerPanel);

		coinPanel = new coinPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image oldImg = new ImageIcon("labelbackground.png").getImage();
				Image newNewgame = oldImg.getScaledInstance(300, 120, java.awt.Image.SCALE_SMOOTH);
				while ((g.drawImage(newNewgame, 0, 0, null)) != true)
					;
			}
		};
		coinPanel.setOpaque(false);
		component.add(coinPanel);

		maze = new JPanel();
		maze.setSize(new Dimension(800, 800));
		mazeLayout = new CardLayout();
		maze.setLayout(mazeLayout);
		maze.setOpaque(false);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = null;

		mazePanel.setLayout(gridbag);
		c = setGridBagConstraints(0, 0, 16, 16, 1, 1, GridBagConstraints.BOTH);
		mazePanel = addComponent(mazePanel, maze, gridbag, c);
//		 c = setGridBagConstraints(16, 0, 16, 4, 1, 1,GridBagConstraints.BOTH);
		c = setGridBagConstraints(18, 0, 16, 2, 1, 1, GridBagConstraints.BOTH);

		mazePanel = addComponent(mazePanel, component, gridbag, c);

//		System.out.println(component.getWidth() + " " + component.getHeight());

		return mazePanel;
	}

	private JPanel addComponent(JPanel jPanel, JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
		gridbag.setConstraints(comp, c);
		jPanel.add(comp);

		return jPanel;
	}

	private GridBagConstraints setGridBagConstraints(int gridx, int gridy, int gridheight, int gridwidth,
			double weightx, double weighty, int fill) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridheight = gridheight;
		c.gridwidth = gridwidth;
		c.weightx = gridwidth;
		c.weighty = weighty;
		c.fill = fill;

		return c;
	}

	KeyEventDispatcher ked = null;
	KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();

	private void startNewGame(Difficulty difficulty) {
		// if there is a saved game, delete it
		deleteGame();

		currGame = new Game();

		ked = formKeyEventDispatcher();
		checkGameWon = false;
		// currGame.setKeyDetect(ked);

		kfm.addKeyEventDispatcher(ked);

		currGame.start(difficulty);
		currGame.setOpaque(true);
		// maze.setOpaque(false);
		maze.add("currGame", currGame);

		saveFlag = false;
		gameRunning = true;

		cardLayout.show(this, "mazeScreen");

		timerPanel.startTimer();
	}

	private void closeNoticeBox() {
		if (noticBox == null)
			return;
		if (noticBox.getComponentCount() != 0) {
			noticBox.setVisible(false);
//			System.out.println(noticBox.getComponentCount());
			noticBox.removeAll();
//			System.out.println(noticBox.getComponentCount());

		}
	}

	private void continueGame() {
		if (saveFlag == false) {
			cardLayout.show(this, "newGame");
			return;
		}
		if (maze.getComponentCount() != 0) {
			currGame.pauseGame(false);
			saveFlag = false;
			gameRunning = true;
			cardLayout.show(this, "mazeScreen");
			kfm.addKeyEventDispatcher(ked);
			timerPanel.startTimer();
		}

		else
			return;
	}

	private void deleteGame() {
		difficulty = 0;
		kfm.removeKeyEventDispatcher(ked);
		if (currGame != null) {
			currGame.stop();
		}
		if (maze.getComponentCount() != 0) {
			maze.remove(0);
//			System.out.println(maze.getComponentCount());
		}
	}

	public JFrame askNewGame() {

		JFrame notice = new JFrame("New Game");
		notice.setUndecorated(true);
		notice.pack();
		notice.setResizable(false);
		notice.setAlwaysOnTop(true);
		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);

		JLabel title = new JLabel("<html><p>Are you sure? This will delete your saved game</p></html>", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 15));
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		notice.getContentPane().add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		component.add(new Button("Sure", this, null,  notice.getWidth()/3, notice.getWidth()/2));
		component.add(new Button("Continue Saved Game", this, null,  notice.getWidth()/3, notice.getWidth()/2));

		notice.getContentPane().add(component);
		notice.getContentPane().setLayout(new GridLayout(2, 1));

		return notice;

	}

	public JFrame askSaveGame() {

		JFrame notice = new JFrame("Game Not Saved");
		notice.setUndecorated(true);
		
		notice.setResizable(false);
		notice.setAlwaysOnTop(true);

		notice.setMinimumSize(new Dimension(400, 300));
		notice.pack();
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);

		JLabel title = new JLabel("Save Game?", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 15));
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		notice.getContentPane().add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		
		//button sizes are: 133 150 for all these dialog box
		component.add(new Button("Save Game", this, null, notice.getWidth()/3, notice.getWidth()/2));
		component.add(new Button("Don't Save", this, null, notice.getWidth()/3, notice.getWidth()/2));
		component.add(new Button("Resume", this, null, notice.getWidth()/3, notice.getWidth()/2));
		
		System.out.println(notice.getWidth()/3+" "+ notice.getHeight()/2);
		
		notice.getContentPane().add(component);
		notice.getContentPane().setLayout(new GridLayout(2, 1));

		return notice;

	}

	public JFrame askGiveUp() {

		JFrame notice = new JFrame("Give Up");
		notice.setUndecorated(true);
		notice.pack();
		notice.setResizable(false);
		notice.setAlwaysOnTop(true);
		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);

		JLabel title = new JLabel("Give Up Game?", JLabel.CENTER);
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		title.setFont(new Font("Courier New", Font.BOLD, 15));
		notice.getContentPane().add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		component.add(new Button("Yes", this, null,  notice.getWidth()/3, notice.getWidth()/2));
		component.add(new Button("Resume", this, null, notice.getWidth()/3, notice.getWidth()/2));

		notice.getContentPane().add(component);
		notice.getContentPane().setLayout(new GridLayout(2, 1));

		return notice;

	}

	private void debug() {
		String info = "--------------------\n";
		info += "gameRunning = " + gameRunning + "\n";
		info += "saveFlag = " + saveFlag + "\n";
		if (noticBox != null) {
			info += "noticBox Size = " + noticBox.getComponentCount() + "\n";

		}

		System.out.println(info);

	}

	public class Button extends JButton {

		private Image oldImage = null;
		private int buttonWidth = 0;
		private int buttonHight = 0;

		public Button(String text, final JPanel parentPanel, String imgName, int width, int height) {

			super(text);
			 
			setFont(new Font("Courier New", Font.BOLD, 15));
			Dimension d = new Dimension(width, height);
			setSize(d);
			setOpaque(false);
			setContentAreaFilled(false);
			setBorderPainted(false);

			if (imgName == null) {
//				globalPaint = false;
				oldImage = null;

			} else {
				try {
					oldImage = getImage(imgName);
					globalPaint = true;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			buttonWidth = width;
			buttonHight = height;

			if (oldImage == null)
				System.out.println(text +" null");

			if (text.equals("New Game")) {
				
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {
							if (saveFlag) {
								noticBox = askNewGame();
							} else {
								cardLayout.show(parentPanel, "newGame");
							}
							debug();
						}
					}
				});

			} else if (text.equals("Sure")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						closeNoticeBox();
						cardLayout.show(parentPanel, "newGame");
						debug();
					}
				});

			} else if (text.equals("Easy")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {
							startNewGame(Difficulty.EASY);
							difficulty = 1;
							debug();
						}
					}

				});

			} else if (text.equals("Medium")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {
							startNewGame(Difficulty.MEDIUM);
							difficulty = 2;
							debug();
						}
					}
				});

			} else if (text.equals("Hard")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {
							startNewGame(Difficulty.HARD);
							difficulty = 3;
							debug();
						}
					}
				});

			} else if (text.equals("Main Menu") || text.equals("Return to Main Menu")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						if (noticBox == null || noticBox.getComponentCount() == 0) {
							if (!saveFlag && gameRunning) {
								// timerPanel.pauseTimer();
								noticBox = askSaveGame();
							} else {
								gameRunning = false;
								cardLayout.show(parentPanel, "mainMenu");
							}
							debug();
						}
					}
				});

			} else if (text.equals("Resume")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						closeNoticeBox();
						timerPanel.startTimer();
						// set timer start
						saveFlag = false;
						gameRunning = true;
						currGame.pauseGame(false);
						cardLayout.show(parentPanel, "mazeScreen");
						debug();
					}
				});

			} else if (text.equals("Restart") || text.equals("Replay")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// currGame.restart();
						// cardLayout.show(parentPanel, "mazeScreen");
						// debug();
					}
				});

			} else if (text.equals("Continue") || text.equals("Continue Saved Game")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {
							// timerPanel.startTimer();
							// gameRunning = true;
							// closeNoticeBox();
							continueGame();
							debug();
						}
					}
				});

			} else if (text.equals("Give Up")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {
							// ask if really give up
							noticBox = askGiveUp();
							debug();
						}
					}
				});

			} else if (text.equals("Pause")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {
							// timer pause
							timerPanel.pauseTimer();
							gameRunning = true;
							currGame.pauseGame(true);
							cardLayout.show(parentPanel, "pauseScreen");
							// kfm.removeKeyEventDispatcher(ked);
							debug();
						}
					}
				});

			} else if (text.equals("Save")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {
							// timer pause and save in buffer
							timerPanel.pauseTimer();
							saveFlag = true;
							// JOptionPane.showMessageDialog(parentPanel, "GAME
							// SAVED!", "Save Game",
							// JOptionPane.INFORMATION_MESSAGE);
							kfm.removeKeyEventDispatcher(ked);
							gameRunning = false;
							cardLayout.show(parentPanel, "mainMenu");
							debug();
						}
					}
				});

			} else if (text.equals("Save Game")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						// timer pause and save in buffer
						timerPanel.pauseTimer();

						// JOptionPane.showMessageDialog(parentPanel, "GAME
						// SAVED!", "Save Game",
						// JOptionPane.INFORMATION_MESSAGE);

						closeNoticeBox();
						saveFlag = true;
						 kfm.removeKeyEventDispatcher(ked);
						gameRunning = false;
						cardLayout.show(parentPanel, "mainMenu");
						debug();

					}
				});

			} else if (text.equals("Don't Save")) {
				addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						timerPanel.clearTimer();
						closeNoticeBox();
						deleteGame();
						saveFlag = false;
						gameRunning = false;
						cardLayout.show(parentPanel, "mainMenu");
						debug();

					}
				});

			} else if (text.equals("How To Play")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						noticBox = askHelp();
						debug();
						
					}
				});

			} else if (text.equals("Quit")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});

			} else if (text.equals("Hint")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {
							if (currGame.getNumCoins() > 0) {
								currGame.hintCoinActivated();
							}
						}
					}
				});

			} else if (text.equals("Yes")) {
				addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						closeNoticeBox();

						String resultTime = timerPanel.totalTime();
						timerPanel.clearTimer();
						String resultScore = ((screen.coinPanel) coinPanel).format();
						
						deleteGame();

						quitEndScreen = createQuitEndScreen(resultTime, resultScore);
//						if(parentPanel.getComponentCount() >4){
//							parentPanel.remove(5);
//						}
//							System.out.println("not create before"+parentPanel.getComponentCount() );
						parentPanel.add("quitEndScreen", quitEndScreen);
						cardLayout.show(parentPanel, "quitEndScreen");
						debug();

					}
				});
			} else if (text.equals("OK")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						closeNoticeBox();
						// System.out.println(parentPanel.getComponentZOrder(newGame));
					}
				});

			} else {
				System.out.println(text + "button not implemented yet! FIX");
			}

		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
//			if (globalPaint) {
				if (oldImage != null) {
					Image newNewgame = oldImage.getScaledInstance(buttonWidth, buttonHight,
							java.awt.Image.SCALE_SMOOTH);
					g.drawImage(newNewgame, 0, 0, null);

				}
//			}
		};

		
	}

	/**
	 * Gets the image from file and returns the image so that it can be used in
	 * the program
	 * 
	 * @param fileName
	 *            the name of the image file
	 * @return the image
	 */
	private Image getImage(String fileName) throws IOException {
		Image img = null;
		if (fileName == null)
			return null;
		try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

}
