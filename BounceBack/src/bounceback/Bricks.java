
package bounceback;

import java.awt.Rectangle;
import java.util.ArrayList;


public class Bricks 
{
    int x, y, height, width, x2, y2;
    
    ArrayList<Bricks> bricks = new ArrayList<Bricks>();
    public Bricks(int x, int y, int height, int width)
    {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.x2 = x + width;
        this.y2 = y + height;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getX2()
    {
        return x2;
    }
    
    public int getY()
    {
        return y;
    }
    
    public int getY2()
    {
        return y2;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public void addToList(Bricks object)
    {
        bricks.add(object);
    }
    
    public Bricks equals(Rectangle ball)
    {
        for(int i = 0; i < bricks.size(); i++)
        {
            if(ball.x == bricks.get(i).getX() && ball.x + ball.width == bricks.get(i).getX2() && ball.y == bricks.get(i).y)
            {   
                System.out.println(ball.x);
                Bricks brickDummy = bricks.get(i);
                bricks.remove(i);
                return brickDummy;
            }
        }
        return null;
    }
}
