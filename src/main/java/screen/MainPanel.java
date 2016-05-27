package screen;

import java.awt.CardLayout;
import java.awt.Color;
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

	//creates Card Layout for the Panel
	private CardLayout cardLayout = new CardLayout();
	
	//creates the screens required as JPanels
	private JPanel mainMenu;
	private JPanel newGame;
	private JPanel mazeScreen;
	private JPanel pauseScreen;
	private JPanel winEndScreen;
	private JPanel quitEndScreen;
	private JPanel maze;
	private CardLayout mazeLayout;

	//various boolean conditions used by GUI
	private boolean gameRunning = false;
	private boolean saveFlag = false;

	private Game currGame;
	private JFrame noticBox;
	private TimerPanel timerPanel;
	private JPanel coinPanel;
	private int difficulty;

	// LOAD IMAGE

	/**
	 * Constructor for the MainPnael
	 */
	public MainPanel() {
		
		//set the maximum size of the window
		setMaximumSize(new Dimension(1000, 800));

		//sets layout to the card layout field;
		setLayout(cardLayout);

		//creates the Main Menu Screen and adds the screen to the Card Layout
		createMainMenu();
		add(mainMenu, "mainMenu");
		cardLayout.show(this, "mainMenu");
//		System.out.println("+");

		//creates the New Game Screen and adds the screen to the Card Layout
		createNewGame();
		add("newGame", newGame);
//		System.out.println("+");

		//creates the Game Screen and adds the screen to the Card Layout
		mazeScreen = createMazeScreen();
		add("mazeScreen", mazeScreen);
//		System.out.println("+");

		//creates the Pause Screen and adds the screen to the Card Layout
		createPauseScreen();
		add("pauseScreen", pauseScreen);
//		System.out.println("+");

	}

	/**
	 * Receives keyboard events
	 * @return Key Event Dispatcher
	 */
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

	/**
	 * Checks if the game has been won (and creates win screen) or updates the number of coins
	 */
	private void checkState() {
		// check game won
//		System.out.println(currGame.isGameWon());
		if (currGame.isGameWon() == true) {
//			System.out.println("enter here?");
			//sets GameWon boolean to be true
			//retrieves the total time taken to finish the maze
			String resultTime = timerPanel.totalTime();
			//resets the timer
			timerPanel.clearTimer();
			//retrieves the total coins collected
			String resultScore = ((screen.coinPanel) coinPanel).format();
			//resets the number of coins collected
			((screen.coinPanel) coinPanel).clearCoin();

			

//			if(this.getComponentCount() >4){
//				this.remove(5);
//			}
			//creates the Win Screen and adds the screen to the Card Layout
			winEndScreen = createWinEndScreen(resultTime, resultScore);
			add("winEndScreen", winEndScreen);
			cardLayout.show(this, "winEndScreen");
			//stop and delete the current game
			deleteGame();
			currGame.stop();
			debug();

		} else {
//			System.out.println(currGame.getNumCoins());
			//update the number of coins
			((screen.coinPanel) coinPanel).updateCoin(currGame.getNumCoins());
		}
	}

	/**
	 * Creates the Main Menu Screen
	 */
	public void createMainMenu() {
//		System.out.println(true);
		
		//creates panel for the main menu and sets its opacity, padding and size properties
		mainMenu = new JPanel();
		mainMenu.setOpaque(false);
		mainMenu.setFocusable(false);
		mainMenu.setBorder(new EmptyBorder(100, 200, 100, 200));
		mainMenu.setMinimumSize(new Dimension(1000, 800));
		
		//Paints the main title "Mazecraft"and makes the panel background transparent
		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 0, 0, null);
			}
		};
		titlePanel.setOpaque(false);

		//creates the buttons on the Main Menu screen and adds to a Grid Layout Panel called component
		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1, 15, 15));
		component.add(new Button("New Game", this, "newgame.png", 595, 75));
		component.add(new Button("Continue", this, "continue.png", 595, 75));
		component.add(new Button("How To Play", this, "howtoplay.png", 595, 75));
		component.add(new Button("Quit", this, "quitgame.png", 595, 75));

		//sets layout of the Main Menu panel to the Group Layout and adds the respective layouts
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
	
	/**
	 * Creates New Game Screen
	 */
	private void createNewGame() {

		//creates panel for the new game screen and sets its opacity, padding and size properties
		newGame = new JPanel();
		newGame.setOpaque(false);
		newGame.setFocusable(false);
		newGame.setBorder(new EmptyBorder(100, 200, 100, 200));
		newGame.setMinimumSize(new Dimension(1000, 800));

		//Paints the main title "Mazecraft"and makes the panel background transparent
		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 0, 0, null);
			}
		};
		titlePanel.setOpaque(false);

