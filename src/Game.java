import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JPanel{
    private Plateau p;
    private JButton retour;
    private GameWindow gm;
    public Game(GameWindow g){
        this.setLayout(new BorderLayout());
        gm = g;
        p = new Plateau();
        add(p);
        retour = new JButton("retour");
        retour.addActionListener(actionEvent -> gm.stopGame());
        add(retour, BorderLayout.SOUTH);
    }

    public void startGame(int i){
        p.reset();
        repaint();
    }

}
