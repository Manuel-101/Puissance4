import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
// todo pas de rafraischissement si aucun pion isfalling
public class Plateau extends JPanel implements  ComponentListener {
    Case[][] grid;
    private int nbMoves = 0;
    private int maxNbMoves;
    private int player = 1; //0 : rouge 1 : jaune
    private int nbRows = 6;
    private int nbCols = 7;
    private boolean draw =  false;
    private boolean over = false;
    private DisplayThread dt;
    private Stack<Case> undoStack;

    //pour parcourir la grille dans les 4 directions
    private static int[] dirx = {0,1,1,1};
    private static int[] diry = {1,0,1,-1};

    public Plateau(MouseListener ml){
        grid = new Case[7][6];
        for (int i = 0; i < 7; i++){
            for(int j = 0; j < 6; j++) {
                grid[i][j] = new Case(i,j, this);
            }
        }
        addMouseListener(ml);
        addComponentListener(this);
        setBackground(new Color(20,20,50));
        setPreferredSize(new Dimension(Case.getColSize()* nbCols, Case.getRowSize() * nbRows));
        maxNbMoves = nbCols * nbRows;
        undoStack = new Stack<Case>();
    }

    public int getNbCols(){
        return nbCols;
    }

    public int getNbRows() {
        return nbRows;
    }

    public boolean isOver(){
        return over;
    }

    public Case getCase(int col, int row){
        return grid[col][row];
    }

    public void start(){
        player = 1;
        nbMoves = 0;
        over = false;
        draw = false;
        for (int i = 0; i < nbCols; i++){
            for(int j = 0; j < nbRows; j++) {
                grid[i][j].reset();
            }
        }
        Case.setRowSize(getSize().height/ nbRows);
        Case.setColSize(getSize().width/ nbCols);
        dt = new DisplayThread(this);
        dt.start();
    }

