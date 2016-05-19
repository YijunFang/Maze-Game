
package screen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.*;

import Common.Difficulty;
import GameRep.Game;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private CardLayout cardLayout = new CardLayout();
	private JPanel mainMenu;
	private JPanel newGame;
	private JPanel mazeScreen;
	private JPanel pauseScreen;
	// private JPanel helpScreen;
	private JPanel endScreen;

	private boolean gameRunning = false;
	private boolean saveFlag = false;
	private JPanel maze;
	private CardLayout mazeLayout;

	private Game currGame;
	private JFrame noticBox;
	private TimerPanel timerPanel = new TimerPanel();

	public MainPanel(int r, int d) {
		currGame = new Game();
		// this.countComponents();
		setBounds(0, 0, r, d);
		setLayout(cardLayout);

		mainMenu = createMainMenu();
		add("mainMenu", mainMenu);
		cardLayout.show(this, "mainMenu");

		newGame = createNewGame();
		add("newGame", newGame);
		mazeScreen = createMazeScreen();
		add("mazeScreen", mazeScreen);
		// helpScreen = createHelpScreen();
		// add("helpScreen", helpScreen);
		pauseScreen = createPauseScreen();
		add("pauseScreen", pauseScreen);

	}

	public JPanel createMainMenu() {
		// System.out.println("Create Main Menu Once Only");

		JPanel mainMenuPanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("background.jpg").getImage(), -200, -50, null);
			}
		};

		JLabel title = new JLabel("", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 50));

		JPanel component = new JPanel();
		
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1));
		component.add(new Button("New Game", this), JPanel.CENTER_ALIGNMENT);
		component.add(new Button("Continue", this));
		component.add(new Button("How To Play", this));
		component.add(new Button("Quit", this));
		component.setBorder(new EmptyBorder(0, 200, 0, 200));

		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 150, 50, null);
			}
		};
		titlePanel.setOpaque(false);
		mainMenuPanel.add(title, JPanel.TOP_ALIGNMENT);
		mainMenuPanel.add(titlePanel);//, JPanel.CENTER_ALIGNMENT);
		mainMenuPanel.add(component);//, JPanel.BOTTOM_ALIGNMENT);
		mainMenuPanel.setLayout(new GridLayout(4, 1));

		return mainMenuPanel;
	}

	private JPanel createNewGame() {
		// System.out.println("Create New Game Panel Once Only");

		Button button1 = new Button("Easy", this);
		Button button2 = new Button("Medium", this);
		Button button3 = new Button("Hard", this);
		Button button4 = new Button("Main Menu", this);

		JPanel newGamePanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("background.jpg").getImage(), -200, -50, null);
			}
		};


		JLabel title = new JLabel("Select Level", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 50));
		newGamePanel.add(title, JPanel.TOP_ALIGNMENT);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1));
		component.add(button1);
		component.add(button2);
		component.add(button3);
		component.add(button4);

		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 150, 50, null);
			}
		};
		titlePanel.setOpaque(false);
		newGamePanel.add(titlePanel, JPanel.TOP_ALIGNMENT);
		newGamePanel.add(title);//, JPanel.CENTER_ALIGNMENT);
		newGamePanel.add(component);//, JPanel.BOTTOM_ALIGNMENT);
		newGamePanel.setLayout(new GridLayout(4, 1));
		
		
		
//		
//		newGamePanel.add(component, JPanel.BOTTOM_ALIGNMENT);
//		newGamePanel.setLayout(new GridLayout(3, 1));

		return newGamePanel;
	}

	/*
	 * public JPanel createHelpScreen() { System.out.println(
	 * "Create Help Screen Once Only");
	 * 
	 * 
	 * JPanel helpScreenPanel = new JPanel() { public void
	 * paintComponent(Graphics g) { super.paintComponent(g); g.drawImage(new
	 * ImageIcon("src/main/resources/background.jpg").getImage(), -100, -100,
	 * null); } };
	 * 
	 * helpScreenPanel.setLayout(new GridLayout(0, 1));
	 * helpScreenPanel.setBorder(new EmptyBorder(100, 30, 100, 30));
	 * 
	 * helpScreenPanel.add(new Button("How To Play", this));
	 * helpScreenPanel.add(new Button("Resume", this)); helpScreenPanel.add(new
	 * Button("Save", this)); helpScreenPanel.add(new Button(
	 * "Return to Main Menu", this));
	 * 
	 * return helpScreenPanel; }
	 * 
	 */

	private JPanel createPauseScreen() {
		// System.out.println("Create Pause Screen Once Only");

		JPanel pauseScreen = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("background.jpg").getImage(), -200, -50, null);
			}
		};
		
		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 150, 50, null);
			}
		};
		titlePanel.setOpaque(false);
		pauseScreen.add(titlePanel, JPanel.TOP_ALIGNMENT);
