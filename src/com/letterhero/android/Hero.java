package com.letterhero.android;

import android.os.Parcel;
import android.os.Parcelable;

public class Hero implements Parcelable {
	private int userScore;

	public Hero() {
		this.setScore(0);
	}

	public Hero(String name) {
		this.setScore(0);
	}

	public void resetScore() {
		this.userScore = 0;
	}

	public int getScore() {
		return userScore;
	}

	public void setScore(int userScore) {
		this.userScore = userScore;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		// TODO Auto-generated method stub
		out.writeInt(userScore);
	}

	public static final Parcelable.Creator<Hero> CREATOR = new Parcelable.Creator<Hero>() {
		public Hero createFromParcel(Parcel in) {
			return new Hero(in);
		}

		public Hero[] newArray(int size) {
			return new Hero[size];
		}
	};

	private Hero(Parcel in) {
		this.userScore = in.readInt();
	}

}
