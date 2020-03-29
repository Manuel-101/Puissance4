public class MoveNode {
    private MoveNode[] children;
    private int value = 0;
    private int ind; //todo a suppr
    private boolean possible = true;
    private MoveTree mt;
    private boolean opponent;
    private boolean fin = false;
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
            //todo tester si c'est la fin ou possible
            int res = children[0].MinMax();
            int v;
            if(opponent){
                for (int j = 1; j < mt.getPlateau().getNbCols(); j++){
                    v = children[j].MinMax();
                    if(v < res){
                        res = v;
                    }
                }
            }else{
                for (int j = 1; j < mt.getPlateau().getNbCols(); j++){
                    v = children[j].MinMax();
                    if(v > res){
                        res = v;
                    }
                }
            }
            return res;
        }
    }

    public int MinMax(int alpha, int beta){
        // retourne objectifs si pas de fils,
        // ou min des valeur de objectifs  si adversaire
        // ou max de la valeur e objectifs si !adversaire
        if(children == null){
            // System.out.println("sull");

            return value;
        }else{
            //todo tester si c'est la fin ou possible
            int v;
            if(opponent){
                v = Integer.MAX_VALUE;
                for (int j = 0; j < mt.getPlateau().getNbCols(); j++){
                    v = Math.min(v,children[j].MinMax(alpha, beta));
                    if(alpha >= v){ //coupure alpha
                        return v;
                    }
                    beta = Math.min(v,beta);
                }
            }else{
                v = Integer.MIN_VALUE;
                for (int j = 0; j < mt.getPlateau().getNbCols(); j++){
                    v = Math.max(v,children[j].MinMax(alpha, beta));
                    if(beta <= v){ //coupure alpha
                        return v;
                    }
                    alpha = Math.max(v,alpha);
                }
            }
            return v;
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
                children = new MoveNode[mt.getPlateau().getNbCols()];
                for (int j = 0; j < mt.getPlateau().getNbCols(); j++) {
                    children[j] = new MoveNode(mt, j, !opponent);
                }
            }else{
                //calcules le valeurs d'objectifs pour chaque fils
                for (int j = 0; j < mt.getPlateau().getNbCols(); j++) {
                    children[j].computeNextProf();
                }
            }
            mt.getPlateau().undo();
        }


    }

    public int MinMaxNextProf() throws TimeLimitException{

        if(!mt.getCt().isInterrupted()) {
            int a = 0;
            int res = 0;
            int v;
            if (possible && !fin) {
                try{

                    mt.getPlateau().joue(ind);
                //  System.out.println("computing");
                if (children == null) {
                    //creer des fils et donner une valeur et faire min ou  max
                    children = new MoveNode[mt.getPlateau().getNbCols()];
                    for (int j = 0; j < mt.getPlateau().getNbCols(); j++) {
                        children[j] = new MoveNode(mt, j, !opponent);
                    }
                    //calcule minmax
                    res = children[0].value;
                    if (opponent) {
                        for (int j = 1; j < mt.getPlateau().getNbCols(); j++) {
                            v = children[j].value;
                            if (v < res) {
                                res = v;
                            }
                        }
                    } else {
                        for (int j = 1; j < mt.getPlateau().getNbCols(); j++) {
                            v = children[j].value;
                            if (v > res) {
                                res = v;
                            }
                        }
                    }
                } else {
                    //todo tester si c'est la fin ou possible
                    res = children[0].MinMaxNextProf();
                    if (opponent) {
                        for (int j = 1; j < mt.getPlateau().getNbCols(); j++) {
                            v = children[j].MinMaxNextProf();
                            if (v < res) {
                                res = v;
                            }
                        }
                    } else {
                        for (int j = 1; j < mt.getPlateau().getNbCols(); j++) {
                            v = children[j].MinMaxNextProf();
                            if (v > res) {
                                res = v;
                            }
                        }
                    }
                }
                mt.getPlateau().undo();
                return res;
                }catch(TimeLimitException e){
                    mt.getPlateau().undo();
                    throw e;
                }
            } else {
                return value;
            }

        }else{
            //return value;
            throw new TimeLimitException();
        }
    }


    public int MinMaxNextProf(int alpha, int beta) throws TimeLimitException{

        if(!mt.getCt().isInterrupted()) {
            int a = 0;
            int res = 0;
            int v;
            if (possible && !fin) {
                mt.getPlateau().joue(ind);
                //  System.out.println("computing");
                if (children == null) {
                    //creer des fils et donner une valeur et faire min ou  max

                    children = new MoveNode[mt.getPlateau().getNbCols()];
                    for (int j = 0; j < mt.getPlateau().getNbCols(); j++) {
                        children[j] = new MoveNode(mt, j, !opponent);
                    }
                    //calcule minmax
                    //todo le faire ici aussi
                    res = children[0].value;
                    if (opponent) {
                        for (int j = 1; j < mt.getPlateau().getNbCols(); j++) {
                            v = children[j].value;
                            if (v < res) {
                                res = v;
                            }
                        }
                    } else {
                        for (int j = 1; j < mt.getPlateau().getNbCols(); j++) {
                            v = children[j].value;
                            if (v > res) {
                                res = v;
                            }
                        }
                    }
                } else {
                    //todo tester si c'est la fin ou possible
                    if (opponent) {
                        v = Integer.MAX_VALUE;
                        for (int j = 0; j < mt.getPlateau().getNbCols(); j++) {
                            v = Integer.min(v, MinMaxNextProf(alpha, beta));
                            if (alpha >= v) { //coupure alpha
                                return v;
                            }
                            beta = Integer.min(beta, v);
                        }
                    } else {
                        v = Integer.MIN_VALUE;
                        for (int j = 0; j < mt.getPlateau().getNbCols(); j++) {
                            v = Integer.max(v, MinMaxNextProf(alpha, beta));
                            if (beta <= v) { //coupure beta
                                return v;
                            }
                            alpha = Integer.max(alpha, v);
                        }
                    }
                    res = v;
                }
                mt.getPlateau().undo();
                return res;

            } else {
                return value;
            }

        }else{
            //return value;
            throw new TimeLimitException();
        }
    }


        public void setInd(int ind) {
        this.ind = ind;
    }
}
