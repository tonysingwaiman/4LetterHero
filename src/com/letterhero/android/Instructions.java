package com.letterhero.android;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


@SuppressLint("NewApi")
public class Instructions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructions);
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Instructions");
		
		Button doneButton = (Button) findViewById(R.id.done_button);
		doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		Intent intent = new Intent(v.getContext(), Home.class);
    			startActivity(intent);
            }
        });
	}
}
