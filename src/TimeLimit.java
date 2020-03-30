public class TimeLimit extends Thread{

    private ComputerThread ct;

    public TimeLimit(ComputerThread ct){
        this.ct = ct;
        start();
    }

    public void run(){
        try {
            while (true){
                synchronized (this) {
                        wait();
                        sleep(5000);
                        ct.interrupt();
                }
            }
        } catch (InterruptedException e) {
        }
    }

    public void begin(){
        synchronized (this){
            notify();
        }
    }
}
