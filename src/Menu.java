import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    JButton pvp;
    JButton pvm;
    JButton quit;
    MainWindow window;

    public Menu(MainWindow w){
        window = w;
        JPanel jp = new JPanel();
        GridLayout layout = new GridLayout(3,1);
        layout.setHgap(10);
        layout.setVgap(10);
        jp.setLayout(layout);
        setLayout(new GridBagLayout());
        pvp = new JButton("joueur contre jouer");
        pvp.addActionListener(actionEvent -> window.startGame(0));
        jp.add(pvp);
        pvm = new JButton("joueur contre machine");
        pvm.addActionListener(actionEvent -> window.startGame(1));
        jp.add(pvm);
        quit = new JButton("quitter");
        quit.addActionListener(actionEvent -> System.exit(0));
        setAlignmentY(SwingConstants.CENTER);
        jp.add(quit);
        add(jp);
    }
}
