package com.letterhero.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class GameOver extends Activity {
	public final static String CONTINUE_GAME = "com.letterhero.android.FourLetterHero";
	public final static String INCREASE_DIFFICULTY = "HARDER";
	public final static String HIGHSCORE_KEY = "Highscore_Key";

	private boolean passedLevel = false;
	private int CURRENT_DIFFICULTY;
	private boolean SCORE_SUBMITTED = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);

		Intent intentFromGame = getIntent();
		String score_value = intentFromGame
				.getStringExtra(FourLetterHero.EXTRA_MESSAGE);
		TextView displayed_score = (TextView) findViewById(R.id.game_over_score);
		displayed_score.setText(score_value);

		if (intentFromGame.hasExtra(FourLetterHero.EXTRA_LEVEL)) {
			passedLevel = getIntent().getExtras().getBoolean(
					FourLetterHero.EXTRA_LEVEL);

			if (passedLevel) {
				Button continueButton = (Button) findViewById(R.id.replay);
				continueButton.setText("Continue");
			}

			CURRENT_DIFFICULTY = intentFromGame.getIntExtra(
					FourLetterHero.EXTRA_GAME_DIFFICULTY, 1);
		}
	}

	public void buttonPressed(View v) {
		switch (v.getId()) {
		case R.id.replay:
			if (SCORE_SUBMITTED == false) {
				Intent intent = new Intent(v.getContext(), FourLetterHero.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);

				if (passedLevel) {
					String player_score = ((TextView) findViewById(R.id.game_over_score))
							.getText().toString();

					intent.putExtra(CONTINUE_GAME, player_score);
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
				int submittedScore = Integer
						.parseInt(((TextView) findViewById(R.id.game_over_score))
								.getText().toString());
				showHighscoreConfirmation(submittedScore);
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
		SharedPreferences sp = this.getSharedPreferences("Highest_score",
				MODE_PRIVATE);
		Editor editor = sp.edit();

		String previousName = sp.getString("Name", null);
		int previousHScore = sp.getInt("Highscore", 0);

		if (previousName == null && previousHScore == 0) {

			editor.putString("Name", currentName);
			editor.putInt("Highscore", currentScore);
			editor.commit();

		} else {
			if (currentScore > previousHScore) {

				editor.putString("Name", currentName);
				editor.putInt("Highscore", currentScore);
				editor.commit();

				HighScoreDialogFragment congrats = new HighScoreDialogFragment();
				congrats.show(getFragmentManager(), "HIGHSCORE_SUCCESS");

				SCORE_SUBMITTED = true;
			} else {
				Toast.makeText(
						getApplication(),
						"Sorry, " + previousName + " is still the 4LetterHero!",
						Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void scoreAlreadySubmittedToast(){
		Toast.makeText(
				getApplication(),
				"Your score has already been submitted. Play another game to break the highscore again.",
				Toast.LENGTH_SHORT).show();
	}
}
