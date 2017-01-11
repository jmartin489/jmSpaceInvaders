package chapter6;

import java.awt.Rectangle;

import chapter6.GameEvent.Event;

public class Alien extends Entity 
{
	private final double DEFAULT_SPEED = -80;

	public Alien(String ref, int x, int y, GameController controller) 
	{
		super(ref, x, y, controller);
		horizontalSpeed = DEFAULT_SPEED;
	}
	
	public Alien(String ref, int imageCount, int x, int y, GameController controller) 
	{
		super(ref, imageCount, x, y, controller);
		horizontalSpeed = DEFAULT_SPEED;
	}
	
	public void setSpeed(int speed)
	{
		horizontalSpeed = DEFAULT_SPEED;
	}
	
	@Override
	public void updateLocation(long elapsedTime)
	{
		if((horizontalSpeed < 0) && (locX < 10))
		{
			controller.onGameEvent(new GameEvent(this, Event.LOGIC_REQUIRED));
		}
		
		Rectangle rect = controller.getBounds();
		if((horizontalSpeed > 0) && (locX > rect.width - 50))
		{
			controller.onGameEvent(new GameEvent(this, Event.LOGIC_REQUIRED));
		}
		super.updateLocation(elapsedTime);
	}
	
	@Override
	public void doLogic() 
	{
		horizontalSpeed = -horizontalSpeed;
		locY += 10;
		Rectangle rect = controller.getBounds();
		
		if(locY > rect.height - 50)
			controller.onGameEvent(new GameEvent(this, Event.NOTIFY_LOSS));
	}
	
	@Override
	public String toString()
	{
		return "Alien";
	}

}
