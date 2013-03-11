package com.applabz.findtweet;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		//setContentView(R.layout.settings_activity);
		
		//ActionBar actionBar = getActionBar();
	    //actionBar.setDisplayHomeAsUpEnabled(true);
        
        //TextView tvWelcome = (TextView) findViewById(R.id.welcome);
	}

}
