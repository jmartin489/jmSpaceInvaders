package chapter6;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum SoundFX implements LineListener
{
	WIN("audio/win.wav"), LOSE("audio/noMercy.wav"), COLLIDE("audio/hit.wav"), START("audio/start.wav"),
	SHOOT("audio/laser.wav");
	
	private Clip clip;
	
	SoundFX(String file)
	{
		try
		{
			URL url = this.getClass().getClassLoader().getResource(file);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			
			if(!AudioSystem.isLineSupported(info))
				throw new LineUnavailableException("Line not supported!");
			
			clip = (Clip) AudioSystem.getLine(info);
			clip.addLineListener(this);
			
			clip.open(audioStream);
		}
		
		catch(LineUnavailableException e){
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void play()
	{		
		Runnable playNote = new Runnable()
		{

			@Override
			public void run() 
			{
				if(clip.isRunning())
					clip.stop();
				clip.setFramePosition(0);
				clip.start();
			}
			
		};
		
		Thread playThread = new Thread(playNote);
		playThread.start();
	}

	@Override
	public void update(LineEvent event) 
	{
		LineEvent.Type type = event.getType();
		
		if(type == LineEvent.Type.START)
			System.out.println(this.toString() + "Started audio!");
		if(type == LineEvent.Type.STOP)
			System.out.println(this.toString() + "Stopped audio!");
	}
}
