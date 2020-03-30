import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class Plateau extends JPanel implements  ComponentListener {
    Case[][] grid;
    private int nbMoves = 0;
    private int maxNbMoves = 7 * 6;
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
        setPreferredSize(new Dimension(Case.getColSize()* nbCols, Case.getRowSize() * nbRows));
        setBackground(new Color(0,0,110));
        undoStack = new Stack<Case>();
    }

    public Plateau(MouseListener ml, int nbCols, int nbRows){
        this(ml);
        this.nbCols = nbCols;
        this.nbRows = nbRows;
        maxNbMoves = nbCols * nbRows;
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
            } catch (WinException e) {
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

    public void gagne(int i, int j)  throws WinException {
        // met les pions gagnants en surbrillance et over = true si le coup en i,j est gagnant,
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
            if(nba == 4){ // gagné!
                for( Case c : winnings){ // surligne les pions gagnants
                    c.setWinner(true);
                }
                return;
            }
        }
        throw new WinException();
    }


    public int objectif(int player){
        //todo faire 1000 / nombre de colonnes en dessus
        //+1 * (nombre de colonnes - distance au pion en dessous) si 2 alignés et 2 libres
        //+1000 *     -                     -             -              si 3 alignés et 1 libre
        //+infinity si 4 alignés
        //de meme pour l'autre couleur en négatif
        int playerColor = player;
        int nba; // nombre d'aligné rempli
        int nbl; // nombre d'alignés libres
        int res = 0;

        int x;
        int y;
        int dx;
        int dy;
        int mult = 0;
        int color = 0;

        //pour savoir si il en a déja 4 alignés
        for (int i = 0; i < nbCols; i ++){
            int j = nbRows -1;
            while (j >= 0 && !grid[i][j].isFilled()) { // descendre jusqu'a trouver un pion
                j--;
            }
            if(j>=0 && grid[i][j].getColor() == playerColor) {
                mult = 1;
                color = playerColor;
            }else if(j>=0){
                mult = -1;
                color = 1-playerColor;
            }
            if(j>=0) {
                for (int k = 0; k < 4; k++) {
                    dx = dirx[k];
                    dy = diry[k];
                    nba = 1;
                    x = i + dx;
                    y = j + dy;
                    while (x >= 0 && x < 7 && y >= 0 && y < 6 && grid[x][y].getColor() == color && nba < 4) {
                        nba++;
                        x += dx;
                        y += dy;
                    }
                    x = i -dx;
                    y = j - dy;
                    while (x >= 0 && x < 7 && y >= 0 && y < 6 && grid[x][y].getColor() == color && nba < 4) {
                        nba++;
                        x -= dx;
                        y -= dy;
                    }
                    switch (nba) {
                        case 4:
                            if(mult == -1){
                                return Integer.MIN_VALUE;
                            }else{
                                return Integer.MAX_VALUE;
                            }
                    }
                }
            }

        }

        //pour les prochains coups : cherche 3 alignés et 1 libres ou  2 alignés et 2 libres
        for (int i = 0; i < nbCols; i ++) {
            int pionbas = 0;
            while ( pionbas < nbRows && grid[i][pionbas].isFilled()){
                pionbas ++;
            }
            for (int j = nbRows-1; j >= 0 && !grid[i][j].isFilled(); j--) {
                for (color = 0; color < 2; color++) {
                    if (color == playerColor) {
                        mult = 1;
                    } else {
                        mult = -1;
                    }
                    for (int k = 0; k < 4; k++) {
                        dx = dirx[k];
                        dy = diry[k];
                        nba = 0;
                        nbl = 1;
                        x = i + dx;
                        y = j + dy;
                        if(x >= 0 && x < 7 && y >= 0 && y < 6 && !grid[x][y].isFilled()){
                            nbl++;
                            x = i + dx;
                            y = j + dy;
                        }
                        while (x >= 0 && x < 7 && y >= 0 && y < 6 && (grid[x][y].getColor() == color) && nba< 4 ) {
                            nba++;
                            x += dx;
                            y += dy;
                        }
                        x = i - dx;
                        y = j - dy;
                        if(nbl < 2 && x >= 0 && x < 7 && y >= 0 && y < 6 && !grid[x][y].isFilled()){
                            nbl++;
                            x = i - dx;
                            y = j - dy;
                        }
                        while (x >= 0 && x < 7 && y >= 0 && y < 6 && (grid[x][y].getColor() == color) && nba< 4) {
                            nba++;
                            x -= dx;
                            y -= dy;
                        }

                        if (nba == 2 && nbl == 2) {
                            res += mult * (nbRows + 1 - (j-pionbas));
                        } else if (nba >= 3) {
                            res += mult * (nbRows + 1 - (j-pionbas)) * 1000;
                        }
                    }
                }

            }
        }
    return res;
    }

    public void undo(){
        // défait le coup joué
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
