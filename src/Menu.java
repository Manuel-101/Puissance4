import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    JButton jcj;
    JButton jcm;
    JButton quit;
    MainWindow gm;
    public Menu(MainWindow g){
        gm = g;
        JPanel jp = new JPanel();
        GridLayout layout = new GridLayout(3,1);
        layout.setHgap(10);
        layout.setVgap(10);
        jp.setLayout(layout);
        setLayout(new GridBagLayout());
        jp.setSize(200,300);
        jcj = new JButton("joueur contre jouer");
        jcj.addActionListener(actionEvent -> gm.startGame(0));
        //jcj.setAlignmentX(Component.CENTER_ALIGNMENT);
        jp.add(jcj);
        jcm = new JButton("joueur contre machine");
        jcm.addActionListener(actionEvent -> gm.startGame(1));
        //jcm.setAlignmentX(Component.CENTER_ALIGNMENT);
        jp.add(jcm);
        quit = new JButton("quitter");
        quit.addActionListener(actionEvent -> System.exit(0));
        setAlignmentY(SwingConstants.CENTER);
        jp.add(quit);
        add(jp);
    }
}
