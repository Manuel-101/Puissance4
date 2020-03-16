import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameWindow extends JFrame {
    private Plateau p;

    public GameWindow() {
        p = new Plateau();
        this.add(p);
        setTitle("Puissance 4");
        setSize(getPreferredSize());
        requestFocusInWindow();
        setFocusable(false);
        setVisible(true);

    }


}
