import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame  {

    private Game g;
    private Menu m;


    public GameWindow() {
        g = new Game(this);
        m = new Menu(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setTitle("Puissance 4");
        setSize(600,400);
        this.setContentPane(m);
        setVisible(true);
    }

    public void startGame(int i){
        this.setContentPane(g);
        g.startGame(i);
        setSize(getPreferredSize());
    }

    public void stopGame(){
        setContentPane(m);
        setSize(600,400);
    }

}
