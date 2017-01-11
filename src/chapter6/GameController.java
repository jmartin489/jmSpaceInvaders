/**
 * 
 */
package chapter6;

import java.awt.Rectangle;

/**
 * @author jam4761
 *
 */
public interface GameController 
{
	public void onGameEvent(GameEvent e);
	public Rectangle getBounds();
}
