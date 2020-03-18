import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel {
    JButton jcj;
    JButton jcm;
    GameWindow gm;
    public Menu(GameWindow g){
        gm = g;
        setLayout(new GridLayout(3,1));
        jcj = new JButton("joueur contre jouer");
        jcj.addActionListener(actionEvent -> gm.startGame(0));
        add(jcj);
        jcm = new JButton("joueur contre machine");
        jcm.addActionListener(actionEvent -> gm.startGame(1));
        add(jcm);
        JButton quit = new JButton("quitter");
        quit.addActionListener(actionEvent -> System.exit(0));
        add(quit);
    }
}
