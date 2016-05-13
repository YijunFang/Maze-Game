package GameRep;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Common.CoordinatePair;
import Common.Difficulty;

@SuppressWarnings("serial")
public class Game extends JPanel {
    static final int squareLength = 50;
    int mazeLength;
    GameState gs;
    
    public Game() {
        initKeyPressDetect(); //debug
    }

    
    Square[][] maze;
    
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
        CoordinatePair playerLoc = gs.getPlayerPosition();
        System.out.println("PlayerLocation = x: " + playerLoc.across + " y: " + playerLoc.down);
        Rectangle2D player = new Rectangle2D.Double(
                playerLoc.across * squareLength + 2.5, playerLoc.down * squareLength + 2.5, squareLength - 5, squareLength - 5);
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
        }
    }
    public void keyPressedLeft() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        if (!currentSquare.isBorderedOn(SquareSide.LEFT)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down, playerPosition.across - 1));
        }
    }
    public void keyPressedUp() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        if (!currentSquare.isBorderedOn(SquareSide.UP)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down - 1, playerPosition.across));
        }
    }
    public void keyPressedRight() {
        CoordinatePair playerPosition = gs.getPlayerPosition();
        Square currentSquare = gs.getSquareAt(playerPosition);
        if (!currentSquare.isBorderedOn(SquareSide.RIGHT)) {
            setNewPlayerPosition (new CoordinatePair(playerPosition.down, playerPosition.across + 1));
        }
    }
    
    private void setNewPlayerPosition (CoordinatePair newLocation) {
        gs.setPlayerPosition(newLocation);
        repaint();
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