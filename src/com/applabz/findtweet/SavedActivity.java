package com.applabz.findtweet;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SavedActivity extends MainActivity {

	private ListAdapter listAdapter;
	private ListView listView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_search);
		
    	ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true); // creates back button out of icon
	    actionBar.setTitle(R.string.title_saved);
	    
	    
	    
	    listAdapter = new ArrayAdapter<Tweet>(this, R.layout.list_tweet, MainActivity.db.getAllTweets()){
	    	
	    	@Override
	    	public int getCount() { 
	    		return MainActivity.db.size(); 
	    	}	

	    	@Override
	    	public Tweet getItem(int position) {
	    		return MainActivity.db.getTweet(position);
	    	}

	    	@Override
	    	public long getItemId(int position) {
	    		return MainActivity.db.getTweet(position).getTweetId();
	    	}
	    	
	    	@Override
	    	public View getView(int position, View convertView, ViewGroup parent) {
	    		View v = convertView;
	    		if (v == null) {
	    			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    			v = inflater.inflate(R.layout.list_tweet, null);
	    		}
	    		Tweet i = MainActivity.db.getTweet(position);	
	    		
	    		if (i != null) {
	    			TextView textUser = (TextView) v.findViewById(R.id.textUser);
	    			TextView textDate = (TextView) v.findViewById(R.id.textDate);
	    			TextView textTweet = (TextView) v.findViewById(R.id.textTweet);
	    			if (textUser != null){
	    				textUser.setText(i.getUser());
	    			}
	    			if (textDate != null){
	    				textDate.setText(i.getCreatedAsString());
	    			}
	    			if (textTweet != null){
	    				textTweet.setText(i.getTweet());
	    			}
	    		}		
	    		return v;
	    	}
	    };
		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(listAdapter);
		
		
		DataSetObservable DSO = new DataSetObservable();
		
		DataSetObserver observer = new DataSetObserver() {  
			@Override  
			public void onChanged() {
				((BaseAdapter) listAdapter).notifyDataSetChanged();
			}  
		};		
		DSO.registerObserver(observer);

		MainActivity.db.setObservers(DSO);
		
		
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
					//TODO
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
