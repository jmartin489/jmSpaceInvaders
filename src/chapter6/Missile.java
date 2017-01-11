package chapter6;

import chapter6.GameEvent.Event;

public class Missile extends Entity 
{
	private static final double DEFAULT_SPEED = -500;
	private boolean spent = false;
	
	public Missile(String ref, int x, int y, GameController controller) 
	{
		super(ref, x, y, controller);
		verticalSpeed = DEFAULT_SPEED;
	}
	
	public Missile(String ref, int imageCount, int x, int y, GameController controller)
	{
		super(ref, imageCount, x, y, controller);
		verticalSpeed = DEFAULT_SPEED;
	}

	@Override
	public void updateLocation(long elapsedTime)
	{
		super.updateLocation(elapsedTime);
		
		if(locY < -100)
			controller.onGameEvent(new GameEvent(this, Event.REMOVE_ENTITY));
	}
	
	@Override
	public boolean detectCollision(Entity other)
	{
		if(spent)
			return false;
		else
			spent = super.detectCollision(other);
		
		return spent;
	}
	
	public void setSpeed(int speed)
	{
		verticalSpeed = speed;
	}
	
	@Override
	public String toString()
	{
		return "Missile";
	}
	
	@Override
	public void doLogic() 
	
	{
		// TODO Auto-generated method stub

	}


}
