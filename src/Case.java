import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Case   {
    private int r;
    private int c;
    private int color = -1;    // -1 : vide, 0 : rouge,  1 : jaune
    private Plateau p;
    private boolean winner = false;
    private boolean falling = false;
    private int verticalPos;
    private int speed = 0;

    private static int acceleration = 2;
    private static int colSize = 80;
    private static int rowSize = 80;

    private static BufferedImage imageYellow;
    private static BufferedImage imageRed;
    private static BufferedImage imageYellowWin;
    private static BufferedImage imageRedWin;
    private static BufferedImage imageFront;
    static {
        try {
            imageYellow = ImageIO.read(new File("res/yellow.png"));
            imageRed = ImageIO.read(new File("res/red.png"));
            imageYellowWin = ImageIO.read(new File("res/yellow_win.png"));
            imageRedWin = ImageIO.read(new File("res/red_win.png"));
            imageFront = ImageIO.read(new File("res/front.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Case(int col, int row, Plateau pl){
        r=row;
        c=col;
        p = pl;
    }

    public boolean isFilled(){
        return color >= 0;
    }

    public  int getColor(){
        return color;
    }

    public static void setColSize(int colSize) {
        Case.colSize = colSize;
    }

    public static void setRowSize(int rowSize) {
        Case.rowSize = rowSize;
    }

    public static int getColSize() {
        return colSize;
    }

    public static int getRowSize() {
        return rowSize;
    }

    public void draw(Graphics g) {
        if(!falling) {//todo a simplifier, use vertical pos meme si pas falling
            switch (color){
                case -1: // todo lag si on l'enlève
                    g.setColor(Color.black);
                    g.fillOval(c * colSize, (5- r) * rowSize, colSize /2, rowSize /2);
                    break;
                case 0:
                    if(winner){
                        g.drawImage(imageRedWin,c * colSize, (5- r) * rowSize, colSize, rowSize,null);
                    }else{
                        g.drawImage(imageRed,c * colSize, (5- r) * rowSize, colSize, rowSize,null);
                    }
                    break;
                case 1:
                    if(winner){
                        g.drawImage(imageYellowWin,c * colSize, (5- r) * rowSize, colSize, rowSize,null);
                    }else{
                        g.drawImage(imageYellow,c * colSize, (5- r) * rowSize, colSize, rowSize,null);
                    }
                    break;
            }
        }else{
            switch (color){
                case 0:
                    if(winner){
                        g.drawImage(imageRedWin,c * colSize, verticalPos, colSize, rowSize,null);
                    }else{
                        g.drawImage(imageRed,c * colSize, verticalPos, colSize, rowSize,null);
                    }
                    break;
                case 1:
                    if(winner){
                        g.drawImage(imageYellowWin,c * colSize, verticalPos, colSize, rowSize,null);
                    }else{
                        g.drawImage(imageYellow,c * colSize, verticalPos, colSize, rowSize,null);
                    }
                    break;
            }

            verticalPos += speed;
            speed += acceleration;

            //position de la case en dessous pour eventuellement rebondir dessus
            int posUnder;
            if(r > 0){ // il y a une case en dessous
                posUnder = p.getCase(c, r -1).getVerticalPos()-rowSize;
            }else{
                posUnder = (p.getNbRows()-1)* rowSize;
            }

            if(verticalPos > posUnder ) {
                verticalPos = posUnder;
                if(speed > 0){
                    if(speed > 10){ // rebond
                        speed = -speed/3;
                    }else{
                        // si il n'a pas beaucoup de vitesse et il est proche de sa position finale
                        // on arrete de le faire rebondir
                        if( verticalPos > (p.getNbRows()-1- r) * rowSize - 5 && verticalPos < (p.getNbRows()-1- r) * rowSize + 5){
                            falling = false;
                            speed = 0;
                        }
                    }
                }
            }
        }
        g.drawImage(imageFront,c * colSize, (5- r) * rowSize, colSize, rowSize,null);
    }


//todo bug quand on spamme
    public int getVerticalPos() { //retourne la position du haut du pion, utilisé pour les collisions
        if(falling){
            return verticalPos;
        }else{
            return (p.getNbRows()-1-r) * rowSize;
        }
    };

    public void reset(){
        winner = false;
        falling = false;
        color = -1;
    }

    public void setWinner(boolean w){
        winner = w;
    }

    public void joue(int c) {
        color = c;
        falling = true;
        verticalPos = -rowSize;
        speed = 0;


    //todo nbfalling pion

//        timer = new Timer(50,
//                new ActionListener() {
//                    public void actionPerformed(ActionEvent ev){
//                        posf += sl/4;
//                        if(posf > (5-l) * sl ) {
//                            isFalling = false;
//                            timer.stop();
//                        }
//                        panel.repaint();
//                    }
//                });
//        timer.start();
    }

}
