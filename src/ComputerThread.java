import javax.swing.*;
import java.util.Random;
//todo si plusieurs coup opti ont la meme valeur alors random entre les coups de valeurs 0;
public class ComputerThread extends Thread {
    private Game g;
    private Random gen;
    private MoveTree mt;
    Object m;
    private Thread limit;
    TimeLimit tl;
    public ComputerThread(Game game, int joueur){
        g = game;
        mt = new MoveTree(joueur);
        gen = new Random();
        tl = new TimeLimit(this);
    }


    public void run(){
        while (true){
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                }
            }
            tl.begin();
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
                while (!interrupted()) {
                    mt.computeNextProf();
                    System.out.println("camc");

                    //sleep(50);


                }
            //} catch (InterruptedException e) {
              //  System.out.println("interrupted");
            //}*/

            mt.getPlateau().imprime();
            System.out.println("plat ^");
            g.joue(mt.getCoupOpti());
            System.out.println("FINIAJOUE ========================");


        }

    }

    public void joue(){
        synchronized (this){
            notify();
        }
    }

    public void updateTree(int i){
        mt.joue(i);
    }


}
