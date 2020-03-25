import javax.swing.*;

public class DisplayThread extends Thread {
    private JPanel p;
    public DisplayThread(JPanel jp){
        p = jp;
    }
    //todo à optimiser
    public void run(){
        while (true) {
            try {
                p.repaint();
                sleep(30);
            } catch (InterruptedException e) {
            }
        }
    }


}
