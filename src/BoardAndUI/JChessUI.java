package BoardAndUI;

import javax.swing.JFrame;

import java.awt.EventQueue;

public class JChessUI extends JFrame {
    public JChessUI()
    {
        initUI();
    }

    private void initUI()
    {
        add(new JChessBoard());

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("Chess");
        setLocationRelativeTo(null);
    }

    public static void main(String args[])
    {
        EventQueue.invokeLater( () -> {
            JFrame UI = new JChessUI();
            UI.setVisible(true);
        });
        
    }
}
