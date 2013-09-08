package com.letterhero.android;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FourLetterHero extends Activity {
	public final static String EXTRA_HERO = "FourLetterHero.Hero";
	public final static String EXTRA_LEVEL = "FourLetterHero.Extra_Level";
	public final static String EXTRA_GAME_DIFFICULTY = "FourLetterHero.Extra_Persist_Difficulty";

	private int GAME_DIFFICULTY = 1;
	private int CORRECT_LETTER_TIME_BONUS = 0;
	private boolean FROM_BACKGROUND_PAUSED = false;

	private int CDGameLength;
	private int CDTickInterval = 2000;

	private char letter = ' ';
	private int failedAttempts = 0;

	private CountDownTimerWithPause cdTimer = null;

	private TextView displayedScore;
	private SquareView displayedLetter;
	
	private Hero hero;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_display);
		displayedScore = (TextView) findViewById(R.id.score);
		displayedLetter = (SquareView) findViewById(R.id.letter);
		

		Intent continuedGameIntent = getIntent();

		if (continuedGameIntent.hasExtra(GameOver.CONTINUE_GAME)) {
			hero = continuedGameIntent.getParcelableExtra(GameOver.EXTRA_HERO);
			
			displayedScore.setText(Integer.toString(hero.getScore()));

			GAME_DIFFICULTY = continuedGameIntent.getIntExtra(
					GameOver.INCREASE_DIFFICULTY, 1);

			CDTickInterval -= GAME_DIFFICULTY * 10;

			if (CDTickInterval < 500) {
				CDTickInterval = 500;
			}

			CDGameLength = CDTickInterval * GameConsts.LETTERS_PER_LEVEL;

		} else {
			hero = new Hero();
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
		CDGameLength = CDTickInterval * GameConsts.LETTERS_PER_LEVEL;
		hero.resetScore();
		
		displayedScore.setText(Integer.toString(hero.getScore()));
	}

	public void startGamePlay() {
		cdTimer = new CountDownTimerWithPause(CDGameLength, CDTickInterval,
				true) {

			public void onTick(long millisUntilFinished) {
				CORRECT_LETTER_TIME_BONUS += 15;
				buttonState(true);
				displayedLetter.setTextColor(Color.BLACK);
				
				letter = generateLetter();
				displayedLetter.setText(String.valueOf(letter));
			}

			public void onFinish() {
				buttonState(false);
		
				Intent onGameFinish_Intent = new Intent();

				onGameFinish_Intent.setClassName("com.letterhero.android",
						"com.letterhero.android.GameOver");
				
				onGameFinish_Intent.putExtra(EXTRA_HERO, hero);
				onGameFinish_Intent.putExtra(EXTRA_LEVEL, true);
				onGameFinish_Intent.putExtra(EXTRA_GAME_DIFFICULTY,
						GAME_DIFFICULTY);

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
		
		if (user_letter == letter) {
			int newScore = (100 + ((2 * CORRECT_LETTER_TIME_BONUS) + ((GAME_DIFFICULTY - 1) * 2)));
			hero.setScore(hero.getScore() + newScore);
			displayedScore.setText(Integer.toString(hero.getScore()));
			
			displayedLetter.setTextColor(Color.GREEN);

		} else {
			failedAttempts++;
			
			int newScore = ((GAME_DIFFICULTY - 1) + 100);			
			hero.setScore(hero.getScore() - newScore);
			
			displayedScore.setText(Integer.toString(hero.getScore()));
			displayedLetter.setTextColor(Color.RED);

			if (failedAttempts == GameConsts.MAX_FAIL_COUNT) {
				buttonState(false);
				
				Intent onGameLost_Intent = new Intent();
				onGameLost_Intent.setClassName("com.letterhero.android",
						"com.letterhero.android.GameOver");

				onGameLost_Intent.putExtra(EXTRA_HERO, hero);
				onGameLost_Intent.putExtra(EXTRA_LEVEL, false);

				startActivity(onGameLost_Intent);
			}
		}
	}

	public void onPause() {
		super.onPause();
		buttonState(false);
		cdTimer.pause();
		FROM_BACKGROUND_PAUSED = true;
	}

	public void onResume() {
		super.onResume();
		if (FROM_BACKGROUND_PAUSED) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.pause_title)
					.setPositiveButton("Resume",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									FROM_BACKGROUND_PAUSED = false;
									buttonState(true);
									cdTimer.resume();
								}
							}).show();
		}
	}

}
