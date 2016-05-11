package GameRep;

import java.awt.EventQueue;

import javax.swing.JFrame;

import MazeRep.AdjacencyListGraphNode;
import MazeRep.ExposedGraphMaze;
import MazeRep.GraphMaze;
import MazeRep.Maze;
import MazeRep.MazeFactory;
import MazeRep.Node;
import MazeRep.MazeGenStrategy.MazeGenStrategy;
import MazeRep.MazeGenStrategy.RandomisedRecursiveDFS;

public class Game<T> {
    JFrame frame;
    Node<T>[][] nodes;
    Maze<T> maze;
    
    public Game(int height, int width) {
        frame = new JFrame();
        frame.setBounds(400, 400, 0, 0);
        frame.setLocationRelativeTo(null);
        maze = new MazeFactory().generateMaze(GraphMaze.class, width, height, 1);
        
        System.out.println(maze.getHeight());
        System.out.println(maze.getLength());
    }
    
    public static void main (String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Game<AdjacencyListGraphNode> window = new Game<AdjacencyListGraphNode>(30, 30);
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
}
