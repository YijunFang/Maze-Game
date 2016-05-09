package GUI.mainMenuRes;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class TitlePanel extends JPanel {

    /**
     * Create the panel.
     */
    public TitlePanel() {
        
        JLabel lblMaze = new JLabel("MAZE");
        lblMaze.setFont(new Font("Tahoma", Font.PLAIN, 72));
        add(lblMaze);

    }

}
