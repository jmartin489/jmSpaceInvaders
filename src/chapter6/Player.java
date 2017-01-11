package chapter6;

import java.io.Serializable;

public class Player implements Comparable<Player>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int score;
	
	public Player(){
		score = 0;
		name = "psycho killer";
	}

	public Player(String name){
		this.name = name;
		score = 0;
	}
	
	public void addScore() {
		this.score += score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	public void incrementScore(){
		score += 100;
	}
	public int getScore(){
		return score;
	}
	public String getName(){
		return name;
	}

	@Override
	public int compareTo(Player player) {
		if(score == player.score)
			return 0;
		else if(score > player.score)
			return 1;
		else
			return -1;
	}
	
	@Override
	public String toString(){
		return "[" + name + ": " + score + "]";
	}

}
