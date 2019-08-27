
package bounceback;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.Timer;
import javazoom.jl.player.advanced.AdvancedPlayer;


public class BounceBack implements ActionListener, MouseListener, MouseMotionListener
{
    public static BounceBack game;
    public final int HEIGHT = 800, WIDTH = 800;
    Renderer renderer = new Renderer();
    JFrame jframe = new JFrame();
    Rectangle ball;
    Rectangle bar;
    Rectangle brick;
    int ticks = 0; 
    int yMotion, xMotion;
    int differenceFromCenter = 0;
    Paint paint;
    boolean hitBar = false;
    boolean hitSide = false;
    boolean gameOver = true, newGame = false;
    static int score = 0;
    static int speed = 0;
    boolean dragging = false;
    ArrayList<Rectangle> bricks = new ArrayList<Rectangle>();
    
    public BounceBack()
    {
        Timer timer = new Timer(1, this);
        //Adding in the renderer class to consistently repaint
        jframe.add(renderer);
        //All the jframe for the main window
        jframe.setBounds(500, 150, HEIGHT, WIDTH);
        jframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jframe.setTitle("Brick Breaker");
        jframe.setVisible(true);
        jframe.setResizable(false);
        
        ball = new Rectangle(WIDTH/2, 20, 20, 20);
        bar = new Rectangle(WIDTH/2 - 30, HEIGHT/8 + 600, 20, 100);
        
        for(int i = 0; i < 192; i++)
        {
            if(i == 0)
            {
                brick = new Rectangle(40, HEIGHT/6 + 100, 20, 25);
                bricks.add(brick);
            }
            else if(bricks.get(i-1).x >= 720)
            {
                brick = new Rectangle(40, bricks.get(i-1).y - 30, 20, 25);
                bricks.add(brick);
            }
            else
            {
                brick = new Rectangle(bricks.get(i-1).x + 30, bricks.get(i-1).y, 20, 25);
                bricks.add(brick);
                //brick.addToList(brick);
            }
        }
        
        jframe.addMouseListener(this);
        jframe.addMouseMotionListener(this);
        timer.start();
    }
   
    public static void main(String[] args) 
    {
        game = new BounceBack();
        //Music starts
        startMusic();
        
    }

    public void repaint(Graphics g) 
    {
        //Background color
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, jframe.getHeight(), jframe.getWidth());
        //Ball Object
        g.setColor(Color.BLUE);
        g.fillOval(ball.x, ball.y, ball.height, ball.width);
        //Bar Object
        g.setColor(Color.gray);
        g.fillRect(bar.x, bar.y, bar.height, bar.width);
        //Bricks
        for(int i = 0; i < bricks.size(); i++)
        {
            if(i < bricks.size()/4)
            {
                g.setColor(Color.ORANGE);
            }
            else if(i > bricks.size()/4 && i < bricks.size()/2)
            {
                g.setColor(Color.yellow);
            }
            else if(i > bricks.size()/2 && i < (bricks.size()/2 + bricks.size()/4))
            {
                g.setColor(Color.RED);
            }
            else
            {
                g.setColor(Color.GREEN);
            }
            g.fillRect(bricks.get(i).x, bricks.get(i).y, bricks.get(i).width, bricks.get(i).height);
            renderer.repaint();
        }
        //Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1, 25));
        if(newGame && !gameOver)
        {
            g.drawString("Score: " + score, 10, HEIGHT - 50);
        }
        if(gameOver)
        {
            g.setFont(new Font("Arial", 1, 40));
            g.drawString("Your score was: " + score, jframe.getWidth()/2 - 220, jframe.getHeight()/2 - 100);  
            g.drawString("Click to play again!", jframe.getWidth()/2 - 220, jframe.getHeight()/2 - 50);
        }
        if(bricks.isEmpty() == true)
        {
            g.drawString("YOU WIN!", jframe.getWidth()/2 - 220, jframe.getHeight()/2 - 100);
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        ticks++;
        yMotion = ball.y;
        xMotion = ball.x;
        int barX1 = bar.x;
        int barX2 = bar.x + bar.height;
        int ballX1 = ball.x;
        int ballX2 = ball.x + ball.width;
        
        if(ticks%2 == 0)
        {
            getSpeed();
            
            if(ball.y == bar.y && ((ballX1 <= barX2) && (ballX2 >= barX1)))//When the ball hits the bar
            {
                hitBar = true;
                differenceFromCenter = (ballX1 + ballX2)/2 - (barX1 + barX2)/2;
            }
            if(ball.y == jframe.getY() - 170)
            {
                hitBar = false;
                ball.y = 0;
            }
            if(ball.x < (jframe.getWidth() - 800) || ball.x > jframe.getWidth())//Ball hits the side
            {
                hitSide = true;
            }
            if(hitBar == true && !hitSide)//Ball hits bar, doesn't hit side
            {
                ball.x = xMotion + differenceFromCenter/2;
                ball.y = yMotion - speed;
                for(int i = 0; i < bricks.size(); i++)
                {
                    if(ball.intersects(bricks.get(i)))
                    {
                        bricks.remove(i);
                        hitBar = false;
                        score++;
                    }
                }
            }
            else if(hitSide)
            {
                if(ball.x < (jframe.getWidth() - 800))
                {
                    ball.x = 1;
                }
                else
                {
                    ball.x = 770;
                }
                differenceFromCenter = -1 * differenceFromCenter;
                hitSide = false;
            }
            else if(!hitBar)//Ball doesn't hit bar
            {
                ball.y = yMotion + speed;
                ball.x = xMotion + differenceFromCenter/2;
            }
            if(ball.y > 800)
            {
                gameOver = true;
                newGame = false;
            }

        }
        
        renderer.repaint();
    }
    
