
package bounceback;

import java.awt.Graphics;
import javax.swing.JPanel;


public class Renderer extends JPanel
{
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        bounceback.BounceBack.game.repaint(g);
    }
}
