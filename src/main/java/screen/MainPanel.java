package screen;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Common.Difficulty;
import GameRep.Game;
import screen.TimerPanel;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

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
	private TimerPanel timerPanel; //= new TimerPanel()
	private JPanel coinPanel;
	private int coinNumber;
	
	public MainPanel() {
		setLayout(cardLayout);		
		
		createMainMenu();
		add(mainMenu, "mainMenu");
		cardLayout.show(this, "mainMenu");
		
		createNewGame(); 
		add("newGame", newGame);
		
		mazeScreen = createMazeScreen();
		add("mazeScreen", mazeScreen);
		
		createPauseScreen();
		add("pauseScreen", pauseScreen);

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
		//check game won
		if(currGame.isGameWon() == true){
			checkGameWon = true;
			JLabel resultTime = timerPanel.saveTimer();
			timerPanel.clearTimer();
			deleteGame();

			winEndScreen = createWinEndScreen(resultTime);
			add("winEndScreen", winEndScreen);
			cardLayout.show(this, "winEndScreen");
			debug();
			
		}
		else{
			System.out.println(currGame.getNumCoins());
			((screen.coinPanel) coinPanel).updateCoin(currGame.getNumCoins());
		}
		
	}




	public void createMainMenu() {
		
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
		component.add(new Button("New Game", this));
		component.add(new Button("Continue", this));
		component.add(new Button("How To Play", this));
		component.add(new Button("Quit", this));		

		GroupLayout gl_mainMenu = new GroupLayout(mainMenu);
		gl_mainMenu.setHorizontalGroup(
			gl_mainMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainMenu.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainMenu.createParallelGroup(Alignment.LEADING)
						.addComponent(titlePanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
						.addComponent(component, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
					.addGap(0))
		);
		gl_mainMenu.setVerticalGroup(
			gl_mainMenu.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainMenu.createSequentialGroup()
					.addContainerGap()
					.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
					.addComponent(component, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
					.addGap(47))
		);
		
		gl_mainMenu.setAutoCreateContainerGaps(true);
		mainMenu.setLayout(gl_mainMenu);
	
	}

	private  void createNewGame() {

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
		
		JLabel title = new JLabel("Select Level", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 50));

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1, 10, 10));
		component.add(new Button("Easy", this));
		component.add(new Button("Medium", this));
		component.add(new Button("Hard", this));
		component.add(new Button("Main Menu", this));
		
		
		GroupLayout gl_newGame = new GroupLayout(newGame);
		gl_newGame.setHorizontalGroup(
			gl_newGame.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_newGame.createSequentialGroup()
					.addGroup(gl_newGame.createParallelGroup(Alignment.LEADING)
						.addComponent(component, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
						.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
						.addComponent(title, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_newGame.setVerticalGroup(
			gl_newGame.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_newGame.createSequentialGroup()
					.addContainerGap()
					.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
					.addComponent(title)
					.addComponent(component, GroupLayout.DEFAULT_SIZE, 6, Short.MAX_VALUE)
					.addContainerGap())
		);
		newGame.setLayout(gl_newGame);
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
		component.setLayout(new GridLayout(0, 1,10,10));
		
		component.add(new Button("How To Play", this));
		component.add(new Button("Resume", this));
		component.add(new Button("Restart", this));
		component.add(new Button("Save", this));
		component.add(new Button("Give Up", this));
		component.add(new Button("Return to Main Menu", this));

		GroupLayout gl_pauseScreen = new GroupLayout(pauseScreen);
		gl_pauseScreen.setHorizontalGroup(
				gl_pauseScreen.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pauseScreen.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pauseScreen.createParallelGroup(Alignment.LEADING)
						.addComponent(titlePanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
						.addComponent(component, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
					.addGap(0))
		);
		gl_pauseScreen.setVerticalGroup(
				gl_pauseScreen.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pauseScreen.createSequentialGroup()
					.addContainerGap()
					.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
					.addComponent(component, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
					.addGap(47))
		);
		gl_pauseScreen.setAutoCreateContainerGaps(true);
		pauseScreen.setLayout(gl_pauseScreen);

	}
	
	private JPanel createWinEndScreen(JLabel resultTime) {	
		saveFlag = false;
		gameRunning = false;

		JPanel endScreen = new JPanel();

		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 0, 0, null);
			}
		};
		titlePanel.setOpaque(false);
		endScreen.add(titlePanel, JPanel.TOP_ALIGNMENT);
			
		// endScreen.setBackground(Color.BLACK);
		endScreen.setLayout(new GridLayout(3, 1));
		endScreen.setBorder(new EmptyBorder(100, 30, 100, 30));


		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1));
	
		component.add(new Button("Replay", this));
		component.add(new Button("New Game", this));
		component.add(new Button("Main Menu", this));
		
		
		JPanel ScorePanel = new JPanel();
		ScorePanel.setOpaque(false);
		ScorePanel.setLayout(new GridLayout(1, 2));
		
