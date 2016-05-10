package GUI.mainMenuRes;

import javax.swing.JPanel;

import GUI.MainMenu;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ButtonPanel extends JPanel {
    private MainMenu master;
    
    /**
     * Create the panel.
     */
    public ButtonPanel() {
        load();

    }

    public void load() {
        JButton btnNewGame = new JButton("New Game");
        btnNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                master.startNewGame();
            }

        });
        add(btnNewGame);
        
        JButton btnQuit = new JButton("Quit");
        btnQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
        add(btnQuit);
    }
    
    public void setMaster(MainMenu m) {
        this.master = m;
    }
}
