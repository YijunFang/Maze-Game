package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AppWindow extends JFrame {

    private JPanel contentPane;

    public AppWindow() {
        setBounds(100, 100, 900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        JPanel MainMenu = new MainMenu();
        getContentPane().add(MainMenu, BorderLayout.CENTER);
    }

}
