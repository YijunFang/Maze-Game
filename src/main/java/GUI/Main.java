package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;

public class Main {

    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frame.setVisible(true);
                    UIManager.setLookAndFeel(
                            UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new AppWindow();
        
        
    }

}
