package chapter6;

public class Barrier extends Entity {
	
	private int timesHit = 0;

	public Barrier(String ref, int i, int x, int y, GameController controller) {
		super(ref, i, x, y, controller);
	}
	
	@Override
	public void updateLocation(long elapsedTime){
		super.updateLocation(elapsedTime);
	}
	
	public int getTimesHit(){
		return timesHit;
	}
	
	public void addHitPoint(){
		timesHit = getTimesHit() + 1;
	}

	
	@Override
	public void doLogic() {
		
	}
	
	@Override
	public String toString(){
		return "Barrier";
	}

}
