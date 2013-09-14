package com.letterhero.android;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class GameOver extends Activity {
	public final static String CONTINUE_GAME = "com.letterhero.android.FourLetterHero";
	public final static String INCREASE_DIFFICULTY = "GameOver.Increase_Difficulty";
	public final static String HIGHSCORE_KEY = "GameOver.Highscore_Key";
	public final static String EXTRA_HERO = "GameOver.Hero";

	private boolean passedLevel = false;
	private int CURRENT_DIFFICULTY = -1;

	private TextView displayedScore;
	private Button continueButton;
	private Hero hero;
	private HighscoreManager hm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
		displayedScore = (TextView) findViewById(R.id.game_over_score);
		continueButton = (Button) findViewById(R.id.replay);

		hm = new HighscoreManager(this, getFragmentManager());
		hm.setScoreSetted(false);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Game Over");

		Intent intentFromGame = getIntent();

		hero = intentFromGame.getParcelableExtra(FourLetterHero.EXTRA_HERO);

		displayedScore.setText(Integer.toString(hero.getScore()));

		passedLevel = intentFromGame.getBooleanExtra(
				FourLetterHero.EXTRA_LEVEL, false);

		if (passedLevel) {
			continueButton.setText("CONTINUE");
		}

		if (intentFromGame.hasExtra(FourLetterHero.EXTRA_GAME_DIFFICULTY)) {
			CURRENT_DIFFICULTY = intentFromGame.getIntExtra(
					FourLetterHero.EXTRA_GAME_DIFFICULTY, 1);
		}

	}

	public void buttonPressed(View v) {
		switch (v.getId()) {
		case R.id.replay:
			if (!hm.isScoreSetted()) {
				Intent intent = new Intent(v.getContext(), FourLetterHero.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);

				if (passedLevel) {
					intent.putExtra(EXTRA_HERO, hero);
					intent.putExtra(CONTINUE_GAME, true);
					intent.putExtra(INCREASE_DIFFICULTY,
							(int) (CURRENT_DIFFICULTY + 10));
				}

				startActivity(intent);
			} else {
				scoreAlreadySubmittedToast();
			}
			break;
		case R.id.highscore:
			if (!hm.isScoreSetted()) {
				hm.showHighscoreConfirmationDialog(hero.getScore());
			} else {
				scoreAlreadySubmittedToast();
			}
			break;
		case R.id.home:
			Intent intent2 = new Intent(v.getContext(), Home.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent2);
			break;
		}
	}

	public void scoreAlreadySubmittedToast() {
		Toast.makeText(
				getApplication(),
				"Your score has already been submitted. Play another game to break the highscore again.",
				Toast.LENGTH_SHORT).show();
	}
}
