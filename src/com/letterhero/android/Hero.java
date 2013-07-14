package com.letterhero.android;

public class Hero {
	private String name;
	private int score;
	private int hitRate;
	
	
	public Hero(){
		this.name = "";
		this.score = 0;
		this.hitRate = 0;
	}
	
	public Hero(String name, int score, int hitRate){
		this.name = name;
		this.score = 0;
		this.hitRate = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getHitRate() {
		return hitRate;
	}

	public void setHitRate(int hitRate) {
		this.hitRate = hitRate;
	}
	
	
	
}
