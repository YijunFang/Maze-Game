package GUI.mainMenuRes;

import javax.swing.JPanel;
import javax.swing.JButton;

public class ButtonPanel extends JPanel {

    /**
     * Create the panel.
     */
    public ButtonPanel() {
        
        JButton btnNewGame = new JButton("New Game");
        add(btnNewGame);
        
        JButton btnQuit = new JButton("Quit");
        add(btnQuit);

    }

}
