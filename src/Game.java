import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game extends JPanel implements MouseListener {//todo mettre dans un thread
    private Plateau p;
    private MainWindow gm;
    private boolean ia = false;
    private boolean tourJoeur = false;
    private ComputerThread iat;
    private Timer t;

    public Game(MainWindow g){
        this.setLayout(new BorderLayout());
        gm = g;
        p = new Plateau(this);
        add(p);
        JButton retour = new JButton("retour");
        retour.addActionListener(actionEvent -> gm.stopGame());
        add(retour, BorderLayout.SOUTH);

    }



    public void startGame(int i){
        p.start();
        tourJoeur = true;
        if(i ==1){
            ia = true;
            iat = new ComputerThread(this, 0);
            iat.start();
        }else{
            ia = false;
        }

    }

    public void stopGame(){
        p.stop();
    }

    public void iajoue(){
        synchronized (this){
            notify();
        }
    }


    //todo : click : p.joue; next;

    public void joue(int i){
        if(p.joue(i) == 0){
            if(ia){
                iat.updateTree(i);
            }
            tourJoeur = !ia || !tourJoeur;
            if(!tourJoeur){
                iat.joue();
//                try {
//                    Thread.sleep(3000);
//                    iat.interrupt();
//                    System.out.println("enterrupion");;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
        //iajoue()
        //        synchronized (this){
//            notify();
//        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        if(tourJoeur && !p.isOver()){
            joue(x/Case.getColSize());
        }
        System.out.println("objectif : " + p.objectif(0));

    }



    @Override
    public void mousePressed(MouseEvent mouseEvent) {}
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}
    @Override
    public void mouseExited(MouseEvent mouseEvent) {}


}
