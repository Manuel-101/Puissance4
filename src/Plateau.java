import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
// todo pas de rafraischissement si aucun pion isfalling
public class Plateau extends JPanel implements  ComponentListener {
    Case[][] p = new Case[7][6];
    //1 : rouge 2 : jaune
    private int nbcoups = 0;
    private int joueur = 1;
    private int nblignes = 6;
    private int nbcolonnes = 7;
    private int nbCoupsMax;
    private boolean draw =  false;
    private boolean fin = false;
    private DisplayThread dt;
    private Stack<Case> lastPlayedCases;

    public Plateau(MouseListener ml){
        for (int i = 0; i < 7; i++){
            for(int j = 0; j < 6; j++) {
                p[i][j] = new Case(i,j, this);
            }
        }
        //addMouseListener(this);
        addMouseListener(ml);
        addComponentListener(this);
        setBackground(new Color(20,20,150));
        setPreferredSize(new Dimension(Case.sc*nbcolonnes, Case.sl * nblignes));
        nbCoupsMax = nbcolonnes*nblignes;
        lastPlayedCases = new Stack<Case>();
    }

    public void undo(){
        // CTRL Z equivalent
        if(!lastPlayedCases.empty()){
            lastPlayedCases.pop().reset();
            nbcoups--;
            //qqu a gagné  //todo remplacer par quelque chose de plus facile

            if(!fin || draw){ //todo rmeplacer ar un int gagnant
                joueur = 1-joueur;
            }
            fin = false;
            draw = false;
        }
    }
    public boolean isFin(){
        return fin;
    }

    public void start(){
        // reset toutes les cases et
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
            if(draw){ //draw
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
        // joue et retourne 0 si c'est possible
        // joue et retourne 2 si c'est possible et la fin (gagné ou plateau plein)
        // retourne 1 si coup impossible
        if (i >= nbcolonnes) {
            return 1;
        }
        int j = 0;
        while (j < nblignes && p[i][j].isFilled()) {
            j++;
        }
        if (j == nblignes) {// la colonne est deja remplie
            return 1;
        } else {
            p[i][j].joue(joueur);
            lastPlayedCases.push(p[i][j]);
            nbcoups++;
            try {
                gagne(i, j);
                fin = true;
                return 2;
            } catch (GagneException e) {
               // imprime();
                joueur = 1-joueur;
                if(nbcoups == nbCoupsMax){ // plateau rempli, egalite
                    fin = true;
                    draw = true;
                    return 2;
                }
                return 0;
            }
        }

    }

    public void imprime(){
        for (int i = nblignes - 1; i >= 0; i--){
            System.out.print("|");

            for(int j = 0; j < nbcolonnes; j++) {
                switch (p[j][i].getColor()){
                    case 0:
                        System.out.print(" ");
                        break;
                    case 1:
                        System.out.print("0");
                        break;
                    case 2:
                        System.out.print("X");
                        break;
                }
            }
            System.out.println("|");
        }
    }


    public Case[] gagne(int i, int j)  throws GagneException{
        // todo a modifier le return
        boolean gagne = false;
        int couleurj = joueur+1;
        int nba;
        int k;
        int l;
        Case[] res = new Case[4];
        res[0] = p[i][j];

        for(int x = 0; x < 2; x++){
            for(int y = -1; y <2; y++){
                if (x != 0 || y == 1) {
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
                        return res;
                    }
                }
            }
        }
        throw new GagneException();
    }
    public int getNbcolonnes(){
        return  nbcolonnes;
    }

    public int objectif(int jr){
        //todo tableau d'indice deja pris en compte ( en fait peut etre pas)
        //+1 si 2 alignés et 1 libre
        //+2 si 3
        //100 si 4
        //- de meme pour l'autre couleur
        int couleurj = jr +1;
        int nba;
        int k;
        int res = 0;
        int l;





        //pour les coup de c

        for (int i = 0; i < 7; i ++){
            int j = 5;
            while (j >= 0 && !p[i][j].isFilled()) {
                j--;
            }
            if(j>=0 && p[i][j].getColor() == couleurj) {
                for (int x = 0; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        if (x != 0 || y == 1) {
                            nba = 1;
                            k = i + x;
                            l = j + y;
                            while (k >= 0 && k < 7 && l >= 0 && l < 6 && p[k][l].getColor() == couleurj && nba < 4) {
                                nba++;
                                k += x;
                                l += y;
                            }
                            k = i - x;
                            l = j - y;
                            while (k >= 0 && k < 7 && l >= 0 && l < 6 && p[k][l].getColor() == couleurj && nba < 4) {
                                nba++;
                                k -= x;
                                l -= y;
                            }
                            switch (nba) {
//                                case 2:
//                                    res += 1;
//                                    break;
//                                case 3:
//                                    res += 2;
//                                    break;
                                case 4:
                                    return 100;
                            }
                        }
                    }
                }
            }
        }

