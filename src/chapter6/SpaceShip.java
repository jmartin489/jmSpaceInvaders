/**
 * 
 */
package chapter6;

import java.awt.Rectangle;

/**
 * @author jam4761
 *
 */
public class SpaceShip extends Entity 
{

	public SpaceShip(String ref, int x, int y, GameController controller) 
	{
		super(ref, x, y, controller);
	}
	
	public SpaceShip(String ref, int imageCount, int x, int y, GameController controller) 
	{
		super(ref, imageCount, x, y, controller);
	}
	
	@Override
	public void updateLocation(long elapsedTime)
	{
		if(horizontalSpeed < 0 && locX < 10)
			return;
		Rectangle rect = controller.getBounds();
		
		if((horizontalSpeed > 0) && (locX > (rect.width - sprite.getWidth() - 5)))
			return;
		super.updateLocation(elapsedTime);
	}

	/* (non-Javadoc)
	 * @see chapter2.Entity#doLogic()
	 */
	@Override
	public void doLogic()
	{
		// TODO Auto-generated method stub

	}
}