//		JLabel title = new JLabel("Select Level", JLabel.CENTER);
//		title.setFont(new Font("Arial", Font.BOLD, 50));
//		title.setSize(new Dimension(150, 60));

		//creates the buttons on the Main Menu screen and adds to a Grid Layout Panel called component
		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1, 15, 15));

		component.add(new Button("Easy", this, "easy.png", 595, 75));
		component.add(new Button("Medium", this, "medium.png", 595, 75));
		component.add(new Button("Hard", this, "hard.png", 595, 75));
		component.add(new Button("Main Menu", this, "mainmenu.png", 595, 75));

		//sets layout of the New Game panel to the Group Layout and adds the respective layouts
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
//		title.setFont(new Font("Arial", Font.BOLD, 50));
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

	/**
	 * Creates the How to Help dialog box
	 * @return How to Help JFrame
	 */
	private JFrame askHelp() {

		//creates a new JFrame for the Help dialog and sets title bar, size, resizeable and visibility properties
		JFrame help = new JFrame("How to Play");
		help.setUndecorated(true);
		help.pack();

		help.setMinimumSize(new Dimension(800, 800));
		help.setResizable(true);
		help.setLocationRelativeTo(null);
		help.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		help.setVisible(true);

		//creates a new Panel for the content of the screen
		JPanel mainTextArea = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("helpboxback.png").getImage(), -200, -50, null);
			}
		};
		
		
		//sets layout of content Panel to Box Layout and sets color and padding properties
		mainTextArea.setLayout(new BoxLayout(mainTextArea, BoxLayout.Y_AXIS));
		//mainTextArea.setBorder(new EmptyBorder(50, 50, 50, 50));
		mainTextArea.setVisible(true);
		mainTextArea.setForeground(Color.WHITE);

		//creates new Grid Layout panel and sets transparent background
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(0, 2));
		grid.setOpaque(false);

		//adds first set of instructions and accompanying image, sets properties to standard help text properties and adds to Grid panel
		JLabel introText = new JLabel("<html><p>You're a zombie and it has been a week since you have had some delicious villager brain</p></html>");
		grid.add(helpTextProperties(introText));
		
		
		
		ImageIcon zombie = new ImageIcon(getClass().getResource("zombiebrain.png"));
		grid.add(new JLabel(zombie));
		
		JLabel goalText = new JLabel("<html><p>Trouble is, the villager's hiding somewhere in the maze and you have got to use your puzzle solving skills to get to him</p></html>");
		grid.add(helpTextProperties(goalText));
		ImageIcon villager = new ImageIcon(getClass().getResource("villagerhelp.png"));
		grid.add(new JLabel(villager));	
		
		JLabel controlText = new JLabel("<html><p>Use the W key to move up, the A key to move left, the S key to move down and and D key to move right</p></html>");
		grid.add(helpTextProperties(controlText));
		ImageIcon controls = new ImageIcon(getClass().getResource("controls.png"));
		grid.add(new JLabel(controls));
		
		JLabel enderText = new JLabel("<html><p>If you come across an eye of ender, you can use it to show some portion of the correct path</p></html>");
		grid.add(helpTextProperties(enderText));
		ImageIcon eye = new ImageIcon(getClass().getResource("eyeofenderhelp.png"));
		grid.add(new JLabel(eye));

		//adds the grid to the mainPanel
		mainTextArea.add(grid);
	
		//creates a new box panel for the button and sets various properties
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.X_AXIS));
		box.setOpaque(false);
		box.add(new Button("OK", this, null, 300, 15));

		//adds the box button layout to the Content panel
		mainTextArea.add(box);
		
		//adds Content panel to the Help Frame
		help.getContentPane().add(mainTextArea);

		return help;
	}
	
	/**
	 * Sets JLabel text properties to standard font and color properties
	 * @param text the help text on the help screen
	 * @return help text JLabel
	 */
	private JLabel helpTextProperties (JLabel text) {
		text.setFont(new Font("Arial", Font.BOLD, 18));
		text.setForeground(Color.white);
		
		return text;
	}

	/**
	 * Creates the Pause Screen
	 */
	private void createPauseScreen() {

		//creates new Grid Layout Panel for the pause screen and sets transparency and padding properties
		pauseScreen = new JPanel();
		pauseScreen.setLayout(new GridLayout(0, 1));
		pauseScreen.setOpaque(false);
		pauseScreen.setBorder(new EmptyBorder(80, 200, 60, 200));
		
		//creates a new panel for the title paints the "Mazecraft" image and sets transparency properties
		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 0, 0, null);
			}
		};
		titlePanel.setOpaque(false);

		//creates new Grid Layout Panel for the buttons and sets transparency properties
		JPanel component = new JPanel();
		component.setLayout(new GridLayout(0, 1, 7, 7));
		component.setOpaque(false);

		//creates and adds the buttons to the component panel
		component.add(new Button("How To Play", this, "howtoplay.png", 595, 70));
		component.add(new Button("Resume", this, "continue.png", 595, 70));
		component.add(new Button("Save", this, "save.png", 595, 70));
		component.add(new Button("Give Up", this, "giveup.png", 595, 70));
		component.add(new Button("Return to Main Menu", this, "returntomainmenu.png", 595, 70));

		//sets layout of the Pause Game panel to the Group Layout and adds the respective layouts
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

	/**
	 * Create End Screen when player has won their game
	 * @param resultTime
	 * @param resultCoin
	 * @return Win Screen Panel
	 */
	private JPanel createWinEndScreen(String resultTime, String resultCoin) {
		
		//sets save and game running booleans to false
		saveFlag = false;
		gameRunning = false;
		
//		System.out.println(difficulty);
		
		//creates new panel for the End Screen and sets transparency, padding and size properties
		JPanel endScreen = new JPanel();
		endScreen = new JPanel();
		endScreen.setOpaque(false);
		endScreen.setFocusable(false);
		endScreen.setBorder(new EmptyBorder(100, 150, 100, 150));
		endScreen.setMinimumSize(new Dimension(1000, 800));

		//creates new panel for the title of the end screen and scales down the image to fit the window
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
//		title.setFont(new Font("Arial", Font.BOLD, 30));
//		title.setSize(new Dimension(150, 60));

		//creates new Grid Layout panel for the buttons on the end screen and sets transparency properties
		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1, 10, 10));
		component.setBorder(new EmptyBorder(0, 50, 0, 50));

		//creates and adds buttons for the end screen to the component panel
		component.add(new Button("New Game", this, "newgame.png", 595, 75));
		component.add(new Button("Main Menu", this, "mainmenu.png", 595, 75));

		//create new Grid Bag Layout panel to contain stats and score
		JPanel ScorePanel = new JPanel();
		ScorePanel.setOpaque(false);
		//ScorePanel.setLayout(new GridLayout(1, 2));
		ScorePanel.setLayout(new GridBagLayout());
		GridBagConstraints endScreenConstraints = new GridBagConstraints();

		//creates a new Label for the number of coins and places in the first cell, first row
		JLabel showCoin = new JLabel("Eyes of Ender: " + resultCoin, JLabel.CENTER);
		showCoin.setFont(new Font("Arial", Font.BOLD, 25));
		endScreenConstraints.weightx = 0.5;
		endScreenConstraints.fill = GridBagConstraints.HORIZONTAL;
		endScreenConstraints.gridx = 0;
		endScreenConstraints.gridy = 0;
		ScorePanel.add(showCoin, endScreenConstraints);
		
		//creates a new label for the time and places in the second cell, first row