//		pauseScreen.setLayout(new GridLayout(0, 1));
		
		pauseScreen.setLayout(new GridLayout(0, 1));
		pauseScreen.setBorder(new EmptyBorder(50, 30, 100, 30));

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1));
		
		component.add(new Button("How To Play", this));
		component.add(new Button("Resume", this));
		component.add(new Button("Restart", this));
		component.add(new Button("Save", this));
		component.add(new Button("Give Up", this));
		component.add(new Button("Return to Main Menu", this));

		pauseScreen.add(component, JPanel.CENTER_ALIGNMENT);
		return pauseScreen;
	}

	private JPanel createEndScreen(JLabel resultTime) {
		saveFlag = false;
		gameRunning = false;
		// System.out.println("End Screen Created");

		
		JPanel endScreen = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("background.jpg").getImage(), -200, -50, null);
			}
		};

		JPanel titlePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("Mazecraft.png").getImage(), 150, 50, null);
			}
		};
		titlePanel.setOpaque(false);
		endScreen.add(titlePanel, JPanel.TOP_ALIGNMENT);
			
		// endScreen.setBackground(Color.BLACK);
		endScreen.setLayout(new GridLayout(3, 1));
		endScreen.setBorder(new EmptyBorder(100, 30, 100, 30));

		pauseScreen.setLayout(new GridLayout(0, 1));
		pauseScreen.setBorder(new EmptyBorder(50, 30, 100, 30));

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
		// System.out.println("Create maze screen once only");

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
		mazePanel.setBorder(new EmptyBorder(1, 2, 0, 0));

		JPanel component = new JPanel()//;
		{	public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("sidebackground.jpg").getImage(), 0, 0, null);
			}
		};
//		component.setOpaque(false);
		component.setLayout(new GridLayout(0, 1));
		// component.add(new Button("Help", this));
		// component.add(new Button("Save", this));
		component.add(new Button("Hint", this));
		component.add(new Button("Pause", this));
		component.add(new Button("Main Menu", this));

		// timer here!!
		timerPanel = new TimerPanel();
		timerPanel.setOpaque(false);
		component.add(timerPanel);

		JLabel showCoin = new JLabel("SHOULD SHOW COINS", JLabel.CENTER);
		component.add(showCoin);
		// component.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		maze = new JPanel();
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

		c = setGridBagConstraints(0, 0, 8, 8, 1, 1, GridBagConstraints.BOTH);
		mazePanel = addComponent(mazePanel, maze, gridbag, c);
		// System.out.println("here"+maze.getSize());
		c = setGridBagConstraints(8, 0, 8, 1, 1, 8, GridBagConstraints.BOTH);
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

	private void startNewGame(Difficulty difficulty) {

		currGame.start(difficulty);
		currGame.setOpaque(false);

		// if there is a saved game, delete it
		deleteGame();

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
		if (maze.getComponentCount() != 0) {
			maze.remove(0);
		}
	}

	public JFrame askNewGame() {

		JFrame notice = new JFrame("New Game");

		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);

		JLabel title = new JLabel("Are you sure? This will delete your saved game", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 15));
		notice.add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		component.add(new Button("Sure", this));
		component.add(new Button("Continue Saved Game", this));

		notice.add(component);
		notice.setLayout(new GridLayout(2, 1));

		return notice;

	}

	public JFrame askSaveGame() {

		JFrame notice = new JFrame("Game Not Saved");

		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);

		JLabel title = new JLabel("Save Game?", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 15));
		notice.add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		component.add(new Button("Save Game", this));
		component.add(new Button("Don't Save", this));
		component.add(new Button("Resume", this));

		notice.add(component);
		notice.setLayout(new GridLayout(2, 1));

		return notice;

	}

	public JFrame askGiveUp() {

		JFrame notice = new JFrame("Give Up");

		notice.setMinimumSize(new Dimension(400, 300));
		notice.setLocationRelativeTo(null);
		notice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		notice.setVisible(true);

		JLabel title = new JLabel("Give Up Game?", JLabel.CENTER);
		title.setFont(new Font("Courier New", Font.BOLD, 15));
		notice.add(title);

		JPanel component = new JPanel();
		component.setOpaque(false);
		component.setLayout(new GridLayout(1, 2));
		component.add(new Button("Yes", this));
		component.add(new Button("Resume", this));

		notice.add(component);
		notice.setLayout(new GridLayout(2, 1));

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

	private class Button extends JButton {

		public Button(String text, JPanel parentPanel) {
			
			super(text);
			setFont(new Font("Courier New", Font.BOLD, 15));
			Dimension d = new Dimension(10, 5);
			setMinimumSize(d);
			setOpaque(false);
			setContentAreaFilled(false);
			setBorderPainted(false);

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
						JOptionPane.showMessageDialog(parentPanel, "Will Show Next Move", "Hint",
								JOptionPane.INFORMATION_MESSAGE);

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

						endScreen = createEndScreen(resultTime);
						parentPanel.add("endScreen", endScreen);
						cardLayout.show(parentPanel, "endScreen");
						debug();

					}
				});
			} else {
				System.out.println(text + "button not implemented yet! FIX");
			}

		}

	}

}
