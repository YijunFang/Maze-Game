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

		//creates the New Game Screen and adds the screen to the Card Layout
		createNewGame();
		add("newGame", newGame);

		//creates the Game Screen and adds the screen to the Card Layout
		mazeScreen = createMazeScreen();
		add("mazeScreen", mazeScreen);

		//creates the Pause Screen and adds the screen to the Card Layout
		createPauseScreen();
		add("pauseScreen", pauseScreen);

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
		if (currGame.isGameWon() == true) {
			//sets GameWon boolean to be true
			//retrieves the total time taken to finish the maze
			String resultTime = timerPanel.totalTime();
			//resets the timer
			timerPanel.clearTimer();
			//retrieves the total coins collected
			String resultScore = ((screen.coinPanel) coinPanel).format();
			//resets the number of coins collected
			((screen.coinPanel) coinPanel).clearCoin();

			//creates the Win Screen and adds the screen to the Card Layout
			winEndScreen = createWinEndScreen(resultTime, resultScore);
			add("winEndScreen", winEndScreen);
			cardLayout.show(this, "winEndScreen");
			//stop and delete the current game
			deleteGame();
			currGame.stop();

		} else {
			//update the number of coins
			((screen.coinPanel) coinPanel).updateCoin(currGame.getNumCoins());
		}
	}

	/**
	 * Creates the Main Menu Screen
	 */
	public void createMainMenu() {
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
				g.drawImage(new ImageIcon("spruceplankbig.png").getImage(), 0, 0, null);
			}
		};
		
		
		//sets layout of content Panel to Box Layout and sets color and padding properties
		mainTextArea.setLayout(new BoxLayout(mainTextArea, BoxLayout.Y_AXIS));
		//mainTextArea.setBorder(new EmptyBorder(50, 50, 50, 50));
		mainTextArea.setVisible(true);
		mainTextArea.setForeground(Color.WHITE);

		//creates new Grid Layout panel and sets transparent background
		JPanel grid = new JPanel();
		grid.setBorder(new EmptyBorder(50, 50, 50, 50));
		grid.setLayout(new GridLayout(0, 2));
		grid.setOpaque(false);

		//adds set of instructions and accompanying image, sets properties to standard help text properties and adds to Grid panel
		JLabel introText = new JLabel("<html><p>You're a zombie and it has been a week since you have had some delicious villager brain</p></html>");
		grid.add(helpTextProperties(introText));	
		JLabel zombie = helpImgProperties("zombiebrain.png");
		grid.add(zombie);
		
		JLabel goalText = new JLabel("<html><p>Trouble is, the villager's hiding somewhere in the maze and you have got to use your puzzle solving skills to get to him</p></html>");
		grid.add(helpTextProperties(goalText));
		JLabel villager = helpImgProperties("villagerhelp.png");
		grid.add(villager);
		
		JLabel controlText = new JLabel("<html><p>Use the W key to move up, the A key to move left, the S key to move down and and D key to move right</p></html>");
		grid.add(helpTextProperties(controlText));
		JLabel controls = helpImgProperties("controls.png");
		grid.add(controls);
		
		JLabel enderText = new JLabel("<html><p>If you come across an eye of ender, you can use it to show some portion of the correct path</p></html>");
		grid.add(helpTextProperties(enderText));
		JLabel eye = helpImgProperties("eyeofenderhelp.png");
		grid.add(eye);

		//adds the grid to the mainPanel
		mainTextArea.add(grid);
	
		//creates a new box panel for the button and sets various properties
		JPanel box = new JPanel();
		box.setLayout(new GridLayout(0,1));
		box.setOpaque(false);
		box.add(new Button("OK", this, "continue.png", 800, 100));

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
	 * Sets JLabel graphics properties for the images on the help screen
	 * @param imgFile
	 * @return
	 */
	private JLabel helpImgProperties (final String imgFile) {
		JLabel img = new JLabel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon(imgFile).getImage(), 140, 50, null);
			}
		};
		
		return img;
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
		
		//shows time on the end screen
		JLabel showTime = new JLabel(resultTime, JLabel.CENTER);
		
		showTime.setFont(new Font("Arial", Font.BOLD, 25));
		endScreenConstraints.weightx = 0.5;
		endScreenConstraints.fill = GridBagConstraints.HORIZONTAL;
		endScreenConstraints.gridx = 1;
		endScreenConstraints.gridy = 0;
		ScorePanel.add(showTime, endScreenConstraints);
		
		//parses time string to split seconds, minutes and hours as integers
		String[] timeSplit = resultTime.split(":");
		int minutes = Integer.parseInt(timeSplit[0]);
		int seconds = Integer.parseInt(timeSplit[1]);
		
		//parses coin string to get the number of coins as an integer
		int coinsInt = Integer.parseInt(resultCoin);
		
		//converts the time into the number of seconds taken to complete game
		int timeInt = (minutes*60) + (seconds);
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
		
		component.add(new Button("Hint", this, "mainpanelHint.png", 210, 120));
		component.add(new Button("Pause", this, "mainpanelPause.png", 210, 120));
		component.add(new Button("Main Menu", this, "mainpanelMainMenu.png", 210, 120));
		component.add(new Button("", this, "mainpanelTransparent.png", 210, 120));

		// shows timer
		timerPanel = new TimerPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image oldImg = new ImageIcon("timelabelbackground.png").getImage();
				Image newNewgame = oldImg.getScaledInstance(210, 120, java.awt.Image.SCALE_SMOOTH);
				while ((g.drawImage(newNewgame, 0, 0, null)) != true)
					;
			}
		};
		timerPanel.setOpaque(false);
		component.add(timerPanel);

		//shows eyes of enders
		coinPanel = new coinPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image oldImg = new ImageIcon("coinlabelbackground.png").getImage();
				Image newNewgame = oldImg.getScaledInstance(210, 120, java.awt.Image.SCALE_SMOOTH);
				while ((g.drawImage(newNewgame, 0, 0, null)) != true)
					;
			}
		};
		coinPanel.setOpaque(false);
		component.add(coinPanel);

		//creates maze
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
		c = setGridBagConstraints(18, 0, 16, 2, 1, 1, GridBagConstraints.BOTH);

		mazePanel = addComponent(mazePanel, component, gridbag, c);

		return mazePanel;
	}

	/**
	 * Adds component to the mazegame screen
	 * @param jPanel
	 * @param comp
	 * @param gridbag
	 * @param c
	 * @return
	 */
	private JPanel addComponent(JPanel jPanel, JComponent comp, GridBagLayout gridbag, GridBagConstraints c) {
		gridbag.setConstraints(comp, c);
		jPanel.add(comp);

		return jPanel;
	}

	/**
	 * sets grid bag constraints for maze game screen
	 * @param gridx
	 * @param gridy
	 * @param gridheight
	 * @param gridwidth
	 * @param weightx
	 * @param weighty
	 * @param fill
	 * @return
	 */
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

		kfm.addKeyEventDispatcher(ked);

		currGame.start(difficulty);
		currGame.setOpaque(true);
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
			noticBox.removeAll();
		}
	}

	/**
	 * Continues a saved game if present, otherwise prompts to create a new game
	 */
	private void continueGame() {
		
		if (saveFlag == false) {
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
		}
	}

	/**
	 * Prompts the user to delete their saved game
	 * @return a dialog box acting as the prompt
     */
	public JFrame askNewGame() {

		//create new dialog box for confirming deletion of saved game
		JFrame notice = new JFrame("New Game");
		notice.setUndecorated(true);
		notice.pack();
		notice.setResizable(false);
		notice.setAlwaysOnTop(true);
		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);
		
		//creation of panel that will be contained by dialog box
		JPanel gameDialog = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("spruceplankbig.png").getImage(), -200, -50, null);
			}
		};
		gameDialog.setLayout(new GridLayout(2, 1));
		gameDialog.setVisible(true);
		gameDialog.setForeground(Color.WHITE);

		//text prompt
		JLabel title = new JLabel("<html><p>Are you sure? This will delete your saved game</p></html>", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 25));
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		title.setForeground(Color.white);
		gameDialog.add(title);

		//panel for buttons
		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));

		component.add(new Button("Sure", this, "dialogueYes.png",  notice.getWidth()/2, notice.getHeight()/2));
		component.add(new Button("NO", this, "dialogueNo.png",  notice.getWidth()/2, notice.getHeight()/2));

		gameDialog.add(component);

		notice.add(gameDialog);
		return notice;

	}

	/**
	 * Prompts the user to save their game before returning to the main menu
	 * @return a dialog box acting as the prompt
     */
	public JFrame askSaveGame() {

		//new frame for save game dialog
		JFrame notice = new JFrame("Game Not Saved");
		notice.setUndecorated(true);
		
		notice.setResizable(false);
		notice.setAlwaysOnTop(true);

		notice.setMinimumSize(new Dimension(400, 300));
		notice.pack();
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);
		
		//new panel for the save game dialog
		JPanel gameDialog = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("spruceplankbig.png").getImage(), -200, -50, null);
			}
		};
		
		gameDialog.setLayout(new GridLayout(2, 1));
		gameDialog.setVisible(true);
		gameDialog.setForeground(Color.WHITE);

		//text for save game dialog
		JLabel title = new JLabel("Do You Want To Save Game", JLabel.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 25));
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		title.setForeground(Color.WHITE);
		gameDialog.add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		
		//button sizes are: 200 150 for all these dialog box
		component.add(new Button("Save Game", this, "dialogueYes.png", notice.getWidth()/2 , notice.getHeight()/2 ));
		component.add(new Button("Don't Save", this, "dialogueNo.png", notice.getWidth()/2, notice.getHeight()/2));
		gameDialog.add(component);
		
		notice.add(gameDialog);

		return notice;

	}

	/**
	 * Prompts the user if they really want to give up
	 * @return a dialog box acting as the prompt
     */
	public JFrame askGiveUp() {

		//new frame for give up confirmation dialog
		JFrame notice = new JFrame("Give Up");
		notice.setUndecorated(true);
		notice.pack();
		notice.setResizable(false);
		notice.setAlwaysOnTop(true);
		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);
		
		//new panel for content of frame
		JPanel gameDialog = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("spruceplankbig.png").getImage(), -200, -50, null);
			}
		};
		
		gameDialog.setLayout(new GridLayout(2, 1));
		gameDialog.setVisible(true);
		gameDialog.setForeground(Color.WHITE);

		//content for the panel
		JLabel title = new JLabel("Give Up Game?", JLabel.CENTER);
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		title.setFont(new Font("Arial", Font.BOLD, 15));
		title.setForeground(Color.white);
		gameDialog.add(title);

		//buttons for the panel
		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));

		component.add(new Button("Yes", this, "dialogueYes.png",  notice.getWidth()/2, notice.getHeight()/2));
		component.add(new Button("Resume", this, "dialogueNo.png", notice.getWidth()/2, notice.getHeight()/2));
		gameDialog.add(component);
		notice.add(gameDialog);
		return notice;

	}

	/**
	 * 
	 * Button class
	 *
	 */
	public class Button extends JButton {

		private Image oldImage = null;
		private int buttonWidth = 0;
		private int buttonHight = 0;

		public Button(String text, final JPanel parentPanel, String imgName, int width, int height) {

			super(text);
			
			//set standard button properties
//			setFont(new Font("Arial", Font.BOLD, 15));
			Dimension d = new Dimension(width, height);
			setSize(d);
			setOpaque(false);
			setContentAreaFilled(false);
			setBorderPainted(false);
			
			
			if (imgName == null) {
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

			//different behaviours for different buttons
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
						}
					}
				});

			} else if (text.equals("Sure")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						closeNoticeBox();
						cardLayout.show(parentPanel, "newGame");
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
					}
				});

			} else if (text.equals("Continue") || text.equals("Continue Saved Game")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (noticBox == null || noticBox.getComponentCount() == 0) {

							continueGame();
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
							gameRunning = false;
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
						gameRunning = false;

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
					}
				});

			} else if (text.equals("How To Play")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						noticBox = askHelp();
						
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
						parentPanel.add("quitEndScreen", quitEndScreen);
						cardLayout.show(parentPanel, "quitEndScreen");

					}
				});
			} else if (text.equals("OK")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						closeNoticeBox();
					}
				});

			}else if (text.equals("NO")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						closeNoticeBox();
					}
				});

			}

		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
				if (oldImage != null) {
					Image newNewgame = oldImage.getScaledInstance(buttonWidth, buttonHight,
							java.awt.Image.SCALE_SMOOTH);
					g.drawImage(newNewgame, 0, 0, null);

				}
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