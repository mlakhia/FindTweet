package com.applabz.findtweet;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;

public class SavedActivity extends MainActivity {
	
	private static Context context;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
    	setContentView(R.layout.activity_search);
		
    	ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true); // creates back button out of icon
	    actionBar.setTitle(R.string.title_saved);
	    //setDisplayShowTitleEnabled(
		
	}
}
