public class ComputerThread extends Thread {
    private Game game;
    private boolean stopped = false;
    private MoveRootNode mt;
    private TimeLimit timelimit;
    private int optimalMove = 0;

    public ComputerThread(Game g, int player){
        this.game = g;
        mt = new MoveRootNode(this,player);
        timelimit = new TimeLimit(this);
    }

    public void run(){
        int depth;
        while (true){
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            if(stopped){
                return;
            }
            timelimit.begin();
            depth = 0;
            while (!interrupted()) {
                try{
                    optimalMove = mt.MinMaxNextDepthAlphaBeta();
                    depth++;
                }catch (TimeLimitException te){
                }catch (MaxDepthException me){
                }
            }
            if(stopped){
                return;
            }
            game.joue(optimalMove);
        }
    }

    public void play(){
        synchronized (this){
            notify();
        }
    }

    public void end(){
        stopped = true;
        timelimit.interrupt();
        interrupt();
    }

    public void updateTree(int i){
        // met à jour l'arbre minmax en fonction du coup joué i
        mt.joue(i);
    }

}
