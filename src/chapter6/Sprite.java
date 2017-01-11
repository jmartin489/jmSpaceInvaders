package chapter6;

import java.awt.Graphics;
import java.awt.Image;

public class Sprite 
{
	protected final Image image;
	private final int imageCount;
	
	public Sprite(Image image)
	{
		this(image, 1);
	}
	
	public Sprite(Image image, int imageCount)
	{
		this.imageCount = imageCount;
		this.image = image;
	}

	public int getWidth()
	{
		return image.getWidth(null)/imageCount;
	}
	
	public int getHeight()
	{
		return image.getHeight(null);
	}
	
	public void draw(Graphics gc, int dx, int dy, int dwidth, int dheight, int index)
	{
		int sx1 = getWidth() * index;
		int sx2 = sx1 + getWidth();
		
		gc.drawImage(image, dx, dy, dx+dwidth, dy+dheight, sx1, 0, sx2, getHeight(), null);
	}
	
	public void draw(Graphics gc, int dx, int dy, int dwidth, int dheight)
	{
		gc.drawImage(image, dx, dy, dx + dwidth, dy + dheight, 0, 0, getWidth(), getHeight(), null);
	}
	
	public void draw(Graphics gc, int x, int y, int index)
	{
		int sx1 = getWidth() * index;
		int sx2 = sx1 + getWidth();
		
		gc.drawImage(image, x, y, x+getWidth(), y+getHeight(), 
				sx1, 0, sx2, getHeight(), null);
	}
	
	public void draw(Graphics gc, int x, int y)
	{
		draw(gc, x, y, 0);
	}
}