//		Image eyeold = new ImageIcon("ender_eye.png").getImage();
//		Image newNewgame = eyeold.getScaledInstance(50, 50,java.awt.Image.SCALE_SMOOTH);
//		
//		JPanel imgEye = new JPanel(){
//			public void paintComponent(Graphics g) {
//				super.paintComponent(g);
//				g.drawImage(newNewgame, 0, 0, null);
//			}
//		};
		
		JLabel showTime = new JLabel(resultTime, JLabel.CENTER);
		
//		JPanel cointhing =  new JPanel();
//		cointhing.setOpaque(false);
//		cointhing.add(imgEye, JPanel.LEFT_ALIGNMENT);
//		cointhing.add(showTime, JPanel.RIGHT_ALIGNMENT);
		
		showTime.setFont(new Font("Arial", Font.BOLD, 25));
		endScreenConstraints.weightx = 0.5;
		endScreenConstraints.fill = GridBagConstraints.HORIZONTAL;
		endScreenConstraints.gridx = 1;
		endScreenConstraints.gridy = 0;
		ScorePanel.add(showTime, endScreenConstraints);
		
		//parses time string to split seconds, minutes and hours as integers
		String[] timeSplit = resultTime.split(":");
		int hours = Integer.parseInt(timeSplit[0]);
		int minutes = Integer.parseInt(timeSplit[1]);
		int seconds = Integer.parseInt(timeSplit[2]);
		
		//parses coin string to get the number of coins as an integer
		int coinsInt = Integer.parseInt(resultCoin);
		
		//converts the time into the number of seconds taken to complete game
		int timeInt = (hours*60*60) + (minutes*60) + (seconds);
		//long scoreCalc = Math.round(((Math.exp(-timeInt))+coinsInt)*10)*10;
		
		//determines base score for different difficulties
		int initial = determineDifficulty();
		
		//calculates the score to be shown
		int scoreCalc = initial - (timeInt)*10 + coinsInt;
		scoreCalc = (scoreCalc < 0) ? 0 : scoreCalc;
		
		//creates new label for the score and places in the second row
		JLabel showScore = new JLabel(("Your Score is:" + scoreCalc + "!"), JLabel.CENTER);
		showScore.setFont(new Font("Arial", Font.BOLD, 32));
		endScreenConstraints.weightx = 0.5;
		endScreenConstraints.fill = GridBagConstraints.HORIZONTAL;
		endScreenConstraints.gridwidth = 2;
		endScreenConstraints.gridx = 0;
		endScreenConstraints.gridy = 1;
		ScorePanel.add(showScore,endScreenConstraints ); 
		
		//sets layout of the End Game panel to the Group Layout and adds the respective layouts
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
	
	/**
	 * Determines the base score according to difficulty
	 * @return base score according to difficulty
	 */
	private int determineDifficulty() {
		if (difficulty == 1) {
			return 600;
		} else if (difficulty == 2) {
			return 3600;
		} else {
			return 80000;
		}
	}

	/**
	 * Creates the End Screen when player gives up the game
	 * @param resultTime
	 * @param resultScore
	 * @return
	 */
	private JPanel createQuitEndScreen(String resultTime, String resultScore) {
		
		//sets save and game running booleans to false
		saveFlag = false;
		gameRunning = false;
		
		//creates new panel for the end screen and transparency, padding and size properties
		JPanel endScreen = new JPanel();
		endScreen = new JPanel();
		endScreen.setOpaque(false);
		endScreen.setFocusable(false);
		endScreen.setBorder(new EmptyBorder(100, 200, 100, 200));
		endScreen.setMinimumSize(new Dimension(1000, 800));

		//creates new panel for the title of the end screen
		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("losetitle.png").getImage(), 100, 0, null);
			}
		};
		titlePanel.setOpaque(false);