    public static void startMusic()
    {
        JFileChooser chooser = new JFileChooser();
        AdvancedPlayer ap;
        chooser.setSelectedFile(new File("C:\\Users\\Owner\\LuxAeterna.mp3"));
        File file = chooser.getSelectedFile();
        try 
        {
            FileInputStream f = new FileInputStream(file);
            ap = new AdvancedPlayer(f);
            ap.play();
        } 
        catch (Exception ex) 
        {
            ex.getMessage();
        }
    }
    
    public static void getSpeed()
    {
        if(score <= 10)
        {
            speed = 10;
        }
        else if(score <= 30 && score > 10)
        {
            speed = 15;
        }
        else if(score <= 50 && score > 30)
        {
            speed = 30;
        }
        else if(score <= 80 && score > 50)
        {
            speed = 40;
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        
    }

    @Override
    public void mousePressed(MouseEvent me) //Can alter it so when we click it, it will reset the game
    {
        Point point = me.getPoint();
        
        dragging = true;
    
        if(gameOver)
        {
            ball = new Rectangle(WIDTH/2, 20, 20, 20);
            bar = new Rectangle(WIDTH/2 - 30, HEIGHT/8 + 600, 20, 100);
            score = 0;
            differenceFromCenter = 0;
            speed = 0;
            gameOver = false;
            newGame = true;
            bricks.removeAll(bricks);
            for(int i = 0; i < 192; i++)
            {
                if(i == 0)
                {
                    brick = new Rectangle(40, HEIGHT/6 + 100, 20, 25);
                    bricks.add(brick);
                }
                else if(bricks.get(i-1).x >= 720)
                {
                    brick = new Rectangle(40, bricks.get(i-1).y - 30, 20, 25);
                    bricks.add(brick);
                }
                else
                {
                    brick = new Rectangle(bricks.get(i-1).x + 30, bricks.get(i-1).y, 20, 25);
                    bricks.add(brick);
                    //brick.addToList(brick);
                }
            }
        }
        renderer.repaint();

    }

    @Override
    public void mouseReleased(MouseEvent me) {
        dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }

    @Override
    public void mouseDragged(MouseEvent me) 
    {
        Point p = me.getPoint();
        if(dragging)
        {
            if(newGame)
            {
            bar.x = p.x;
            renderer.repaint();
            }
        }
       
        renderer.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent me) 
    {
        
    }
}
