package GameRep;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    
    //rendering information
    private double squareLength = 0;
    private double playerSize;
    private double centreShift;
    private double playerLocationX = 0;
    private double playerLocationY = 0;
    
    //game state
    private GameState gs;
    private Square[][] maze;
    private boolean isPaused = false;
    private boolean gameWon = false;
    private int mazeLength;
    private boolean playerPlaced = false;
    List<CoordinatePair> hcList; //hint path list
    
    public Game() {
        enableKeyPressDetect(); //debug
    }
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Maze");
        Game game = new Game();
        frame.add(game);
        frame.setSize(frameSize, frameSize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        game.start(Difficulty.HARD);
    }
    public void start(Difficulty diff) {
        setSize(frameSize, frameSize);
        gs = new GameState(diff);
        
        //Get maze length
        mazeLength = diff.getSideLength();
        maze = new Square[mazeLength][mazeLength];
        squareLength = frameSize/mazeLength * 0.95;
        playerSize = squareLength * 0.6;
        centreShift = squareLength * 0.2;
        
        //Initialise maze representation
        for (int down = 0; down < mazeLength; down++) {
            for (int across = 0; across < mazeLength; across++) {
                maze[down][across] = gs.getSquareAt(new CoordinatePair (down, across));
            }
        }
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        if (isPaused) return; //If the game is paused, don't paint anything
        if (gs == null) return; //If game state is not initialised yet, don't paint anything
        super.paint(g);
        if (gameWon) {
            renderEndGame(g);
        } else {
            renderGame(g);
        }
    }
    private void renderGame (Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Get images
        Image wall      = getImage(wallImg);
        Image player    = getImage(playerImg);
        Image ground    = getImage(groundImg);
        Image hintImage = getImage(hintImg);
        Image goalImage = getImage(goalImg);
        
        int wallImageSize   = (int) squareLength/4;
        int groundImageSize = (int) squareLength/4;
        //draw the ground
        for (int x = 0; x < frameSize; x += groundImageSize) {
            for (int y = 0; y < frameSize; y += groundImageSize) {
                g2d.drawImage(ground, x, y, groundImageSize, groundImageSize, null, null);
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
                    g2d.draw(new Line2D.Double(pixelX, pixelY, pixelX + squareLength, pixelY));
                    int imgYCoordinate = (int) (pixelY - wallImageSize/2);
                    
                    for (int imglocation = (int) pixelX - wallImageSize/2; imglocation < pixelX + squareLength; imglocation += wallImageSize) {
                        g2d.drawImage(wall, imglocation, imgYCoordinate, wallImageSize, wallImageSize, null, null);
                    }
                }
                if (s.isBorderedOn(SquareSide.RIGHT)) {
                    g2d.draw(new Line2D.Double(pixelX + squareLength, pixelY, pixelX + squareLength, pixelY + squareLength));
                    int imgXCoordinate = (int) (pixelX - wallImageSize/2 + squareLength);
                    
                    for (int imglocation = (int) pixelY - wallImageSize/2; imglocation < pixelY + squareLength; imglocation += wallImageSize) {
                        g2d.drawImage(wall, imgXCoordinate, imglocation, wallImageSize, wallImageSize, null, null);
                    }
                }
                if (s.isBorderedOn(SquareSide.DOWN)) {
                    g2d.draw(new Line2D.Double(pixelX, pixelY + squareLength, pixelX + squareLength, pixelY + squareLength));
                    int imgYCoordinate = (int) (pixelY - wallImageSize/2 + squareLength);
                    
                    for (int imglocation = (int) pixelX - wallImageSize/2; imglocation < pixelX + squareLength; imglocation += wallImageSize) {
                        g2d.drawImage(wall, imglocation, imgYCoordinate, wallImageSize, wallImageSize, null, null);
                    }
                }
                if (s.isBorderedOn(SquareSide.LEFT)) {
                    g2d.draw(new Line2D.Double(pixelX, pixelY, pixelX, pixelY + squareLength));
                    int imgXCoordinate = (int) (pixelX - wallImageSize/2);
                    
                    for (int imglocation = (int) pixelY - wallImageSize/2; imglocation < pixelY + squareLength; imglocation += wallImageSize) {
                        g2d.drawImage(wall, imgXCoordinate, imglocation, wallImageSize, wallImageSize, null, null);
                    }
                }
                
                //draw hint coin if square has one
                if (s.getContent() == Content.CREDIT) {
                    g2d.drawImage(hintImage, (int)(pixelX + centreShift), (int)(pixelY + centreShift),
                            (int)(playerSize * 01), (int)(playerSize * 1), null, null);
                    maze[down][across] = gs.getSquareAt(new CoordinatePair (down, across));
                }
            }
        }
        
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
    }
    private void renderEndGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawString("You win!", 100, 100);
    }
    
    private Image getImage(String fileName) {
        Image img = null;
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    private void setNewPlayerPosition (CoordinatePair newLocation) {
        this.gs.setPlayerPosition(newLocation);
    }
    
    public void hintCoinActivated() {
        
    }
    public void pauseGame(boolean isPaused) {
        this.isPaused = isPaused;
        repaint();
    }
    public int getNumCoins() {
        return this.gs.getNumberOfCoins();
    }
    public void restart() {
        
    }
    private void checkWinState() {
        CoordinatePair goal = gs.getGoalPosition();
        if (gs.getPlayerPosition().equals(goal)) {
            this.gameWon = true;
            repaint();
        }
    }
    
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
    private void enableKeyPressDetect() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ked);
    }
    private void disableKeyPressDetect() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(ked);
    }
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
                    if (newPlayerLocationY < playerLocationY) {
                        moveTimer.stop();
                        repaintTimer.stop();
                        enableKeyPressDetect();
                        repaint();
                        checkWinState();
                    }
                    playerLocationY+=1;
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
            
            disableKeyPressDetect();
            
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
                    if (newPlayerLocationX > playerLocationX) {
                        //Character has moved sufficiently, so stop movement process
                        moveTimer.stop();
                        repaintTimer.stop();
                        enableKeyPressDetect();
                        repaint();
                        checkWinState();
                    }
                    playerLocationX-=1;
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
            
            disableKeyPressDetect();
            
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
                    if (newPlayerLocationY > playerLocationY) {
                        moveTimer.stop();
                        repaintTimer.stop();
                        enableKeyPressDetect();
                        repaint();
                        checkWinState();
                    }
                    playerLocationY-=1;
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
            
            disableKeyPressDetect();
            
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
                    if (newPlayerLocationX < playerLocationX) {
                        moveTimer.stop();
                        repaintTimer.stop();
                        enableKeyPressDetect();
                        repaint();
                        checkWinState();
                    }
                    playerLocationX+=1;
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
            
            disableKeyPressDetect();
            
            moveTimer.setInitialDelay(0);
            moveTimer.start(); 
            repaintTimer.start();
        }
    }
}