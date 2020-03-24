public class MoveNode {
    private MoveNode[] children;
    private int value = 0;
    private int ind; //todo a suppr
    private boolean possible = true;
    private MoveTree mt;
    private boolean opponent;
    private boolean fin = false;
    static int prof = 0;
    //todo supprimer ind, remplacer root par childrens

    public MoveNode(MoveTree mt, int ind, boolean opp){
        this.mt = mt;
        this.ind = ind;
        this.opponent = opp;
        int a = mt.getPlateau().joue(ind);
        if(a == 1){
            possible = false;
           //todo a refaire et tester les cas particuliers
        }else{
            value = mt.objectif();
            mt.getPlateau().undo();
        }
        if(a == 2){
            fin = true;
            //System.out.println("a == 2 value : "+value);
        }
    }

    public boolean isPossible() {
        return possible;
    }

    public int MinMax(){
        // retourne objectifs si pas de fils,
        // ou min des valeur de objectifs  si adversaire
        // ou max de la valeur e objectifs si !adversaire
        if(children == null){
           // System.out.println("sull");

            return value;
        }else{
            //todo tester si c'est la fin
            int res = children[0].MinMax();
            int v;
            if(opponent){
                for (int j =1; j < mt.getPlateau().getNbcolonnes(); j++){
                    v = children[j].MinMax();
                    if(v < res){
                        res = v;
                    }
                }
            }else{
                for (int j =1; j < mt.getPlateau().getNbcolonnes(); j++){
                    v = children[j].MinMax();
                    if(v > res){
                        res = v;
                    }
                }
            }
            return res;
        }
    }

    public MoveNode[] getChildren() {
        return children;
    }

    public void computeNextProf(){
        int a =0;
        if(possible && !fin){
            mt.getPlateau().joue(ind);

          //  System.out.println("computing");
            if (children == null) {
                //creer des fils et donner un valeur
                children = new MoveNode[mt.getPlateau().getNbcolonnes()];
                for (int j = 0; j < mt.getPlateau().getNbcolonnes(); j++) {
                    children[j] = new MoveNode(mt, j, !opponent);
                }
            }else{
                //calcules le valeurs d'objectifs pour chaque fils
                for (int j = 0; j < mt.getPlateau().getNbcolonnes(); j++) {
                    children[j].computeNextProf();
                }
            }
            mt.getPlateau().undo();
        }


    }

    public void setInd(int ind) {
        this.ind = ind;
    }
}
