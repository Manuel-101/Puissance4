import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Plateau extends JPanel implements MouseListener, ComponentListener {
    Case[][] p = new Case[7][6];
    //1 : rouge 2 : jaune
    int joueur = 1;
    boolean fin = false;
    public Plateau(){
        for (int i = 0; i < 7; i++){
            for(int j = 0; j < 6; j++) {
                p[i][j] = new Case(i,j, this);
            }
        }
        addMouseListener(this);
        addComponentListener(this);
        setPreferredSize(new Dimension(Case.sc*7, Case.sl * 6));
    }

    public void reset(){
        joueur = 1;
        fin = false;
        for (int i = 0; i < 7; i++){
            for(int j = 0; j < 6; j++) {
                p[i][j] = new Case(i,j, this);
            }
        }
        repaint();
    }

    public void componentResized(ComponentEvent e) {
        Case.sl = getSize().height/6;
        Case.sc = getSize().width/7;
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {

    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 7; i++){
            for(int j = 5; j >=0; j--){
                p[i][j].draw(g);
            }
        }
    }


    public int joue(int i){
        //return 1 if continue 0 si gagne
        if (i > 6) {
            return 1;
        }
        int j = 0;
        while (j < 6 && p[i][j].isFilled()) {
            j++;
        }
        if (j == 6) {
            return 1;
        } else {
            p[i][j].joue(joueur + 1);
            joueur = 1 - joueur;
            repaint();
            try {
                gagne(i, j);
                System.out.println("fin gagnant : " + (1 - joueur));
                return 0;
            } catch (GagneException e) {
                return 1;
            }
        }

    }

    public Case[] gagne(int i, int j)  throws GagneException{
        int ply = 2-joueur;
        boolean gagne = false;
        int nba;
        int k;
        int l;
        int m;
        Case[] res = new Case[4];
        res[0] = p[i][j];

        for(int x = 0; x < 2; x++){
            for(int y = -1; y <2; y++){
                if(x != 0 || y !=0){
                    nba = 1;
                    k = i+x;
                    l = j+y;
                    while (k >= 0 && k < 7 && l >= 0 && l < 6 && p[k][l].getColor() == ply && nba < 4){
                        res[nba] = p[k][l];
                        nba ++;
                        k+=x;
                        l+=y;
                    }
                    k = i-x;
                    l = j-y;
                    while (k >= 0 && k <7 && l >= 0 && l < 6 && p[k][l].getColor() == ply && nba < 4 ){
                        res[nba] = p[k][l];
                        nba ++;
                        k-=x;
                        l-=y;
                    }
                    if(nba == 4){
                        //todo surligner les pions gagnants
                        //todo suppremier listener ou boolean fini
                        fin = true;
                        return res;
                    }
                }
            }
        }
        throw new GagneException();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        if(!fin){
            joue(x/Case.sc);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
