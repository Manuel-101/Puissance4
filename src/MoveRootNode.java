import java.awt.event.MouseListener;

public class MoveRootNode{
    // implémentation de l'algo minMax
    // c'est la racine de l'arbre qui retourne l'indice au lieu de la valeur de minMax
    private Plateau p;
    private ComputerThread computer;
    private int player;
    private MoveNode[] children;
    private int nbNodes = 0;

    MoveRootNode(ComputerThread ct, int player){
        this.computer = ct;
        this.p = new Plateau(null);
        this.player = player;
        children = new MoveNode[p.getNbCols()];
        for (int j = 0; j < p.getNbCols(); j++) {
            children[j] = new MoveNode(this, j, true);
        }
        nbNodes = p.getNbCols();
    }

    public int getNbNodes() {
        return nbNodes;
    }

    public void increaseNbNodes(){
        nbNodes += p.getNbCols();
    }

    public ComputerThread getComputer(){
        return computer;
    }

    public Plateau getPlateau() {
        return p;
    }

    public int objectif(){
        return p.objectif(player);
    }


    public void joue(int i){
        p.joue(i);
        nbNodes = p.getNbCols();
        children = new MoveNode[p.getNbCols()];
        for (int j = 0; j < p.getNbCols(); j++) {
            children[j] = new MoveNode(this, j, true);
        }
    }



    public int MinMaxNextDepthAlphaBeta() throws TimeLimitException, MaxDepthException {
        int oldNbnodes = nbNodes;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int V = Integer.MIN_VALUE;
        int v;
        int ind = 0;
        for (int j = 0; j < p.getNbCols(); j++) {
            if(children[j].isPossible()){
                v = children[j].MinMaxNextDepth(alpha,beta);
                if(v > V){
                    V = v;
                    ind = j;
                }
                alpha  = Integer.max(alpha,V);
            }
        }
        if(nbNodes == oldNbnodes){ //pas de nouveaux noeuds à calculer
            throw new MaxDepthException();
        }
        return ind;
    }



}
