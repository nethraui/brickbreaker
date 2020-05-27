package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    private int numOfBricks = 21;

    private Timer timer;
    private int delay = 8;
    private int playerX = 310; //player position

    private int ballX = 120; // ball position
    private int ballY = 350;
    private int ballDirX = -1;
    private int ballDirY = -2;

    private MapGenerator map;

    public GamePlay(){
        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        timer = new Timer(delay, this);
        timer.start();

    }

    public void paint(Graphics g){
        super.paint(g);
        //background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);

        //drap map
        map.draw((Graphics2D) g);

        //borders
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);

        //scores
        g.setColor(Color.green);
        g.setFont(new Font("serif", Font.ITALIC, 25));
        g.drawString(""+score, 590, 30);

        //padel
        g.setColor(Color.green);
        g.fillRect(playerX,550, 100, 8);

        //Ball
        g.setColor(Color.yellow);
        g.fillOval(ballX,ballY,20,20);

        if(numOfBricks <= 0){
            play = false;
            ballDirY = 0;
            ballDirX = 0;
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.ITALIC, 30));
            g.drawString("YOU WON!! "+score, 230, 300);

            g.setFont(new Font("serif", Font.ITALIC, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }
        if(ballY > 570){
            play = false;
            ballDirY = 0;
            ballDirX = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.ITALIC, 30));
            g.drawString("GAME OVER, Score: "+score, 190, 300);

            g.setFont(new Font("serif", Font.ITALIC, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(play){
            if(new Rectangle(ballX, ballY, 20, 20).intersects(playerX, 550, 100,8)){
                ballDirY = -ballDirY;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if(map.map[i][j] > 0){
                        int brickX  = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballX, ballY,20,20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0,i,j);
                            numOfBricks--;
                            score += 5;

                            if(ballX + 19  <= brickRect.x || ballX + 1  >= brickRect.x + brickRect.width){
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }

                            break A;
                        }
                    }
                }
            }

            ballX += ballDirX;
            ballY += ballDirY;

            if(ballX < 0){
                ballDirX = -ballDirX;
            }

            if(ballY < 0){
                ballDirY = -ballDirY;
            }

            if(ballX > 670){
                ballDirX = -ballDirX;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
     if(e.getKeyCode() == KeyEvent.VK_RIGHT){
        if(playerX >= 600){
            playerX = 600;
        }else{
            moveRight();
        }
     }
     if(e.getKeyCode() == KeyEvent.VK_LEFT){
         if(playerX < 10){
             playerX = 10;
         }else{
             moveLeft();
         }
     }
     if(e.getKeyCode() == KeyEvent.VK_ENTER){
         if(!play){
             play = true;
             ballX = 120;
             ballY = 350;
             ballDirX = -1;
             ballDirY = -2;
             playerX = 310;
             numOfBricks = 21;
             score = 0;
             map = new MapGenerator(3, 7);
             repaint();
         }
     }
    }

    public void moveRight(){
        play = true;
        playerX += 20;
    }

    public void moveLeft(){
        play = true;
        playerX -= 20;
    }

}
