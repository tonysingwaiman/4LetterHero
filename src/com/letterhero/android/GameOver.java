package com.letterhero.android;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
	private boolean SCORE_SUBMITTED = false;

	private TextView displayedScore;
	private Button continueButton;
	private Hero hero;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
		displayedScore = (TextView) findViewById(R.id.game_over_score);
		continueButton = (Button) findViewById(R.id.replay);

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
			if (!SCORE_SUBMITTED) {
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
			if (SCORE_SUBMITTED == false) {
				showHighscoreConfirmation(hero.getScore());
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

	public void showHighscoreConfirmation(final int score) {
		if (score > 0) {
			final EditText input = new EditText(this);
			input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
					20) });

			new AlertDialog.Builder(this)
					.setTitle("Confirm Highscore")
					.setMessage(R.string.highscore_confirmation)
					.setView(input)
					.setPositiveButton(
							R.string.highscore_confirmation_positive,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if (input.getText() != null
											&& input.getText().toString()
													.length() > 0) {
										submitHighscore(input.getText()
												.toString(), score);
									} else {
										Toast.makeText(getApplication(),
												"Please enter a proper name.",
												Toast.LENGTH_SHORT).show();
									}

								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return;
								}
							}).show();
		} else {
			Toast.makeText(getApplicationContext(), R.string.highscore_toast,
					Toast.LENGTH_SHORT).show();
		}

	}

	public void submitHighscore(String currentName, int currentScore) {
		SharedPreferences sp = this.getSharedPreferences(GameConsts.PREF_NAME,
				MODE_PRIVATE);
		Editor editor = sp.edit();

		String previousName = sp.getString("Name", null);
		int previousHScore = sp.getInt("Highscore", 0);

		if (previousName == null && previousHScore == 0) {

			editor.putString("Name", currentName);
			editor.putInt("Highscore", currentScore);
			editor.commit();

			HighScoreDialogFragment congrats = new HighScoreDialogFragment();
			congrats.show(getFragmentManager(), "HIGHSCORE_SUCCESS");
		} else {
			if (currentScore > previousHScore) {

				editor.putString("Name", currentName);
				editor.putInt("Highscore", currentScore);
				editor.commit();

				HighScoreDialogFragment congrats = new HighScoreDialogFragment();
				congrats.show(getFragmentManager(), "HIGHSCORE_SUCCESS");

			} else {
				Toast.makeText(
						getApplication(),
						"Sorry, " + previousName + " is still the 4LetterHero!",
						Toast.LENGTH_LONG).show();
			}
		}

		SCORE_SUBMITTED = true;
	}

	public void scoreAlreadySubmittedToast() {
		Toast.makeText(
				getApplication(),
				"Your score has already been submitted. Play another game to break the highscore again.",
				Toast.LENGTH_SHORT).show();
	}
}
