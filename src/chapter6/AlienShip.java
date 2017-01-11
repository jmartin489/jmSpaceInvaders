package chapter6;

import java.awt.Rectangle;

public class AlienShip extends Entity 
{
	private final double DEFAULT_SPEED = 128;


	public AlienShip(String ref, int x, int y, GameController controller) 
	{
		super(ref, x, y, controller);
		horizontalSpeed = DEFAULT_SPEED;
	}
	
	public AlienShip(String ref, int imageCount, int x, int y, GameController controller)
	{
		super(ref, imageCount, x, y, controller);
		horizontalSpeed = DEFAULT_SPEED;
	}
	
	public void setSpeed(int speed)
	{
		horizontalSpeed = speed;
	}
	
	public void updateLocation(long elapsedTime)
	{
		Rectangle rect = controller.getBounds();
		
		if(horizontalSpeed > 0 && locX > (rect.width - sprite.getWidth() + 50))
			locX = -100;
		
		super.updateLocation(elapsedTime);
	}

	@Override
	public void doLogic() 
	{

	}

}