//		JLabel title = new JLabel("YOU LOSE", JLabel.CENTER);
//		title.setFont(new Font("Arial", Font.BOLD, 30));
//		title.setSize(new Dimension(150, 60));

		//creates new Grid Layout panel for the buttons on the end screen and sets transparency properties
		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1, 10, 10));

		//creates and adds buttons for the end screen to the component panel
		component.add(new Button("New Game", this, "newgame.png", 595, 75));
		component.add(new Button("Main Menu", this, "mainmenu.png", 595, 75));

		//create new Grid Bag Layout panel to contain stats
		JPanel ScorePanel = new JPanel();
		ScorePanel.setOpaque(false);
		ScorePanel.setLayout(new GridLayout(1, 2));

		/*
		//Creates labels for the number of coins and time, sets font and adds to the grid
		JLabel showCoin = new JLabel(resultScore, JLabel.CENTER);
		showCoin.setFont(new Font("Arial", Font.BOLD, 25));
		ScorePanel.add(showCoin);
		JLabel showTime = new JLabel(resultTime, JLabel.CENTER);
		showTime.setFont(new Font("Arial", Font.BOLD, 25));
		ScorePanel.add(showTime);
		*/
		
		//sets layout of the End Game panel to the Group Layout and adds the respective layouts
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

	/**
	 * Create actual maze green screen
	 * @return Maze Game Panel
	 */
	private JPanel createMazeScreen() {

		//creates new maze panel and paint background with graphic
		JPanel mazePanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("background.jpg").getImage(), -200, -50, null);
			}
		};

		// mazePanel.setBorder(new EmptyBorder(1, 2, 0, 0));

		//creates new panel for the side menu and paints background with graphic
		JPanel component = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("sidebackground.jpg").getImage(), -10, 0, null);
			}
		};
		//sets layout of component to Grid Layout and 
		component.setOpaque(false);
		component.setLayout(new GridLayout(6, 1, 10, 10));
		component.setSize(new Dimension(200, 800));
		// component.add(new Button("Help", this,null, 595, 75));
		
		component.add(new Button("Hint", this, "mainpanelHint.png", 220, 120));
		component.add(new Button("Pause", this, "mainpanelPause.png", 220, 120));
		component.add(new Button("Main Menu", this, "mainpanelMainMenu.png", 220, 120));
		component.add(new Button("", this, "mainpanelTransparent.png", 220, 120));

		// timer here!!
		timerPanel = new TimerPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image oldImg = new ImageIcon("timelabelbackground.png").getImage();
				Image newNewgame = oldImg.getScaledInstance(220, 120, java.awt.Image.SCALE_SMOOTH);
				while ((g.drawImage(newNewgame, 0, 0, null)) != true)
					;
			}
		};
		timerPanel.setOpaque(false);
		component.add(timerPanel);

		coinPanel = new coinPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image oldImg = new ImageIcon("coinlabelbackground.png").getImage();
				Image newNewgame = oldImg.getScaledInstance(220, 120, java.awt.Image.SCALE_SMOOTH);
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

	/**
	 * Starts a new game based on the given difficulty
	 * @param difficulty a Difficulty enum representing the difficulty of the new game
     */
	private void startNewGame(Difficulty difficulty) {
		// if there is a saved game, delete it
		deleteGame();

		currGame = new Game();

		ked = formKeyEventDispatcher();
		// currGame.setKeyDetect(ked);

		kfm.addKeyEventDispatcher(ked);

		currGame.start(difficulty);
		currGame.setOpaque(true);
		// maze.setOpaque(false);
		maze.add("currGame", currGame);

		saveFlag = false;
		gameRunning = true;

		cardLayout.show(this, "mazeScreen");

		timerPanel.clearTimer();
		timerPanel.startTimer();
	}

	/**
	 * Closes any visible notice boxes
	 */
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

	/**
	 * Continues a saved game if present, otherwise prompts to create a new game
	 */
	private void continueGame() {
		

		if(currGame == null){
			currGame = new Game();
			long time = currGame.load();
			
			if(time != -1){
				
				ked = formKeyEventDispatcher();

				kfm.addKeyEventDispatcher(ked);

				currGame.setOpaque(true);
				maze.add("currGame", currGame);

				saveFlag = false;
				gameRunning = true;

				cardLayout.show(this, "mazeScreen");
				
				timerPanel.setStartTime(time);
				
				timerPanel.startTimer();
				
			}
			currGame = null;
		}
		
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

	/**
	 * Deletes a currently running or saved game
	 */
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

	/**
	 * Prompts the user to delete their saved game
	 * @return a dialog box acting as the prompt
     */
	public JFrame askNewGame() {

		JFrame notice = new JFrame("New Game");
		
		JPanel gameDialog = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("dirt.png").getImage(), -200, -50, null);
			}
		};
		
		gameDialog.setLayout(new GridLayout(2, 1));
		//gameDialog.setBorder(new EmptyBorder(50, 50, 50, 50));
		gameDialog.setVisible(true);
		gameDialog.setForeground(Color.WHITE);
		
		notice.setUndecorated(true);
		notice.pack();
		notice.setResizable(false);
		notice.setAlwaysOnTop(true);
		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);

		JLabel title = new JLabel("<html><p>Are you sure? This will delete your saved game</p></html>", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 25));
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		//notice.getContentPane().add(title);
		gameDialog.add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));

		component.add(new Button("Sure", this, "dialogueSure.png",  notice.getWidth()/3, notice.getHeight()/2));
		component.add(new Button("Continue Saved Game", this, null,  notice.getWidth()/3, notice.getHeight()/2));

		gameDialog.add(component);