    public void stop(){
        dt.interrupt();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < nbCols; i++){
            for(int j = 0; j < nbRows; j++){
                grid[i][j].draw(g);
            }
        }
        if(over){ // affiche le gagnant ou égalité
            String s;
            if(draw){
                g.setColor(Color.white);
                s="Draw!";
            }else if(player == 0) {
                g.setColor(Color.red);
                s="Red win!";
            }else{
                g.setColor(Color.yellow);
                s = "Yellow win!";
            }
            g.setFont(new Font(null,0,Math.min(getSize().height,getSize().width)/6));
            g.drawString(s, (getSize().width-g.getFontMetrics().stringWidth(s))/2,(getSize().height+g.getFontMetrics().getDescent())/2);
        }
    }
    
    public int joue(int i){
        // joue et retourne 0 si c'est possible
        // joue et retourne 2 si c'est possible et la fin (gagné ou plateau plein)
        // retourne 1 si coup impossible
        if (i < 0 || i >= nbCols) {
            return 1;
        }
        int j = 0;
        while (j < nbRows && grid[i][j].isFilled()) {
            j++;
        }
        if (j == nbRows) {// la colonne est pleine
            return 1;
        } else {
            grid[i][j].joue(player);
            undoStack.push(grid[i][j]);
            nbMoves++;
            try {
                gagne(i, j); //on teste si le joueur a gagné
                over = true;
                return 2;
            } catch (GagneException e) {
                player = 1 - player; // change de joueur
                if(nbMoves == maxNbMoves){ // plateau rempli, égalité
                    over = true;
                    draw = true;
                    return 2;
                }
                return 0;
            }
        }
    }

    public void imprime(){
        for (int i = nbRows - 1; i >= 0; i--){
            System.out.print("|");
            for(int j = 0; j < nbCols; j++) {
                switch (grid[j][i].getColor()){
                    case -1:
                        System.out.print(" ");
                        break;
                    case 0:
                        System.out.print("0");
                        break;
                    case 1:
                        System.out.print("X");
                        break;
                }
            }
            System.out.println("|");
        }
    }

    public void gagne(int i, int j)  throws GagneException{
        // met fin = true
        // met les pions gagnants en surbrillance si le coup en i,j est gagnant, 
        // throws GagneException si le joueur n'as pas gagné
        int nba; // nombre d'alignés
        int x;
        int y;
        int dx;
        int dy;
        Case[] winnings = new Case[4];
        winnings[0] = grid[i][j];
        for(int k = 0; k < 4; k ++){ // parcoure la grille dans les lignes, colonnes et 2 diagonales pour trouver 4 alignés
            dx = dirx[k];
            dy = diry[k];
            x = i+dx;
            y = j+dy;
            nba = 1;
            while (x >= 0 && x < nbCols && y >= 0 && y < nbRows && grid[x][y].getColor() == player && nba < 4){
                winnings[nba] = grid[x][y];
                nba++;
                x+=dx;
                y+=dy;
            }
            x = i-dx;
            y = j-dy;
            while (x >= 0 && x < nbCols && y >= 0 && y < nbRows && grid[x][y].getColor() == player && nba < 4 ){
                winnings[nba] = grid[x][y];
                nba++;
                x-=dx;
                y-=dy;
            }
            if(nba == 4){
                for( Case c : winnings){ // surligne les pions gagnants
                    c.setWinner(true);
                }
                return;
            }
        }
        throw new GagneException();
    }


    public int objectif(int jr){
        //todo à améliorer
        //todo tableau d'indice deja pris en compte ( en fait peut etre pas)
        //+1 si 2 alignés et 1 libre
        //+10 si 3
        //100 si 4
        //- de meme pour l'autre couleur
        int couleurj = jr;
        int nba;
        int nbl;
        int k;
        int res = 0;
        int l;

        int x;
        int y;




        //pour les coup de c

        for (int i = 0; i < 7; i ++){
            int j = 5;
            while (j >= 0 && !grid[i][j].isFilled()) {
                j--;
            }
            if(j>=0 && grid[i][j].getColor() == couleurj) {
                for(int ii = 0; ii < 4; ii ++){
                    x = dirx[ii];
                    y = diry[ii];
                    nba = 1;
                    k = i + x;
                    l = j + y;
                    while (k >= 0 && k < 7 && l >= 0 && l < 6 && grid[k][l].getColor() == couleurj && nba < 4) {
                        nba++;
                        k += x;
                        l += y;
                    }
                    k = i - x;
                    l = j - y;
                    while (k >= 0 && k < 7 && l >= 0 && l < 6 && grid[k][l].getColor() == couleurj && nba < 4) {
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

        //pour les prochains coups de c
        for (int i = 0; i < 7; i ++){
            int j = 0;
            while (j < nbRows && grid[i][j].isFilled()) {
                j++;
            }
            if(j<= 6) {
                for(int ii = 0; ii < 4; ii ++){
                    x = dirx[ii];
                    y = diry[ii];
                    nba = 0;
                    nbl = 0;
                    k = i + x;
                    l = j + y;
                    //TESTE SI iL Y A UN PION LIBRE DANS L'AUTRE DIRECTION
                    if(k >= 0 && k < 7 && l >= 0 && l < 6 && !grid[k][l].isFilled()) {
                        nbl++;
                    }
                    while (k >= 0 && k < 7 && l >= 0 && l < 6 && grid[k][l].getColor() == couleurj && nba < 4) {
                        nba++;
                        k += x;
                        l += y;
                    }
                    k = i - x;
                    l = j - y;
                    //TESTE SI iL Y A UN PION LIBRE DANS L'AUTRE DIRECTION
                    if(k >= 0 && k < 7 && l >= 0 && l < 6 && !grid[k][l].isFilled()) {
                        nbl++;
                    }
                    while (k >= 0 && k < 7 && l >= 0 && l < 6 && grid[k][l].getColor() == couleurj && nba < 4) {
                        nba++;
                        k -= x;
                        l -= y;
                    }

                    if (nba ==  2 && nbl > 0) {
                        res += 1;
                    }else if(nba >= 3) {
                        res += 10;
                    }
                }


            }
        }

        //pour les coups de c-1
        couleurj = 1-jr;

        for (int i = 0; i < 7; i ++){
            int j = 5;
            while (j >= 0 && !grid[i][j].isFilled()) {
                j--;
            }
            if(j>=0 && grid[i][j].getColor() == couleurj) {
                for(int ii = 0; ii < 4; ii ++){
                    x = dirx[ii];
                    y = diry[ii];
                    nba = 1;
                    k = i + x;
                    l = j + y;
                    while (k >= 0 && k < 7 && l >= 0 && l < 6 && grid[k][l].getColor() == couleurj && nba < 4) {
                        nba++;
                        k += x;
                        l += y;
                    }
                    k = i - x;
                    l = j - y;
                    while (k >= 0 && k < 7 && l >= 0 && l < 6 && grid[k][l].getColor() == couleurj && nba < 4) {
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

        //pour les prochains coups de c-1
        for (int i = 0; i < 7; i ++){
            int j = 0;
            while (j < nbRows && grid[i][j].isFilled()) {
                j++;
            }
            if(j>=0) {
                for(int ii = 0; ii < 4; ii ++){
                    x = dirx[ii];
                    y = diry[ii];
                    nba = 0;
                    nbl = 0;
                    k = i + x;
                    l = j + y;
                    //TESTE SI iL Y A UN PION LIBRE DANS L'AUTRE DIRECTION
                    if(k >= 0 && k < 7 && l >= 0 && l < 6 && !grid[k][l].isFilled()) {
                        nbl++;
                    }
                    while (k >= 0 && k < 7 && l >= 0 && l < 6 && grid[k][l].getColor() == couleurj && nba < 4) {
                        nba++;
                        k += x;
                        l += y;
                    }
                    k = i - x;
                    l = j - y;
                    //TESTE SI iL Y A UN PION LIBRE DANS L'AUTRE DIRECTION
                    if(k >= 0 && k < 7 && l >= 0 && l < 6 && !grid[k][l].isFilled()) {
                        nbl++;
                    }
                    while (k >= 0 && k < 7 && l >= 0 && l < 6 && grid[k][l].getColor() == couleurj && nba < 4) {
                        nba++;
                        k -= x;
                        l -= y;
                    }

                    if (nba ==  2 && nbl > 0) {
                        res -= 1;
                    }else if(nba == 3) {
                        res -= 10;
                    }
                }
            }
        }
    return res;
    }

    public void undo(){
        if(!undoStack.empty()){
            undoStack.pop().reset();
            nbMoves--;
            if(!over || draw){
                player = 1-player;
            }
            over = false;
            draw = false;
        }
    }

    //todo nombre de piont falling si 0 : dt .interrupt

    public void componentResized(ComponentEvent e) {
        Case.setRowSize(getSize().height/6);
        Case.setColSize(getSize().width/7);
    }
    @Override
    public void componentMoved(ComponentEvent componentEvent) {}
    @Override
    public void componentShown(ComponentEvent componentEvent) {}
    @Override
    public void componentHidden(ComponentEvent componentEvent) {}
}
