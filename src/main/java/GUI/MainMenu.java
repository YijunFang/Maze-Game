package GUI;

import javax.swing.JPanel;

import GUI.mainMenuRes.ButtonPanel;
import GUI.mainMenuRes.TitlePanel;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;

public class MainMenu extends JPanel {

    /**
     * Create the panel.
     */
    public MainMenu() {
        setLayout(new BorderLayout(0, 0));
        
        JPanel titlePanel = new TitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new ButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

}
