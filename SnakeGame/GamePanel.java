import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; //how big we want the objects in our game
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;//how many objects we can fit on the screen
    static final int DELAY = 75; //delay for timer- the higher the delay, the slower the game
    final int x[] = new int[GAME_UNITS]; // x-coordinates of the bodyparts of snake(including head)
    final int y[] = new int[GAME_UNITS];// y-coorinates of snake
    int bodyparts = 6;
    int applesEaten;
    int appleX; //x coordinates of apple location- will appear randomly each time
    int appleY; //y coordinates of apple
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
            // draw lines across game panel to make grid
            /*for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++ ){     
                g.drawLine(i * UNIT_SIZE,0, i * UNIT_SIZE, SCREEN_HEIGHT);//x axis
                g.drawLine(0,i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);//y axis 
            } */
            g.setColor(Color.red);
            g.fillOval(appleX, appleY,UNIT_SIZE,UNIT_SIZE);

            //drawing snake - for loop to iterate through all bodyparts of the snake
            for(int i = 0; i < bodyparts; i++){
                if(i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE,UNIT_SIZE);
                } else {
                    g.setColor(new Color(45,180,0));
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))) ---> can use to change colour of snake
                    g.fillRect(x[i], y[i], UNIT_SIZE,UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont()); //used to line up text in middle of screen
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyparts; i > 0;i--){
            x[i] = x[i - 1]; //shifting all coordinates in array over by 1 spot
            y[i] = y[i - 1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE; //moves head of snake to next position
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE; //moves head of snake to next position
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE; //moves head of snake to the left
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE; //moves head of snake to the right
                break;
        }
    }
    public void checkApple(){
        // x[0] is x position of snake head. appleX is x position of apple
        if((x[0] == appleX) &&(y[0] == appleY) ){
            bodyparts++;
            applesEaten++;
            newApple(); //generates a new apple
        }
    }
    public void checkCollisions(){ 
        //check if snake head collides with body
        for(int i = bodyparts; i > 0; i--){
            if((x[0]== x[i]) &&(y[0] == y[i])){
                running = false; //triggers game over method
            }
        }
        //checks if head touches left boarder
        if(x[0] < 0){
            running = false;
        }
        // checks if head touches right boarder
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        //checks if head touches top boarder
        if(y[0] < 0){
            running = false;
        }
        //check if head touches bottom boarder
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }
        //stops the timer
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont()); //used to line up text in middle of screen
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        //Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont()); //used to line up text in middle of screen
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
        if(running){
            move(); //first move the snake
            checkApple(); // check to see if snake collided with apple
            checkCollisions();
        }
        repaint(); //redraws component onscreen
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT: //we want to limit users to 90 deg turns and not 180 deg
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT: //we want to limit users to 90 deg turns and not 180 deg
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP: //we want to limit users to 90 deg turns and not 180 deg
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN: //we want to limit users to 90 deg turns and not 180 deg
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}