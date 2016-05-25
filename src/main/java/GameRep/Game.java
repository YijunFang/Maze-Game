package GameRep;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import Common.Content;
import Common.CoordinatePair;
import Common.Difficulty;

@SuppressWarnings("serial")
public class Game extends JPanel {
    //window size
    private static final int frameSize = 800;
    
    //image locations
    private static final String wallImg   = "cobblestone.png";
    private static final String playerImg = "zombie.png";
    private static final String groundImg = "greengrass.png";
    private static final String hintImg   = "ender_eye.png";
    private static final String goalImg   = "villager.png";
    private BufferedImage mazeImage = null;
    private BufferedImage hintImage = null;
    
    //rendering information
    private double squareLength;
    private double playerSize;
    private double centreShift;
    private double playerLocationX;
    private double playerLocationY;
    private int moveAmount = 2;
    
    //game state
    private GameState gs;
    private Square[][] maze;
    private boolean isPaused = false;
    private boolean gameWon = false;
    private boolean displayHint = false;
    private int mazeLength;
    private boolean playerPlaced = false;
    private List<CoordinatePair> hintCoinList = null;
    private List<CoordinatePair> hintPathList; //hint path list

//	changed here
	protected KeyEventDispatcher ked;
    
    public Game() {
//    	changed here
//      enableKeyPressDetect(); //debug
        
    }
    
//	changed here   
    /*
    public void setKeyDetect(KeyEventDispatcher initked) {
    	ked = initked;
    	enableKeyPressDetect();
	}*/

//	changed here
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Maze");
        final Game game = new Game();
        KeyEventDispatcher ked = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Game.class) {
                    switch (ke.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        switch (ke.getKeyCode()) {
                            case KeyEvent.VK_W:
                                game.keyPressedUp();System.out.println("coin = "+game.getNumCoins());
                                break;
                            case KeyEvent.VK_A:
                            	 game.keyPressedLeft();System.out.println("coin = "+game.getNumCoins());
                                break;
                            case KeyEvent.VK_S:
                            	 game.keyPressedDown();System.out.println("coin = "+game.getNumCoins());
                                break;
                            case KeyEvent.VK_D:
                            	 game.keyPressedRight();System.out.println("coin = "+game.getNumCoins());
                                break;
                        }
                        break;
                    }
                    return false;
                }
            }
        };
        
        frame.add(game);
        frame.setSize(frameSize, frameSize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        game.start(Difficulty.EASY);
        

    }
    
    /**
     * Sets the side length of the maze depending on diff.
     * Initialises rendering information (maze size, player size, centre shift).
     * Initialises the maze representation (maze, hint coins list).
     * Repaints the maze using the new representation and initialization.
     * @param diff the requested level of difficulty
     */
    public void start(Difficulty diff) {
        setSize(frameSize, frameSize);
        gs = new GameState(diff);
        
        //Get maze length
        mazeLength = diff.getSideLength();
        
        //Initialise rendering information
        squareLength = frameSize/mazeLength * 0.95;
        playerSize = squareLength * 0.6;
        centreShift = squareLength * 0.2;

        //Initialise maze representation
        maze = new Square[mazeLength][mazeLength];
        hintCoinList = new LinkedList<CoordinatePair>();
        
        for (int down = 0; down < mazeLength; down++) {
            for (int across = 0; across < mazeLength; across++) {
                //initialise each index of the maze
                maze[down][across] = gs.getSquareAt(new CoordinatePair (down, across));
                
                //if index also contains hint coin, add it to the hint coin list
                if (maze[down][across].getContent() == Content.CREDIT) {
                    hintCoinList.add(maze[down][across].getCoordinatePair());
                }
            }
        }
        hintCoinActivated();
        repaint();
    }
    /**
     * Stop resets the data in the Game object to ensure that none of it gets reused in the next iteration of the game
     */
    public void stop() {
        //disableKeyPressDetect();
        mazeImage = null;
        hintImage = null;
        
        gs = null;
        maze = null;
        gameWon = false;
        displayHint = false;
        hintCoinList = null;
        hintPathList = null;
    }
    
    /**
     * Paint renders the 2D graphics onto the screen
     * @param g the canvas that the maze is being painted onto
     */
    @Override
    public void paint(Graphics g) {
        if (isPaused) return; //If the game is paused, don't paint anything
        if (gs == null) return; //If game state is not initialised yet, don't paint anything
        super.paint(g);
        if (gameWon) {
//        	change here
//        	renderEndGame(g);
//			reset everything?
        	gs = null;
        	gameWon = false;
        	ked = null;
        	;
        } else {
            renderGame(g);
        }
    }
    
    /**
     * Renders the game. To do this, it firstly gets the necessary images
     * using the getImage(String imageName) function. Then, if a maze image
     * isn't already  generated, generate a maze image (so that the maze
     * doesn't need to be rerendered every time the player moves). Then render
     * the hint coins.
     * @param g the canvas that the maze is being painted onto
     */
    private void renderGame (Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Get images
        Image wall          = getImage(wallImg);
        Image player        = getImage(playerImg);
        Image ground        = getImage(groundImg);
        Image hintCoinImage = getImage(hintImg);
        Image goalImage     = getImage(goalImg);
        
        //Maze will be a pre-generated image to remove the need to rerender the maze everytime the player moves
        if (mazeImage == null) {
            mazeImage = new BufferedImage (frameSize, frameSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D mazeImageGraphic = mazeImage.createGraphics();
            
            int wallImageSize   = (int) squareLength/4;
            int groundImageSize = (int) squareLength/4;
            
            //draw the ground
            for (int x = 0; x < frameSize; x += groundImageSize) {
                for (int y = 0; y < frameSize; y += groundImageSize) {
                    mazeImageGraphic.drawImage(ground, x, y, groundImageSize, groundImageSize, null, null);
                }
            }
            //draw the squares
            for (int down = 0; down < mazeLength; down++) {
                for (int across = 0; across < mazeLength; across++) {
                    double pixelX = across * squareLength;
                    double pixelY =   down * squareLength;
                    
                    Square s = maze[down][across];
                    
                    //Draw square borders (i.e. walls)
                    if (s.isBorderedOn(SquareSide.UP)) {
                        mazeImageGraphic.draw(new Line2D.Double(pixelX, pixelY, pixelX + squareLength, pixelY));
                        int imgYCoordinate = (int) (pixelY - wallImageSize/2);
                        
                        for (int imglocation = (int) pixelX - wallImageSize/2; imglocation < pixelX + squareLength; imglocation += wallImageSize) {
                            mazeImageGraphic.drawImage(wall, imglocation, imgYCoordinate, wallImageSize, wallImageSize, null, null);
                        }
                    }
                    if (s.isBorderedOn(SquareSide.RIGHT)) {
                        mazeImageGraphic.draw(new Line2D.Double(pixelX + squareLength, pixelY, pixelX + squareLength, pixelY + squareLength));
                        int imgXCoordinate = (int) (pixelX - wallImageSize/2 + squareLength);
                        
                        for (int imglocation = (int) pixelY - wallImageSize/2; imglocation < pixelY + squareLength; imglocation += wallImageSize) {
                            mazeImageGraphic.drawImage(wall, imgXCoordinate, imglocation, wallImageSize, wallImageSize, null, null);
                        }
                    }
                    if (s.isBorderedOn(SquareSide.DOWN)) {
                        mazeImageGraphic.draw(new Line2D.Double(pixelX, pixelY + squareLength, pixelX + squareLength, pixelY + squareLength));
                        int imgYCoordinate = (int) (pixelY - wallImageSize/2 + squareLength);
                        
                        for (int imglocation = (int) pixelX - wallImageSize/2; imglocation < pixelX + squareLength; imglocation += wallImageSize) {
                            mazeImageGraphic.drawImage(wall, imglocation, imgYCoordinate, wallImageSize, wallImageSize, null, null);
                        }
                    }
                    if (s.isBorderedOn(SquareSide.LEFT)) {
                        mazeImageGraphic.draw(new Line2D.Double(pixelX, pixelY, pixelX, pixelY + squareLength));
                        int imgXCoordinate = (int) (pixelX - wallImageSize/2);
                        
                        for (int imglocation = (int) pixelY - wallImageSize/2; imglocation < pixelY + squareLength; imglocation += wallImageSize) {
                            mazeImageGraphic.drawImage(wall, imgXCoordinate, imglocation, wallImageSize, wallImageSize, null, null);
                        }
                    }
                }
            }
        }
        
        //draw maze image
        g2d.drawImage(mazeImage, 0, 0, frameSize, frameSize, null, null);
        
        //Hint coins will be displayed as a rendered image that will be redrawn only when player takes one from the maze
        if (hintImage == null || playerOnHintCoin(gs.getPlayerPosition())) {
            if (hintImage != null)  {
                hintCoinList.remove(gs.getPlayerPosition());
                maze[gs.getPlayerPosition().down][gs.getPlayerPosition().across] = gs.getSquareAt(gs.getPlayerPosition());
            }
            hintImage = new BufferedImage (frameSize, frameSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D hintImageGraphics = hintImage.createGraphics();
            
            //rerender the hint coins
            for (CoordinatePair cp : hintCoinList) {
                double pixelX = cp.across * squareLength;
                double pixelY = cp.down   * squareLength;
                
                hintImageGraphics.drawImage(hintCoinImage, (int)(pixelX + centreShift), (int)(pixelY + centreShift),
                        (int)(playerSize), (int) playerSize, null, null);
            }
        }
        
        
        //draw hint coin image
        g2d.drawImage(hintImage, 0, 0, frameSize, frameSize, null, null);
        //draw goal
        CoordinatePair goal = gs.getGoalPosition();
        double goalPixelX = goal.across * squareLength;
        double goalPixelY = goal.down   * squareLength;
        g2d.drawImage(goalImage, (int)(goalPixelX + centreShift * 1.5), (int)(goalPixelY + centreShift), 
                (int)(playerSize * 0.8), (int)(playerSize * 1.1), null, null);
        
        //draw zombie (player)
        if (!playerPlaced) { //if player not placed, determine its initial location
            CoordinatePair playerLoc = gs.getPlayerPosition();
            playerLocationX = playerLoc.across * squareLength;
            playerLocationY = playerLoc.down * squareLength;
            playerPlaced = true;
        }
        g2d.drawImage(player, (int) (playerLocationX + centreShift), (int) (playerLocationY + centreShift), 
                (int) playerSize, (int) playerSize, null, null);
        
        //draw hint squares if needed to be displayed) 
        
        if (displayHint) {
            if (hintPathList == null) {
                System.out.println("No hint path list exists.");
            } else {
                for (CoordinatePair cp : hintPathList) {
                    Rectangle2D hintSquare = new Rectangle2D.Double(
                            cp.across * squareLength, cp.down * squareLength, squareLength, squareLength);
                    g2d.fill(hintSquare);
                }
            }
        }
    }

    
//    change here
//    /**
//     * Renders the end state of the game.
//     * @param g the canvas that the maze is being painted onto
//     */
//    private void renderEndGame(Graphics g) {
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.drawString("You win!", 100, 100);
//    }
    
    /**
     * Gets the image from file and returns the image so that it can be used in the program
     * @param fileName the name of the image file
     * @return the image
     */
    private Image getImage(String fileName) {
        Image img = null;
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
    
    /**
     * Uses the coordinates of the player's current position and checks if
     * there is a hint coin on that position
     * @param playerPosition the current position of the player
     * @return true if there is a hint coin on the player's current position,
     * false otherwise
     */
    private boolean playerOnHintCoin(CoordinatePair playerPosition) {
        if (hintCoinList == null) return false;
        
        for (CoordinatePair compare : hintCoinList) {
            if (compare.equals(playerPosition)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Sets the new position for the player
     * @param newLocation the coordinates of the location where the
     * player is being set to
     */
    private void setNewPlayerPosition (CoordinatePair newLocation) {
        this.gs.setPlayerPosition(newLocation);
    }
    
    /**
     *
     */
    public void hintCoinActivated() {
        hintPathList = gs.getHintCoordinateList();
        final Timer hintTimer = new Timer (5000, null);
        ActionListener hintTimerListener = new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent evt) {
                hintTimer.stop();
                displayHint = false;
            }
        };
        displayHint = true;
        hintTimer.addActionListener(hintTimerListener);
        hintTimer.start();
    }
    
    /**
     * Returns true if the game is won, false otherwise
     * @return true if the game is won, false otherwise
     */
    public boolean isGameWon() {
        return this.gameWon;
    }
    
    /**
     * Repaints the maze screen to show paused game state
     * @param isPaused true if game is paused, false otherwise
     */
    public void pauseGame(boolean isPaused) {
        this.isPaused = isPaused;
        repaint();
    }
    /**
     * Gets the number of coins that a player has
     * @return the current number of coins that a player has
     */
    public int getNumCoins() {
        return this.gs.getNumberOfCoins();
    }
    
    /**
     * Resets the maze. This method does not generate a new maze.
     */
    public void restart() {
        
    }
    
    /**
     * Checks if the current coordinates of the player's position
     * is equal to the goal's coordinates. If it is, then set gameWon
     * to true and repaint the maze
     */
    private void checkWinState() {
        CoordinatePair goal = gs.getGoalPosition();
        if (gs.getPlayerPosition().equals(goal)) {
            this.gameWon = true;
            repaint();
        }
    }


//    Change here
    /*
    private KeyEventDispatcher formKeyEventDispatcher() {
        KeyEventDispatcher ked = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Game.class) {
                    switch (ke.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        switch (ke.getKeyCode()) {
                            case KeyEvent.VK_W:
                                keyPressedUp();
                                break;
                            case KeyEvent.VK_A:
                                keyPressedLeft();
                                break;
                            case KeyEvent.VK_S:
                                keyPressedDown();
                                break;
                            case KeyEvent.VK_D:
                                keyPressedRight();
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
*/
    /**
     * Enables the use of computer keyboard to control the game
     */
    /*
    private void enableKeyPressDetect() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ked);
    }*/
    /**
     * Disables the use of computer keyboard to control the game
     */
    /*
    private void disableKeyPressDetect() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(ked);
    }
    */
    /**
     * Moves the current position of the player one coordinate downwards
     * Firstly, it ensures that the current position of the player is not
     * already at the bottom coordinate of the maze. If it isn't then
     * set the player's position to be one coordinate downwards of its
     * current position.
     */
    public void keyPressedDown() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        if (!currentSquare.isBorderedOn(SquareSide.DOWN)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down + 1, playerPosition.across));
            CoordinatePair playerLoc = gs.getPlayerPosition();
            final double newPlayerLocationY = playerLoc.down * squareLength;
            
            final Timer moveTimer = new Timer(1, null);
            final Timer repaintTimer = new Timer (15, null);
            ActionListener moveAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationY < playerLocationY + centreShift/2) {
                        moveTimer.stop();
                        repaintTimer.stop();
                        repaint();
                        checkWinState();
                    }
                    playerLocationY+=moveAmount;
                }
                
            };
            ActionListener repaintAction = new ActionListener() {
                @Override
                public void actionPerformed (ActionEvent e) {
                    repaint();
                }
            };
            moveTimer.addActionListener(moveAction);
            repaintTimer.addActionListener(repaintAction);
            
            //disableKeyPressDetect();
            
            moveTimer.setInitialDelay(0);
            moveTimer.start(); 
            repaintTimer.start();
            
        }
    }
    public void keyPressedLeft() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        
        if (!currentSquare.isBorderedOn(SquareSide.LEFT)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down, playerPosition.across - 1));
            
            CoordinatePair playerLoc = gs.getPlayerPosition();
            final double newPlayerLocationX = playerLoc.across * squareLength;
            
            final Timer moveTimer = new Timer(1, null);
            final Timer repaintTimer = new Timer (15, null);
            ActionListener moveAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationX > playerLocationX - centreShift/2) {
                        //Character has moved sufficiently, so stop movement process
                        moveTimer.stop();
                        repaintTimer.stop();
                        //enableKeyPressDetect();
                        repaint();
                        checkWinState();
                    }
                    playerLocationX-=moveAmount;
                }
                
            };
            ActionListener repaintAction = new ActionListener() {
                @Override
                public void actionPerformed (ActionEvent e) {
                    repaint();
                }
            };
            moveTimer.addActionListener(moveAction);
            repaintTimer.addActionListener(repaintAction);
            
            //disableKeyPressDetect();
            
            moveTimer.setInitialDelay(0);
            moveTimer.start(); 
            repaintTimer.start();
        }
    }
    public void keyPressedUp() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        if (!currentSquare.isBorderedOn(SquareSide.UP)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down - 1, playerPosition.across));
            CoordinatePair playerLoc = gs.getPlayerPosition();
            final double newPlayerLocationY = playerLoc.down * squareLength;
            
            final Timer moveTimer = new Timer(1, null);
            final Timer repaintTimer = new Timer (15, null);
            ActionListener moveAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationY > playerLocationY - centreShift/2) {
                        moveTimer.stop();
                        repaintTimer.stop();
                        //enableKeyPressDetect();
                        repaint();
                        checkWinState();
                    }
                    playerLocationY-=moveAmount;
                }
                
            };
            ActionListener repaintAction = new ActionListener() {
                @Override
                public void actionPerformed (ActionEvent e) {
                    repaint();
                }
            };
            moveTimer.addActionListener(moveAction);
            repaintTimer.addActionListener(repaintAction);
            
            //disableKeyPressDetect();
            
            moveTimer.setInitialDelay(0);
            moveTimer.start(); 
            repaintTimer.start();
            
        }
    }
    public void keyPressedRight() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        if (!currentSquare.isBorderedOn(SquareSide.RIGHT)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down, playerPosition.across + 1));

            CoordinatePair playerLoc = gs.getPlayerPosition();
            final double newPlayerLocationX = playerLoc.across * squareLength;
            
            final Timer moveTimer = new Timer(1, null);
            final Timer repaintTimer = new Timer (15, null);
            ActionListener moveAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationX < playerLocationX + centreShift/2) {
                        moveTimer.stop();
                        repaintTimer.stop();
                        //enableKeyPressDetect();
                        repaint();
                        checkWinState();
                    }
                    playerLocationX+=moveAmount;
                }
                
            };
            ActionListener repaintAction = new ActionListener() {
                @Override
                public void actionPerformed (ActionEvent e) {
                    repaint();
                }
            };
            moveTimer.addActionListener(moveAction);
            repaintTimer.addActionListener(repaintAction);
            
            //disableKeyPressDetect();
            
            moveTimer.setInitialDelay(0);
            moveTimer.start(); 
            repaintTimer.start();
        }
    }
}
