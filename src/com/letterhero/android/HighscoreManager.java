package com.letterhero.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class HighscoreManager {
	private Context context;
	private SharedPreferences mSharedPreferences;
	private FragmentManager fm;
	private boolean scoreSetted = false;

	public HighscoreManager(Context ctx, FragmentManager _fm) {
		this.context = ctx;
		this.fm = _fm;

		setmSharedPreferences(context.getSharedPreferences(
				GameConsts.PREF_NAME, Context.MODE_PRIVATE));
	}

	public void showHighscoreConfirmationDialog(final int userscore) {
		final EditText input = new EditText(context);
		input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });

		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

		builder1.setView(input);
		builder1.setMessage("Confirm Highscore");
		builder1.setMessage(R.string.highscore_confirmation);
		builder1.setPositiveButton(R.string.highscore_confirmation_positive,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						if (input.getText() != null
								&& input.getText().toString().length() > 0) {

							submitHighscore(input.getText().toString(),
									userscore);
						} else {
							Toast.makeText(context,
									R.string.highscore_enter_proper_name,
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		builder1.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder1.show();
	}

	@SuppressLint("NewApi")
	private void submitHighscore(String userName, int userScore) {
		Editor editor = mSharedPreferences.edit();

		String previousName = mSharedPreferences.getString(
				GameConsts.PREF_USERNAME_KEY, null);
		int previousHScore = mSharedPreferences.getInt(
				GameConsts.PREF_HIGHSCORE_KEY, 0);

		if (previousName == null && previousHScore == 0) {

			editor.putString(GameConsts.PREF_USERNAME_KEY, userName);
			editor.putInt(GameConsts.PREF_HIGHSCORE_KEY, userScore);
			editor.commit();

			HighScoreDialogFragment congrats = new HighScoreDialogFragment();
			congrats.show(fm, "HIGHSCORE_SUCCESS");
			setScoreSetted(true);
		} else {
			if (userScore > previousHScore) {

				editor.putString(GameConsts.PREF_USERNAME_KEY, userName);
				editor.putInt(GameConsts.PREF_HIGHSCORE_KEY, userScore);
				editor.commit();

				HighScoreDialogFragment congrats = new HighScoreDialogFragment();
				congrats.show(fm, "HIGHSCORE_SUCCESS");
				setScoreSetted(true);
			} else {
				Toast.makeText(
						context,
						"Sorry, " + previousName + " is still the 4LetterHero!",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public SharedPreferences getmSharedPreferences() {
		return mSharedPreferences;
	}

	public void setmSharedPreferences(SharedPreferences mSharedPreferences) {
		this.mSharedPreferences = mSharedPreferences;
	}

	public boolean isScoreSetted() {
		return scoreSetted;
	}

	public void setScoreSetted(boolean scoreSetted) {
		this.scoreSetted = scoreSetted;
	}

}