//		int coin=currGame.
		JLabel showCoin = new JLabel("SHOULD SHOW COINS", JLabel.CENTER);
		ScorePanel.add(showCoin);
		JLabel showTime = new JLabel("SHOULD SHOW Time", JLabel.CENTER);
		ScorePanel.add(showTime);
		
		endScreen.add(ScorePanel,JPanel.CENTER_ALIGNMENT);
		
		 endScreen.add(component,JPanel.BOTTOM_ALIGNMENT);

		return endScreen;
	}
	

	private JPanel createQuitEndScreen(JLabel resultTime) {
		saveFlag = false;
		gameRunning = false;

		
		JPanel endScreen = new JPanel() ;

		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 0, 0, null);
			}
		};
		titlePanel.setOpaque(false);
		endScreen.add(titlePanel, JPanel.TOP_ALIGNMENT);
			
		// endScreen.setBackground(Color.BLACK);
		endScreen.setLayout(new GridLayout(3, 1));
		endScreen.setBorder(new EmptyBorder(100, 30, 100, 30));

//		pauseScreen.setLayout(new GridLayout(0, 1));
//		pauseScreen.setBorder(new EmptyBorder(50, 30, 100, 30));

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1));
	
		component.add(new Button("Replay", this));
		component.add(new Button("New Game", this));
		component.add(new Button("Main Menu", this));
		
		
		JPanel ScorePanel = new JPanel();
		ScorePanel.setOpaque(false);
		ScorePanel.setLayout(new GridLayout(1, 2));
		
		JLabel showCoin = new JLabel("SHOULD SHOW COINS", JLabel.CENTER);
		ScorePanel.add(showCoin);
		JLabel showTime = new JLabel("SHOULD SHOW Time", JLabel.CENTER);
		ScorePanel.add(showTime);
		
		endScreen.add(ScorePanel,JPanel.CENTER_ALIGNMENT);
		
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
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = null;

		mazePanel.setSize(getWidth(), getHeight());
		mazePanel.setLayout(gridbag);
//		mazePanel.setBorder(new EmptyBorder(1, 2, 0, 0));

		JPanel component = new JPanel()//;
		{	public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("sidebackground.jpg").getImage(), 0, 0, null);
			}
		};
