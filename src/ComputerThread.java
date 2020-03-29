import java.util.Random;
//todo si plusieurs coup opti ont la meme valeur alors random entre les coups de valeurs 0;
public class ComputerThread extends Thread {
    private Game g;
    private Random gen;
    private MoveTree mt;
    TimeLimit timelimit;
    private int optimalMove = 0;
    public ComputerThread(Game game, int joueur){
        g = game;
        mt = new MoveTree(this,joueur);
        gen = new Random();
        timelimit = new TimeLimit(this);
    }
    //todo bug quand tu quitter

    public void run(){
        int nbprof = 0;
        while (true){
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("ljlkj");
                }
            }
            timelimit.begin();
            //sleep(300);
     /*       while(true){

                System.out.println("IAJOUE ========================");
                System.out.println("olateua prof : "+ MoveNode.prof);
                mt.computeNextProf();
                if (interrupted()){System.out.println("signal recu");break;  }
            }*/
            //System.out.println("FINIAJOUE ========================");
//            mt.computeNextProf();



           //try {
            nbprof = 0;

                while (!interrupted()) {
                    try{
                        optimalMove = mt.MinMaxNextProf();
                        System.out.println("prof +1");
                        nbprof++;

                    }catch (TimeLimitException e){

                    }

                    //sleep(50);


                }
            //} catch (InterruptedException e) {
              //  System.out.println("interrupted");
            //}*/
            System.out.println("NBPROF : "+nbprof);
            mt.getPlateau().imprime();
            System.out.println("plat ^");
            g.joue(optimalMove);
            System.out.println("FINIAJOUE ========================");


        }

    }

    public void joue(){
        synchronized (this){
            notify();
        }
    }

    public void updateTree(int i){
        // met à jour l'arbre minmax en fonction du coup joué i
        mt.joue(i);
    }

}
