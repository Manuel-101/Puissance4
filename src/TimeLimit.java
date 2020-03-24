public class TimeLimit extends Thread{
    private ComputerThread ct;
    public TimeLimit(ComputerThread ct){
        this.ct = ct;
        start();
    }

    public void run(){
        while (true){
            synchronized (this) {
                try {
                    this.wait();
                    sleep(1000);
                    ct.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void begin(){
        synchronized (this){
            notify();
        }
    }
}