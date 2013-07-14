package com.letterhero.android;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FourLetterHero extends Activity {
	public final static String EXTRA_MESSAGE = "com.letterhero.android.GameOver";
	public final static String EXTRA_LEVEL = "NEXT_LEVEL_AVAIL";
	public final static String EXTRA_GAME_DIFFICULTY = "PERSIST_GAME_DIFFICULTY";

	private int GAME_DIFFICULTY = 1;
	private int MAX_AMOUNT_OF_FAILS = 3000;
	private int LETTERS_PER_LEVEL = 11;
	private int CORRECT_LETTER_TIME_BONUS = 0;

	private int CDGameLength;
	private int CDTickInterval = 2000;

	private char letter = ' ';
	private int score = 0;
	private int failedAttempts = 0;

	private SquareView display_letter;
	private CountDownTimerWithPause cdTimer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_display);
		display_letter = (SquareView) findViewById(R.id.letter);

		Intent continuedGameIntent = getIntent();

		if (continuedGameIntent.hasExtra(GameOver.CONTINUE_GAME)) {
			score = Integer.parseInt(continuedGameIntent
					.getStringExtra(GameOver.CONTINUE_GAME));
			
			TextView score_value = (TextView) findViewById(R.id.score);
			score_value.setText(Integer.toString(score));

			GAME_DIFFICULTY = continuedGameIntent.getIntExtra(
					GameOver.INCREASE_DIFFICULTY, 1);

			CDTickInterval -= GAME_DIFFICULTY * 10;

			if (CDTickInterval < 500) {
				CDTickInterval = 500;
			}

			CDGameLength = CDTickInterval * LETTERS_PER_LEVEL;

		} else {
			resetValues();
		}

		failedAttempts = 0;
		startGamePlay();
	}

	public void buttonPressed(View v) {
		switch (v.getId()) {
		case R.id.aButton:
			checkEntry('A');
			break;
		case R.id.bButton:
			checkEntry('B');
			break;
		case R.id.cButton:
			checkEntry('C');
			break;
		case R.id.dButton:
			checkEntry('D');
			break;
		}
	}

	public void buttonState(boolean state) {
		LinearLayout layout = (LinearLayout) findViewById(R.id.button_section);
		for (int i = 0; i < layout.getChildCount(); i++) {
			View child = layout.getChildAt(i);
			child.setEnabled(state);
		}
	}

	public void resetValues() {
		GAME_DIFFICULTY = 1;
		CDTickInterval = 2000;
		CDGameLength = CDTickInterval * LETTERS_PER_LEVEL;
		score = 0;
		
		TextView score_value = (TextView) findViewById(R.id.score);
		score_value.setText(Integer.toString(score));
	}

	public void startGamePlay() {
		cdTimer = new CountDownTimerWithPause(CDGameLength, CDTickInterval,
				true) {

			public void onTick(long millisUntilFinished) {
				CORRECT_LETTER_TIME_BONUS += 15;
				buttonState(true);
				display_letter.setTextColor(Color.BLACK);
				letter = generateLetter();
				display_letter.setText(String.valueOf(letter));
			}

			public void onFinish() {
				buttonState(false);
				Intent onGameFinish_Intent = new Intent();
				onGameFinish_Intent.setClassName("com.letterhero.android",
						"com.letterhero.android.GameOver");

				String player_score = ((TextView) findViewById(R.id.score))
						.getText().toString();
				onGameFinish_Intent.putExtra(EXTRA_MESSAGE, player_score);
				onGameFinish_Intent.putExtra(EXTRA_LEVEL, true);
				onGameFinish_Intent.putExtra(EXTRA_GAME_DIFFICULTY, GAME_DIFFICULTY);

				startActivity(onGameFinish_Intent);
			}
		};

		cdTimer.create();
	}

	public char generateLetter() {
		Random r = new Random();
		return (char) (r.nextInt(4) + 'A');
	}

	public void checkEntry(char user_letter) {
		buttonState(false);
		TextView scoreView = (TextView) findViewById(R.id.score);
		
		if (user_letter == letter) {
			
			score += (100 + ((2 * CORRECT_LETTER_TIME_BONUS) + ((GAME_DIFFICULTY - 1) * 2)));
			scoreView.setText(Integer.toString(score));
			display_letter.setTextColor(Color.GREEN);

		} else {
			failedAttempts++;
			score -= ((GAME_DIFFICULTY - 1) + 100);
			scoreView.setText(Integer.toString(score));
			display_letter.setTextColor(Color.RED);
			

			if (failedAttempts == MAX_AMOUNT_OF_FAILS) {
				buttonState(false);
				Intent onGameLost_Intent = new Intent();
				onGameLost_Intent.setClassName("com.letterhero.android",
						"com.letterhero.android.GameOver");

				String player_score = ((TextView) findViewById(R.id.score))
						.getText().toString();
				onGameLost_Intent.putExtra(EXTRA_MESSAGE, player_score);
				onGameLost_Intent.putExtra(EXTRA_LEVEL, false);

				startActivity(onGameLost_Intent);
			}
		}
	}

	public void onPause() {
		super.onPause();
		buttonState(false);
		cdTimer.pause();
	}

	public void onResume() {
		super.onResume();
		buttonState(true);
		cdTimer.resume();
	}

}
