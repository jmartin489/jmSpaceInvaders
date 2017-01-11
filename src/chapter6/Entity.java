package chapter6;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public abstract class Entity 
{
	protected final GameController controller;
	protected double locX, locY;
	protected Sprite sprite;
	protected final int imageCount;
	
	protected double horizontalSpeed, verticalSpeed;
	private int index;
	private boolean active;
	private boolean debug = false;
	
	private int killPoints = 1;
	
	public int getKillPoints() 
	{
		return killPoints;
	}

	public void setKillPoints(int killPoints) 
	{
		this.killPoints = killPoints;
	}

	public Entity(String ref, int imageCount, int x, int y, GameController controller)
	{
		this.sprite = SpriteFactory.getInstance().getSprite(ref, imageCount);
		locX = x;
		locY = y;
		this.controller = controller;
		this.imageCount = imageCount;
	}
	
	public Entity(String ref, int x, int y, GameController controller)
	{
		this(ref, 1, x, y, controller);
	}

	public double getLocX() 
	{
		return locX;
	}

	public void setLocX(double locX)
	{
		this.locX = locX;
	}

	public double getLocY() 
	{
		return locY;
	}

	public void setLocY(double locY) 
	{
		this.locY = locY;
	}

	public Sprite getSprite() 
	{
		return sprite;
	}

	public void setSprite(Sprite sprite) 
	{
		this.sprite = sprite;
	}

	public double getHorizontalSpeed() 
	{
		return horizontalSpeed;
	}

	public void setHorizontalSpeed(double horizontalSpeed) 
	{
		this.horizontalSpeed = horizontalSpeed;
	}

	public double getVerticalSpeed() 
	{
		return verticalSpeed;
	}

	public void setVerticalSpeed(double verticalSpeed) 
	{
		this.verticalSpeed = verticalSpeed;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index) 
	{
		this.index = index;
	}

	public boolean isActive() 
	{
		return active;
	}

	public void activate(boolean active) 
	{
		this.active = active;
	}

	public GameController getController() 
	{
		return controller;
	}

	public int getImageCount() 
	{
		return imageCount;
	}
	
	/**
	 * Request that this entity updates its location based on specified time interval
	 * @param elapsedTime in milliseconds
	 */
	public void updateLocation(long elapsedTime)
	{
		locX += (elapsedTime * horizontalSpeed)/1000.0;
		locY += (elapsedTime * verticalSpeed)/1000.0;
	}
	
	public void draw(Graphics gc)
	{
		if(active)
			index = (++index) % imageCount;
		if(debug)
		{
			Color c = gc.getColor();
			gc.setColor(Color.CYAN);
			gc.drawRect((int)locX, (int)locY, getWidth(), getHeight());
			gc.setColor(c);
		}
		sprite.draw(gc, (int)locX, (int)locY, index);
	}
	
	public int getWidth()
	{
		return sprite.getWidth();
	}
	
	public int getHeight()
	{
		return sprite.getHeight();
	}
	
	public Rectangle getBounds()
	{
		return new Rectangle((int)locX, (int)locY, getWidth(), getHeight());
	}
	

	public boolean detectCollision(Entity other)
	{
		return getBounds().intersects(other.getBounds());
	}
	
	public abstract void doLogic();

}
