package com.letterhero.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	public void buttonPressed(View v) {
		switch (v.getId()) {
		case R.id.playButton:
			Intent intent = new Intent(v.getContext(), FourLetterHero.class);
			startActivity(intent);
			break;
		case R.id.highscoreButton:
			showHighScoreDialog();
			break;
		case R.id.instructionButton:
	
//			SharedPreferences sp = this.getSharedPreferences("Highest_score",
//					MODE_PRIVATE);
//			Editor editor = sp.edit();
//			editor.remove("Name").remove("Highscore").commit();
			
			
			Intent intent1 = new Intent(v.getContext(), Instructions.class);
			startActivity(intent1);

			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	public void showHighScoreDialog() {
		SharedPreferences sp = this.getSharedPreferences("Highest_score",
				MODE_PRIVATE);

		String currentHighscoreName = sp.getString("Name", null);
		int currentHighscore = sp.getInt("Highscore", 0);

		if (currentHighscoreName != null && currentHighscore > 0) {
			new AlertDialog.Builder(this)
					.setTitle(
							"All hail the current hero : "
									+ currentHighscoreName)
					.setMessage("Their top score : " + currentHighscore)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();
		} else {
			Toast.makeText(getApplication(), "No current hero",
					Toast.LENGTH_SHORT).show();
		}

	}
}
