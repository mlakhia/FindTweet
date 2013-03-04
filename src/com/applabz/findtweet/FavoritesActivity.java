package com.applabz.findtweet;

import java.util.ArrayList;

import com.applabz.findtweet.MainActivity.TweetComparator;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FavoritesActivity extends MainActivity {

	private ListAdapter listAdapter;
	private ListView listView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_favorites);
		
    	ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true); // creates back button out of icon
	    actionBar.setTitle(R.string.title_favorites);
	    
	    
	    listAdapter = new DBArrayAdapter(this, R.layout.list_tweet, MainActivity.db.getAllTweets());
		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(listAdapter);		    
			
	        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView parent, View v, int position, long id) {
					confirmDeleteTweet((Tweet) listAdapter.getItem(position));
				}
	        });
	        
	    
	}
	

	/*
	 * Present the user with a simple dialog to confirm a tweet deletion
	 */
	private void confirmDeleteTweet(final Tweet t) {
	
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Delete tweet?")
			 .setMessage(t.getTweet())
			 .setPositiveButton("Ok", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {
					MainActivity.db.deleteTweet(t);
				}
			 })
			 .setIcon(android.R.drawable.ic_delete)
			 .setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int button) {} // cancel
			 })
			 .show();
	}
	
	
}