        //pour les prochains coups de c
        for (int i = 0; i < 7; i ++){
            int j = 0;
            while (j < nblignes && p[i][j].isFilled()) {
                j++;
            }
            if(j<= 6) {
                for (int x = 0; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        if (x != 0 || y == 1) {
                            nba = 0;
                            k = i + x;
                            l = j + y;
                            while (k >= 0 && k < 7 && l >= 0 && l < 6 && p[k][l].getColor() == couleurj && nba < 4) {
                                nba++;
                                k += x;
                                l += y;
                            }
                            k = i - x;
                            l = j - y;
                            while (k >= 0 && k < 7 && l >= 0 && l < 6 && p[k][l].getColor() == couleurj && nba < 4) {
                                nba++;
                                k -= x;
                                l -= y;
                            }

                            if (nba ==  2) {
                                res += 1;
                            }else if(nba >= 3) {
                                res += 2;
                            }
                        }
                    }
                }
            }
        }

        //pour les coups de c-1
        couleurj = 2-jr;

        for (int i = 0; i < 7; i ++){
            int j = 5;
            while (j >= 0 && !p[i][j].isFilled()) {
                j--;
            }
            if(j>=0 && p[i][j].getColor() == couleurj) {
                for (int x = 0; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        if (x != 0 || y == 1) {
                            nba = 1;
                            k = i + x;
                            l = j + y;
                            while (k >= 0 && k < 7 && l >= 0 && l < 6 && p[k][l].getColor() == couleurj && nba < 4) {
                                nba++;
                                k += x;
                                l += y;
                            }
                            k = i - x;
                            l = j - y;
                            while (k >= 0 && k < 7 && l >= 0 && l < 6 && p[k][l].getColor() == couleurj && nba < 4) {
                                nba++;
                                k -= x;
                                l -= y;
                            }
                            switch (nba) {
//                                case 2:
//                                    res += 1;
//                                    break;
//                                case 3:
//                                    res += 2;
//                                    break;
                                case 4:
                                    return -100;
                            }
                        }
                    }
                }
            }
        }

        //pour les prochains coups de c
        for (int i = 0; i < 7; i ++){
            int j = 0;
            while (j < nblignes && p[i][j].isFilled()) {
                j++;
            }
            if(j>=0) {
                for (int x = 0; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        if (x != 0 || y == 1) {
                            nba = 0;
                            k = i + x;
                            l = j + y;
                            while (k >= 0 && k < 7 && l >= 0 && l < 6 && p[k][l].getColor() == couleurj && nba < 4) {
                                nba++;
                                k += x;
                                l += y;
                            }
                            k = i - x;
                            l = j - y;
                            while (k >= 0 && k < 7 && l >= 0 && l < 6 && p[k][l].getColor() == couleurj && nba < 4) {
                                nba++;
                                k -= x;
                                l -= y;
                            }

                            if (nba ==  2) {
                                res -= 1;
                            }else if(nba == 3) {
                                res -= 2;
                            }
                        }
                    }
                }
            }
        }
    return res;
    }
    //todo : highlith du pion gagnant tombant
    //todo nombre de piont falling si 0 : dt .interrupt
    public Case getCase(int col, int ligne){
        return p[col][ligne];
    }

    public void componentResized(ComponentEvent e) {
        Case.sl = getSize().height/6;
        Case.sc = getSize().width/7;
    }
    @Override
    public void componentMoved(ComponentEvent componentEvent) {}
    @Override
    public void componentShown(ComponentEvent componentEvent) {}
    @Override
    public void componentHidden(ComponentEvent componentEvent) {}
}
