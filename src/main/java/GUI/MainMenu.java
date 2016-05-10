package GUI;

import javax.swing.JPanel;

import GUI.mainMenuRes.ButtonPanel;
import GUI.mainMenuRes.TitlePanel;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;

public class MainMenu extends JPanel implements Screen {
    /**
     * Create the panel.
     */
    public MainMenu() {
        setLayout(new BorderLayout(0, 0));
        
        JPanel titlePanel = new TitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.setMaster(this);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public void startNewGame() {
        System.out.println("I am a potato");
    }

    @Override
    public void load() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMaster(Screen s) {
        // TODO Auto-generated method stub
        
    }

}
