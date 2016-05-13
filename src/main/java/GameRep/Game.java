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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import Common.CoordinatePair;
import Common.Difficulty;

@SuppressWarnings("serial")
public class Game extends JPanel {
    private static final int squareLength = 50;
    private int mazeLength;
    private boolean playerPlaced = false;
    GameState gs;
    Square[][] maze;
    
    private double playerLocationX = 0;
    private double playerLocationY = 0;
    
    public Game() {
        initKeyPressDetect(); //debug
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (gs == null) return;
        //draw the squares
        for (int down = 0; down < mazeLength; down++) {
            for (int across = 0; across < mazeLength; across++) {
                int pixelX = across * squareLength;
                int pixelY =   down * squareLength;
                
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
        
        //draw character
        //System.out.println("PlayerLocation = x: " + playerLoc.across + " y: " + playerLoc.down); //debug
        int playerSize = squareLength - 5;
        Rectangle2D player;
        
        if (!playerPlaced) { //if player not placed, determine its initial location
            CoordinatePair playerLoc = gs.getPlayerPosition();
            playerLocationX = playerLoc.across * squareLength + 2.5;
            playerLocationY = playerLoc.down * squareLength + 2.5;
            playerPlaced = true;
        }
        
        player = new Rectangle2D.Double(
                                        playerLocationX, playerLocationY, playerSize, playerSize);
        
        g2d.setPaint(Color.red);
        g2d.fill(player);
    }
    
    public void start(Difficulty diff) {
        setSize(500, 500);
        gs = new GameState(diff);
        
        //Get maze length
        mazeLength = diff.getSideLength();
        maze = new Square[mazeLength][mazeLength];
        
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
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        game.start(Difficulty.HARD);
    }
    
    public void keyPressedDown() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        if (!currentSquare.isBorderedOn(SquareSide.DOWN)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down + 1, playerPosition.across));
            CoordinatePair playerLoc = gs.getPlayerPosition();
            double newPlayerLocationY = playerLoc.down * squareLength + 2.5;
            
            Timer moveTimer = new Timer(1, null);
            Timer repaintTimer = new Timer (15, null);
            ActionListener moveAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationY < playerLocationY - 2.5) {
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
            double newPlayerLocationX = playerLoc.across * squareLength + 2.5;
            
            Timer timer = new Timer(1, null);
            ActionListener timerAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationX > playerLocationX) {
                        timer.stop();
                    }
                    playerLocationX-=2;
                    repaint();
                }
                
            };
            timer.addActionListener(timerAction);
            
            timer.setInitialDelay(0);
            timer.start();
            repaint();
        }
    }
    public void keyPressedUp() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        if (!currentSquare.isBorderedOn(SquareSide.UP)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down - 1, playerPosition.across));
            CoordinatePair playerLoc = gs.getPlayerPosition();
            double newPlayerLocationY = playerLoc.down * squareLength + 2.5;
            
            Timer timer = new Timer(1, null);
            ActionListener timerAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationY > playerLocationY) {
                        timer.stop();
                    }
                    playerLocationY-=2;
                    repaint();
                }
                
            };
            timer.addActionListener(timerAction);
            
            timer.setInitialDelay(0);
            timer.start();
        }
    }
    public void keyPressedRight() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        if (!currentSquare.isBorderedOn(SquareSide.RIGHT)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down, playerPosition.across + 1));
            
            CoordinatePair playerLoc = gs.getPlayerPosition();
            double newPlayerLocationX = playerLoc.across * squareLength + 2.5;
            
            Timer timer = new Timer(1, null);
            ActionListener timerAction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (newPlayerLocationX < playerLocationX) {
                        timer.stop();
                    }
                    playerLocationX+=2;
                    repaint();
                }
                
            };
            timer.addActionListener(timerAction);
            
            timer.setInitialDelay(0);
            timer.start();
            repaint();
        }
    }
    
    private void setNewPlayerPosition (CoordinatePair newLocation) {
        gs.setPlayerPosition(newLocation);
        /*
         CoordinatePair playerLoc = gs.getPlayerPosition();
         double newPlayerLocationX = playerLoc.across * squareLength + 2.5;
         double newPlayerLocationY = playerLoc.down * squareLength + 2.5;
         
         while (newPlayerLocationX != playerLocationX || newPlayerLocationY != playerLocationY) {
         if (newPlayerLocationX != playerLocationX) {
         playerLocationX += (newPlayerLocationX - playerLocationX)/10;
         }
         repaint();
         }
         
         repaint();
         */
    }
    
    public void hintCoinActivated() {
        
    }
    public void pauseGame(boolean isPaused) {
        
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
    
}