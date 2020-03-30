import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game extends JPanel implements MouseListener {
    private Plateau p;
    private MainWindow window;
    private boolean tourJoeur = false;
    private Timer t;
    private ComputerThread computer;
//    private JPanel CurrentPlayer;


    public Game(MainWindow w){
        this.setLayout(new BorderLayout());
        window = w;
        p = new Plateau(this);
        add(p);
        JButton retour = new JButton("retour");
        retour.addActionListener(actionEvent -> stopGame());
        add(retour, BorderLayout.SOUTH);
    }


    public void startGame(int i){
        // i = 0 : joueur contre joueur
        // i = 1 : joueur contre ordinateur
        p.start();
        tourJoeur = true;
        if(i ==1){
            computer = new ComputerThread(this, 0); //todo joueur 0 = jaune
            computer.start();
        }else{
            computer = null;
        }
    }

    public void stopGame(){
        p.stop();
        if(computer != null){
            computer.end();
        }
        window.stopGame();
    }

    public void joue(int i){
        if(p.joue(i) == 0){ // coup possible : changement de joueur ou l'ordi joue
            if(computer!=null){
                computer.updateTree(i);
            }
            tourJoeur = computer == null || !tourJoeur;
            if(!tourJoeur && !p.isOver()){ // c'est a l'ordi de jouer
                computer.play();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // joue si c'est le tour du joueur
        int x = mouseEvent.getX();
        if(tourJoeur && !p.isOver()){
            joue(x/Case.getColSize());
        }
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
