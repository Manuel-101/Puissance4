import javax.swing.*;

public class DisplayThread extends Thread {
    private JPanel p;

    public DisplayThread(JPanel jp){
        p = jp;
    }

    public void run(){
        try {
            while (true) {
                p.repaint();
                sleep(30);
            }
        } catch (InterruptedException e) {
        }
    }
}
