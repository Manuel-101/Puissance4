import java.util.Arrays;

public class MoveNode implements Comparable<MoveNode>{
    private MoveNode[] children;
    private int value = 0;
    private int ind;
    private boolean possible = true;
    private MoveRootNode mt;
    private boolean opponent;
    private boolean fin = false;

    public MoveNode(MoveRootNode mt, int ind, boolean opp){
        this.mt = mt;
        this.ind = ind;
        this.opponent = opp;
        int a = mt.getPlateau().joue(ind);
        if(a == 1){
            possible = false;
        }else{
            value = mt.objectif();
            mt.getPlateau().undo();
        }
        if(a == 2){
            fin = true;
        }
    }

    public boolean isPossible() {
        return possible;
    }

    public int MinMaxNextDepth(int alpha, int beta) throws TimeLimitException{
        if(!mt.getComputer().isInterrupted()) {
            int a = 0;
            int v;
            if (possible && !fin) {
                try{
                    mt.getPlateau().joue(ind);
                    if (children == null) {
                        //creer des fils et donner une valeur et faire minmax
                        children = new MoveNode[mt.getPlateau().getNbCols()];
                        for (int j = 0; j < mt.getPlateau().getNbCols(); j++) {
                            children[j] = new MoveNode(mt, j, !opponent);
                        }
                        mt.increaseNbNodes();

                        //mtd(f)
                        Arrays.sort(children);

                        v = children[0].value;
                        if (opponent) {
                            for (int j = 1; j < mt.getPlateau().getNbCols(); j++) {
                                v = Integer.min(v,children[j].value);
                            }
                        } else {
                            for (int j = 1; j < mt.getPlateau().getNbCols(); j++) {
                                v = Integer.max(v, children[j].value);
                            }
                        }
                    } else {
                        //mtd(f)
                        Arrays.sort(children);

                        if (opponent) {
                            v = Integer.MAX_VALUE;
                            for (int j = 0; j < mt.getPlateau().getNbCols(); j++) {
                                v = Integer.min(v,children[j].MinMaxNextDepth(alpha,beta));
                                if(alpha >= v){
                                    mt.getPlateau().undo();
                                    value = v;
                                    return v;
                                }
                                beta = Integer.min(v,beta);
                            }
                        } else {
                            v = Integer.MIN_VALUE;
                            for (int j = 0; j < mt.getPlateau().getNbCols(); j++) {
                                v = Integer.max(v,children[j].MinMaxNextDepth(alpha,beta));
                                if (beta <= v) {
                                    mt.getPlateau().undo();
                                    value = v;
                                    return v;
                                }
                                alpha = Integer.max(v,alpha);
                            }
                        }
                    }
                    mt.getPlateau().undo();
                    value = v;
                    return v;
                }catch(TimeLimitException e){
                    mt.getPlateau().undo();
                    throw e;
                }
            } else {
                return value;
            }
        }else{
            throw new TimeLimitException();
        }
    }


    @Override
    public int compareTo(MoveNode that) {
        return value-that.value;
    }
}
