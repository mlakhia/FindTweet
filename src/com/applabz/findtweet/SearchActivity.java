package com.applabz.findtweet;

import java.util.Observable;
import java.util.Observer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public class SearchActivity extends MainActivity implements Observer {

	private String searchString = null;
	private ProgressDialog progressDialog;
	
	private ListAdapter listAdapter;
	private ListView listView;
	
	private TwitterSource TS = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_search);
    	handleIntent(getIntent());
    	
    	ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
        
	    progressDialog = ProgressDialog.show(this, "", this.getString(R.string.loading));

		/*
		listAdapter = new ListAdapter(this, R.layout.tweet, null);		
		
		listView = (ListView)findViewById(R.id.listView);
		listView.setAdapter(listAdapter);
*/
    }
    
    public void onListItemClick(ListView l, View v, int position, long id) { 
    	// call detail activity for clicked entry  
    }

    @Override
    protected void onNewIntent(Intent intent) {
    	setIntent(intent); 
        handleIntent(intent);
    }
    
    @Override
    public boolean onSearchRequested(){
    	Log.d("","here");
		return true;
    	
    }
    
    private void doSearch(String query) { 
    	setSearchString(query);
    	
    	//TS = new TwitterSource(getSearchString(), new Tweet(null,null,null,null,null,null,null));
        
        Toast.makeText(this, getSearchString(), Toast.LENGTH_LONG).show(); //makeToast(query);      
    	
    }
    
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	doSearch( intent.getStringExtra(SearchManager.QUERY) );        	      
        }
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
		return searchString;
	}

	private void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}

}
