import javax.swing.*;
import java.awt.event.*;

public class MainWindow extends JFrame  {
    private Game game;
    private Menu menu;

    public MainWindow() {
        game = new Game(this);
        menu = new Menu(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setTitle("Puissance 4");
        setSize(500,400);
        this.setContentPane(menu);
        setVisible(true);
    }

    public void startGame(int i){
        this.setContentPane(game);
        game.startGame(i);
        setSize(getPreferredSize());
    }

    public void stopGame(){
        setContentPane(menu);
        setSize(500,400);
    }
}
