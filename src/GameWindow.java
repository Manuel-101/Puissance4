import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame  {
    private Plateau p;

    public GameWindow() {
        p = new Plateau();
        this.add(p);
        JPanel cp = new JPanel();
        cp.setLayout(new GridLayout(1,2));
        JButton start = new JButton("nouvelle partie");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                p.reset();
            }
        });
        cp.add(start);

        JButton quit = new JButton("quitter");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        cp.add(quit);
        this.add(cp, BorderLayout.PAGE_END);



        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setTitle("Puissance 4");
        setSize(getPreferredSize());
        requestFocusInWindow();
        setFocusable(false);
        setVisible(true);

    }



}
