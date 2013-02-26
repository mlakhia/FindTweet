package com.applabz.findtweet;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SavedActivity extends MainActivity {

	private ListAdapter listAdapter;
	private ListView listView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_search);
		
    	ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true); // creates back button out of icon
	    actionBar.setTitle(R.string.title_saved);

	    listAdapter = new ListArrayAdapter(this, R.layout.list_tweet, MainActivity.db.getAllTweets());
		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(listAdapter);	    
	}
}
