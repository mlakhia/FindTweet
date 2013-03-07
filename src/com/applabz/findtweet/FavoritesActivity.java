package com.applabz.findtweet;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.DataSetObservable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FavoritesActivity extends MainActivity {

	private ListAdapter listAdapter;
	private ListView listView;
	private DataSetObservable DSO_FA = new DataSetObservable();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_favorites);
		
    	ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true); // creates back button out of icon
	    actionBar.setTitle(R.string.title_favorites);
	    
	    
	    listAdapter = new DBArrayAdapter(this, R.layout.list_tweet, db.getAllTweets());
		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(listAdapter);		    
		
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				confirmDeleteTweet((Tweet) listAdapter.getItem(position));
			}
        });
        
        DSO_FA.registerObserver( ((DBArrayAdapter) listAdapter).getDataSetObserver() );
		db.setObservers(DSO_FA);		
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
