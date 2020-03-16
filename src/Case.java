import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Case {
    private int l;
    private int c;
    private int color = 0;    //1 : rouge 2 : jaune
    private boolean isFalling = false;
    private int posf;
    private Timer timer;
    private JPanel panel;

    public static int sc = 40;
    public static int sl = 40;
    public Case(int col, int ligne, JPanel p){
        l=ligne;
        c=col;
        panel = p;
    }

    public void draw(Graphics g) {
        if(isFalling){
            g.setColor(Color.black);
            g.fillOval(c * sc, (5-l) * sl, sc,sl);
        }
        switch (color){
            case 0:
                g.setColor(Color.black);
                break;
            case 1:
                g.setColor(Color.red);
                break;
            case 2:
                g.setColor(Color.yellow);
                break;
        }
        if(!isFalling) {
            g.fillOval(c * sc, (5-l) * sl, sc,sl);
        }else{
            g.fillOval(c * sc, posf, sc, sl);
        }
    }

    public void joue(int c) {
        color = c;
        isFalling = true;
        posf = -sl;
        timer = new Timer(50,
                new ActionListener() {
                    public void actionPerformed(ActionEvent ev){
                        posf += sl/4;
                        if(posf > (5-l) * sl ) {
                            isFalling = false;
                            timer.stop();
                        }
                        panel.repaint();
                    }
                });
        timer.start();
    }

    public void setTimer(Timer t){
        this.timer = t;
        timer.start();
    }

    public boolean isFilled(){
        return color > 0;
    }
    public  int getColor(){
        return color;
    }
}
