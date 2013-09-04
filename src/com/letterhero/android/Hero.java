package com.letterhero.android;

public class Hero {
	private String userName;
	private int userScore;

	public Hero() {
		this.setUsername("Hero");
		this.setScore(0);
	}

	public Hero(String name) {
		this.setUsername(name);
		this.setScore(0);
	}
	
	public void resetScore() {
		this.userScore = 0;
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}

	public int getScore() {
		return userScore;
	}

	public void setScore(int userScore) {
		this.userScore = userScore;
	}

	
}
