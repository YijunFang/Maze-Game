package GameRep;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import Common.CoordinatePair;
import Common.Difficulty;

@SuppressWarnings("serial")
public class Game extends JPanel {
    private static final int frameSize = 800;
    private double squareLength = 0;
    private double playerSize;
    private double centreShift;
    private boolean isPaused = false;
    
    private int mazeLength;
    private boolean playerPlaced = false;
    GameState gs;
    Square[][] maze;
    List<CoordinatePair> hcList;
    
    private double playerLocationX = 0;
    private double playerLocationY = 0;
    
    public Game() {
        initKeyPressDetect(); //debug
    }
    
    @Override
    public void paint(Graphics g) {
        if (isPaused) return;
        
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        if (gs == null) return;
        //draw the squares
        for (int down = 0; down < mazeLength; down++) {
            for (int across = 0; across < mazeLength; across++) {
                double pixelX = across * squareLength;
                double pixelY =   down * squareLength;
                
                Square s = maze[down][across];
                
                //Draw square borders
                if (s.isBorderedOn(SquareSide.UP)) {
                    g2d.draw(new Line2D.Double(pixelX, pixelY, pixelX + squareLength, pixelY));
                }
                if (s.isBorderedOn(SquareSide.RIGHT)) {
                    g2d.draw(new Line2D.Double(pixelX + squareLength, pixelY, pixelX + squareLength, pixelY + squareLength));
                }
                if (s.isBorderedOn(SquareSide.DOWN)) {
                    g2d.draw(new Line2D.Double(pixelX, pixelY + squareLength, pixelX + squareLength, pixelY + squareLength));
                }
                if (s.isBorderedOn(SquareSide.LEFT)) {
                    g2d.draw(new Line2D.Double(pixelX, pixelY, pixelX, pixelY + squareLength));
                }
            }
        }
        //display hint coins
        hcList = gs.getHintCoordinateList();
        for (CoordinatePair cp : hcList) {
            System.out.println("hc = x: " + cp.across + " y: " + cp.down);
            double pixelX = cp.across * squareLength;
            double pixelY = cp.down   * squareLength;
            g2d.setPaint(Color.yellow);
            g2d.fill(new Rectangle2D.Double(pixelX + centreShift, pixelY + centreShift, 20, 20));
        }
        
        //draw character
        //System.out.println("PlayerLocation = x: " + playerLoc.across + " y: " + playerLoc.down); //debug
        Rectangle2D player;
        
        if (!playerPlaced) { //if player not placed, determine its initial location
            CoordinatePair playerLoc = gs.getPlayerPosition();
            playerLocationX = playerLoc.across * squareLength;
            playerLocationY = playerLoc.down * squareLength;
            playerPlaced = true;
        }
        
        player = new Rectangle2D.Double(
                playerLocationX + centreShift, playerLocationY + centreShift, playerSize, playerSize);
        
        g2d.setPaint(Color.red);
        g2d.fill(player);
    }
    
    public void start(Difficulty diff) {
        setSize(frameSize, frameSize);
        gs = new GameState(diff);
        
        //Get maze length
        mazeLength = diff.getSideLength();
        maze = new Square[mazeLength][mazeLength];
        squareLength = frameSize/mazeLength * 0.95;
        playerSize = squareLength * 0.8;
        centreShift = squareLength * 0.1;
        
        //Initialise maze representation
        for (int down = 0; down < mazeLength; down++) {
            for (int across = 0; across < mazeLength; across++) {
                maze[down][across] = gs.getSquareAt(new CoordinatePair (down, across));
            }
        }
        
        repaint();
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Maze");
        Game game = new Game();
        frame.add(game);
        frame.setSize(frameSize, frameSize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        game.start(Difficulty.MEDIUM);
    }
    
    
    
    private void setNewPlayerPosition (CoordinatePair newLocation) {
        gs.setPlayerPosition(newLocation);
    }
    
    public void hintCoinActivated() {
        
    }
    public void pauseGame(boolean isPaused) {
        this.isPaused = isPaused;
        repaint();
    }
    public int getTime() {
        return 0;
    }
    
    public void initKeyPressDetect() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

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
        });
    }
    public void keyPressedDown() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        if (!currentSquare.isBorderedOn(SquareSide.DOWN)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down + 1, playerPosition.across));
            CoordinatePair playerLoc = gs.getPlayerPosition();
            double newPlayerLocationY = playerLoc.down * squareLength;
            
            Timer moveTimer = new Timer(1, null);
            Timer repaintTimer = new Timer (15, null);
            ActionListener moveAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationY < playerLocationY) {
                        moveTimer.stop();
                        repaintTimer.stop();
                        repaint();
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
            double newPlayerLocationX = playerLoc.across * squareLength;
            
            Timer moveTimer = new Timer(1, null);
            Timer repaintTimer = new Timer (15, null);
            ActionListener moveAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationX > playerLocationX) {
                        moveTimer.stop();
                        repaintTimer.stop();
                        repaint();
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
            double newPlayerLocationY = playerLoc.down * squareLength;
            
            Timer moveTimer = new Timer(1, null);
            Timer repaintTimer = new Timer (15, null);
            ActionListener moveAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationY > playerLocationY) {
                        moveTimer.stop();
                        repaintTimer.stop();
                        repaint();
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
            double newPlayerLocationX = playerLoc.across * squareLength;
            
            Timer moveTimer = new Timer(1, null);
            Timer repaintTimer = new Timer (15, null);
            ActionListener moveAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationX < playerLocationX) {
                        moveTimer.stop();
                        repaintTimer.stop();
                        repaint();
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
            
            moveTimer.setInitialDelay(0);
            moveTimer.start(); 
            repaintTimer.start();
        }
    }
    
}