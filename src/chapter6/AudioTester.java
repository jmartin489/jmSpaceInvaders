package chapter6;

public class AudioTester 
{

	public static void main(String[] args)
	{
		for(int n = 0; n < 5; n++)
		{
			SoundFX.SHOOT.play();
			try{
				Thread.sleep(1000);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

}
