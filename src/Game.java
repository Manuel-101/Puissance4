import javax.swing.*;
import java.awt.*;

public class Game extends JPanel{
    private Plateau p;
    private GameWindow gm;
    public Game(GameWindow g){
        this.setLayout(new BorderLayout());
        gm = g;
        p = new Plateau();
        add(p);
        JButton retour = new JButton("retour");
        retour.addActionListener(actionEvent -> gm.stopGame());
        add(retour, BorderLayout.SOUTH);
    }

    public void startGame(int i){
        p.start();
    }
    public void stopGame(){
        p.stop();
    }

}
