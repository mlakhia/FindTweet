package com.applabz.findtweet;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.applabz.findtweet.MainActivity.TweetComparator;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


public class SearchActivity extends MainActivity {

	private String searchString = null;
	private ProgressDialog progressDialog;
	private ListAdapter listAdapter;
	private ListView listView;
	private DataSetObservable DSO_SA = new DataSetObservable();

	static TwitterSource TS;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	handleIntent(getIntent());
    	setContentView(R.layout.activity_search);
    	
    	ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true); // creates back button out of icon	    
	    
	    TS = new TwitterSource(new TweetComparator());
	    
	    listAdapter = new ListArrayAdapter(this, R.layout.list_tweet, TS.getAllTweets());
		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(listAdapter);
		
		DataSetObserver observer = new DataSetObserver() {  
			@Override  
			public void onChanged() {
				if(progressDialog != null){
					progressDialog.dismiss();
					progressDialog = null;
				}
			}  
		};		
		DSO_SA.registerObserver(observer);
		DSO_SA.registerObserver( ((ListArrayAdapter) listAdapter).getDataSetObserver() );
		TS.setObservers(DSO_SA);

		TS.setSearchString(this.searchString);
		
    	progressDialog = ProgressDialog.show(this, "", this.getString(R.string.loading));
    	
		Toast toast = Toast.makeText(this, this.searchString+" - started", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP, 0, 25);
		toast.show();
				
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position, long id) {
				Log.v("", parent.toString());
            	Log.v("", ""+position);
            	Log.v("", v.toString());
            	Log.v("", ""+id);
            	
            	Tweet tweet = (Tweet) listAdapter.getItem(position);
            	MainActivity.db.addTweet(tweet);
            	
            	Toast.makeText(context, "saved tweet!", Toast.LENGTH_LONG).show();
			}
        });
		
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	setIntent(intent); 
        handleIntent(intent);
    }
    
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	this.searchString = intent.getStringExtra(SearchManager.QUERY);        	      
        }
    }    
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);

    	SearchView searchView = (SearchView) menu.findItem(R.id.menu_find).getActionView();
	    //searchView.setQuery(this.searchString, false);
	    searchView.setFocusable(false);
	    searchView.setIconified(false);
	    searchView.requestFocusFromTouch();
	    
	    //progressDialog.dismiss();

	    //ActionBar actionBar = getActionBar();
	    //actionBar.setCustomView(searchView);
	    //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    	
		return true;
    }

/*
	@Override
	public void update(Observable arg0, Object arg1) {
		//((ListArrayAdapter) listAdapter).notifyDataSetChanged();
		if(progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}		
	}*/
	
}
