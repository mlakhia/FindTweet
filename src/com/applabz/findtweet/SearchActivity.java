package com.applabz.findtweet;

import java.util.ArrayList;
import java.util.Comparator;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public class SearchActivity extends MainActivity {

	private static Context context;

	private String searchString = null;
	private ProgressDialog progressDialog;
	private ListAdapter listAdapter;
	private ListView listView;
	
	private TwitterSource TS = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	context = this;
    	setContentView(R.layout.activity_search);
    	handleIntent(getIntent());
    	
    	ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true); // creates back button out of icon

	    progressDialog = ProgressDialog.show(this, "", this.getString(R.string.loading));
			    
	    class TweetComparator implements Comparator<Tweet> {
	    	public int compare(Tweet lhs, Tweet rhs) {
	    	    if (lhs.getTweetId() == rhs.getTweetId()) {
	    	      return 0;
	    	    } else {
	    	      return lhs.getTweetId() < rhs.getTweetId() ? -1 : 1;
	    	    }
	    	}
	    }
	    
	    TS = new TwitterSource(new TweetComparator());	    
	    
	    listAdapter = new ListArrayAdapter(this, R.layout.list_tweet, new ArrayList<Tweet>() );		
		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(listAdapter);
		
        listView.setTextFilterEnabled(true); // ???
        
        listView.setOnItemClickListener( new OnItemClickListener() {

			public void onItemClick(AdapterView parent, View v, int position, long id) {
            	Log.v("", parent.toString());
            	Log.v("", ""+position);
            	Log.v("", v.toString());
            	Log.v("", ""+id);
            	
            	Tweet tweet = (Tweet) listAdapter.getItem(position);            	
            	
            	TweetDbSource db = new TweetDbSource(MainActivity.context);   
            	db.addTweet(tweet);            	

            	Toast.makeText(context, "saved tweet!", Toast.LENGTH_LONG).show();
            }
        });
    }
    
    public void onListItemClick(ListView l, View v, int position, long id) { 
    	Log.v("", l.toString());
    	Log.v("", ""+position);
    	Log.v("", v.toString());
    	Log.v("", ""+id);
    }

    @Override
    protected void onNewIntent(Intent intent) {
    	setIntent(intent); 
        handleIntent(intent);
    }
    
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	doSearch( intent.getStringExtra(SearchManager.QUERY) );        	      
        }
    }
    
    private void doSearch(String query) { 
    	setSearchString(query);
    	TS.setSearchString(query);
    	       
        Toast.makeText(this, getSearchString()+" - started", Toast.LENGTH_LONG).show();
    	
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);

    	SearchView searchView = (SearchView) menu.findItem(R.id.menu_find).getActionView();
	    searchView.setQuery(getSearchString(), false);
	    searchView.setFocusable(false);
	    searchView.setIconified(false);
	    searchView.requestFocusFromTouch();
	    
	    progressDialog.dismiss();

	    //ActionBar actionBar = getActionBar();
	    //actionBar.setCustomView(searchView);
	    //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    	
		return true;
    }
    
    private String getSearchString() {
		return this.searchString;
	}

	private void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}
