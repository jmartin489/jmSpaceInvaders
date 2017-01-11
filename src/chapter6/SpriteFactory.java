package chapter6;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteFactory 
{
	private static SpriteFactory instance = null;
	
	private HashMap<String, Sprite> spriteMap = new HashMap<>();
	
	protected SpriteFactory()
	{
		//exists to suppress the no arg constructor
	}
	
	public static SpriteFactory getInstance()
	{
		if(instance == null)
			instance = new SpriteFactory();
		return instance;
	}
	
	private void abend(String message)
	{
		System.err.println(message);
		System.exit(0);
	}
	
	public Sprite getSprite(String resid, int imageCount)
	{
		if(spriteMap.get(resid) != null)
		{
			return spriteMap.get(resid);
		}
		BufferedImage srcImage = null;
		
		try
		{
			URL url = this.getClass().getClassLoader().getResource(resid);
			
			if(url == null)
				abend("Cannot find " + resid);
			srcImage = ImageIO.read(url);
		}
		catch(IOException e)
		{
			abend("Failed to load resource - " + e.getMessage());
		}
		
		GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment()
										.getDefaultScreenDevice().getDefaultConfiguration();
		
		Image image = graphicsConfiguration.createCompatibleImage(srcImage.getWidth(), 
										srcImage.getHeight(), Transparency.BITMASK);
		
		image.getGraphics().drawImage(srcImage, 0, 0, null);
		Sprite sprite = new Sprite(image, imageCount);
		spriteMap.put(resid, sprite);
		return sprite;
	}
	
	public Sprite getSprite(String resid)
	{
		return getSprite(resid, 1);
	}
}
