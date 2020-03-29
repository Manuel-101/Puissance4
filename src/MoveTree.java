import java.awt.event.MouseListener;

public class MoveTree {
    // implémentation de l'algo minMax
    // c'est la racine de l'arbre qui retourne l'indice au lieu de la valeur de minMax
    private Plateau p;
    private ComputerThread ct;
    int joueur;
    private MoveNode[] children;

    MoveTree(ComputerThread ct, int joueur){
        this.ct = ct;
        this.p = new Plateau((MouseListener) null);
        this.joueur = joueur;
        children = new MoveNode[p.getNbCols()];
        for (int j = 0; j < p.getNbCols(); j++) {
            children[j] = new MoveNode(this, j, true);
        }
    }

    public ComputerThread getCt(){
        return ct;
    }

    public Plateau getPlateau() {
        return p;
    }

    public int objectif(){
        return p.objectif(joueur);
    }

    public void joue(int i){
        p.joue(i);
//        if(children[i].getChildren() == null){
            children = new MoveNode[p.getNbCols()];
            for (int j = 0; j < p.getNbCols(); j++) {
                children[j] = new MoveNode(this, j, true);
            }
//        }else{
//            children = children[i].getChildren();
//        }

    }

    public int getCoupOpti(){
        // retourne i si pas de fils,
        // ou l'indice min des valeur de objectifs  si adversaire
        // ou l'indice max de la valeur e objectifs si !adversaire
        int res = Integer.MIN_VALUE;
        int v;
        int ind = 0;
        for (int j = 0; j < p.getNbCols(); j++){
            if(children[j].isPossible()){
                v = children[j].MinMax();
                if(v > res){
                    res = v;
                    ind = j;
                }
            }
        }
        return ind;
    }

    //todo : merge minman et compute next prof.
    // compute next prof : fait les objectifs puis minmax
    // METTRE LA PROFONDEUR EN PARAM au cas ou l'exception
    public void computeNextProf() {//todo : alpha beta
        //calcules le valeurs d'objectifs pour chaque fils
        if (children == null) {
            //creer des fils et donner un valeur
            children = new MoveNode[p.getNbCols()];
            for (int j = 0; j < p.getNbCols(); j++) {
                children[j] = new MoveNode(this, j, true);
            }
        }else{
            //calcules le valeurs d'objectifs pour chaque fils
            for (int j = 0; j < p.getNbCols(); j++) {
                children[j].computeNextProf();
            }
        }

    }


    public int MinMaxNextProf() throws TimeLimitException{
        try{
            int v;
            int ind = 0;
            int res = Integer.MIN_VALUE;
            //todo if children = null
            for (int j = 0; j < p.getNbCols(); j++) {
                if(children[j].isPossible()){
                    v = children[j].MinMaxNextProf();
                    if(v > res){
                        res = v;
                        ind = j;
                    }
                }
            }
            return ind;
        }catch (TimeLimitException e){
            throw e;
        }
    }


    public int MinMaxNextProfAlphaBeta() throws TimeLimitException{

            int v;
            int ind = 0;
            int res = Integer.MIN_VALUE;
            //todo if children = null

            for (int j = 0; j < p.getNbCols(); j++) {
                if (children[j].isPossible()) {
                    v = children[j].MinMaxNextProf(Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (v > res) {
                        res = v;
                        ind = j;
                    }
                }
            }
            return ind;

    }



}