//		notice.getContentPane().add(component);
//		notice.getContentPane().setLayout(new GridLayout(2, 1));

		notice.add(gameDialog);
		return notice;

	}

	/**
	 * Prompts the user to save their game before returning to the main menu
	 * @return a dialog box acting as the prompt
     */
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
		title.setFont(new Font("Arial", Font.BOLD, 25));
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		notice.getContentPane().add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		
		//button sizes are: 200 150 for all these dialog box
		component.add(new Button("Save Game", this, null, notice.getWidth()/2 , notice.getHeight()/2 ));
		component.add(new Button("Don't Save", this, null, notice.getWidth()/2, notice.getHeight()/2));
//		component.add(new Button("Resume", this, null, notice.getWidth()/2, notice.getHeight()/2));
		
		
		notice.getContentPane().add(component);
		notice.getContentPane().setLayout(new GridLayout(2, 1));

		return notice;

	}

	/**
	 * Prompts the user if they really want to give up
	 * @return a dialog box acting as the prompt
     */
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
		
		JPanel gameDialog = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("helpboxback.png").getImage(), -200, -50, null);
			}
		};
		
		gameDialog.setLayout(new GridLayout(2, 1));
		gameDialog.setBorder(new EmptyBorder(50, 50, 50, 50));
		gameDialog.setVisible(true);
		gameDialog.setForeground(Color.WHITE);

		JLabel title = new JLabel("Give Up Game?", JLabel.CENTER);
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		title.setFont(new Font("Arial", Font.BOLD, 15));
		//notice.getContentPane().add(title);
		gameDialog.add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));

		component.add(new Button("Yes", this, null,  notice.getWidth()/2, notice.getHeight()/2));
		component.add(new Button("Resume", this, null, notice.getWidth()/2, notice.getHeight()/2));
		gameDialog.add(component);
