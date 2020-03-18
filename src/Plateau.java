import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Plateau extends JPanel implements MouseListener, ComponentListener {
    Case[][] p = new Case[7][6];
    //1 : rouge 2 : jaune
    int nbcoups = 0;
    int joueur = 1;
    int nblignes = 6;
    int nbcolonnes = 7;
    int nbCoupsMax;
    boolean fin = false;
    DisplayThread dt;
    public Plateau(){
        for (int i = 0; i < 7; i++){
            for(int j = 0; j < 6; j++) {
                p[i][j] = new Case(i,j, this);
            }
        }
        addMouseListener(this);
        addComponentListener(this);
        setBackground(new Color(20,20,150));
        setPreferredSize(new Dimension(Case.sc*nbcolonnes, Case.sl * nblignes));
        nbCoupsMax = nbcolonnes*nblignes;
    }

    public void start(){
        joueur = 1;
        nbcoups = 0;
        fin = false;
        for (int i = 0; i < nbcolonnes; i++){
            for(int j = 0; j < nblignes; j++) {
                p[i][j].reset();
            }
        }
        Case.sl = getSize().height/nblignes;
        Case.sc = getSize().width/nbcolonnes;
        dt = new DisplayThread(this);
        dt.start();
    }

    public void stop(){
        dt.interrupt();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < nbcolonnes; i++){
            for(int j = nblignes-1; j >=0; j--){
                p[i][j].draw(g);
            }
        }
        if(fin){
            String s;
            if(nbcoups == nbCoupsMax){ //draw
                g.setColor(Color.white);
                s="Draw!";
            }else if(joueur == 0) {
                g.setColor(Color.red);
                s="Red win!";
            }else{
                g.setColor(Color.yellow);
                s = "Yellow win!";
            }
            g.setFont(new Font(null,0,Math.min(getSize().height,getSize().width)/6));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(s, (getSize().width-g.getFontMetrics().stringWidth(s))/2,(getSize().height+g.getFontMetrics().getDescent())/2);

        }
    }


    public int joue(int i){
        //return 0 if continue 1 si gagne
        if (i >= nbcolonnes) {
            return 0;
        }
        int j = 0;
        while (j < nblignes && p[i][j].isFilled()) {
            j++;
        }
        if (j == nblignes) {// la colonne est deja remplie
            return 0;
        } else {
            p[i][j].joue(joueur);
            try {
                gagne(i, j);
                return 1;
            } catch (GagneException e) {
                joueur = 1 - joueur; // on change de joueur
                nbcoups++;
                if(nbcoups == nbCoupsMax){ // plateau rempli, egalite
                    fin = true;
                }
                return 0;
            }
        }

    }

    public Case[] gagne(int i, int j)  throws GagneException{
        boolean gagne = false;
        int couleurj = joueur+1;
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
                    while (k >= 0 && k < 7 && l >= 0 && l < 6 && p[k][l].getColor() == couleurj && nba < 4){
                        res[nba] = p[k][l];
                        nba ++;
                        k+=x;
                        l+=y;
                    }
                    k = i-x;
                    l = j-y;
                    while (k >= 0 && k <7 && l >= 0 && l < 6 && p[k][l].getColor() == couleurj && nba < 4 ){
                        res[nba] = p[k][l];
                        nba ++;
                        k-=x;
                        l-=y;
                    }
                    if(nba == 4){
                        for( Case c : res){ // surligne les pions gagnants
                            c.setWinner(true);
                        }
                        fin = true;
                        return res;
                    }
                }
            }
        }
        throw new GagneException();
    }

    //todo : joue s'execute a la fin de la chute

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
}