//		component.setOpaque(false);
		component.setLayout(new GridLayout(6, 1));
		// component.add(new Button("Help", this));
		 component.add(new Button("Save", this));
		component.add(new Button("Hint", this));
		component.add(new Button("Pause", this));
		component.add(new Button("Main Menu", this));

		// timer here!!
		timerPanel = new TimerPanel();
		timerPanel.setOpaque(false);
		component.add(timerPanel);

		coinPanel = new coinPanel();
		component.add(coinPanel);
		// component.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		maze = new JPanel();
		maze.setMaximumSize(new Dimension(100, 800));
		// System.out.println("mazeScreen -> maze should be created once only");
		mazeLayout = new CardLayout();
		maze.setLayout(mazeLayout);
		maze.setOpaque(false);

		// GridBagLayout gridBagLayout = new GridBagLayout();
		// maze.setLayout(gridBagLayout);

		// add panels to screen
		// maze.setBackground(Color.white);
		// currGame.setOpaque(true);
		// currGame.setBackground(Color.BLUE);
		// if(component.is)
		// System.out.println(component.getWidth());

		c = setGridBagConstraints(0, 0, 8, 9, 1, 1, GridBagConstraints.BOTH);
		mazePanel = addComponent(mazePanel, maze, gridbag, c);
		c = setGridBagConstraints(9, 0, 8, 1, 1, 8, GridBagConstraints.BOTH);
		mazePanel = addComponent(mazePanel, component, gridbag, c);

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
		//currGame.setKeyDetect(ked);
	    
	    kfm.addKeyEventDispatcher(ked);

		currGame.start(difficulty);
		currGame.setOpaque(true);

		maze.add("currGame", currGame);
		saveFlag = false;
		gameRunning = true;
		// System.out.println(maze.getComponentCount());

		cardLayout.show(this, "mazeScreen");

		// start timer
		timerPanel.startTimer();
	}

	private void closeNoticeBox() {
		if (noticBox == null)
			return;
		if (noticBox.getComponentCount() != 0) {
			noticBox.setVisible(false);
			System.out.println(noticBox.getComponentCount());
			noticBox.removeAll();
			System.out.println(noticBox.getComponentCount());

		}
	}

	private void continueGame() {
		if (saveFlag == false)
			return;
		if (maze.getComponentCount() != 0) {
			currGame.pauseGame(false);
			saveFlag = false;
			gameRunning = true;
			cardLayout.show(this, "mazeScreen");
			timerPanel.startTimer();
		}

		else
			return;
	}

	private void deleteGame() {
		kfm.removeKeyEventDispatcher(ked);
	    if (currGame != null) {
	        currGame.stop();
	    }
		if (maze.getComponentCount() != 0) {
			maze.remove(0);
			System.out.println(maze.getComponentCount());
		}
	}

	public JFrame askNewGame() {

		JFrame notice = new JFrame("New Game");
		notice.setUndecorated(true);
		notice.pack();

//		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);

		JLabel title = new JLabel("Are you sure? This will delete your saved game", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 15));
		notice.getContentPane().add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		component.add(new Button("Sure", this));
		component.add(new Button("Continue Saved Game", this));

		notice.getContentPane().add(component);
		notice.getContentPane().setLayout(new GridLayout(2, 1));

		return notice;

	}

	public JFrame askSaveGame() {

		JFrame notice = new JFrame("Game Not Saved");
		notice.setUndecorated(true);
		notice.pack();

//		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);

		JLabel title = new JLabel("Save Game?", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 15));
		notice.getContentPane().add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		component.add(new Button("Save Game", this));
		component.add(new Button("Don't Save", this));
		component.add(new Button("Resume", this));

		notice.getContentPane().add(component);
		notice.getContentPane().setLayout(new GridLayout(2, 1));

		return notice;

	}

	public JFrame askGiveUp() {

		JFrame notice = new JFrame("Give Up");
		notice.setUndecorated(true);
		notice.pack();

//		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);

		JLabel title = new JLabel("Give Up Game?", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 15));
		notice.getContentPane().add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		component.add(new Button("Yes", this));
		component.add(new Button("Resume", this));

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

		public Button(String text, final JPanel parentPanel) {
			
			super(text);
			setFont(new Font("Courier New", Font.BOLD, 20));
//			Dimension d = new Dimension(300, 50);
//			setMaximumSize(d);
//			setOpaque(false);
//			setContentAreaFilled(false);
//			setBorderPainted(false);

			if (text.equals("New Game")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// if a game saved before ask: are you sure? this will
						// delete your saved game
						// buttons: new game / continue
						if (saveFlag) {
							noticBox = askNewGame();
						} else {
							cardLayout.show(parentPanel, "newGame");
						}
						debug();
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
						startNewGame(Difficulty.EASY);
						debug();
					}

				});

			} else if (text.equals("Medium")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						startNewGame(Difficulty.MEDIUM);
						debug();
					}
				});

			} else if (text.equals("Hard")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						startNewGame(Difficulty.HARD);
						debug();
					}
				});

			} else if (text.equals("Main Menu") || text.equals("Return to Main Menu")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						// if a running game not saved, ask save or not
						if (!saveFlag && gameRunning) {
							timerPanel.pauseTimer();
							noticBox = askSaveGame();
						}

						// if save ->save and return to main
						// else return to main
						else {
							gameRunning = false;
							cardLayout.show(parentPanel, "mainMenu");
						}
						debug();
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
						// if closed before: get the previous game from buffer
						// else show previous game
						timerPanel.startTimer();
						gameRunning = true;
						closeNoticeBox();
						continueGame();
						// System.out.println("Should continue Game");
						debug();
					}
				});

			} else if (text.equals("Give Up")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// ask if really give up
						noticBox = askGiveUp();
						debug();
					}
				});

			} else if (text.equals("Pause")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// timer pause
						timerPanel.pauseTimer();
						gameRunning = true;
						currGame.pauseGame(true);
						cardLayout.show(parentPanel, "pauseScreen");
						debug();
					}
				});

			} else if (text.equals("Help")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// timmer stop
						gameRunning = false;
						currGame.pauseGame(true);
						cardLayout.show(parentPanel, "helpScreen");
						debug();

					}
				});

			} else if (text.equals("Save")) {
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						// timer pause and save in buffer
						timerPanel.pauseTimer();
						saveFlag = true;
						JOptionPane.showMessageDialog(parentPanel, "GAME SAVED!", "Save Game",
								JOptionPane.INFORMATION_MESSAGE);
						debug();
					}
				});

			} else if (text.equals("Save Game")) {
				addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						// timer pause and save in buffer
						timerPanel.pauseTimer();

						JOptionPane.showMessageDialog(parentPanel, "GAME SAVED!", "Save Game",
								JOptionPane.INFORMATION_MESSAGE);

						closeNoticeBox();
						saveFlag = true;
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
						JOptionPane.showMessageDialog(parentPanel,
								"Please usw 'up', 'down', 'left', 'right' to move the character", "How To Play",
								JOptionPane.INFORMATION_MESSAGE);
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
						// show hint: direction
						currGame.hintCoinActivated();
//						JOptionPane.showMessageDialog(parentPanel, "Will Show Next Move", "Hint",
//								JOptionPane.INFORMATION_MESSAGE);

					}
				});

			} else if (text.equals("Yes")) {
				addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						closeNoticeBox();

						JLabel resultTime = timerPanel.saveTimer();
						timerPanel.clearTimer();
						deleteGame();

						quitEndScreen = createQuitEndScreen(resultTime);
						parentPanel.add("quitEndScreen", quitEndScreen);
						cardLayout.show(parentPanel, "quitEndScreen");
						debug();

					}
				});
			} else {
				System.out.println(text + "button not implemented yet! FIX");
			}

		}

	}
}
