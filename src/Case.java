import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Case {
    private int l;
    private int c;
    private int color = 0;    //1 : rouge 2 : jaune
    private boolean isFalling = false;
    private int posf;
    private Plateau p;
    private boolean isWinner;

    private static BufferedImage imageYellow;
    private static BufferedImage imageRed;
    private static BufferedImage imageYellowWin;
    private static BufferedImage imageRedWin;

    static {
        try {
            imageYellow = ImageIO.read(new File("res/yellow.png"));
            imageRed = ImageIO.read(new File("res/red.png"));
            imageYellowWin = ImageIO.read(new File("res/yellow_win.png"));
            imageRedWin = ImageIO.read(new File("res/red_win.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static int sc = 80;
    public static int sl = 80;
    public Case(int col, int ligne, Plateau pl){
        l=ligne;
        c=col;
        p = pl;
    }

    public void draw(Graphics g) {
        if(!isFalling) {
            switch (color){
                case 0:
                    g.setColor(Color.black);
                    g.fillOval(c * sc, (5-l) * sl, sc,sl);
                    break;
                case 1:
                    if(isWinner){
                        g.drawImage(imageRedWin,c * sc, (5-l) * sl, sc,sl,null);
                    }else{
                        g.drawImage(imageRed,c * sc, (5-l) * sl, sc,sl,null);
                    }
                    break;
                case 2:
                    if(isWinner){
                        g.drawImage(imageYellowWin,c * sc, (5-l) * sl, sc,sl,null);
                    }else{
                        g.drawImage(imageYellow,c * sc, (5-l) * sl, sc,sl,null);
                    }
                    break;
            }
        }else{
            g.setColor(Color.black);
            g.fillOval(c * sc, (5-l) * sl, sc,sl);
            switch (color){
                case 1:
                    g.drawImage(imageRed,c * sc, posf, sc,sl,null);
                    break;
                case 2:
                    g.drawImage(imageYellow,c * sc, posf, sc,sl,null);
                    break;
            }
            posf += sl/5;
            if(posf > (5-l) * sl ) {
                isFalling = false;
            }
        }
    }

    public void reset(){
        isWinner = false;
        color = 0;
    }

    public void setWinner(boolean w){
        isWinner = w;
    }

    public void joue(int c) {
        color = c+1;
        isFalling = true;
        posf = -sl;
//        timer = new Timer(50,
//                new ActionListener() {
//                    public void actionPerformed(ActionEvent ev){
//                        posf += sl/4;
//                        if(posf > (5-l) * sl ) {
//                            isFalling = false;
//                            timer.stop();
//                        }
//                        panel.repaint();
//                    }
//                });
//        timer.start();
    }



    public boolean isFilled(){
        return color > 0;
    }
    public  int getColor(){
        return color;
    }
}
