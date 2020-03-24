public class MoveTree {
    // implémentation de l'algo minMax
    // c'est la racine de l'arbre qui retourne l'indice au lieu de la valeur de minMax
    private Plateau p;
    int joueur;
    private MoveNode[] children;

    MoveTree(int joueur){
        this.p = new Plateau(null);
        this.joueur = joueur;
        children = new MoveNode[p.getNbcolonnes()];
        for (int j = 0; j < p.getNbcolonnes(); j++) {
            children[j] = new MoveNode(this, j, true);
        }
    }

    public Plateau getPlateau() {
        return p;
    }

    public int objectif(){
        return p.objectif(joueur);
    }

    public void joue(int i){
        p.joue(i);
        if(children[i].getChildren() == null){
            children = new MoveNode[p.getNbcolonnes()];
            for (int j = 0; j < p.getNbcolonnes(); j++) {
                children[j] = new MoveNode(this, j, true);
            }
        }else{
            children = children[i].getChildren();
        }
//        children = children[i].getChildren();
//*/
//        children = new MoveNode[p.getNbcolonnes()];
//        for (int j = 0; j < p.getNbcolonnes(); j++) {
//            children[j] = new MoveNode(this, j, true);
//        }
    }

    public int getCoupOpti(){
        // retourne i si pas de fils,
        // ou l'indice min des valeur de objectifs  si adversaire
        // ou l'indice max de la valeur e objectifs si !adversaire
        int res = Integer.MIN_VALUE;
        int v;
        int ind = 0;
        for (int j =0; j < p.getNbcolonnes(); j++){
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

    public void computeNextProf() {
        //calcules le valeurs d'objectifs pour chaque fils
        if (children == null) {
            //creer des fils et donner un valeur
            children = new MoveNode[p.getNbcolonnes()];
            for (int j = 0; j < p.getNbcolonnes(); j++) {
                children[j] = new MoveNode(this, j, true);
            }
        }else{
            //calcules le valeurs d'objectifs pour chaque fils
            for (int j = 0; j < p.getNbcolonnes(); j++) {
                children[j].computeNextProf();
            }
        }

    }



}