//		notice.getContentPane().add(component);
//		notice.getContentPane().setLayout(new GridLayout(2, 1));
		notice.add(gameDialog);
		return notice;

	}

	private void debug() {/*
		String info = "--------------------\n";
		info += "gameRunning = " + gameRunning + "\n";
		info += "saveFlag = " + saveFlag + "\n";
		if (noticBox != null) {
			info += "noticBox Size = " + noticBox.getComponentCount() + "\n";

		}

//		System.out.println(info);
 **/

	}

	public class Button extends JButton {

		private Image oldImage = null;
		private int buttonWidth = 0;
		private int buttonHight = 0;

		public Button(String text, final JPanel parentPanel, String imgName, int width, int height) {

			super(text);
			 
			setFont(new Font("Arial", Font.BOLD, 15));
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
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			buttonWidth = width;
			buttonHight = height;

//			if (oldImage == null)
//				System.out.println(text +" null");

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
							checkState();
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
							checkState();
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
							checkState();
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

			} else if (text.equals("Continue") || text.equals("Continue Saved Game")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {

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
							kfm.removeKeyEventDispatcher(ked);
							cardLayout.show(parentPanel, "mainMenu");
//							currGame.save(timerPanel.toSaveTime());
							gameRunning = false;
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

						closeNoticeBox();
						saveFlag = true;
						kfm.removeKeyEventDispatcher(ked);
						cardLayout.show(parentPanel, "mainMenu");
//						currGame.save(timerPanel.toSaveTime());
						gameRunning = false;
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
								checkState();
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
